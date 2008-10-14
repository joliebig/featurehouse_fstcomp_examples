{-# LANGUAGE ExistentialQuantification, FlexibleInstances, GeneralizedNewtypeDeriving,
             MultiParamTypeClasses, TypeSynonymInstances, CPP #-}
-- required for deriving Typeable
{-# OPTIONS_GHC -fglasgow-exts #-}

-----------------------------------------------------------------------------
-- |
-- Module      :  XMonad.Core
-- Copyright   :  (c) Spencer Janssen 2007
-- License     :  BSD3-style (see LICENSE)
--
-- Maintainer  :  sjanssen@cse.unl.edu
-- Stability   :  unstable
-- Portability :  not portable, uses cunning newtype deriving
--
-- The X monad, a state monad transformer over IO, for the window
-- manager state, and support routines.
--
-----------------------------------------------------------------------------

module XMonad.Core (
    X, WindowSet, WindowSpace, WorkspaceId,
    ScreenId(..), ScreenDetail(..), XState(..),
    XConf(..), XConfig(..), LayoutClass(..),
    Layout(..), readsLayout, Typeable, Message,
    SomeMessage(..), fromMessage, LayoutMessages(..),
    runX, catchX, userCode, io, catchIO, doubleFork,
    withDisplay, withWindowSet, isRoot, runOnWorkspaces,
    getAtom, spawn, getXMonadDir, recompile, trace, whenJust, whenX,
    atom_WM_STATE, atom_WM_PROTOCOLS, atom_WM_DELETE_WINDOW, ManageHook, Query(..), runQuery
  ) where

import XMonad.StackSet hiding (modify)

import Prelude hiding ( catch )
import Control.Exception (catch, bracket, throw, Exception(ExitException))
import Control.Applicative
import Control.Monad.State
import Control.Monad.Reader
import System.IO
import System.Info
import System.Posix.Process (executeFile, forkProcess, getProcessStatus, createSession)
import System.Process
import System.Directory
import System.Exit
import Graphics.X11.Xlib
import Graphics.X11.Xlib.Extras (Event)
import Data.Typeable
import Data.Monoid

import qualified Data.Map as M
import qualified Data.Set as S

-- | XState, the (mutable) window manager state.
data XState = XState
    { windowset    :: !WindowSet           -- ^ workspace list
    , mapped       :: !(S.Set Window)      -- ^ the Set of mapped windows
    , waitingUnmap :: !(M.Map Window Int)  -- ^ the number of expected UnmapEvents
    , dragging     :: !(Maybe (Position -> Position -> X (), X ())) }

-- | XConf, the (read-only) window manager configuration.
data XConf = XConf
    { display       :: Display        -- ^ the X11 display
    , config        :: !(XConfig Layout)       -- ^ initial user configuration
    , theRoot       :: !Window        -- ^ the root window
    , normalBorder  :: !Pixel         -- ^ border color of unfocused windows
    , focusedBorder :: !Pixel         -- ^ border color of the focused window
    , keyActions    :: !(M.Map (KeyMask, KeySym) (X ()))
                                      -- ^ a mapping of key presses to actions
    , buttonActions :: !(M.Map (KeyMask, Button) (Window -> X ()))
                                      -- ^ a mapping of button presses to actions
    }

-- todo, better name
data XConfig l = XConfig
    { normalBorderColor  :: !String              -- ^ Non focused windows border color. Default: \"#dddddd\"
    , focusedBorderColor :: !String              -- ^ Focused windows border color. Default: \"#ff0000\"
    , terminal           :: !String              -- ^ The preferred terminal application. Default: \"xterm\"
    , layoutHook         :: !(l Window)          -- ^ The available layouts
    , manageHook         :: !ManageHook          -- ^ The action to run when a new window is opened
    , workspaces         :: ![String]            -- ^ The list of workspaces' names
    , defaultGaps        :: ![(Int,Int,Int,Int)] -- ^ The list of gaps, per screen
    , numlockMask        :: !KeyMask             -- ^ The numlock modifier
    , modMask            :: !KeyMask             -- ^ the mod modifier
    , keys               :: !(XConfig Layout -> M.Map (ButtonMask,KeySym) (X ()))
                                                 -- ^ The key binding: a map from key presses and actions
    , mouseBindings      :: !(XConfig Layout -> M.Map (ButtonMask, Button) (Window -> X ()))
                                                 -- ^ The mouse bindings
    , borderWidth        :: !Dimension           -- ^ The border width
    , logHook            :: !(X ())              -- ^ The action to perform when the windows set is changed
    , startupHook        :: !(X ())              -- ^ The action to perform on startup
    , focusFollowsMouse  :: !Bool                -- ^ Whether window entry events can change focus
    }


type WindowSet   = StackSet  WorkspaceId (Layout Window) Window ScreenId ScreenDetail
type WindowSpace = Workspace WorkspaceId (Layout Window) Window

-- | Virtual workspace indices
type WorkspaceId = String

-- | Physical screen indices
newtype ScreenId    = S Int deriving (Eq,Ord,Show,Read,Enum,Num,Integral,Real)

-- | The 'Rectangle' with screen dimensions and the list of gaps
data ScreenDetail   = SD { screenRect :: !Rectangle
                         , statusGap  :: !(Int,Int,Int,Int) -- ^ gaps on the sides of the screen that shouldn't be tiled, usually for status bars
                         } deriving (Eq,Show, Read)

------------------------------------------------------------------------

-- | The X monad, ReaderT and StateT transformers over IO
-- encapsulating the window manager configuration and state,
-- respectively.
--
-- Dynamic components may be retrieved with 'get', static components
-- with 'ask'. With newtype deriving we get readers and state monads
-- instantiated on XConf and XState automatically.
--
type MonadStateXState = MonadState XState
type MonadReaderXConf = MonadReader XConf
type MonadReaderWindow = MonadReader Window

newtype X a = X (ReaderT XConf (StateT XState IO) a)
-- #ifndef __HADDOCK__
    deriving (Functor, Monad, MonadIO, MonadStateXState, MonadReaderXConf)
-- #endif

instance Applicative X where
  pure = return
  (<*>) = ap

instance (Monoid a) => Monoid (X a) where
    mempty  = return mempty
    mappend = liftM2 mappend

type ManageHook = Query (Endo WindowSet)
newtype Query a = Query (ReaderT Window X a)
-- #ifndef __HADDOCK__
    deriving (Functor, Monad, MonadReaderWindow, MonadIO)
-- #endif

runQuery :: Query a -> Window -> X a
runQuery (Query m) w = runReaderT m w

instance Monoid a => Monoid (Query a) where
    mempty  = return mempty
    mappend = liftM2 mappend

-- | Run the X monad, given a chunk of X monad code, and an initial state
-- Return the result, and final state
runX :: XConf -> XState -> X a -> IO (a, XState)
runX c st (X a) = runStateT (runReaderT a c) st

-- | Run in the X monad, and in case of exception, and catch it and log it
-- to stderr, and run the error case.
catchX :: X a -> X a -> X a
catchX job errcase = do
    st <- get
    c <- ask
    (a, s') <- io $ runX c st job `catch` \e -> case e of
                            ExitException {} -> throw e
                            _ -> do hPrint stderr e; runX c st errcase
    put s'
    return a

-- | Execute the argument, catching all exceptions.  Either this function or
-- catchX should be used at all callsites of user customized code.
userCode :: X () -> X ()
userCode a = catchX (a >> return ()) (return ())

-- ---------------------------------------------------------------------
-- Convenient wrappers to state

-- | Run a monad action with the current display settings
withDisplay :: (Display -> X a) -> X a
withDisplay   f = asks display >>= f

-- | Run a monadic action with the current stack set
withWindowSet :: (WindowSet -> X a) -> X a
withWindowSet f = gets windowset >>= f

-- | True if the given window is the root window
isRoot :: Window -> X Bool
isRoot w = (w==) <$> asks theRoot

-- | Wrapper for the common case of atom internment
getAtom :: String -> X Atom
getAtom str = withDisplay $ \dpy -> io $ internAtom dpy str False

-- | Common non-predefined atoms
atom_WM_PROTOCOLS, atom_WM_DELETE_WINDOW, atom_WM_STATE :: X Atom
atom_WM_PROTOCOLS       = getAtom "WM_PROTOCOLS"
atom_WM_DELETE_WINDOW   = getAtom "WM_DELETE_WINDOW"
atom_WM_STATE           = getAtom "WM_STATE"

------------------------------------------------------------------------
-- LayoutClass handling. See particular instances in Operations.hs

-- | An existential type that can hold any object that is in 'Read'
--   and 'LayoutClass'.
data Layout a = forall l. (LayoutClass l a, Read (l a)) => Layout (l a)

-- | Using the 'Layout' as a witness, parse existentially wrapped windows
-- from a 'String'.
readsLayout :: Layout a -> String -> [(Layout a, String)]
readsLayout (Layout l) s = [(Layout (asTypeOf x l), rs) | (x, rs) <- reads s]

-- | Every layout must be an instance of 'LayoutClass', which defines
-- the basic layout operations along with a sensible default for each.
--
-- Minimal complete definition:
--
-- * 'runLayout' || (('doLayout' || 'pureLayout') && 'emptyLayout'), and
--
-- * 'handleMessage' || 'pureMessage'
--
-- You should also strongly consider implementing 'description',
-- although it is not required.
--
-- Note that any code which /uses/ 'LayoutClass' methods should only
-- ever call 'runLayout', 'handleMessage', and 'description'!  In
-- other words, the only calls to 'doLayout', 'pureMessage', and other
-- such methods should be from the default implementations of
-- 'runLayout', 'handleMessage', and so on.  This ensures that the
-- proper methods will be used, regardless of the particular methods
-- that any 'LayoutClass' instance chooses to define.
class Show (layout a) => LayoutClass layout a where

    -- | By default, 'runLayout' calls 'doLayout' if there are any
    --   windows to be laid out, and 'emptyLayout' otherwise.  Most
    --   instances of 'LayoutClass' probably do not need to implement
    --   'runLayout'; it is only useful for layouts which wish to make
    --   use of more of the 'Workspace' information (for example,
    --   "XMonad.Layout.PerWorkspace").
    runLayout :: Workspace WorkspaceId (layout a) a
              -> Rectangle
              -> X ([(a, Rectangle)], Maybe (layout a))
    runLayout (Workspace _ l ms) r = maybe (emptyLayout l r) (doLayout l r) ms

    -- | Given a 'Rectangle' in which to place the windows, and a 'Stack'
    -- of windows, return a list of windows and their corresponding
    -- Rectangles.  If an element is not given a Rectangle by
    -- 'doLayout', then it is not shown on screen.  The order of
    -- windows in this list should be the desired stacking order.
    --
    -- Also possibly return a modified layout (by returning @Just
    -- newLayout@), if this layout needs to be modified (e.g. if it
    -- keeps track of some sort of state).  Return @Nothing@ if the
    -- layout does not need to be modified.
    --
    -- Layouts which do not need access to the 'X' monad ('IO', window
    -- manager state, or configuration) and do not keep track of their
    -- own state should implement 'pureLayout' instead of 'doLayout'.
    doLayout    :: layout a -> Rectangle -> Stack a
                -> X ([(a, Rectangle)], Maybe (layout a))
    doLayout l r s   = return (pureLayout l r s, Nothing)

    -- | This is a pure version of 'doLayout', for cases where we
    -- don't need access to the 'X' monad to determine how to lay out
    -- the windows, and we don't need to modify the layout itself.
    pureLayout  :: layout a -> Rectangle -> Stack a -> [(a, Rectangle)]
    pureLayout _ r s = [(focus s, r)]

    -- | 'emptyLayout' is called when there are no windows.
    emptyLayout :: layout a -> Rectangle -> X ([(a, Rectangle)], Maybe (layout a))
    emptyLayout _ _ = return ([], Nothing)

    -- | 'handleMessage' performs message handling.  If
    -- 'handleMessage' returns @Nothing@, then the layout did not
    -- respond to the message and the screen is not refreshed.
    -- Otherwise, 'handleMessage' returns an updated layout and the
    -- screen is refreshed.
    --
    -- Layouts which do not need access to the 'X' monad to decide how
    -- to handle messages should implement 'pureMessage' instead of
    -- 'handleMessage' (this restricts the risk of error, and makes
    -- testing much easier).
    handleMessage :: layout a -> SomeMessage -> X (Maybe (layout a))
    handleMessage l  = return . pureMessage l

    -- | Respond to a message by (possibly) changing our layout, but
    -- taking no other action.  If the layout changes, the screen will
    -- be refreshed.
    pureMessage :: layout a -> SomeMessage -> Maybe (layout a)
    pureMessage _ _  = Nothing

    -- | This should be a human-readable string that is used when
    -- selecting layouts by name.  The default implementation is
    -- 'show', which is in some cases a poor default.
    description :: layout a -> String
    description      = show

instance LayoutClass Layout Window where
    runLayout (Workspace i (Layout l) ms) r = fmap (fmap Layout) `fmap` runLayout (Workspace i l ms) r
    doLayout (Layout l) r s  = fmap (fmap Layout) `fmap` doLayout l r s
    emptyLayout (Layout l) r = fmap (fmap Layout) `fmap` emptyLayout l r
    handleMessage (Layout l) = fmap (fmap Layout) . handleMessage l
    description (Layout l)   = description l

instance Show (Layout a) where show (Layout l) = show l

-- | Based on ideas in /An Extensible Dynamically-Typed Hierarchy of
-- Exceptions/, Simon Marlow, 2006. Use extensible messages to the
-- 'handleMessage' handler.
--
-- User-extensible messages must be a member of this class.
--
class Typeable a => Message a

-- |
-- A wrapped value of some type in the 'Message' class.
--
data SomeMessage = forall a. Message a => SomeMessage a

-- |
-- And now, unwrap a given, unknown 'Message' type, performing a (dynamic)
-- type check on the result.
--
fromMessage :: Message m => SomeMessage -> Maybe m
fromMessage (SomeMessage m) = cast m

-- X Events are valid Messages.
instance Message Event

-- | 'LayoutMessages' are core messages that all layouts (especially stateful
-- layouts) should consider handling.
data LayoutMessages = Hide              -- ^ sent when a layout becomes non-visible
                    | ReleaseResources  -- ^ sent when xmonad is exiting or restarting
    deriving (Typeable, Eq)

instance Message LayoutMessages

-- ---------------------------------------------------------------------
-- | General utilities
--
-- Lift an IO action into the X monad
io :: MonadIO m => IO a -> m a
io = liftIO

-- | Lift an IO action into the X monad.  If the action results in an IO
-- exception, log the exception to stderr and continue normal execution.
catchIO :: MonadIO m => IO () -> m ()
catchIO f = io (f `catch` \e -> hPrint stderr e >> hFlush stderr)

-- | spawn. Launch an external application
spawn :: MonadIO m => String -> m ()
spawn x = doubleFork $ executeFile "/bin/sh" False ["-c", x] Nothing

-- | Double fork and execute an IO action (usually one of the exec family of
-- functions)
doubleFork :: MonadIO m => IO () -> m ()
doubleFork m = io $ do
    pid <- forkProcess $ do
        forkProcess (createSession >> m)
        exitWith ExitSuccess
    getProcessStatus True False pid
    return ()

-- | This is basically a map function, running a function in the X monad on
-- each workspace with the output of that function being the modified workspace.
runOnWorkspaces :: (WindowSpace -> X WindowSpace) -> X ()
runOnWorkspaces job = do
    ws <- gets windowset
    h <- mapM job $ hidden ws
    c:v <- mapM (\s -> (\w -> s { workspace = w}) <$> job (workspace s))
             $ current ws : visible ws
    modify $ \s -> s { windowset = ws { current = c, visible = v, hidden = h } }

-- | Return the path to @~\/.xmonad@.
getXMonadDir :: MonadIO m => m String
getXMonadDir = io $ getAppUserDataDirectory "xmonad"

-- | 'recompile force', recompile @~\/.xmonad\/xmonad.hs@ when any of the
-- following apply:
--      * force is True
--      * the xmonad executable does not exist
--      * the xmonad executable is older than xmonad.hs
--
-- The -i flag is used to restrict recompilation to the xmonad.hs file only.
--
-- Compilation errors (if any) are logged to ~\/.xmonad\/xmonad.errors.  If
-- GHC indicates failure with a non-zero exit code, an xmessage displaying
-- that file is spawned.
--
-- False is returned if there are compilation errors.
--
recompile :: MonadIO m => Bool -> m Bool
recompile force = io $ do
    dir <- getXMonadDir
    let binn = "xmonad-"++arch++"-"++os
        bin  = dir ++ "/" ++ binn
        base = dir ++ "/" ++ "xmonad"
        err  = base ++ ".errors"
        src  = base ++ ".hs"
    srcT <- getModTime src
    binT <- getModTime bin
    if (force || srcT > binT)
      then do
        status <- bracket (openFile err WriteMode) hClose $ \h -> do
            waitForProcess =<< runProcess "ghc" ["--make", "xmonad.hs", "-i", "-no-recomp", "-v0", "-o",binn] (Just dir)
                                    Nothing Nothing Nothing (Just h)

        -- now, if it fails, run xmessage to let the user know:
        when (status /= ExitSuccess) $ do
            ghcErr <- readFile err
            let msg = unlines $
                    ["Error detected while loading xmonad configuration file: " ++ src]
                    ++ lines ghcErr ++ ["","Please check the file for errors."]
            -- nb, the ordering of printing, then forking, is crucial due to
            -- lazy evaluation
            hPutStrLn stderr msg
            doubleFork $ executeFile "xmessage" True ["-default", "okay", msg] Nothing
        return (status == ExitSuccess)
      else return True
 where getModTime f = catch (Just <$> getModificationTime f) (const $ return Nothing)

-- | Conditionally run an action, using a @Maybe a@ to decide.
whenJust :: Monad m => Maybe a -> (a -> m ()) -> m ()
whenJust mg f = maybe (return ()) f mg

-- | Conditionally run an action, using a X event to decide
whenX :: X Bool -> X () -> X ()
whenX a f = a >>= \b -> when b f

-- | A 'trace' for the X monad. Logs a string to stderr. The result may
-- be found in your .xsession-errors file
trace :: MonadIO m => String -> m ()
trace = io . hPutStrLn stderr

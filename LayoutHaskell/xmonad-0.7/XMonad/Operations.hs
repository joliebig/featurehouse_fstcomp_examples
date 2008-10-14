{-# OPTIONS_GHC -fno-warn-orphans #-}
{-# OPTIONS_GHC -fglasgow-exts    #-} -- For deriving Data/Typeable
{-# LANGUAGE FlexibleInstances, MultiParamTypeClasses, PatternGuards, TypeSynonymInstances #-}

-- --------------------------------------------------------------------------
-- |
-- Module      :  XMonad.Operations
-- Copyright   :  (c) Spencer Janssen 2007
-- License     :  BSD3-style (see LICENSE)
--
-- Maintainer  :  dons@cse.unsw.edu.au
-- Stability   :  unstable
-- Portability :  not portable, Typeable deriving, mtl, posix
--
-- Operations.
--
-----------------------------------------------------------------------------

module XMonad.Operations where

import XMonad.Core
import XMonad.Layout (Full(..))
import qualified XMonad.StackSet as W

import Data.Maybe
import Data.Monoid          (appEndo)
import Data.List            (nub, (\\), find)
import Data.Bits            ((.|.), (.&.), complement)
import Data.Ratio
import qualified Data.Map as M
import qualified Data.Set as S

import Control.Applicative
import Control.Monad.Reader
import Control.Monad.State
import qualified Control.Exception as C

import System.IO
import System.Posix.Process (executeFile)
import Graphics.X11.Xlib
import Graphics.X11.Xinerama (getScreenInfo)
import Graphics.X11.Xlib.Extras

-- ---------------------------------------------------------------------
-- |
-- Window manager operations
-- manage. Add a new window to be managed in the current workspace.
-- Bring it into focus.
--
-- Whether the window is already managed, or not, it is mapped, has its
-- border set, and its event mask set.
--
manage :: Window -> X ()
manage w = whenX (not <$> isClient w) $ withDisplay $ \d -> do
    sh <- io $ getWMNormalHints d w

    let isFixedSize = sh_min_size sh /= Nothing && sh_min_size sh == sh_max_size sh
    isTransient <- isJust <$> io (getTransientForHint d w)

    (sc, rr) <- floatLocation w
    -- ensure that float windows don't go over the edge of the screen
    let adjust (W.RationalRect x y wid h) | x + wid > 1 || y + h > 1 || x < 0 || y < 0
                                              = W.RationalRect (0.5 - wid/2) (0.5 - h/2) wid h
        adjust r = r

        f ws | isFixedSize || isTransient = W.float w (adjust rr) . W.insertUp w . W.view i $ ws
             | otherwise                  = W.insertUp w ws
            where i = fromMaybe (W.tag . W.workspace . W.current $ ws) $ W.lookupWorkspace sc ws

    mh <- asks (manageHook . config)
    g <- fmap appEndo (runQuery mh w) `catchX` return id
    windows (g . f)

-- | unmanage. A window no longer exists, remove it from the window
-- list, on whatever workspace it is.
--
unmanage :: Window -> X ()
unmanage = windows . W.delete

-- | Modify the size of the status gap at the top of the current screen
-- Taking a function giving the current screen, and current geometry.
modifyGap :: (Int -> (Int,Int,Int,Int) -> (Int,Int,Int,Int)) -> X ()
modifyGap f = do
    windows $ \ws@(W.StackSet { W.current = c@(W.Screen { W.screenDetail = sd }) }) ->
        let n = fromIntegral . W.screen $ c
            g = f n . statusGap $ sd
        in ws { W.current = c { W.screenDetail = sd { statusGap = g } } }

-- | Kill the currently focused client. If we do kill it, we'll get a
-- delete notify back from X.
--
-- There are two ways to delete a window. Either just kill it, or if it
-- supports the delete protocol, send a delete event (e.g. firefox)
--
kill :: X ()
kill = withDisplay $ \d -> withFocused $ \w -> do
    wmdelt <- atom_WM_DELETE_WINDOW  ;  wmprot <- atom_WM_PROTOCOLS

    protocols <- io $ getWMProtocols d w
    io $ if wmdelt `elem` protocols
        then allocaXEvent $ \ev -> do
                setEventType ev clientMessage
                setClientMessageEvent ev w wmprot 32 wmdelt 0
                sendEvent d w False noEventMask ev
        else killClient d w >> return ()

-- ---------------------------------------------------------------------
-- Managing windows

-- | windows. Modify the current window list with a pure function, and refresh
windows :: (WindowSet -> WindowSet) -> X ()
windows f = do
    XState { windowset = old } <- get
    let oldvisible = concatMap (W.integrate' . W.stack . W.workspace) $ W.current old : W.visible old
        ws = f old
    XConf { display = d , normalBorder = nbc, focusedBorder = fbc } <- ask

    mapM_ setInitialProperties (W.allWindows ws \\ W.allWindows old)

    whenJust (W.peek old) $ \otherw -> io $ setWindowBorder d otherw nbc
    modify (\s -> s { windowset = ws })

    -- notify non visibility
    let tags_oldvisible = map (W.tag . W.workspace) $ W.current old : W.visible old
        gottenhidden    = filter (flip elem tags_oldvisible . W.tag) $ W.hidden ws
    mapM_ (sendMessageWithNoRefresh Hide) gottenhidden

    -- for each workspace, layout the currently visible workspaces
    let allscreens     = W.screens ws
        summed_visible = scanl (++) [] $ map (W.integrate' . W.stack . W.workspace) allscreens
    visible <- fmap concat $ forM (zip allscreens summed_visible) $ \ (w, vis) -> do
        let wsp   = W.workspace w
            this  = W.view n ws
            n     = W.tag wsp
            flt   = filter (flip M.member (W.floating ws)) (W.index this)
            tiled = (W.stack . W.workspace . W.current $ this)
                    >>= W.filter (`M.notMember` W.floating ws)
                    >>= W.filter (`notElem` vis)
            (SD (Rectangle sx sy sw sh)
                (gt,gb,gl,gr))          = W.screenDetail w
            viewrect = Rectangle (sx + fromIntegral gl)        (sy + fromIntegral gt)
                                 (sw - fromIntegral (gl + gr)) (sh - fromIntegral (gt + gb))

        -- just the tiled windows:
        -- now tile the windows on this workspace, modified by the gap
        (rs, ml') <- runLayout wsp { W.stack = tiled } viewrect `catchX`
                     runLayout wsp { W.stack = tiled, W.layout = Layout Full } viewrect
        mapM_ (uncurry tileWindow) rs
        updateLayout n ml'

        -- now the floating windows:
        -- move/resize the floating windows, if there are any
        forM_ flt $ \fw -> whenJust (M.lookup fw (W.floating ws)) $
          \(W.RationalRect rx ry rw rh) -> do
            tileWindow fw $ Rectangle
                (sx + floor (toRational sw*rx)) (sy + floor (toRational sh*ry))
                (floor (toRational sw*rw)) (floor (toRational sh*rh))

        let vs = flt ++ map fst rs
        io $ restackWindows d vs
        -- return the visible windows for this workspace:
        return vs

    whenJust (W.peek ws) $ \w -> io $ setWindowBorder d w fbc
    setTopFocus
    asks (logHook . config) >>= userCode

    -- hide every window that was potentially visible before, but is not
    -- given a position by a layout now.
    mapM_ hide (nub oldvisible \\ visible)

    -- all windows that are no longer in the windowset are marked as
    -- withdrawn, it is important to do this after the above, otherwise 'hide'
    -- will overwrite withdrawnState with iconicState
    mapM_ (flip setWMState withdrawnState) (W.allWindows old \\ W.allWindows ws)

    clearEvents enterWindowMask

-- | setWMState.  set the WM_STATE property
setWMState :: Window -> Int -> X ()
setWMState w v = withDisplay $ \dpy -> do
    a <- atom_WM_STATE
    io $ changeProperty32 dpy w a a propModeReplace [fromIntegral v, fromIntegral none]

-- | hide. Hide a window by unmapping it, and setting Iconified.
hide :: Window -> X ()
hide w = whenX (gets (S.member w . mapped)) $ withDisplay $ \d -> do
    io $ do selectInput d w (clientMask .&. complement structureNotifyMask)
            unmapWindow d w
            selectInput d w clientMask
    setWMState w iconicState
    -- this part is key: we increment the waitingUnmap counter to distinguish
    -- between client and xmonad initiated unmaps.
    modify (\s -> s { waitingUnmap = M.insertWith (+) w 1 (waitingUnmap s)
                    , mapped       = S.delete w (mapped s) })

-- | reveal. Show a window by mapping it and setting Normal
-- this is harmless if the window was already visible
reveal :: Window -> X ()
reveal w = withDisplay $ \d -> do
    setWMState w normalState
    io $ mapWindow d w
    modify (\s -> s { mapped = S.insert w (mapped s) })

-- | The client events that xmonad is interested in
clientMask :: EventMask
clientMask = structureNotifyMask .|. enterWindowMask .|. propertyChangeMask

-- | Set some properties when we initially gain control of a window
setInitialProperties :: Window -> X ()
setInitialProperties w = asks normalBorder >>= \nb -> withDisplay $ \d -> do
    setWMState w iconicState
    io $ selectInput d w $ clientMask
    bw <- asks (borderWidth . config)
    io $ setWindowBorderWidth d w bw
    -- we must initially set the color of new windows, to maintain invariants
    -- required by the border setting in 'windows'
    io $ setWindowBorder d w nb

-- | refresh. Render the currently visible workspaces, as determined by
-- the StackSet. Also, set focus to the focused window.
--
-- This is our 'view' operation (MVC), in that it pretty prints our model
-- with X calls.
--
refresh :: X ()
refresh = windows id

-- | clearEvents.  Remove all events of a given type from the event queue.
clearEvents :: EventMask -> X ()
clearEvents mask = withDisplay $ \d -> io $ do
    sync d False
    allocaXEvent $ \p -> fix $ \again -> do
        more <- checkMaskEvent d mask p
        when more again -- beautiful

-- | tileWindow. Moves and resizes w such that it fits inside the given
-- rectangle, including its border.
tileWindow :: Window -> Rectangle -> X ()
tileWindow w r = withDisplay $ \d -> do
    bw <- (fromIntegral . wa_border_width) <$> io (getWindowAttributes d w)
    -- give all windows at least 1x1 pixels
    let least x | x <= bw*2  = 1
                | otherwise  = x - bw*2
    io $ moveResizeWindow d w (rect_x r) (rect_y r)
                              (least $ rect_width r) (least $ rect_height r)
    reveal w

-- ---------------------------------------------------------------------

-- | Returns True if the first rectangle is contained within, but not equal
-- to the second.
containedIn :: Rectangle -> Rectangle -> Bool
containedIn r1@(Rectangle x1 y1 w1 h1) r2@(Rectangle x2 y2 w2 h2)
 = and [ r1 /= r2
       , x1 >= x2
       , y1 >= y2
       , fromIntegral x1 + w1 <= fromIntegral x2 + w2
       , fromIntegral y1 + h1 <= fromIntegral y2 + h2 ]

-- | Given a list of screens, remove all duplicated screens and screens that
-- are entirely contained within another.
nubScreens :: [Rectangle] -> [Rectangle]
nubScreens xs = nub . filter (\x -> not $ any (x `containedIn`) xs) $ xs

-- | Cleans the list of screens according to the rules documented for
-- nubScreens.
getCleanedScreenInfo :: MonadIO m => Display -> m [Rectangle]
getCleanedScreenInfo = io .  fmap nubScreens . getScreenInfo

-- | rescreen.  The screen configuration may have changed (due to
-- xrandr), update the state and refresh the screen, and reset the gap.
rescreen :: X ()
rescreen = do
    xinesc <- withDisplay getCleanedScreenInfo

    windows $ \ws@(W.StackSet { W.current = v, W.visible = vs, W.hidden = hs }) ->
        let (xs, ys) = splitAt (length xinesc) $ map W.workspace (v:vs) ++ hs
            (a:as)   = zipWith3 W.Screen xs [0..] $ zipWith SD xinesc gs
            sgs      = map (statusGap . W.screenDetail) (v:vs)
            gs       = take (length xinesc) (sgs ++ repeat (0,0,0,0))
        in  ws { W.current = a
               , W.visible = as
               , W.hidden  = ys }

-- ---------------------------------------------------------------------

-- | setButtonGrab. Tell whether or not to intercept clicks on a given window
setButtonGrab :: Bool -> Window -> X ()
setButtonGrab grab w = withDisplay $ \d -> io $
    if grab
        then forM_ [button1, button2, button3] $ \b ->
            grabButton d b anyModifier w False buttonPressMask
                       grabModeAsync grabModeSync none none
        else ungrabButton d anyButton anyModifier w

-- ---------------------------------------------------------------------
-- Setting keyboard focus

-- | Set the focus to the window on top of the stack, or root
setTopFocus :: X ()
setTopFocus = withWindowSet $ maybe (setFocusX =<< asks theRoot) setFocusX . W.peek

-- | Set focus explicitly to window 'w' if it is managed by us, or root.
-- This happens if X notices we've moved the mouse (and perhaps moved
-- the mouse to a new screen).
focus :: Window -> X ()
focus w = withWindowSet $ \s -> do
    if W.member w s then when (W.peek s /= Just w) $ windows (W.focusWindow w)
                    else whenX (isRoot w) $ setFocusX w

-- | Call X to set the keyboard focus details.
setFocusX :: Window -> X ()
setFocusX w = withWindowSet $ \ws -> do
    dpy <- asks display

    -- clear mouse button grab and border on other windows
    forM_ (W.current ws : W.visible ws) $ \wk -> do
        forM_ (W.index (W.view (W.tag (W.workspace wk)) ws)) $ \otherw -> do
            setButtonGrab True otherw

    -- If we ungrab buttons on the root window, we lose our mouse bindings.
    whenX (not <$> isRoot w) $ setButtonGrab False w
    io $ do setInputFocus dpy w revertToPointerRoot 0
            -- raiseWindow dpy w

------------------------------------------------------------------------
-- Message handling

-- | Throw a message to the current LayoutClass possibly modifying how we
-- layout the windows, then refresh.
sendMessage :: Message a => a -> X ()
sendMessage a = do
    w <- W.workspace . W.current <$> gets windowset
    ml' <- handleMessage (W.layout w) (SomeMessage a) `catchX` return Nothing
    whenJust ml' $ \l' -> do
        windows $ \ws -> ws { W.current = (W.current ws)
                                { W.workspace = (W.workspace $ W.current ws)
                                  { W.layout = l' }}}

-- | Send a message to all layouts, without refreshing.
broadcastMessage :: Message a => a -> X ()
broadcastMessage a = withWindowSet $ \ws -> do
   let c = W.workspace . W.current $ ws
       v = map W.workspace . W.visible $ ws
       h = W.hidden ws
   mapM_ (sendMessageWithNoRefresh a) (c : v ++ h)

-- | Send a message to a layout, without refreshing.
sendMessageWithNoRefresh :: Message a => a -> W.Workspace WorkspaceId (Layout Window) Window -> X ()
sendMessageWithNoRefresh a w =
    handleMessage (W.layout w) (SomeMessage a) `catchX` return Nothing >>=
    updateLayout  (W.tag w)

-- | Update the layout field of a workspace
updateLayout :: WorkspaceId -> Maybe (Layout Window) -> X ()
updateLayout i ml = whenJust ml $ \l ->
    runOnWorkspaces $ \ww -> return $ if W.tag ww == i then ww { W.layout = l} else ww

-- | Set the layout of the currently viewed workspace
setLayout :: Layout Window -> X ()
setLayout l = do
    ss@(W.StackSet { W.current = c@(W.Screen { W.workspace = ws })}) <- gets windowset
    handleMessage (W.layout ws) (SomeMessage ReleaseResources)
    windows $ const $ ss {W.current = c { W.workspace = ws { W.layout = l } } }

------------------------------------------------------------------------
-- Utilities

-- | Return workspace visible on screen 'sc', or Nothing.
screenWorkspace :: ScreenId -> X (Maybe WorkspaceId)
screenWorkspace sc = withWindowSet $ return . W.lookupWorkspace sc

-- | Apply an X operation to the currently focused window, if there is one.
withFocused :: (Window -> X ()) -> X ()
withFocused f = withWindowSet $ \w -> whenJust (W.peek w) f

-- | True if window is under management by us
isClient :: Window -> X Bool
isClient w = withWindowSet $ return . W.member w

-- | Combinations of extra modifier masks we need to grab keys\/buttons for.
-- (numlock and capslock)
extraModifiers :: X [KeyMask]
extraModifiers = do
    nlm <- asks (numlockMask . config)
    return [0, nlm, lockMask, nlm .|. lockMask ]

-- | Strip numlock\/capslock from a mask
cleanMask :: KeyMask -> X KeyMask
cleanMask km = do
    nlm <- asks (numlockMask . config)
    return (complement (nlm .|. lockMask) .&. km)

-- | Get the Pixel value for a named color
initColor :: Display -> String -> IO (Maybe Pixel)
initColor dpy c = C.handle (\_ -> return Nothing) $
    (Just . color_pixel . fst) <$> allocNamedColor dpy colormap c
    where colormap = defaultColormap dpy (defaultScreen dpy)

------------------------------------------------------------------------

-- | @restart name resume@. Attempt to restart xmonad by executing the program
-- @name@.  If @resume@ is 'True', restart with the current window state.
-- When executing another window manager, @resume@ should be 'False'.
restart :: String -> Bool -> X ()
restart prog resume = do
    broadcastMessage ReleaseResources
    io . flush =<< asks display
    args <- if resume then gets (("--resume":) . return . showWs . windowset) else return []
    catchIO (executeFile prog True args Nothing)
 where showWs = show . W.mapLayout show

------------------------------------------------------------------------
-- | Floating layer support

-- | Given a window, find the screen it is located on, and compute
-- the geometry of that window wrt. that screen.
floatLocation :: Window -> X (ScreenId, W.RationalRect)
floatLocation w = withDisplay $ \d -> do
    ws <- gets windowset
    wa <- io $ getWindowAttributes d w
    bw <- fi <$> asks (borderWidth . config)

    -- XXX horrible
    let sc = fromMaybe (W.current ws) $ find (pointWithin (fi $ wa_x wa) (fi $ wa_y wa) . screenRect . W.screenDetail) $ W.screens ws
        sr = screenRect . W.screenDetail $ sc
        rr = W.RationalRect ((fi (wa_x wa) - fi (rect_x sr)) % fi (rect_width sr))
                            ((fi (wa_y wa) - fi (rect_y sr)) % fi (rect_height sr))
                            (fi (wa_width  wa + bw*2) % fi (rect_width sr))
                            (fi (wa_height wa + bw*2) % fi (rect_height sr))

    return (W.screen $ sc, rr)
  where fi x = fromIntegral x
        pointWithin :: Integer -> Integer -> Rectangle -> Bool
        pointWithin x y r = x >= fi (rect_x r) &&
                            x <  fi (rect_x r) + fi (rect_width r) &&
                            y >= fi (rect_y r) &&
                            y <  fi (rect_y r) + fi (rect_height r)

-- | Make a tiled window floating, using its suggested rectangle
float :: Window -> X ()
float w = do
    (sc, rr) <- floatLocation w
    windows $ \ws -> W.float w rr . fromMaybe ws $ do
        i  <- W.findTag w ws
        guard $ i `elem` map (W.tag . W.workspace) (W.screens ws)
        f  <- W.peek ws
        sw <- W.lookupWorkspace sc ws
        return (W.focusWindow f . W.shiftWin sw w $ ws)

-- ---------------------------------------------------------------------
-- Mouse handling

-- | Accumulate mouse motion events
mouseDrag :: (Position -> Position -> X ()) -> X () -> X ()
mouseDrag f done = do
    drag <- gets dragging
    case drag of
        Just _ -> return () -- error case? we're already dragging
        Nothing -> do
            XConf { theRoot = root, display = d } <- ask
            io $ grabPointer d root False (buttonReleaseMask .|. pointerMotionMask)
                    grabModeAsync grabModeAsync none none currentTime
            modify $ \s -> s { dragging = Just (motion, cleanup) }
 where
    cleanup = do
        withDisplay $ io . flip ungrabPointer currentTime
        modify $ \s -> s { dragging = Nothing }
        done
    motion x y = do z <- f x y
                    clearEvents pointerMotionMask
                    return z

-- | XXX comment me
mouseMoveWindow :: Window -> X ()
mouseMoveWindow w = whenX (isClient w) $ withDisplay $ \d -> do
    io $ raiseWindow d w
    wa <- io $ getWindowAttributes d w
    (_, _, _, ox', oy', _, _, _) <- io $ queryPointer d w
    let ox = fromIntegral ox'
        oy = fromIntegral oy'
    mouseDrag (\ex ey -> io $ moveWindow d w (fromIntegral (fromIntegral (wa_x wa) + (ex - ox)))
                                             (fromIntegral (fromIntegral (wa_y wa) + (ey - oy))))
              (float w)

-- | XXX comment me
mouseResizeWindow :: Window -> X ()
mouseResizeWindow w = whenX (isClient w) $ withDisplay $ \d -> do
    io $ raiseWindow d w
    wa <- io $ getWindowAttributes d w
    sh <- io $ getWMNormalHints d w
    io $ warpPointer d none w 0 0 0 0 (fromIntegral (wa_width wa)) (fromIntegral (wa_height wa))
    mouseDrag (\ex ey -> do
                 io $ resizeWindow d w `uncurry`
                    applySizeHints sh (ex - fromIntegral (wa_x wa),
                                       ey - fromIntegral (wa_y wa)))
              (float w)

-- ---------------------------------------------------------------------
-- | Support for window size hints

type D = (Dimension, Dimension)

-- | Reduce the dimensions if needed to comply to the given SizeHints.
applySizeHints :: Integral a => SizeHints -> (a,a) -> D
applySizeHints sh (w,h) = applySizeHints' sh (fromIntegral $ max 1 w,
                                              fromIntegral $ max 1 h)

-- | XXX comment me
applySizeHints' :: SizeHints -> D -> D
applySizeHints' sh =
      maybe id applyMaxSizeHint                   (sh_max_size   sh)
    . maybe id (\(bw, bh) (w, h) -> (w+bw, h+bh)) (sh_base_size  sh)
    . maybe id applyResizeIncHint                 (sh_resize_inc sh)
    . maybe id applyAspectHint                    (sh_aspect     sh)
    . maybe id (\(bw,bh) (w,h)   -> (w-bw, h-bh)) (sh_base_size  sh)

-- | Reduce the dimensions so their aspect ratio falls between the two given aspect ratios.
applyAspectHint :: (D, D) -> D -> D
applyAspectHint ((minx, miny), (maxx, maxy)) x@(w,h)
    | or [minx < 1, miny < 1, maxx < 1, maxy < 1] = x
    | w * maxy > h * maxx                         = (h * maxx `div` maxy, h)
    | w * miny < h * minx                         = (w, w * miny `div` minx)
    | otherwise                                   = x

-- | Reduce the dimensions so they are a multiple of the size increments.
applyResizeIncHint :: D -> D -> D
applyResizeIncHint (iw,ih) x@(w,h) =
    if iw > 0 && ih > 0 then (w - w `mod` iw, h - h `mod` ih) else x

-- | Reduce the dimensions if they exceed the given maximum dimensions.
applyMaxSizeHint  :: D -> D -> D
applyMaxSizeHint (mw,mh) x@(w,h) =
    if mw > 0 && mh > 0 then (min w mw,min h mh) else x

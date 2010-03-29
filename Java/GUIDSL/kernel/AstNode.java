

import Jakarta.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Vector;

// end of Main class

    //**************************************************
    // class AstNode
    //**************************************************
    
public abstract class AstNode implements Cloneable, Serializable {

    public  AstTokenInterface[]     tok = null;
    public  AstNode[]   arg = null;
    public  AstNode     right = null;
    public  AstNode     left = null;
    public  AstNode     up = null;

    public int stackMarker;

    public abstract boolean[] printorder();

    private static Class obj_class;

    static public Stack aliasStack = new Stack();

    static {
        try {
            obj_class = Class.forName( "java.lang.Object" );
        }
        catch ( Exception e ) {}
    }

    //     public AstNode(int n) {}

            // the following methods are needed to make
            // writing code templates and layer files easier.
            // the methods themselves will be overridden by
            // the Gscope layer.  The problem is that AST
            // generates calls to these methods and it shouldn't
              // good example for refactoring...

    public static  AstNode markStack( int size,  AstNode n ) {
        n.stackMarker = size;
        return n;
    }

    // needed for bootstrapping JakBasic
    public  AstNode markStack() {
        return ( AstNode ) this;
    }

    public  AstNode patch() {
        return ( AstNode ) this;
    }

    //**************************************************
    // For conversion to a String
    //**************************************************
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter( baos );
        AstProperties props = new  AstProperties();

        props.setProperty( "output", pw );
        print( props );
        pw.flush();
        return ( baos.toString() );
    }

        // safeCopy is used in code escapes -- it either returns
    // the code fragment to be plugged in if it is detached,
    // or it supplies a copy that is to be plugged in.  It is
    // included in kernel layer because generated code may
    // be part of a JTS-or-AHEAD tool that doesn't have an
    // ast layer

    public static  AstNode safeCopy( AstNode it ) {
        if ( it == null )
            return null;

        if ( kernelConstants.copyLists && it instanceof  AstList )
            return ( AstNode ) it.clone();

        if ( it.up != null || it.left != null || it.right != null ) {
            AstNode copy = ( AstNode ) it.clone();
            return copy;
        }
        return it;
    }

    // this method deals with the awkward situation where an
    // object is to be cloned, but just happends to be null
    //
    public static Object copy( AstNode it ) {
        if ( it == null )
            return null;
        return it.clone();
    }
        
    public Object clone() {
        AstNode copy;

        copy = null;
        try {
            copy = ( AstNode ) getClass().newInstance();
        }
        catch ( Exception e ) {
            fatalError( "Can't clone "+getClass() +  e.getMessage() );
        }
        initClone( copy );
        return ( copy );
    }

    // Copy of an AstNode copies only it's children
    protected void initClone( AstNode copy ) {
        int i;

        if ( tok != null ) {
            copy.tok = new  AstTokenInterface[tok.length];
            for ( i=0; i < tok.length; i++ )
                if ( tok[i] != null )
                    copy.tok[i] = ( AstTokenInterface ) tok[i].clone();
        }
        if ( arg != null ) {
            copy.arg = new  AstNode[arg.length];
            for ( i=0; i < arg.length; i++ ) {
                if ( arg[i] != null ) {
                    copy.arg[i] = ( AstNode ) arg[i].clone();
                // copy.arg[i].up = copy;
                }
            }
        }

        // initialize children pointers, if this hasn't been done
        // already.  AstListNodes already have been initialized
        if ( ! ( copy instanceof  AstList ) )
            copy.InitChildren();
    }

    // We would like to locate a given ancestor that is an instance of the
    // class specified (classname) or a derived class of that.

    public  AstNode hasAncestor( String classname ) {
        Class ancestorClass;
        String ancestorBaseName;
        AstNode ancestor;

        ancestor = up;
        while ( ancestor != null ) {
            ancestorClass = ancestor.getClass();
            ancestorBaseName = Util.baseType( ancestorClass );
            while ( ancestorClass != obj_class ) {
                if ( classname.compareTo( ancestorBaseName ) == 0 )
                    return ( ancestor );
                ancestorClass = ancestorClass.getSuperclass();
            }
            ancestor = ancestor.up;
        }
        return ( null );
    }

    // InitChildren() initializes the left, right, and up pointers of
    // children nodes.

    public void InitChildren() {
        int i;
        AstNode previous;
        up = left = right = null;

        // Step 1: init node id

        if ( arg[0] == null )
            return; // no children, therefore nothing to initialize

        // Step 2: initialize up and left for each child

        previous = null;
        for ( i=0; i<arg.length; i++ ) {

            if ( kernelConstants.debugAST ) {
                if ( arg[i].up != null )
                    fatalError( "InitChildren called with non-detached arguments" 
                                                 + arg[i].getClass().getName() + " " + arg[i] );
            }

            arg[i].up   = ( AstNode ) this;
            arg[i].left = previous;
            previous    = arg[i];
        }

        // Step 3: initialize right for each child

        previous = null;
        for ( i=arg.length-1; i>=0; i-- ) {
            arg[i].right = previous;
            previous = arg[i];
        }
    }

    public void PrettyDump() {
        PDump( "" );
    }

    public void PDump( String indent ) {
        String ind;
        int i;
       
        //             System.out.println(indent + indent.length() + className() +
        //                                "\t(" + hashCode() + ")");
        System.out.println( indent + indent.length() + className() );
        if ( arg == null )
            return;
        else {
            ind = indent + " ";
            for ( i=0; i<arg.length; i++ )
                if ( arg[i] != null )
                    arg[i].PDump( ind );
        }
    }

    // Replace(k) swaps the current node with node k.  Replace returns
    // k as a result.

    public  AstNode Replace( AstNode withnode ) {

        // Step 1: adjust right pointer

        withnode.right = right;
        if ( right != null )
            right.left = withnode;
        right = null;

        // Step 2: adjust left pointer

        withnode.left = left;
        if ( left != null )
            left.right = withnode;
        left = null;

        // Step 3: adjust up pointer

        withnode.up = up;
        if ( up != null )
            up.ReplaceRef( ( AstNode ) this, withnode );
        up = null;

        // Step 4: return withnode

        return withnode;
    }

    // ReplaceRef(c,n) replaces reference to child c with Ast node n

    public void ReplaceRef( AstNode oldnode,  AstNode newnode ) {
        int i;

        for ( i = 0; i < arg.length; i++ ) {
            if ( arg[i] == oldnode ) {
                arg[i] = newnode;
                return;
            }
        }
        System.out.println( "\nFatal Error - inconsistent AST reference" );
    }

    // print() unparses the contents of the AST rooted at the current node.
    // reduce2java() prints the Java code that is defined by the Jakarta
    //        AST that is rooted at the current node
    // reduce2ast() prints the constructors that are needed to reconstruct
    //        the ASt that is rooted at the current node.

    public void print() {
        boolean order[];
        int     t, n, i;

        order = printorder();
        t = 0;
        n = 0;
        for ( i=0; i<order.length; i++ ) {
            // if order[i] is true; print token else print nonterminal

            if ( order[i] )
                tok[t++].print();
            else
                arg[n++].print();
        }
    }

    public void print( AstProperties props ) {
        boolean order[];
        int     t, n, i;

        order = printorder();
        t = 0;
        n = 0;
        for ( i=0; i<order.length; i++ ) {
            // if order[i] is true; print token else print nonterminal

            if ( order[i] )
                tok[t++].print( props );
            else
                arg[n++].print( props );
        }
    }

    public void reduce2java( AstProperties props ) {
        boolean order[];
        int     t, n, i;

        order = printorder();
        t = 0;
        n = 0;
        for ( i=0; i<order.length; i++ ) {
            // if order[i] is true; print token else print nonterminal

            if ( order[i] )
                tok[t++].reduce2java( props );
            else
                arg[n++].reduce2java( props );
        }
    }

    // Print_Only_Tokens() prints only the tokens of a given node. This
    // method is used only for the printing of lists.  Similarly,
    // Print_Only_Tokens_Ast() prints the ASTs for tokens of a given node.
    // It too is used only for the reduction of lists.

    public void Print_Only_Tokens( AstProperties props ) {
        int i;

        if ( tok == null )
            return;

        for ( i=0; i<tok.length; i++ )
            tok[i].print( props );
    }

    public void Print_Only_Tokens() {
        int i;

        if ( tok == null )
            return;

        for ( i=0; i<tok.length; i++ )
            tok[i].print();
    }

    boolean instanceOf( Class myclass ) {
        Class c;

        for ( c = this.getClass(); c != null; c = c.getSuperclass() )
            if ( c == myclass )
                return ( true );
        return ( false );
    }

    public boolean instanceOf( Object obj ) {
        Class myclass;

        myclass = obj.getClass();
        return ( instanceOf( myclass ) );
    }
    
    public boolean instanceOf( String class_name ) {
        Class myclass;

        try {
            myclass = Class.forName( class_name );
        }
        catch ( ClassNotFoundException e ) {
            System.err.println( "Couldn't find class "+class_name );
            return ( false );
        }
        return ( instanceOf( myclass ) );
    }

    public String className() {
        return ( kernelConstants.globals().LangName + Util.baseType( this ) );
    }

    public boolean Equ( AstNode x ) {
        int i;
   
        // Step 1: check to see if x is null; if so, return false
   
        if ( x == null )
            return false;
  
        // Step 2: must have equal types
 
        if ( getClass() != x.getClass() )
            return false;

        // Step 3: must have equal #s of tokens and equal tokens

        if ( tok != x.tok ) {
            if ( tok != null && x.tok != null && tok.length != x.tok.length )
                return false;
            else
                for ( i=0; i< tok.length; i++ ) {
                    if ( tok[i] != x.tok[i] && tok[i] != null &&
                                                                !tok[i].Equ( ( AstTokenInterface ) x.tok[i] ) )
                        return false;
                }
        }
        // Step 4: must have equal #s of arguments and equal arguments
  
        if ( arg != null && x.arg != null && arg.length != x.arg.length )
            return false;
        else
            for ( i=0; i< arg.length; i++ ) {
                if ( arg[i] != x.arg[i] && arg[i] != null &&
                                                !arg[i].Equ( x.arg[i] ) )
                    return false;
            }
   
        // Step 6: we got this far, must be equal
   
        return true;
    }

    // dumpnode() is used for debugging small ASTs.  It prints the id and
    // type of a node, plus its left,right,up, and arg pointers for all
    // nodes in an AST

    public void dumpnode() {
        int i;
        AstList l;

        // Step 1: print the nodeid, class, and up, left, and right pointers

        System.out.print( hashCode() + " " + className() );
        if ( up == null )
            System.out.print( "\tup:*" );
        else
            System.out.print( "\tup:"+up.hashCode() );
        if ( left == null )
            System.out.print( "\tleft:*" );
        else
            System.out.print( "\tleft:"+left.hashCode() );
        if ( right == null )
            System.out.print( "\trite:*" );
        else
            System.out.print( "\trite:"+right.hashCode() );

        // Step 2: print out last (if it exists)

        if ( this instanceof  AstList ) {
            l = ( AstList ) this;

            if ( l.last == null )
                System.out.print( "\tlast:*" );
            else
                System.out.print( "\tlast:"+l.last.hashCode() );
        }

        // Step 2: print out arguments

        for ( i = 0; i < arg.length; i++ ) {
            if ( arg[i] == null )
                System.out.print( "\targ[" + i + "]:*" );
            else
                System.out.print( "\targ[" + i + "]:"+arg[i].hashCode() );
        }
        System.out.println( "" );

        // Step 3: validate pointers

        if ( left != null && left.right != this )
            System.out.print( "l<->r pointers invalid" );

        if ( right != null && right.left != this )
            System.out.print( "r<->l pointers invalid" );

        if ( arg != null && arg[0] != null && arg[0].up != this )
            System.out.print( "d<->u pointers invalid" );

        // Step 4: recurse

        for ( i=0; i < arg.length; i++ ) {
            if ( arg[i] != null )
                arg[i].dumpnode();
        }
    }

    // Delete() the current node.  Nodes can be deleted only in the following
    // conditions:
    // the node is an element of a list (i.e., up instanceof AstListNode)
    // the node is optional (i.e., up instanceof AstOptNode)
    // the node is a list (i.e., this instanceof AstList)

    // if node is a list, AstList::Delete overrides, so all we need to
    // do here is handle the first two cases.

    public void Delete() { // delete this node

        // Step 1: see if parent is an instance of AstList
        //         if so, remove it instead.

        if ( up instanceof  AstListNode ) {
            ( ( AstListNode ) up ).Delete();
            return;
        }

        // Step 2: else, see if parent is an instance of
        //         AstOptNode.  if so, remove it instead

        if ( up instanceof  AstOptNode ) {
            ( ( AstOptNode ) up ).Delete();
            return;
        }

        // Step 3: else Fatal error - can only delete nodes
        //         that are instances of lists

        fatalError( "cannot delete non-list node" );
    }

    // AddAfter(y) adds AstList y after the current node.  this is possible
    // if the current node's parent (up) is an instance of AstListNode (that
    // is, the current node is on a list.

    public void AddAfter( AstList tree ) { // add tree after this node
        if ( up instanceof  AstListNode ) {
            ( ( AstListNode ) up ).AddAfter( tree );
        }
        else
            fatalError( "illegal AddAfter" );
    }

    // AddBefore(y) adds AstList y before the current node.  this is possible
    // if the current node's parent (up) is an instance of AstListNode (that
    // is, the current node is on a list.

    public void AddBefore( AstList tree ) { // add tree before this node
        if ( up instanceof  AstListNode ) {
            ( ( AstListNode ) up ).AddBefore( tree );
        }
        else
            fatalError( "illegal AddAfter" );
    }

    //**************************************************
    // This method adds the comment given by the parameter to
    // the first AstToken.
    //**************************************************
    public  AstNode addComment( String comment ) {
        return ( addComment( comment, false ) );
    }

    public  AstNode addComment( String comment, boolean replace ) {
        boolean order[] = printorder();
        int t, n, i;
        AstToken ast_token;

        t = 0; // terminal index
        n = 0; // non-terminal index
        for ( i=0; i < order.length; i++ ) {
            // if order[i] is true, a terminal is next; else a non-terminal
            if ( order[i] ) {
                // Terminal can be a token or non-token
                if ( tok[t] instanceof AstToken ) {
                    ast_token = ( AstToken ) tok[t];
                    if ( replace )
                        ast_token.white_space = comment;
                    else
                        ast_token.white_space = comment +
                                                                                                ast_token.white_space;
                    return ( ( AstNode ) this );
                }
                if ( ( ( AstOptToken ) tok[t] ).addComment( comment, replace ) != null )
                    return ( ( AstNode ) this );
                t++;
            }
            else
                if ( ( ( AstNode ) arg[n++] ).addComment( comment, replace ) != null )
                    return ( ( AstNode ) this );
        }
        return ( null );
    }

    // This method gets code generated as the first call to add a comment
    // to an AST. It's main purpose is to determine that the AST is
    // non-null before calling the addComment of the instance.
    public static  AstNode addComment( AstNode ast,
                          String comment ) {
        return ( addComment( ast, comment, false ) );
    }

    public static  AstNode addComment( AstNode ast,
                          String comment,
                          boolean replace ) {
        if ( ast == null )
            return ( null );
        return ( ast.addComment( comment, replace ) );
    }

    // This method is used to repair ASTs that share subtrees.
    // the idea is to walk the tree looking for nodes that don't
    // point to their parent.  In such cases, the subtree is cloned
    // and the result is that normalized trees don't have shared
    // subtrees

    public  AstNode normalizeTree( AstNode parnt ) {
        int i;
        if ( arg == null )
            return ( AstNode ) this;
        for ( i=0; i<arg.length; i++ )
            if ( arg[i] != null )
                arg[i] = ( AstNode ) arg[i].normalizeTree( ( AstNode ) this );

        // if our up pointer != down pointer parnt, we must
        // clone ourselves

        if ( parnt == null )
            return ( AstNode ) this; // at root
        if ( up != parnt ) {
            return ( AstNode ) this.clone();
        }
        else
            return ( AstNode ) this;
    }

    public void normalize() {
        normalizeTree( ( AstNode ) this );
    }

    // Detach isolates a subtree from its left, up, and right
    // node relations.  Useful for detaching a tree from an
    // existing tree

    public void Detach() {
        up    = null;
        left  = null;
        right = null;
    }

    // writeTree serializes an AST to a file

    public void writeTree( String fileName ) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream( new FileOutputStream( fileName + ".ser" ) );
        }
        catch( Exception e ) {
            fatalError( e.getMessage() );
        }

        try {
            os.writeObject( this );
        }
        catch( Exception e ) {
            fatalError( e.getMessage() );
        }

        try {
            os.close();
        }
        catch( Exception e ) {
            fatalError( e.getMessage() );
        }
    }

    // error and warning reporting messages
    // format: <tool> : <file> : <line-number (optional)> : <message>

    private static String eformat( String msg ) {
        return kernelConstants.PackageName + ": " +
              kernelConstants.globals().currentFileName + msg ;
    }

    static public void report( String msg ) {
        Util.report( eformat( msg ) );
    }

    static public void toolReport( String msg ) {
        Util.report( kernelConstants.PackageName + ": " + msg );
        System.exit( 1 );
    }

    static public int errorCount() {
        return Util.errorCount();
    }
         
    static public void fatalError( AstTokenInterface t, String msg ) {
        int lineno = ( ( AstToken ) t ).lineNum();
        Util.fatalError( eformat( ":" + lineno + ": Fatal Error " + msg ) );
    }

    static public void fatalError( Exception e, String msg ) {
        Util.report( eformat( ": Fatal Error (" + e + ") " + msg ) );
        Util.fatalError( e );
    }

    static public void fatalError( String msg ) {
        Util.fatalError( eformat( ": Fatal Error " + msg ) );
    }

    static public void parseError( String msg ) {
        Util.report( eformat( ": Parse Error " + msg ) );
        System.exit( 1 );
    }

    static public void parseError2( String msg ) {
        Util.report( msg );
        System.exit( 1 );
    }

    static public void override( String msg, Object o ) {
        Util.fatalError( eformat( msg + " should be overridden in class " 
                   + o.getClass().getName() ) );
    }

    static public void override( AstTokenInterface t, 
               String msg, Object o ) {
        int lineno = ( ( AstToken ) t ).lineNum();
        Util.fatalError( eformat( ":" + lineno + ": Fatal Error " + 
                   msg + " should be overridden in class " +
                   o.getClass().getName() ) );
    }

    static public void error( AstTokenInterface t, String msg ) {
        int lineno = ( ( AstToken ) t ).lineNum();
        Util.error( eformat( ":" + lineno + ": Error " + msg ) );
    }

    static public void error( String msg ) {
        Util.error( eformat( ": Error " + msg ) );
    }

    static public void warning( AstTokenInterface t, String msg ) {
        int lineno = ( ( AstToken ) t ).lineNum();
        Util.warning( eformat( ":" +  lineno + ": Warning " + msg ) );
    }

    static public void warning( String msg ) {
        Util.warning( eformat( ": Warning " + msg ) );
    }
}

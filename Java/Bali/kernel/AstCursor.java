

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

//**************************************************
// Class AstCursor
//**************************************************
    
public class AstCursor {
    // node references the current node of the cursor
    // in the case of node deletions and replacements,
    // NextIsSet to true, and next is set to the next node
    // to search.  root is the root of the AST to search

    public  AstNode node; // cursor references this node
    boolean             NextIsSet; // set true by Sibling, Delete, Replace
    public  AstNode next; // set by Sibling, Delete, Replace
    AstNode        root; // root of the search tree
    AstNode        CurrElem; // next element in list (used for
    // searching lists only!
    AstNode        NextElem; // for list-searching only!

    // AstCursor constructor just nullifies all of its data members

    public AstCursor() {
        node = null;
        NextIsSet = false;
        next = null;
        root = null;
    }

    // note: to scan an AST rooted at t, use the following
    // AstCursor c;
    // for (c.First(t); c.More(); c.PlusPlus()) { ... }

    public  AstNode Root() {
        return root;
    }

    public void First() {
        First( root );
    }

    // First(r) finds first nonstructural node (i.e., the first
    // node that isn't an AstListNode or an AstOptNode) in the
    // tree rooted by r.  First also remembers the root of the
    // tree.  If no such node can be found, data member node == null.

    public void First( AstNode r ) {
        root = r;
        node = r;
        NextIsSet = false;
        next = null;

        while ( node != null &&
                   ( node instanceof  AstListNode ||
                    node instanceof  AstOptNode ) ) {
            GetNext();
        }
    }

    // More() returns true if there are additional nodes to
    // examine in the AST rooted at root.

    public boolean More() {
        return node != null;
    }

    // PlusPlus() repositions the cursor on the next nonstructural
    // node of the AST.  If there is no such node, data member node
    // is set to null.

    public void PlusPlus() { // got next printable node
        GetNext();
        while ( node != null &&
                   ( node instanceof  AstListNode ||
                    node instanceof  AstOptNode ) ) {
            GetNext();
        }
    }

    //  GetNext() is a method that is internal to the AstCursor class,
    // and should not be exported.  It does the primitive work of
    // advancing a cursor to the next structural or nonstructural
    // node of a tree.  It is an internal method (i.e., one that isn't
    // exported) as it is shared by several exported methods of
    // AstCursor class.

    void GetNext() {

        // Step 1: check if the next pointer has already been determined
        //         by the Replace() or Delete() methods.  if so, use it
        //         and exit

        if ( NextIsSet ) {
            NextIsSet = false;
            node = next;
            next = null;
            return;
        }

        if ( node == null )
            return;

        // Step 2:  else, if there is a child, return it

        if ( node.arg[0] != null ) {
            node = node.arg[0];
            return;
        }

        // Step 3: else, skip to next sibling

        skip();
    }

    // skip() is an internal method (i.e., one that is not exported) by AstClass.
    // it does the work of advancing a cursor to its immediate sibling or relative
    // (but not child).  It is internal to AstClass as it is shared by several
    // exported methods.

    void skip() {

        // Step 1: if we're already positioned on the next node (virtually)
        //         then actually position the cursor before performing a skip

        if ( NextIsSet ) {
            NextIsSet = false;
            node = next;
            next = null;
        }

        if ( node == root ) {
            node = null;
            return;
        }

        // Step 1: if there is a right sibling, return it

        if ( node.right != null ) {
            node = node.right;
            return;
        }

        // Step 2: otherwise, begin examining ancestors
        //         if the ancestor is a root (the starting node of
        //         the search) or the ancestor is null, terminate
        //         the search. Otherwise, continue to search for
        //         an ancestor with a nonnull right pointer.

        while ( true ) {
            node = node.up;

            // Step 2a: stop if we're at the top of the AST or we're at
            //          the root.

            if ( node == null || node == root ) {
                node = null;
                return;
            }

            // Step 2b: otherwise, proceed to right sibling

            if ( node.right != null ) {
                node = node.right;
                return;
            }

        // Step 2c: else, go up.
        }
    }

    // ContinueFrom(k) is an internal method to AstCursor.  It tells the cursor
    // advancement methods that the next node to examine in the tree is node k.
    // ContinueFrom(k) is called by Sibling(), Replace(), and Delete();

    void ContinueFrom( AstNode k ) {
        NextIsSet = true;
        next      = k;
        node      = null;
    }

    public void Position( AstNode x ) {
        AstNode parentNode;

        if ( x != null ) {
            // validate x as a child of r
            parentNode = x;
            while ( parentNode != null ) {
                if ( parentNode == root )
                    break;
                parentNode = parentNode.up;
            }
            if ( parentNode == null ) {
                // x is not a child of root
                node = null;
                return;
            }
        }

        node = x;
        NextIsSet = false;
    }

    public void Position( AstNode r,  AstNode x ) {
        root = r;
        Position( x );
    }

    // Sibling() allows one to skip the searching of the current node's children
    // and to proceed directly to searching the current node's sibling (or next
    // relative.  A typical use for this would be (a) locate a particular node
    // in an AST, and (knowing that a recurrence of that node will never happen
    // in its children) to continue searching from the node's sibling:
    //
    // example:
    // AstCursor c = new AstCursor();
    //
    // for (c.First(t); c.More(); c.PlusPlus()) {
    //   if (c instanceof $TEqn.AstClass) {
    //       ... found an instance of the Class construct
    //       c.Sibling(); // won't find another instance of Class construct
    //                    // as classes aren't nested in Java/Jakarta
    //   }
    // }

    public void Sibling() {
        // Step 1: skip to sibling

        skip();

        // Step 2: after a skip, we need to remember where to continue
        //         after the next advancement

        ContinueFrom( node );
    }

    // Replace(k) swaps the AST rooted at the current node with the tree
    // rooted at k.  An AST search continues from node k.
    // Note: special case if k == root; root is updated.

    public void Replace( AstNode withnode ) {

        // Step 1: if we are replacing the root node of the
        //         search, then update the root variable

        if ( this.node == root )
            root = withnode;

        // Step 2: now replace this with withnode, and continue
        //         from withnode

        ContinueFrom( node.Replace( withnode ) );
    }

    // Delete() the current node.  Accomplished by repositioning the cursor on its
    // Sibling (as this is where the search will continue), followed by a deletion
    // of the original node.

    public void Delete() { // delete node
        AstNode todelete = node;
        NextElem = CurrElem.right;

        // Step 1: position cursor after this node
        Sibling();

        // Step 2: now delete the node itself
        todelete.Delete();
    }

    public void AddAfter( AstList y ) {
        node.AddAfter( y );
    }

    public void AddBefore( AstList y ) {
        node.AddBefore( y );
    }

    // print() unparses the AST rooted at the current node
    // reduce2java() reduces the Jakarta AST to Java code
    // reduce2ast() reduces the Jakarta AST constructors in Java

    public void print() {
        node.print();
    }

    public void reduce2java( AstProperties props ) {
        node.reduce2java( props );
    }

    public  AstNode find( String node_type ) {
        Class query_class;

        try {
            query_class = Class.forName( node_type );
        }
        catch ( Exception e ) {
            return ( null );
        }
        return ( find( query_class ) );
    }

    public  AstNode find( Class query_class ) {
        Class anc_class;

        while ( node != null ) {
            anc_class = node.getClass();
            while ( anc_class != null ) {
                if ( anc_class == query_class )
                    return ( node );
                anc_class = anc_class.getSuperclass();
            }
            PlusPlus();
        }
        return ( null );
    }

    //**************************************************
    // Unlike the above versions of find, this method takes a String
    // naming an unqualified base type. It scans and gets the class name
    // for each node, converts it to a base type and compares to the
    // type desired.
    //**************************************************
    public  AstNode findBaseType( String baseType ) {
        String csr_base_type;

        while ( node != null ) {
            csr_base_type = Util.baseType( node );
            if ( baseType.compareTo( csr_base_type ) == 0 )
                return ( node );
            PlusPlus();
        }
        return ( null );
    }

    //***************************************************
    // The following methods were added to make list traversals
    // with cursors easier
    //***************************************************

    public void FirstElement( AstNode e ) {
        if ( ! ( e instanceof  AstList ) )
            if ( e == null )
                e.fatalError( "FirstElement called with null pointer" );
            else
                e.fatalError( "Non-list node of type " + e.getClass().getName() +
                                             " called with FirstElement()" );
        if ( e.arg[0]==null ) {
            node = null;
            return;
        }
        CurrElem = e.arg[0];
        node = CurrElem.arg[0];
        NextElem = null;
        return;
    }

    public boolean MoreElement() {
        return CurrElem != null;
    }

    public void NextElement() {
        // position on correct next element

        if ( NextElem != null ) {
            CurrElem = NextElem;
            NextElem = null;
        }
        else
            CurrElem = CurrElem.right;

        if ( CurrElem == null )
            node = null;
        else
            node = CurrElem.arg[0];
    }
}

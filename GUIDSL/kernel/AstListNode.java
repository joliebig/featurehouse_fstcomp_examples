

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
// Class AstListNode
//**************************************************
    
public abstract class AstListNode extends  AstNode
    implements Cloneable {

    public  AstListNode setParms( AstNode _arg1 ) {
        arg     = new  AstNode[1];
        arg[0]  = _arg1;
        up = left = right = null;

        // if list is empty just return

        if ( _arg1 == null )
            return ( ( AstListNode ) this );

        if ( kernelConstants.debugAST ) {
            if ( _arg1.up != null )
                fatalError( "setParms called with non-detached argument" );
        }

        // now update traversal pointers of child

        arg[0].left = null;
        arg[0].up   = this;
        arg[0].right = null;
        return ( ( AstListNode ) this );
    }

    public Object clone() {
        AstListNode copy;

        copy = null;
        try {
            copy = ( AstListNode ) getClass().newInstance();
        }
        catch ( Exception e ) {
            e.printStackTrace( System.err );
            System.err.println( "Can't clone "+getClass() );
            System.exit( 1 );
        }
        super.initClone( copy ); // copy child
        initClone( copy ); // copy right
        return ( copy );
    }

    protected void initClone( AstListNode copy ) {
        if ( right != null ) {
            copy.right = ( AstNode ) right.clone();
            copy.right.left = copy;
        }
    }

    // printorder() normally returns a boolean array that indicates how
    // tokens and arguments are to be interlaced when printing.  AstList
    // nodes have a special printing algorithm, and thus printorder should
    // never be called

    public boolean[] printorder() {
        fatalError( "shouldn't call AstListNode::printorder()" );
        return ( new boolean[0] );
    }

    // Delete() the element from the list.  If the list becomes empty, then
    // the list itself is deleted.  Note that list deletion might not
    // entail much unless we are deleting a list that is an element of
    // another list (i.e., list of lists).

    public void Delete() { // physically remove element from list
        AstList head;

        // Step 1: fix left and right pointers
        if ( left != null )
            left.right = right;
        if ( right != null )
            right.left = left;

        // Step 2: fix parent (which is an AstList instance)

        head = ( AstList ) up;
        if ( head.last == this )
            head.last = left;
        if ( head.arg[0] == this )
            head.arg[0] = right;

        // Step 3: now, check to see if the list on which this element
        //         is empty.  If so, delete the list

        if ( head.arg[0] == null )
            head.Delete();

        // Clear neighbor pointers
        left = null;
        right = null;
    }

    // AddAfter(l) adds (splices) list l immediately after this node
    // if l is an empty list, AddAfter(l) does nothing.  Upon return,
    // l is an empty list.

    public void AddAfter( AstList tree ) {
        AstNode nxt, fst, lst, l;
        AstList par = ( AstList ) up;

        // Step 1: ignore operation that inserts an empty tree

        if ( tree == null || tree.arg[0] == null )
            return;

        // Step 2: remember references to common ptrs: next, first, last

        nxt = right;
        fst = tree.arg[0];
        lst = tree.last;

        // Step 3: fix the up pointers on the list we are adding

        for ( l = fst; l != null; l = l.right )
            l.up = par;

        // Step 4: connect links in the right direction

        lst.right = nxt;
        right     = fst;

        // Step 5: connect links in the left direction

        fst.left = this;
        if ( nxt != null )
            nxt.left = lst;

        // Step 6: update up.last if necessary

        if ( par.last == this )
            par.last = lst;

        // Step 7: make sure that the tree is no longer
        //         accessable

        tree.up = null;
        tree.Delete();
    }

    // AddBefore(l) splices list l immediately before the current node
    // otherwise same semantics as AddAfter

    public void AddBefore( AstList tree ) {
        AstNode prv, fst, lst, l;
        AstList par = ( AstList ) up;

        // Step 1: ignore operation that inserts an empty tree

        if ( tree == null || tree.arg[0] == null )
            return;

        // Step 2: remember common pointers: previous, first, last

        prv = left;
        fst =  tree.arg[0];
        lst = tree.last;

        // Step 3: fix the up pointers on the list we are adding

        for ( l = fst; l != null; l = l.right )
            l.up = par;

        // Step 4: connect links in the right direction

        lst.right = this;
        if ( prv != null )
            prv.right = fst;

        // Step 5: connect links in the left direction

        fst.left = prv;
        left = lst;

        // Step 6: update up.arg[0] if necessary

        if ( par.arg[0] == this )
            par.arg[0] = fst;

        // Step 7: make sure that the root of tree is no longer
        //         accessable

        tree.up = null;
        tree.Delete();
    }

    // print() and reduce2java() should never be called.  List printing
    // is done by AstList::print() and AstList::reduce2java().

    public void print() {
        System.out.println( "Error - AstListNode::print should not be called" );
    }

    public void print( AstProperties props ) {
        System.out.println( "Error - AstListNode::print should not be called" );
    }

    public void reduce2java( AstProperties props ) {
        System.out.println( "Error - AstListNode::reduce2java should not be called" );
    }

// reduce2java() generates the Java code that recreates the tree
// rooted at the current element.

}

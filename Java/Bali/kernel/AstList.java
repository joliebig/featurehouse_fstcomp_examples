

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
// Class AstList
//**************************************************
    
public abstract class AstList extends  AstNode {

    public  AstNode last; // reference to last node on list

    public AstList() {
        // initialize all the standard stuff
        arg     = new  AstNode[1];
        arg[0]  = null;
        last    = null;
    }

    public Object clone() {
        AstList copy;

        copy = null;
        try {
            copy = ( AstList ) getClass().newInstance();
        }
        catch ( Exception e ) {
            fatalError( "Can't clone "+getClass() + e.getMessage() );
        }
        super.initClone( copy );
        initClone( copy ); // last + element up pointers
        return ( copy );
    }

    protected void initClone( AstList copy ) {
        AstNode l = copy.arg[0];

        // Step 1: if the list is empty, exit

        copy.last = null;
        if ( l==null )
            return;

        // Step 2: foreach element on the list, set its up pointer
        //         and find the last element on the list

        l.up = copy;
        while ( l.right != null ) {
            l.up = copy;
            l = l.right;
        }

        // Step 3: found last element - set its up pointer and
        //         set last

        l.up = copy;
        copy.last = l;
    }

    // printorder() normally returns a boolean array that indicates how
    // tokens and arguments are to be interlaced when printing.  AstList
    // nodes have a special printing algorithm, and thus printorder should
    // never be called

    public boolean[] printorder() {
        fatalError( "shouldn't call AstList.printorder()" );
        return ( new boolean[0] );
    }

    // dumpnode() displays the pointer contents (up, left, right, args, last)
    // of a list and its descendents.

    public void dumpnode() { // dump list head node
        AstNode l;

        super.dumpnode();

        //         if (last == null)
        //             ps.println("\tlast:*");
        //         else
        //             ps.println("\tlast:"+last.hashCode());

        if ( arg[0] == null )
            return;
        for ( l=arg[0].right; l!=null; l=l.right )
            l.dumpnode();
    }

    // Delete() will delete the list.  Normally, this simply means
    // discarding all elements of the list (by setting the arg[0] and last
    // pointers to null.  However, if the list itself is an element of
    // another list, i.e., the up is an instanceof AstListNode, then up
    // is deleted (i.e., that element is removed from the enclosing list).

    public void Delete() { // delete the list

        // Step 1: if this list is an element of list, simply delete the
        //         element

        if ( up instanceof  AstListNode ) {
            ( ( AstListNode ) up ).Delete();
            return;
        }

        // Step 2: else remove all children by setting the arg[0], last
        //         references to null

        arg[0] = null;
        last   = null;
    }

    // add(x) adds AstListNode x to the end of the list.  add is generally
    // called only during parsing, and typically is not called by Jakarta
    // users.

    // add x to end of this list
    public  AstList add( AstListNode x ) {
        if ( x == null )
            return ( AstList ) this;

        // Remove x from old list, if it's on one.
        if ( x.up != null )
            x.Delete();

        // Insert x into new list
        if ( last == null ) {
            // empty list
            last    = x;
            arg[0]  = x;
            x.right = null;
            x.left  = null;
        }
        else {
            last.right = x;
            x.left     = last;
            last       = x;
        }
        x.up    = this;
        return ( AstList ) this;
    }

    // same as add() below, except that a separator is introduced
    // between lists.

    public  AstList add( AstList newList, String separator ) {

        // Step 1: if newList is null do nothing

        if ( ( newList == null ) || ( newList.last == null ) )
            return ( AstList ) this;

        // Step 2: newList isn't null -- add a separator in
        //         front of the list

        newList.arg[0].tok = new  AstTokenInterface[1];
        newList.arg[0].tok[0] = new  AstToken().setParms( "",separator,0 );

        // Step 3: compose the lists

        return add( newList );
    }

    public  AstList add( AstList newList ) {
        AstNode n;

        // Test if new list empty
        if ( ( newList == null ) || ( newList.last == null ) )
            return ( AstList ) this;

        // Change parent of inserted list nodes
        for ( n = newList.arg[0]; n != null; n = n.right )
            n.up = this;

        if ( last == null ) {
            // original list is empty
            arg[0] = newList.arg[0];
        }
        else {
            last.right = newList.arg[0];
            newList.arg[0].left = last;
        }
        last = newList.last;

        // Remove any dangling pointers from old list
        newList.arg[0] = null;
        newList.last = null;

        return ( AstList ) this;
    }

    // Same as add() but adds at the head of the list.
    public  AstList addHead( AstListNode x ) {
        x.left = null;
        x.right = arg[0];
        x.up = this;
        arg[0] = x;
        if ( last == null )
            last = x;
        return ( AstList ) this;
    }

    public  AstList addHead( AstList newList ) {
        AstNode n;

        // Test if new list empty
        if ( ( newList == null ) || ( newList.last == null ) )
            return ( AstList ) this;

        // Change parent of inserted list nodes
        for ( n=newList.arg[0]; n != null; n = n.right )
            n.up = this;

        if ( last == null ) {
            // original list is empty
            last = newList.last;
        }
        else {
            newList.last.right = arg[0];
            arg[0].left = newList.last;
        }
        arg[0] = newList.arg[0];

        // Remove any dangling pointers from old list
        newList.arg[0] = null;
        newList.last = null;

        return ( AstList ) this;
    }

    // print() prints the list.  This is a special method that overrides
    // the generic method of AstNode.  The same thing holds for reduce2ast()
    // and the reduce2java() methods.

    public void print() {
        AstNode l;

        // Step 1: return if the list is empty

        if ( arg[0] == null )
            return;

        // Step 2: print the first node differently than the rest

        arg[0].arg[0].print();
        for ( l = arg[0].right; l != null; l = l.right ) {
            l.Print_Only_Tokens();
            l.arg[0].print();
        }
    }

    public void print( AstProperties props ) {
        AstNode l;

        // Step 1: return if the list is empty

        if ( arg[0] == null )
            return;

        // Step 2: print the first node differently than the rest

        // Note: the following code assumes that elements of a list
        //       can reference null constructs - this probably is
        //       wrong, but we're going with it now.  Probably what
        //       needs to be done is to normalize the list so that
        //       union (a,b) (c,d) is (a,b,c,d) rather than (a,b,(c,d)).
        //       see original version of AstList.java - it probably
        //       is right given that lists are normalized...

        for ( l = arg[0]; l != null; l = l.right ) {
            if ( l.arg[0] == null )
                continue;
            l.arg[0].print( props );
            l = l.right;
            break;
        }
        for ( ; l != null; l = l.right ) {
            if ( l.arg[0] == null )
                continue;
            l.Print_Only_Tokens( props );
            l.arg[0].print( props );
        }
    }

    public void reduce2java( AstProperties props ) {
        AstNode l;

        // Step 1: return if the list is empty

        if ( arg[0] == null )
            return;

        // Step 2: print the first node differently than the rest

        // Note: the following code assumes that elements of a list
        //       can reference null constructs - this probably is
        //       wrong, but we're going with it now.  Probably what
        //       needs to be done is to normalize the list so that
        //       union (a,b) (c,d) is (a,b,c,d) rather than (a,b,(c,d)).
        //       see original version of AstList.java - it probably
        //       is right given that lists are normalized...

        for ( l = arg[0]; l != null; l = l.right ) {
            if ( l.arg[0] == null )
                continue;
            l.arg[0].reduce2java( props );
            l = l.right;
            break;
        }
        for ( ; l != null; l = l.right ) {
            if ( l.arg[0] == null )
                continue;
            l.Print_Only_Tokens( props );
            l.arg[0].reduce2java( props );
        }
    }

    public void PDump( String indent ) {
        int     i;
        AstNode l;
        String  id;

        System.out.println( indent + indent.length() + className() );
        id = indent+" ";
        for ( l = arg[0]; l != null; l = l.right )
            l.PDump( id );
    }

    public  AstNode addComment( String comment ) {
        return ( addComment( comment, false ) );
    }

    public  AstNode addComment( String comment, boolean replace ) {
        AstNode l;

        for ( l=arg[0]; l != null; l = l.right )
            if ( l.arg[0] != null )
                if ( l.arg[0].addComment( comment, replace ) != null )
                    return ( AstNode ) this;
        return ( null );
    }
}

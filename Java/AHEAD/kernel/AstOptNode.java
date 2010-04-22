

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
// Class AstOptNode
//**************************************************
    
public class AstOptNode extends  AstNode {
    public AstOptNode() {
        setParms( null );
    }

    public  AstOptNode setParms( AstNode child ) {
        arg = new  AstNode[1];
        arg[0]  = child;
        InitChildren();
        return ( ( AstOptNode ) this );
    }

    public boolean[] printorder() {
        FatalError error = new FatalError();
        error.printStackTrace();
        fatalError( "shouldn't call AstOptNode::printorder()" );
        return null;
    }

    // Delete() deletes the AST argument of AstOptNode

    public void Delete() {
        // Step 1: just set arg[0] to null

        if ( arg[0] != null )
            arg[0].up = null;

        arg[0] = null;
    }

    // Replace(w) does one of two things: if w is an instanceof AstOptNode
    // then replace the current node (which is an instance of AstOptNode)
    // with w using the generic Replace() method.  Otherwise, just replace
    // the argument of the current node with w.  The replaced node is
    // returned as a result

    public  AstNode Replace( AstNode withnode ) {
        if ( withnode instanceof  AstOptNode )
            return ( super.Replace( withnode ) );
        if ( arg[0] != null )
            arg[0].up   = null;
        arg[0]         = withnode;
        if ( withnode != null ) {
            withnode.left  = null;
            withnode.right = null;
            withnode.up    = this;
        }
        return ( withnode );
    }

    // print() and reduce2java() print/reduce optional nodes

    public void print() {
        if ( arg[0] != null ) {
            arg[0].print();
        }
    }

    public void print( AstProperties props ) {
        if ( arg[0] != null ) {
            arg[0].print( props );
        }
    }

    public void reduce2java( AstProperties props ) {
        if ( arg[0] != null ) {
            arg[0].reduce2java( props );
        }
    }

    public  AstNode addComment( String comment ) {
        return ( addComment( comment, false ) );
    }

    public  AstNode addComment( String comment, boolean replace ) {
        if ( arg[0] != null )
            return ( arg[0].addComment( comment, replace ) );
        return ( null );
    }
}

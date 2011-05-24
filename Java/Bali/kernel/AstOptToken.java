

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
// Class AstOptToken
//**************************************************
    
public class AstOptToken extends  AstNode
    implements AstTokenInterface {

    public AstOptToken() {
        setParms( null );
    }

    public  AstOptToken setParms( AstTokenInterface child ) {
        arg = new  AstNode[1];
        tok = new  AstTokenInterface[1];
        tok[0]  = child;
        InitChildren();
        return ( ( AstOptToken ) this );
    }

    public boolean Equ( AstTokenInterface x ) {
        return ( this.tokenName().compareTo( x.tokenName() ) == 0 );
    }

    public boolean[] printorder() {
        fatalError( "shouldn't call AstOptToken::printorder()" );
        return null;
    }

    // Delete() deletes the token

    public void Delete() {
        tok[0] = null;
    }

    // Replace(w) does one of two things: if w is an instanceof AstOptToken
    // then replace the current node (which is an instance of AstOptToken)
    // with w using the generic Replace() method.  Otherwise, replace
    // is undefined.

    public  AstNode Replace( AstNode withnode ) {
        if ( withnode instanceof  AstOptToken )
            return ( super.Replace( withnode ) );
        System.out.println( "AstNode::Replace - shouldn't be called" );
        return ( withnode );
    }

    // print(), reduce2java(), and reduce2ast() print/reduce optional nodes

    public void print( AstProperties props ) {
        if ( tok[0] != null )
            tok[0].print( props );
    }

    public void print() {
        if ( tok[0] != null )
            tok[0].print();
    }

    public void reduce2java( AstProperties props ) {
        if ( tok[0] != null ) {
            tok[0].reduce2java( props );
        }
    }

    public String tokenName() {
        if ( tok[0] == null )
            return ( "" );
        return ( tok[0].tokenName() );
    }

    public String getTokenName() {
        return tokenName();
    }

    public void setTokenName( String replacement ) {
        tok[0] = new  AstToken() ;
        ( ( AstToken ) tok [0] ) . setParms( "", replacement, 0 ) ;
    }

    public void printWhitespaceOnly( AstProperties props ) {
        if ( tok[0] != null )
            tok[0].printWhitespaceOnly( props );
    }

    //**************************************************
    // This method adds the comment given by the parameter to
    // the AstToken if it exists.
    //**************************************************
    public  AstNode addComment( String comment ) {
        return ( addComment( comment, false ) );
    }

    public  AstNode addComment( String comment, boolean replace ) {
        if ( arg[0] == null )
            return ( null );
        return ( arg[0].addComment( comment, replace ) );
    }
}

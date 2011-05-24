

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
// Class AstToken
// This is the class to be created and returned by JLex.
//**************************************************
    
public class AstToken
    implements AstTokenInterface, Cloneable, Serializable {

    public String white_space;
    public String name;
    private int def_line_num;

    static private boolean print_white_space = true;
    // static public int identifier_num = -1;
    static public void printWhitespace( boolean v ) {
        print_white_space = v;
    }
    static public boolean printWhitespace() {
        return ( print_white_space );
    }

    public  AstToken setParms( String _ws, String _sym, int lnum ) {
        white_space = _ws;
        name       = _sym;
        def_line_num = lnum;
        return ( ( AstToken ) this );
    }

    // Returns line number (in source file) that this token instance
    // was found in.
    public int lineNum() {
        return ( def_line_num );
    }

    public boolean Equ( AstTokenInterface x ) {
        return ( name.compareTo( x.tokenName() ) == 0 );
    }

    public Object clone() {
        AstToken copy;

        copy = new  AstToken().setParms( white_space, name,
                                                def_line_num );
        return ( copy );
    }

    public void print( AstProperties props ) {
        PrintWriter ps;

        ps = ( PrintWriter ) props.getProperty( "output" );
        if ( print_white_space )
            ps.print( white_space + name );
        else
            ps.print( name );
    }

    public void print() {
        System.out.print( white_space + name );
    }

    public void reduce2java( AstProperties props ) {
        PrintWriter ps;

        ps = ( PrintWriter ) props.getProperty( "output" );
        if ( print_white_space )
            ps.print( white_space + name );
        else
            ps.print( name );
    }

    private String replaceChar( int index, String str, String insert ) {
        return ( str.substring( 0, index ) + insert + str.substring( index+1 ) );
    }

    public String makeString( String str ) {
        int i;
        char ch;

        for ( i=0; i < str.length(); i++ ) {
            ch = str.charAt( i );
            switch ( ch )
            {
                case '\\':
                str = replaceChar( i, str, "\\\\" );
                i++;
                break;
                case '\n':
                str = replaceChar( i, str, "\\n" );
                i++;
                break;
                case '\t':
                str = replaceChar( i, str, "\\t" );
                i++;
                break;
                case '\b':
                str = replaceChar( i, str, "\\b" );
                i++;
                break;
                case '\f':
                str = replaceChar( i, str, "\\f" );
                i++;
                break;
                case '\r':
                str = replaceChar( i, str, "\\r" );
                i++;
                break;
                case '\"':
                str = replaceChar( i, str, "\\\"" );
                i++;
                break;
		}
        }
        return ( str );
    }

    public void printWhitespaceOnly( AstProperties props ) {
        PrintWriter pw = ( PrintWriter ) props.getProperty( "output" );

        pw.print( makeString( white_space ) );
    }

    // really deprecated, to be replaced with getTokenName

    public String tokenName() {
        return ( name );
    }

    public String getTokenName() {
        return ( name );
    }

    public void setTokenName( String replacement ) {
        name = replacement;
    }

}

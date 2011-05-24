

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
// class AstProperties
//**************************************************

public class AstProperties {
    private Hashtable table;
    private PrintWriter pw = null;
    private ByteArrayOutputStream baos = null;
    private boolean pwStdOut = false;

    public AstProperties() {
        table = new Hashtable();
    }

    // All properties should have a string as the key, but the value
    // is an object which must be cast appropriately by the caller.

    public void setProperty( String key, Object value ) {
        table.put( key, value );
    }

    public Object getProperty( String key ) {
        return ( table.get( key ) );
    }

    public Object removeProperty( String key ) {
        return ( table.remove( key ) );
    }

    public boolean containsProperty( String key ) {
        return ( table.containsKey( key ) );
    }

    public void setPw( PrintWriter p ) {
        pw = p;
    }

    public void setBaos( ByteArrayOutputStream b ) {
        baos = b;
    }

    public static  AstProperties open( Writer out ) {
        AstProperties props;
        PrintWriter   pw = null;
       
        props = new  AstProperties();

        String lineSeparator = 
                  System.getProperties().getProperty( "line.separator" );
        if ( lineSeparator.compareTo( "\n" ) != 0 )
            pw = new PrintWriter( new FixDosWriter( pw ) );
        else
            pw = new PrintWriter( out );
        props.setProperty( "output", pw );
        props.setPw( pw );
        return props;
    }

    ///------------new here ----------------------

    public static  AstProperties open( String filename ) {
        return open( null, filename );
    }

    public static  AstProperties open( String directory, String filename ) {
        AstProperties props;
        PrintWriter   pw;
       
        props = new  AstProperties();
        pw = null;
        if ( filename == null ) {
            pw = new PrintWriter( System.out );
            props.pwStdOut = true;
        }
        else {
            String lineSeparator = 
                  System.getProperties().getProperty( "line.separator" );
            try {
                FileOutputStream os = new FileOutputStream( new File( directory,filename ) );
                if ( lineSeparator.compareTo( "\n" ) != 0 )
                    pw = new PrintWriter( new FixDosOutputStream( os ) );
                else
                    pw = new PrintWriter( os );
            }
            catch ( IOException e ) {
                System.err.print( "Cannot open " );
                if ( directory != null )
                    System.err.print( directory + File.separator );
                System.err.println( filename + e.getMessage() );
                System.exit( 1 );
            }
        }
        props.setProperty( "output", pw );
        props.setPw( pw );
        return props;
    }

    //--------------end new here--------------------

        // dump reduction into string array
    public static  AstProperties open() {
        AstProperties         props;
        PrintWriter           pw;
        ByteArrayOutputStream baos;
       
        props = new  AstProperties();
        baos = new ByteArrayOutputStream();
        pw = new PrintWriter( baos );
        props.setProperty( "output", pw );
        props.setPw( pw );
        props.setBaos( baos );
        return props;
    }

    public String close() {
        // Step 1: in any case, flush

        pw.flush();

        // Step 2: different actions for output to file, StdOut, and bytearrays

        if ( baos != null )
            return baos.toString(); // return string of entire buffer

        if ( !pwStdOut ) {
            pw.close(); // if file, close it
        }
        return null; // return null if Stdout or file
    }

    public void print( String arg ) {
        PrintWriter p = ( PrintWriter ) getProperty( "output" );
        p.print( arg );
    }

    public void println( String arg ) {
        PrintWriter p = ( PrintWriter ) getProperty( "output" );
        p.println( arg );
    }

    public void print( AstNode n ) {
        PrintWriter p = ( PrintWriter ) getProperty( "output" );
        p.print( n );
    }

    public void println( AstNode n ) {
        PrintWriter p = ( PrintWriter ) getProperty( "output" );
        p.println( n );
    }
}

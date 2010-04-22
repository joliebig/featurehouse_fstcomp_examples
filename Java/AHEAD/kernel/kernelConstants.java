

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

public class kernelConstants {

    // top of ParseTreeStack holds the formerly "static" variables
    // of individual layers.  When parsing is recursive, the context
    // is pushed/popped from this stack.  Access to top is through
    // globals()

    static public Stack ParseTreeStack = new Stack();
    static {
        ParseTreeStack = new Stack();
        ParseTreeStack.push( new  kernelConstants() );
    }

    static public void PushParseTreeStack( String filename ) {
        kernelConstants k = new  kernelConstants();
        k.currentFileName = filename;
        ParseTreeStack.push( k );
    }

    static public void PopParseTreeStack() {
        try {
            ParseTreeStack.pop();
        }
        catch ( EmptyStackException e ) {
            Util.fatalError( e.getMessage() );
        }
    }

    static public  kernelConstants globals() {
        try {
            return ( kernelConstants ) ParseTreeStack.peek();
        }
        catch( EmptyStackException e ) {
            System.err.println( e.getMessage() );
            System.exit( 1 );
        }
        return null; //pacify whiney compiler
    }

    // used to compute LangName
    public static boolean classFound( String className ) {
        try {
            Class klass = Class.forName( className ) ;
            return ( klass != null ) ;
        }
        catch ( ClassNotFoundException exception ) {
            return false ;
        }
    }

    // debugAST -- used for debugging AST constructors.  The problem is that
    // using constructors and escapes, we don't produce syntactically
    // correct ASTs.  This is problemmatic for post-processing.  One error
    // that occurs is if a subtree is shared.  setting debugAST = true will
    // check that a subtree has a null up pointer -- which suggests that it
    // is still attached.

    static boolean debugAST = false; // used for debugging AST constructors

    // copyLists -- used only to support P3.  P3 is riddled with
    // a lack of cloning/copying ASTs.  copyLists is used in safeCopy
    // only -- whenever safeCopy sees a list, it clones it, no questions
    // asked.

    static boolean copyLists = false; // used for supporting P3

    // this string prefaces or qualifies each generated reference
    // eventually can be deleted once JTS is abandoned and 
    // AHEAD takes over. 

    static String LangName;
    static String PackageName; // also name of tool

    static {
        String klassName = new kernelConstants().getClass().getName();
        int dot = klassName.indexOf( '.' );
        PackageName = klassName.substring( 0,dot );
        LangName = ( classFound( PackageName + ".Lang" ) ) ? "Lang." : "";
    }

    // jakExtension -- this is the file extension that is used
    // when re-entrantly compiling files.  It is currently only
    // used in jak2java

    static String jakExtension = ".jak";

    // currentFileName -- is the name of the current file that is
    // being processed.  This is not a static variable.
    // currentAbsPath  -- absolute path to current file
	 // currentFileExt -- name of current file's extension (including '.')

    String currentFileName = null;
    String currentAbsPath = null;
	 String currentFileExt = null;

    // mainProps -- AstProperties that is used for reduction
    // always create a fresh one upon a parse

    AstProperties mainProps = new  AstProperties();
}

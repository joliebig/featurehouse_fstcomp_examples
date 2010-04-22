

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.*;

// end Main class

public class JTSParseTree {

    public  AST_Program root = null;
    public boolean isExtension; // is reset by refining constructor

    // AST_Program root inherited from preprocess or mixinbase --
    // couldn't figure out how to do it any other way

    static  Parser myParser;
    public String filepath = null; // file that was parsed
    // private boolean isExtension;

    public static void setFlags( boolean typesort, boolean keysort ) {
        Main.typeSort = typesort;
        Main.keySort = keysort;
    }

    public JTSParseTree( String filename ) throws Exception {

        // set the file name before doing anything else
        // set its absolute path if all goes well.

        kernelConstants.globals().currentFileName = filename;
        FileInputStream     baseFile = null; // base file
        baseFile = new FileInputStream( filename );
        filepath = new File( filename ).getAbsolutePath();
        kernelConstants.globals().currentAbsPath = filepath;
 
        myParser =  Parser.getInstance( baseFile ) ;

        root = (AST_Program) myParser.parseAll () ;
        kernelConstants.globals().currentFileName = filename;
        kernelConstants.globals().currentAbsPath = filepath;

        // finally do customizable processing, and then see if
                  // the parsed file is an extension

        preprocessTree( root ); // phase 1
        phase2( root ); // phase 2
        isExtension = root.isExtension();
    }

    public void preprocessTree( AST_Program root ) throws Exception {
        // Step 1: tag each node of the tree with the name of
        //         its origin (i.e. layername)

        root.setSource( root.getAspectName() );
    }

    public void phase2( AST_Program root ) throws Exception {}

    public boolean isExtension() {
        return isExtension;
    }

    public void print() {
        print2file( ( String ) null );
    }

    public void print2file( String filename ) {
        AstProperties props =  AstProperties.open( filename );
        root.print( props );
        props.close();
    }

    public void print2file( File f ) {
        print2file( f.toString() );
    }

    public void print2file( Writer w ) {
        AstProperties props =  AstProperties.open( w );
        root.print( props );
        props.close();
    }

    public String getAspectName() {
        return root.getAspectName();
    }

    public void setAspectName( String pname ) {
        root.setAspectName( pname );
    }

    public void setPackageName( String pname ) {
        System.err.println( "setPackageName deprecated -- use setAspectName" );
        root.setAspectName( pname );
    }

    public static int errorCount() {
        return AstNode.errorCount();
    }

    public static int warningCount() {
        return Jakarta.util.Util.warningCount();
    }

    public String errorCountString() {
        int ecount =  AstNode.errorCount();
        return "Summary "+ ecount + " error" + ( ecount==1?"":"s" );
    }

    public static void resetCounters() {
        Jakarta.util.Util.resetCounters();
    }

    public static void setReportStream( PrintWriter p ) {
        Jakarta.util.Util.setReportStream( p );
    }

    public static void report( String msg ) {
        AstNode.report( msg );
    }

}



import java.util.*;
import Jakarta.util.Util2;
import java.io.*;

/******************** Main class **************************
 * @layer<Basemm>
 */

public class Main     {
    public static Parser myParser = null;
    public static  MMOutput   mmresult; // essentially a global variable

    public static boolean printClean = true;

    public static void marquee() {
       System.err.println( "mmatrix <arguments>" );
       System.err.println( "   -t        (traverse directory ..)" );
       System.err.println( "   -c        (remove unnecessary attributes)");
       System.err.println( "   <file>    (run mmatrix on input file)" );
       System.exit( 1 );
    }
 
    public static void main( String[] args ) {
        int                 argc = args.length;
        int                 non_switch_args;
        boolean             traverseset = false;

        if ( args.length == 0 ) marquee();

        // Step 1: a general routine to pick off command line options
        //         options are removed from command line and
        //         args array is adjusted accordingly.
        //         right now, there are no command-line options
        //         but this code is here for future expansion
      
        non_switch_args = 0;
        for ( int i=0; i < argc; i++ ) {
            if ( args[i].charAt( 0 ) == '-' ) {
   
                // switches of form -xxxxx (where xxx is a sequence of 1
                // or more characters
   
                for ( int j=1; j < args[i].length(); j++ ) {
                // if (args[i].charAt(j) == 'x' { 
                //        ... do this for option 'x'
                // }
                   if (args[i].charAt(j) == 't') {
                      traverseset = true;
                   } else
                   if (args[i].charAt(j) == 'c') {
                      printClean = false;
                   } 
                   else {
                     System.err.println("Error: Unrecognizable option '" +
                         args[i].charAt(j) + "'");
                     marquee();
                   }
                }
            }
            else {
                // non-switch arg
                args[non_switch_args] = args[i];
                non_switch_args++;
            }
        }

        try {
            if (traverseset) {
               TraverseSet.walk( "." );
            }
            else { // default execution
               if (non_switch_args != 1) marquee();
               eval( args[0] );
               mmresult.print();
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }

    } //end main()

    public static  MMOutput eval( String fileName ) throws Exception {

        // Step 1: create MMOutput and open file 

		  kernelConstants.globals().currentFileName = fileName;
        mmresult = new  MMOutput();
        FileInputStream     inputFile = null;
        try {
            inputFile = new FileInputStream( fileName );
        }
        catch ( Exception e ) {
            throw new Exception( "File " + fileName + " not found:" + e.getMessage() );
        }
   
        // Step 2: create a parser and parse input files
        //         inputRoot is root of parse tree of input file
   
        myParser =  Parser.getInstance( inputFile ) ;

        AST_Program    inputRoot = null;
        try {
            inputRoot = (AST_Program) myParser.parseAll() ;

        }
        catch ( Exception e ) {
            throw new Exception( "Parsing Exception Thrown in " + fileName + 
                                 ": " + e.getMessage() );
        }
 
        // Step 3: examine the parse tree here
 
        try {
            inputRoot.execute( 0 );
        }
        catch ( ResultException e ) {}

        if ( mmresult.computed() )
            return mmresult;

        throw new Exception( "No Result Computed for file " + fileName );
    }
}

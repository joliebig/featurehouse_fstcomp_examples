

import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

public class Main {

    final private static String SOURCE_FILE = "source file" ;

    private ArgList instanceArgs = null ;
    private PositionalArg sourceFile = null ;

    private int myLayerID;
    public static boolean verbose = false;

    private static HashSet filesProcessed = new HashSet();
    public static String currentOutputFileName ="";

    // given absolute path of file, directoryPath returns
    // the absolute path of the file's directory

    String directoryPath( String path ) {
        if ( path == null )
            return ".";
        int lst = path.lastIndexOf( File.separatorChar );
        if ( lst == -1 )
            return ".";
        else
            return path.substring( 0, lst );
    }

    /**
     * Overrides previous version to allow multiple source files in the
     * argument list.  This is done by successively substituting the
     * "source file" argument by each of the extra arguments.
     *
     * <p>
     * <em>Note:</em>
     * The argument processing here is a mess, mostly because the command
     * line parsing design is poor.  That's another area of clean-up.
     *
     * @layer<Java>
     */
    protected boolean driver( ArgList args ) {

        instanceArgs = args ;

        // Find the "source file" argument to be updated.
        //
        for ( Iterator p = args.iterator() ; p.hasNext() ; ) {
            Object object = p.next() ;
            if ( object instanceof PositionalArg ) {
                PositionalArg arg = ( PositionalArg ) object ;
                if ( SOURCE_FILE.equals( arg.name ) ) {
                    sourceFile = arg ;
                    break ;
                }
            }
        }

        if ( sourceFile == null )
            throw new IllegalStateException( "invalid source file parse" ) ;

        // Get verbosity level:
        //
        for ( Iterator p = args.iterator() ; p.hasNext() ; ) {
            Object object = p.next() ;
            if ( object instanceof Switch ) {
                Switch sw = ( Switch ) object ;
                if ( sw.name == "quiet" )
                    verbose = false ;
                else
                    if ( sw.name == "verbose" )
                        verbose = true ;
            }
        }

        // Handle first source file argument (at least one is required):
        //
        processing( packageName, sourceFile.binding ) ;

        // If there are no extra arguments, then we're done.
        //
        if ( extraArgs == null || extraArgs.size() < 1 )
            return true;

        // Substitute each extra source file argument into the source file
        // position, then re-evaluate command line.
        //
        for ( Iterator p = extraArgs.iterator() ; p.hasNext() ; )
            processing( packageName, ( String ) p.next() ) ;

        return true;
    }

    protected void processing( String label, String fileName ) {

        sourceFile.binding = fileName ;
        kernelConstants.globals().currentFileName = fileName;
        boolean processed = this.driver( instanceArgs );

        if ( verbose && processed )
            System.err.println( label
                            + ": file \""
                            + fileName
                            + '"' ) ;
    }

    //**************************************************
    // Method called by the top-most layer to allow a layer to request
    // switches and arguments.
    //**************************************************
    protected void argInquire( int _layer ) {
        Switch sw;

        // Save my layer number
        myLayerID = _layer;

        // Register my switches
        sw = new Switch( "d", "debug mode for parser", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "s", "send output to stdout", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "b", "bootstrap from JTS to FOP", null, true, _layer );
        switchRegister( sw );
        sw = new Switch( "v", "FOP (exit(1)) or JTS (exit(0)) version", null,  true, _layer );
        switchRegister( sw );
        sw = new Switch( "x", "override default file extension", new String[1],  true, _layer );
        switchRegister( sw );

        // Verbosity selection:
        //
        switchRegister( new Switch( "quiet",
                    "disables verbose output",
                    null,
                    true,
                    _layer ) ) ;

        switchRegister( new Switch( "verbose",
                    "enables verbose output (default)",
                    null,
                    true,
                    _layer ) ) ;

        // Register my command line positional arguments
        posArgRegister( new PositionalArg( SOURCE_FILE, _layer ) ) ;

        // Allow extra arguments:
        extraArgs = new ArrayList() ;

        // Call next layer
        original( nextLayer() );
    }

    //**************************************************
    // createAST()
    //**************************************************
    protected  AstNode createAST( ArgList argObjects ) {
        FileInputStream fis;
        PositionalArg parg;
        File inputFile;
        AstNode root;

        // do no file processing if -v switch is set;
        // just return version

        if ( argObjects.find( "v", Switch.class, myLayerID ) != null ) {
            if ( kernelConstants.LangName.equals( "" ) )
                System.exit( 1 );
            else
                System.exit( 0 );
        }

        Switch sw = ( Switch ) argObjects.find( "x", Switch.class, myLayerID );
        if ( sw != null ) {
            kernelConstants.jakExtension = sw.args[0];
        }
        parg = ( PositionalArg ) argObjects.first( PositionalArg.class, myLayerID );

        kernelConstants.PushParseTreeStack( parg.binding );
        try {
            inputFile = new File( parg.binding );
            fis = new FileInputStream( inputFile );
            kernelConstants.globals().mainProps.setProperty( "input", inputFile );

            // now add inputDirectory property
            String abspath = inputFile.getAbsolutePath();
            kernelConstants.globals().mainProps.setProperty( "inputDirectory",
                              directoryPath( abspath ) );
            kernelConstants.globals().currentAbsPath = abspath;

            // extract file extension -- if there is none, use ""
            String x = "";
            int i = parg.binding.lastIndexOf( '.' );
            if ( i != -1 )
                x = parg.binding.substring( i );
            kernelConstants.globals().currentFileExt = x;

            // see if we have already processed this file.  If so, return null

            if ( filesProcessed.contains( abspath ) ) {
                fis.close();
                kernelConstants.PopParseTreeStack();
                return null;
            }
        }
        catch ( Exception e ) {
            AstNode.fatalError( "Can't open file "+ parg.binding );
            fis = null;
        }

        try {
            Parser parser =  Parser.getInstance( fis ) ;
            root = parser.parseAll() ;
        }
        catch ( ParseException e ) {
            AstNode.parseError( e.toString() );
            root = null;
        }

        return ( root );
    }

    //**************************************************
    // outputAST()
    //**************************************************
    protected void outputAST( ArgList argObjects,  AstNode ast ) {
        PrintWriter pw;
        String outputFileName="";
        String inputFileName;
        String outputDirectory;
        int lastDot;
        File inputFile=null;

        pw = null;
        String lineSeparator =
                System.getProperties().getProperty( "line.separator" );

        if ( argObjects.find( "b", Switch.class, myLayerID ) != null ) {
            kernelConstants.LangName = "";
        }

        if ( argObjects.find( "s", Switch.class, myLayerID ) != null ) {
            if ( lineSeparator.compareTo( "\n" ) != 0 )
                pw = new PrintWriter( new FixDosOutputStream( System.out ) );
            else
                pw = new PrintWriter( System.out );
            outputDirectory = ".";
        }
        else {
            inputFile = ( File )  kernelConstants.globals().mainProps.getProperty( "input" );
            inputFileName = inputFile.getAbsolutePath();
            lastDot = inputFileName.lastIndexOf( '.' );

            if ( lastDot == -1 )
                outputFileName = inputFileName + kernelConstants.outputFileExtension;
            else
                outputFileName = inputFileName.substring( 0, lastDot ) +
                        kernelConstants.outputFileExtension;

            // @test Refactor into a method
            // outputFileName = computeOutputFileName(lastDot, inputFileName);

            outputDirectory = directoryPath( outputFileName );
            try {
                OutputStream os;
                FileOutputStream fos =
                        new FileOutputStream( outputFileName );

                if ( lineSeparator.compareTo( "\n" ) != 0 )
                    os = new FixDosOutputStream( fos );
                else
                    os = fos;
                pw = new PrintWriter( os );
            }
            catch ( IOException e ) {
                AstNode.fatalError( "Cannot open " + outputFileName + ": " +
                                    e.getMessage() );
            }
        }
        kernelConstants.globals().mainProps.setProperty( "output", pw );

        // in some future version, it might be possible to set
        // outputDirectory from a command-line argument. if so,
        // the property of outputDirectory would have been already set

        if ( ! kernelConstants.globals().mainProps.containsProperty( "outputDirectory" ) )
            kernelConstants.globals().mainProps.setProperty( "outputDirectory", outputDirectory );

        ast.reduce2java( kernelConstants.globals().mainProps );
        pw.println();
        pw.flush();

        // Keeps the value of the OutputFileName to be accessed by Jak2aj tool
        // for file renaming
        currentOutputFileName = outputFileName;

        // add to list of files already processed
        filesProcessed.add( kernelConstants.globals().currentFileName );

        kernelConstants.PopParseTreeStack();

        // Call outputAST() for other layers
        original( argObjects, ast );

    }

}

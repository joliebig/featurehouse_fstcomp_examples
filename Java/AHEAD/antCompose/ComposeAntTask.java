

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;

/**
 * Ant task to compose a sequence of source files or directories to a given
 * target file or directory.  The composition is done by parsing each
 * source file in order and composing the parse trees.
 *
 * @layer<antCompose>
 */
    
public class ComposeAntTask extends Task {
    public class SourceFile {

        /**
         * Returns the {@link File} representing this operand.
         *
         * @layer<antCompose>
         */
        public File getFile() {

            if ( file == null )
                throw new BuildException( "no name specified for file operand",
                                                getLocation() ) ;

            return getProject().resolveFile( file.getPath() ) ;
        }

        /**
         * Specifies the file for this operand.
         *
         * @layer<antCompose>
         */
        public void setName( File file ) {
            this.file = file ;
        }

        private File file = null ;

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    /**
     * Specifies a single operand file to the composition.
     *
     * @layer<antCompose>
     */
    public void addFile( SourceFile file ) {
        operands.add( file ) ;
    }

    /**
     * Specifies a list of operand files to the composition.
     *
     * @layer<antCompose>
     */
    public void addFilelist( FileList files ) {
        operands.add( files ) ;
    }

    /**
     * Invoked when this task is next to be executed.
     *
     * @layer<antCompose>
     */
    public void execute() throws BuildException {

        if ( outputFile == null )
            throw new BuildException( "no output file given", getLocation() ) ;

        File[] inputFiles = getFiles() ;
        if ( inputFiles.length < 1 )
            throw new BuildException( "no input files given", getLocation() ) ;

        if ( upToDate( inputFiles, outputFile ) ) {
            log( "output file is up-to-date", Project.MSG_INFO ) ;
            return ;
        }

        try {
            compose( inputFiles, outputFile ) ;
        }
        catch ( BuildException exception ) {
            throw exception ;
        }
        catch ( Exception exception ) {
            throw new BuildException( exception, getLocation() ) ;
        }
    }

    /**
     * Returns an array of {@link File} objects listing all operands.
     *
     * @layer<antCompose>
     */
    public File[] getFiles() {

        if ( operands.size() < 1 )
            return new File [0] ;

        List files = new ArrayList() ;
        for ( Iterator p = operands.iterator() ; p.hasNext() ; ) {
            Object operand = p.next() ;
            if ( operand instanceof SourceFile )
                files.add( ( ( SourceFile ) operand ) . getFile() ) ;
            else
                if ( operand instanceof FileList )
                    files.addAll( asList( ( FileList ) operand ) ) ;
                else
                    throw new BuildException( "unexpected operand type",
                                                            getLocation() ) ;
        }

        return ( File[] ) files.toArray( new File [files.size()] ) ;
    }

    /**
     * Specifies the aspect name for the generated composition.
     *
     * @layer<antCompose>
     */
    public void setAspect( String aspectName ) {
        this.aspectName = aspectName ;
    }

    /**
     * Specifies whether the enclosing build should abort if the
     * composition fails.
     *
     * @layer<antCompose>
     */
    public void setFailonerror( boolean failOnError ) {
        this.failOnError = failOnError ;
    }

    /**
     * Specifies the output file (or directory) for the generated
     * composition.
     *
     * @layer<antCompose>
     */
    public void setOutput( File outputFile ) {
        this.outputFile = outputFile ;
    }

    /**
     * Specifies the output file (or directory) for the generated
     * composition; this is an alias for <code>output</code>.
     *
     * @layer<antCompose>
     */
    public void setTarget( File outputFile ) {
        this.outputFile = outputFile ;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    /**
     * Returns the files listed in <code>fileList</code> as a {@link List}
     * of fully resolved {@link File} objects.
     *
     * @layer<antCompose>
     */
    final private List asList( FileList fileList ) {

        String[] names = fileList.getFiles( getProject() ) ;
        File[] files = new File [names.length] ;
        for ( int n = names.length ; --n >= 0 ; )
            files [n] = getProject().resolveFile( names [n] ) ;

        return Arrays.asList( files ) ;
    }

    /**
     * Composes a sequence of source files, writing the result into a
     * target file.
     *
     * @layer<antCompose>
     */
    final private void compose( File[] sources, File target )
    throws Exception {

        int errors =  AstNode.errorCount() ;

        log( "reading " + sources [0], Project.MSG_VERBOSE ) ;
        JTSParseTree composition =
                new  JTSParseTree( sources[0].getPath() ) ;

        for ( int n = 1 ; n < sources.length ; ++n ) {
            JTSParseTree extension =
                            new  JTSParseTree( sources[n].getPath() ) ;
            log( "reading " + sources [n], Project.MSG_VERBOSE ) ;
            composition.compose( extension ) ;
        }

        if ( aspectName != null )
            composition.setAspectName( aspectName ) ;

        if ( errors ==  AstNode.errorCount() ) {
            log( "writing " + target, Project.MSG_VERBOSE ) ;
            composition.print2file( target ) ;
        }

        if ( failOnError && errors >  AstNode.errorCount() )
            throw new BuildException( "composition failed with "
                            + ( AstNode.errorCount() - errors )
                            + " errors",
                            getLocation() ) ;
    }

    /**
     * Returns <code>true</code> if the given target file is more recent
     * than the specified source files.
     *
     * @layer<antCompose>
     */
    final private static boolean upToDate( File[] sources, File target ) {

        if ( ! target.exists() )
            return false ;

        long targetTime = target.lastModified() ;
        for ( int n = sources.length ; --n >= 0 ; )
            if ( sources[n].lastModified() >= targetTime )
                return false ;

        return true ;
    }

    private String aspectName = null ;
    private boolean failOnError = false ;
    private List operands = new ArrayList() ;
    private File outputFile = null ;

}

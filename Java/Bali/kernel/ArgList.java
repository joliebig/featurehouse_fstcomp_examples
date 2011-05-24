

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

/**
 * ArgList: encapsulates a list of either Switch or PositionalArg objects.
 *
 * @layer<kernel>
 */
    
public class ArgList extends Vector {
    // Constants used to constrain find(), first(), and next()
    static public final int NO_LAYER = -1;
    static public final Class NO_CLASS = null;

    // Current filter values
    int layerFilter = NO_LAYER;
    Class classFilter = NO_CLASS;

    // Acts as a cursor for current position.
    int csrIndex;

    private CommandLineArg locate( int start ) {
        CommandLineArg arg;

        for ( int i=start; i < elementCount; i++ ) {
            arg = ( CommandLineArg ) elementData[i];
            if ( ( layerFilter != NO_LAYER ) &&
                            ( arg.layerID != layerFilter ) )
                continue;
            if ( ( classFilter != NO_CLASS ) &&
                            ( arg.getClass() != classFilter ) )
                continue;
            csrIndex = i;
            return ( arg );
        }
        return ( null );
    }

    //**************************************************
    // Return first CommandLineArg with class and layerID possible filter
    // criteria.
        //**************************************************
    public CommandLineArg first() {
        return ( locate( 0 ) );
    }

    public CommandLineArg first( Class cls ) {
        classFilter = cls;
        return ( locate( 0 ) );
    }

    public CommandLineArg first( int _layer ) {
        layerFilter = _layer;
        return ( locate( 0 ) );
    }

    public CommandLineArg first( Class cls, int _layer ) {
        classFilter = cls;
        layerFilter = _layer;
        return ( locate( 0 ) );
    }

    //**************************************************
    // Return next CommandLineArg with class and layerID possible filter
    // criteria.
    //**************************************************
    public CommandLineArg next() {
        return ( locate( csrIndex+1 ) );
    }

    public CommandLineArg next( Class cls ) {
        classFilter = cls;
        return ( locate( csrIndex+1 ) );
    }

    public CommandLineArg next( int _layer ) {
        layerFilter = _layer;
        return ( locate( csrIndex+1 ) );
    }

    public CommandLineArg next( Class cls, int _layer ) {
        classFilter = cls;
        layerFilter = _layer;
        return ( locate( csrIndex+1 ) );
    }

    //**************************************************
    // Locate an argument by name with class and layerID possible
    // filter criteria.
    //**************************************************
    public CommandLineArg find( String name ) {
        CommandLineArg arg;

        for ( arg = locate( 0 ); arg != null; arg = locate( csrIndex+1 ) ) {
            if ( name.compareTo( arg.name ) == 0 )
                return ( arg );
        }
        return ( null );
    }

    public CommandLineArg find( String name, Class cls ) {
        classFilter = cls;
        return ( find( name ) );
    }

    public CommandLineArg find( String name, int _layer ) {
        layerFilter = _layer;
        return ( find( name ) );
    }

    public CommandLineArg find( String name, Class cls, int _layer ) {
        classFilter = cls;
        layerFilter = _layer;
        return ( find( name ) );
    }
}

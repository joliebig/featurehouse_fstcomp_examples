

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class NestedClassDeclaration  {
    public void execute( int stage ) {
        if ( stage != 0 ) {
            super.executeBypass( stage );
            return;
        }
        ;

        // technically, we should issue a warning that nested classes are not
        // supported
        System.err.println( 
		      "nested classes (which are not supported) found in file " + 
		      kernelConstants.globals().currentFileName );
    }
}

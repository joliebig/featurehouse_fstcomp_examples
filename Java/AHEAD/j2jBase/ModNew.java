

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class ModNew {

    public void reduce2java( AstProperties props ) {
        // Step 1: the new modifier should not be present, UNLESS
        //         there is a SoUrCe property.  It's an error otherwise.

        if ( props.getProperty( "SoUrCe" ) == null ) {
            AstNode.error( tok[0], "new modifier should not be present" );
            return;
        }
       
        // Step 2: it's OK to be present.  If so, the reduction is to 
        //         print the white-space in front for pretty-printing

        props.print( getComment() );
    }
}

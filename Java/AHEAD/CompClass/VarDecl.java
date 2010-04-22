

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class VarDecl {
    public void ok2compose( int stage,  Hashtable hb ) {

        // Step 0: do nothing if we are inside quoted text

        if ( stage != 0 ) {
            super.ok2compose( stage, hb );
            return;
        }

        // Step 1: get signature of this variable and then get the 
        //         corresponding variable in the base

        String sig = GetName();
        VarDecl d = ( VarDecl ) hb.get( sig );

        // Step 2: if the extension var does override, this is an error
        if ( d!=null )
            AstNode.error( arg[0].arg[0].tok[0],
                       "cannot refine data member " + sig );
    }
}

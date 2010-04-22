

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

//------------- mangle local ids ---------------------------
//------------- requires kernel extensions -----------------

public class QName {

    public void mangleLocalIds( int stage ) {
        // Step 1: if not at outermost stage, ignore

        if ( stage != 0 )
            return;

        // Step 2: see if identifier is in hashtable

        String id = tok[0].tokenName();
        String mangledId = ( String )  kernelConstants.globals().localId_ht.get( id );
 
        // Step 3: if mangledId is null, return, else replace Id
        //         setName added by preprocess.layer

        if ( mangledId == null )
            return;
        ( ( AstToken ) tok[0] ).setName( mangledId );
    }
}

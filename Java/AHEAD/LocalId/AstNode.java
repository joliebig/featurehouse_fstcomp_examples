

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

//-------------- kernel classes ----------------------------

public  abstract class AstNode {
    public void mangleLocalIds( int stage ) {
        int i;
        if ( arg == null )
            return;
        for ( i=0; i<arg.length; i++ )
            if ( arg[i]!=null )
                arg[i].mangleLocalIds( stage );
    }
}

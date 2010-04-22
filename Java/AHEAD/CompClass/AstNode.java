

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

//------------------------ kernel extensions ---------------

public  abstract class AstNode {
    public void baseRewrite( Hashtable hb, Hashtable he, int stage ) {
        int i;
        if ( arg == null )
            return;
        for ( i=0; i<arg.length; i++ )
            if ( arg[i]!=null )
                arg[i].baseRewrite( hb, he, stage );
    }

    public void ok2compose( int stage, Hashtable h ) {
        AstNode.override( "AstNode.ok2compose", this );
    }
}

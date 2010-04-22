

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public  abstract class AstList {
    public void baseRewrite( Hashtable hb, Hashtable he, int stage ) {
        AstNode l;
        if ( arg[0]==null )
            return;
        for ( l = arg[0]; l!=null; l = l.right ) {
            if ( l.arg[0] == null )
                continue;
            l.arg[0].baseRewrite( hb, he, stage );
        }
    }
}

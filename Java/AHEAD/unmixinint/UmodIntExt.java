

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodIntExt {
   
    public boolean propagateChanges( AST_TypeNameList l, 
                                     InterfaceMemberDeclarations i ) {
 
        boolean updated =  UmInterDecl.pc( arg[1], l, i );
        if ( i!=null && ! ( arg[2].toString().equals( i.toString() ) ) ) {
            arg[2].Replace( i );
            updated = true;
        }
        return updated;
    }
}

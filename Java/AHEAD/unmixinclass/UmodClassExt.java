

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodClassExt {
   
    public boolean propagateChanges( ImplementsClause i, 
                                     ClassBody b ) {

        // must laboriously evaluate all individually, as java compiler
                       // optimizes -- and doesn't notice that side-effects occur

        boolean u =  UmodClassDecl.oneChange( arg[1], i );
        u =  UmodClassDecl.oneChange( arg[2], b ) || u;
        return u;
    }
}

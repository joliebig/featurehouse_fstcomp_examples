

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public abstract class AstNode {
    public void unmangleIds( int stage ) {
        int i;
        if ( arg == null )
            return;
        for ( i=0; i<arg.length; i++ )
            if ( arg[i]!=null )
                arg[i].unmangleIds( stage );
    }
}

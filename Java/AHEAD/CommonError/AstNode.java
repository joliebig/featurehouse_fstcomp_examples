

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

//-------------- added to core classes

public abstract class AstNode {

    // this method walks up a tree to discover if an explicit contructor
    // invocation belongs to a constructor of an extension or a base
    // entity

    public void checkForErrors( int stage, String file ) {
        int i;
        if ( arg == null )
            return;
        for ( i=0; i<arg.length; i++ )
            if ( arg[i]!=null )
                arg[i].checkForErrors( stage, file );
    }
}



import java.util.*;
import java.io.*;

public class EnterDecl {

    public void harvestAst( LinkedList ll ) {
        // harvest the block for EnterDecl
        ll.add( arg[1] );
    }

    public void add2Hash( Hashtable h, String source ) {
        verifyState( h, "Enter" );
    }
}

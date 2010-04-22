

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class Initializer {
    public String signature() {
        return "I-" + 
            kernelConstants.globals().compclass.Initializer_counter++;
    }
    public void add2Hash( Hashtable h ) { /* do nothing */
    }

    // composition of initializers is simple - just concatenate
    // initializer blocks.  notice that the above signature
    // string aways returns a unique string, so the compose operation
    // should never be called.

    public void compose( AstNode etree ) {
        AstNode.fatalError( "Initializer.compose should never be called" );
    }

    public void ok2compose( int stage, Hashtable hb ) {
    /* ok to compose initializers at any stage */
    }
}



import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class JTSParseTree {

    public  TypeDeclaration firstType; // used for inheritance
    public  TypeDeclaration lastType;

    public void phase2( AST_Program root ) throws Exception {
        try {
            root.prepare( ( JTSParseTree ) this );
        }
        catch ( Exception e ) {
            e.printStackTrace();
            throw e;
        }
    }

    public void compose( JTSParseTree t ) {
        // apply inheritance composition rule: const o whatever = const
        // we could do better here, and make suer that the name of the
        // const class, interface, etc. matches the name of whatever.
        // in fact, we should.  this will do for now.

        if ( t.isExtension() )
            root.compose( t.root, ( JTSParseTree ) this, t );
        else {
            AstNode.warning( "overrides previous results" );
            root = t.root;
            firstType = t.firstType;
            lastType  = t.lastType;
            isExtension = t.isExtension;
        }
    }
}

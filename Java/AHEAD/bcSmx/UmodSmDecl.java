

import java.util.*;
import java.io.*;
import Jakarta.util.Util2;

//------------------------ bcSmx layer -------------------
//       encapsulates refinement of state machines and anything
//       to do with their composition.  in this case, the j2j tool
//       requires some rewrites of state machines *prior* to their
//       reduction.  Also, the j2j tool will be able to parse extensions
//       to state machines, but will flag them as errors.

public class UmodSmDecl {

    public void reduce2java( AstProperties props ) {

        // Step 0: do a normal reduction if we haven't seen SoUrCe decls

        original( props );

        props.setProperty( "SuperName", "" );
        props.setProperty( "MixinDeclName", "" );
		  props.setProperty( "ThisName", "" );
    }
}

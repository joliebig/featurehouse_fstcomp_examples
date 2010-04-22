

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class PrepareDecl {
    public void reduce2java( AstProperties props ) {
        String    stateName;
        stateInfo s;

        stateName = arg[0].tok[0].tokenName();
        s =  stateInfo.verifyStateName( stateName, "Prepare method", tok[0] );

        AST_Stmt code = ( AST_Stmt ) arg[1].arg[0].arg[0];

        // now do the refinement -- there can be multiple Prepare declarations
        // in an SM specification.  This wierdness arises because of JamPack
        // compositions of SM specs.

        s.prepare_action_ast = ( AST_Stmt )
          kernelConstants.globals().sm4vars.refineMethod( s.prepare_action_ast, code, "Prepare " + 
               stateName, s.name + "_prepare", true, tok[0] );
    }
}

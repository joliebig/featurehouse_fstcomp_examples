

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class OtherDecl {
    public void reduce2java( AstProperties props ) {
        String    stateName;
        stateInfo s;

        stateName = arg[0].tok[0].tokenName();
        s =  stateInfo.verifyStateName( stateName, "Otherwise clause", tok[0] );
        AST_Stmt code = ( AST_Stmt ) arg[1].arg[0].arg[0];

        // now do the refinement -- there can be multiple Otherwise declarations
        // in an SM specification.  This wierdness arises because of JamPack
        // compositions of SM specs.

        s.otherwise_action_ast = ( AST_Stmt )
          kernelConstants.globals().sm4vars.refineMethod( s.otherwise_action_ast, code, "Otherwise " +
                stateName, s.name + "_otherwise", true, tok[0] );
    }
}

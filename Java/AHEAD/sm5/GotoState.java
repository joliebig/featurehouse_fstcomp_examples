

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class GotoState {
    public void reduce2java( AstProperties props ) {

        String stateName = arg[0].tok[0].tokenName();
        stateInfo s =  stateInfo.verifyStateName( stateName, 
                              "goto_state", tok[0] );
        String action =  "/* goto_state " + stateName + " */ " +
         " dispatch.exit" + arg[1] + "; " +
               s.name + "_enter" + arg[1] + ";";
        AST_Stmt smt =  kernelConstants.globals().sm4vars.ToStmt( action );
        smt.reduce2java( props );
    }
}

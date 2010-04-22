

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class TransitionDecl {
    public void reduce2java( AstProperties props ) {
        String transitionName;
        String startName;
        String endName;
        transInfo e, e1;
        stateInfo s, endState = null, startState = null;
        int       i;
        boolean   refining_transition;

        refining_transition = false;
        transitionName  = arg[0].tok[0].tokenName();
        if ( arg[1] instanceof  SmSName )
            startName = arg[1].arg[0].tok[0].tokenName(); // a real name
        else
            startName = arg[1].tok[0].tokenName(); // the "*" name
        endName   = arg[2].tok[0].tokenName();

        e = new  transInfo( transitionName, startName, endName );
        e1 = ( transInfo )  kernelConstants.globals().sm4vars.Sm.TransCont.find( e );
        if ( e1 != null ) { // transition exists - cannot redefine transition
            AstNode.fatalError( tok[0], Utility.SourceName()+
                            "duplicate transition declaration " + transitionName );
        }
        else {
            kernelConstants.globals().sm4vars.Sm.TransCont.add( e );
            if ( !startName.equals( "*" ) )
                startState =  stateInfo.verifyStateName( startName, 
                             "transition declaration " + transitionName, tok[0] );

            endState =  stateInfo.verifyStateName( endName, 
                             "transition declaration " + transitionName, tok[0] );
        }

        e.condition_ast = ( AST_Exp ) arg[3];
        e.action_ast    = ( AST_Stmt ) arg[4].arg[0].arg[0];
    }
}



import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class SmClassDecl {
    public void reduce2java( AstProperties props ) {
        // must have a RootClause if the state machine does not extend
        // another state machine

        RootDecl rd = (RootDecl) arg[0].arg[0];

        if ( kernelConstants.globals().sm4vars.Sm.superSm_name == null && rd == null )
            AstNode.fatalError( tok[0], Utility.SourceName()+
                            "Delivery_parameters clause is missing from "+
                             kernelConstants.globals().sm4vars.Sm.name +" specification." );

        // if there is a superSm, there must be no NoTransitionClause

        if ( kernelConstants.globals().sm4vars.Sm.superSm_name != null && rd !=null
		       && rd.arg[1].arg[0] != null )
            AstNode.fatalError( tok[0], Utility.SourceName()+
                            "NoTransitionClause should not be present in "+
                             kernelConstants.globals().sm4vars.Sm.name +" specification." );

        kernelConstants.globals().sm4vars.Sm.body_ast = ( AST_FieldDecl ) arg[4].arg[0];

        // reduce only selected subtrees at this time - don't reduce
        // arg[4] (AST_FieldDecl) now - do it later in UmodSmDecl.reduce2java
        // reduce all state machine declarations first.
         
        arg[0].reduce2java( props );
        arg[1].reduce2java( props );
        arg[2].reduce2java( props );
        arg[3].reduce2java( props );
    }
}

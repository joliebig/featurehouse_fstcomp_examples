

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

///--------------------

public class DelivDecl {
    public void reduce2java( AstProperties props ) {
        AstCursor c = new  AstCursor();
        String          arglist = null;
        String          var;

        kernelConstants.globals().sm4vars.Sm.pardecl_ast  = ( AST_ParList ) arg[0];
        kernelConstants.globals().sm4vars.Sm.pardecl_ast.Detach();
        kernelConstants.globals().sm4vars.Sm.pardecl      = arg[0].toString();

        for ( c.First( kernelConstants.globals().sm4vars.Sm.pardecl_ast ); c.More(); c.PlusPlus() ) {
            if ( c.node instanceof  DecNameDim ) {
                var = c.node.arg[0].tok[0].tokenName();
                if ( arglist == null )
                    arglist = var;
                else
                    arglist = arglist + ", " + var;
            }
        }

        kernelConstants.globals().sm4vars.Sm.argdecl_ast =  AST_ArgList.MakeAST( arglist );
        kernelConstants.globals().sm4vars.Sm.argdecl = arglist;
    }
}

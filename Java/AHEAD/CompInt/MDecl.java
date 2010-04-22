

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/** production:
InterfaceMemberDeclaration:
    MethodDeclaration::MDecl 
*
* @layer<CompInt>
*/

public class MDecl {

    public void add2Hash( Hashtable h ) {
        h.put( signature(), this );
    }

    public boolean actOnHash( Hashtable h ) {
        // this method is called only on extension tree nodes

        InterfaceMemberDeclaration d;
        String sig = signature();

        d = ( InterfaceMemberDeclaration ) h.get( sig );

        // get the modifier list of the extension and perform
        // some modifier error checking 

        AST_Modifiers ml = ( AST_Modifiers ) arg[0].arg[0].arg[0];
        if ( ml!=null ) {
            if ( ml.findModifier( MethodDeclaration.mn ) ) {
                // new modifier is present in the extension -- make
                // sure that there is no corresponding method in the base
                // and remove new modifier if base is an interface -- we
                // don't want to propagate this modifier to the base

                if ( d!=null )
                    AstNode.error( arg[0].arg[2].tok[0],
                                                "new method " + sig + ") in refinement is overriding "
                        + "method in base" );
                if ( kernelConstants.globals().isBaseAnInterface )
                    ml.remModifier( MethodDeclaration.mn );
            }
            if ( ml.findModifier( MethodDeclaration.mo ) ) {
                // "override" modifer is present in extension method
                // make sure that there is a corresponding method in the
                // base.  And remove the "override" modifier in the 
                // appropriate case

                if ( d==null &&  kernelConstants.globals().isBaseAnInterface )
                    AstNode.error( arg[0].arg[2].tok[0],
                                             "override method " + sig + ") in refinement does not "
                             + "override method in base" );
                if ( kernelConstants.globals().isBaseAnInterface || d!=null )
                    ml.remModifier( MethodDeclaration.mo );
            }
        }

        // if d != null, then the extension is overriding a method
        // in the base -- d is the tree of this method.  If d!=null,
        //  compose them and return true (to delete) else return false

        if ( d != null ) {
            d.compose( this );
            return true;
        }
        return false;
    }

    public String signature() {
        return ( ( MethodDeclaration ) arg[0] ).signature();
    }
}

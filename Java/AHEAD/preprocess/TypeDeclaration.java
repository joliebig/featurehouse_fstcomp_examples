

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/** TypeDeclaration <br>
TypeDeclaration<br>
 : [ AST_Modifiers ] UnmodifiedTypeDeclaration   ::ModTypeDecl<br>
   | ";"                                         ::EmptyTDecl<br>
   ;
*
* @layer<preprocess>
*/

public class TypeDeclaration {

    /** this is an abstract method 
     * @return returns type signature of declaration
     * @layer<preprocess>
     */
    public String GetType() {
        AstNode.override( "TypeDeclaration.GetType", this );
        return null;
    }

    /** this is an abstract method 
     * @return returns name of declaration
     * @layer<preprocess>
     */
    public String GetName() {
        AstNode.override( "TypeDeclaration.GetName", this );
        return null;
    }

    public boolean isExtension() {
        AstNode.override( "TypeDeclaration.isExtension", this );
        return false;
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

   /** production: 

   AST_TypeName<br>
: PrimitiveType [ LOOKAHEAD(2) Dims ]      ::PrimType<br>
| AST_QualifiedName [ LOOKAHEAD(2) Dims ]  ::QNameType<br>
;<br>
   <br>
   PrimitiveType<br>
: "boolean"      ::BoolTyp<br>
| "char"         ::CharTyp<br>
        ...<br>
   * @layer<preprocess>
   */

public class AST_TypeName {

    /** abstract method to return name of Type 
     * @layer<preprocess>
     */

    public String GetName() {
        AstNode.override( "AST_TypeName.GetName", this );
        return null;
    }
}

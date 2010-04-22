

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/********************* Method Declaration *****************
 * @layer<preprocess>
 */

   /** production
   MethodDeclaration
: [ AST_Modifiers ] AST_TypeName MethodDeclarator
  [ ThrowsClause] MethodDeclSuffix::MethodDcl
;
   **/

public class MethodDeclaration {

    // These constants are used in processing method modifiers

    public static final 
       ModNew       mn = new  ModNew().setParms( new  AstToken().setParms( "", "new", 0 ) );
    public static final  
       ModOverrides mo = new  ModOverrides().setParms( new  AstToken().setParms( "", "overrides", 0 ) );

    public String signature() {
        AstNode.override( "MethodDeclaration.signature", this );
        return "";
    }

    public String GetName() {
        AstNode.override( "MethodDeclaration.GetName", this );
        return "";
    }

    public void setName( String name ) {
        AstNode.override( "MethodDeclaration.setName", this );
    }

    public void addModifier( Modifier m ) {
        AstNode.override( "MethodDeclaration.addModifier", this );
    }
}

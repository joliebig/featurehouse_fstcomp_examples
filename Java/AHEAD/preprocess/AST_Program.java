

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/***************************************************************
 * @layer<preprocess>
 */

/** Root of base and extension file abstract syntax trees.<br>
  *  
  *  AST_Program<br>
  *     : [ PackageDeclaration ] [ AST_Imports ] [ AST_Class ] 
  *       :: program <br>
**/

public class AST_Program {

    public boolean isExtension() {
        AstNode.override( "AST_Program.isExtension", this );
        return false;
    }
}

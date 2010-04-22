

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/*********************** AST_IMPORT CODE ***************************
 * @layer<preprocess>
 */

   /** production

   AST_Imports<br>
: ( ImportDeclaration )+<br>
;<br>
   **/

public class AST_Imports {

    private boolean findImport( ImpQual x ) {
        AstCursor c = new  AstCursor();
        AST_Imports b = ( AST_Imports ) this;
        String xName = x.GetName();

        for ( c.FirstElement( b ); c.MoreElement(); c.NextElement() ) {
            if ( ( ( ImportDeclaration ) ( c.node ) ).GetName().equals( xName ) )
                return true;
        }
        return false;
    }

    /** algorithm is to compose sequence of import statements
        ideally without replicating imports.  etree points to 
        an AST_Imports object
    *
    * @layer<preprocess>
    */

    /** compose sequence of base import declarations with 
        extension import declarations without replication */
          
    public void compose( AstNode etree ) {
        AST_Imports b = ( AST_Imports ) this;
        AST_Imports x = ( AST_Imports ) etree;

        // Step 1: foreach element e of extension imports list
        //         see if e is already present on base imports list.
        //         if so, delete it from the extension list

        AstCursor c = new  AstCursor();
        for ( c.FirstElement( x ); c.MoreElement(); c.NextElement() ) {
            if ( findImport( ( ImpQual ) c.node ) )
                c.Delete();
        }

        // Step 2: now add the truncated imports list to the base
        //         list

        b.add( x );
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

   // signature of data members "<var name>" -- doesn't involve type

   /** production
   AST_VarDecl <br>
: VariableDeclarator ( "," VariableDeclarator )* <br>
; <br>
   *
   * @layer<preprocess>
   */

public class AST_VarDecl {

    /** add variables of base to hash table 
     * @layer<preprocess>
     */
    public void add2Hash( Hashtable h ) {
        AstCursor c = new  AstCursor();

        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {
            String s = ( ( VariableDeclarator ) c.node ).GetName();
            h.put( s , c.node );
        }
    }

    /** invoked only on extension nodes -- check if extension
        wants to add fields already present in base. If so, it's
        an error 
        * @layer<preprocess>
        */

    public boolean actOnHash( Hashtable h ) {
        // method called only on nodes of extension tree

        AstCursor c = new  AstCursor();

        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {
            String s = ( ( VariableDeclarator ) c.node ).GetName();
            VariableDeclarator v = ( VariableDeclarator ) h.get( s );

            // if v == null, then the extension is defining a new variable
            // otherwise, v is being overridden.  We don't allow variables
            // to be overridden
         
            if ( v != null )
                if ( v instanceof  VarDecl )
                    AstNode.error( arg[0].arg[0].tok[0],
                                                   "cannot refine data member" + s );
                else
                    AstNode.error( "cannot refine data member" + s );
        }

        // always return false -- we don't want variables to 
        // be deleted

        return false;
    }
}

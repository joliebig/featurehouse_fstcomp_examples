

import java.util.*;
import Jakarta.util.*;
import java.io.*;

/*********************** AST_IMPORT CODE ***************************
 * @layer<mixinbase>
 */

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

    // concatenate sequence of base import declarations with 
    // extension import declarations without replication 
          
    public void compose( AstNode etree,  JTSParseTree base,
                            JTSParseTree ext ) {
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

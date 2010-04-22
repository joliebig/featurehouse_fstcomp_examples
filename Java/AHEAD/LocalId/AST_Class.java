

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

public class AST_Class {
    public void harvestLocalIds() {
        AstCursor c = new  AstCursor();

        // Step 1: initialize the hash table (for this parse tree)

        kernelConstants.globals().localId_ht = new Hashtable();

        // Step 2: localid declarations are at the top level, listed
        //         among the class or interface declarations of a .jak
        //         file.  Find each one, harvest its ids, and delete
        //         the declaration.

        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {
            if ( c.node instanceof  LocalIdProd ) {
                ( ( LocalIdProd ) c.node ).harvestLocalIds();
                c.Delete();
            }
        }
    }
}

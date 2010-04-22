

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

public class LocalIdProd {

    public void harvestLocalIds() {
        AstCursor c = new  AstCursor();

        // Step 1: for each QName on the id-list, add its 
        //         identifier and mangled-identifier to the hash table

        for ( c.FirstElement( arg[0] ); c.MoreElement(); c.NextElement() ) {
            QName q = ( QName ) c.node;
            String id = q.tok[0].tokenName();
            kernelConstants.globals().localId_ht.put( id, Util2.mangleId( id, _source ) );
        }
    }

    public void setParms( AstNode n ) {
        // this method is likely only to be called when typesorting
        // is performed-- localids should never be part of type sorting

        AstNode.fatalError( "LocalIdProd.setParms should never be called" );
    }
  
}

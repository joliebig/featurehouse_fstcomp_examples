

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

//------------------------LocalID layer------------------

//---------------- harvest local ids into hash table ------------
//---------------- does not require kernel extensions -----------

// the following deals with local-id harvesting and could
// be a layer unto itself.

public class AST_Program {
    public void harvestLocalIds() {
        AstNode.override( "harvestLocalIds", this );
    }
}

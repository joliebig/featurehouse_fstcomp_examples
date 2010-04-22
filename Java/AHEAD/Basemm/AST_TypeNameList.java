

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class AST_TypeNameList {
    public String GetSignature() {
        String sig = "(";
        String comma = "";
        AstCursor c = new  AstCursor();

        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {
            TName t = ( ( TName ) c.node );
            sig = sig + comma + t.GetSignature();
            comma = ",";
        }
        return sig + ")";
    }
}

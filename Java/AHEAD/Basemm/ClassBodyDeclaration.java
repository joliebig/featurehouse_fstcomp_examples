

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

//-------------- End TOOL OUTPUT -----------------

//---- ClassBodyDeclaration ----------------
// this is the super class of all core entries in ClassBody.
// we need to supply execute (harvest) methods for each of these
// constructs

public class ClassBodyDeclaration {
    public void execute( int stage ) {
        AstNode.override( "ClassBodyDeclaration.execute", this );
    }
    public void executeBypass( int stage ) {
        super.execute( stage );
    }
}

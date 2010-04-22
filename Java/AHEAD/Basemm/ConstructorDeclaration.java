

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

// the following classes deal with constructor signature harvesting

public class ConstructorDeclaration {
    public void execute( int stage ) {
        AstNode.override( "ConstructorDeclaration.execute()", this );
    }
    public void executeBypass( int stage ) {
        super.execute( stage );
    }
}



import java.util.*;
import Jakarta.util.*;
import java.io.*;

// ImportDeclaration is an abstract class with abstract method
// GetName. Known subclasses is ImpQual 

public class ImportDeclaration {

    /* abstract method that should be overridden by subclasses */

    public String GetName() {
        AstNode.override( "ImportDeclaration.GetName", this );
        return null;
    }
}

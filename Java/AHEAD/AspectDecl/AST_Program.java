

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// this layer encapsulates the check (and grammar rule) that
// layers can (optionally) be defined with the "layer" label.

public class AST_Program {

    public void checkAspect( String filepath ) {
        AstNode.override( "AST_Program.setPackageName", this );
    }
}

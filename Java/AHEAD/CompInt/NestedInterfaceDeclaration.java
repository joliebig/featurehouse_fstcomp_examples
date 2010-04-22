

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class NestedInterfaceDeclaration {

    public String signature() {
        AstNode.fatalError( arg[1].tok[0], 
                  "CompInt cannot compose nested interfaces" );
        return "";
    }
    public boolean actOnHash( Hashtable h ) {
        AstNode.fatalError( arg[1].tok[0], 
                   "CompInt cannot compose nested interfaces" );
        return false;
    }
    public void add2Hash( Hashtable h ) {
        AstNode.fatalError( arg[1].tok[0], 
                   "CompInt cannot compose nested interfaces" );
    }
}

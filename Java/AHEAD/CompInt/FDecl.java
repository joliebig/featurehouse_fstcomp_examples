

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/** production:
InterfaceMemberDeclaration:
  | FieldDeclaration::FDecl 
*
* @layer<CompInt>
*/

public class FDecl {

    public void add2Hash( Hashtable h ) {
        ( ( FieldDeclaration ) arg[0] ).add2Hash( h );
    }

    public String signature() {
        AstNode.fatalError( arg[0].tok[0], 
               "Cannot handle Field declarations in interfaces" );
        return "";
    }

    public boolean actOnHash( Hashtable h ) {
        // this method is called on extension tree nodes
        return ( ( FieldDeclaration ) arg[0] ).actOnHash( h );
    }
}

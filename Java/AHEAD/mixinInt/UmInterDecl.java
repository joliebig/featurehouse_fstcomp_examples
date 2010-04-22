

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

/************ MIXININT layer ****************
 * @layer<mixinInt>
 */

public class UmInterDecl {

    // this method links extension declaration as a "extends" to
    // the base declaration (whose typedeclaration name) is "name"

    public void extensionOf( String name ) {
        // add "name" to the extension list of this interface

        // Step 1: create an AST_TypeNameList

        AST_TypeNameList tl =  AST_TypeNameList.MakeAST( " " + name + " " );

        // Step 2: now either create an extension and plug it in,
  
        if ( arg[1].arg[0] == null ) {
            AstOptNode aon = ( AstOptNode ) arg[1];
            aon.setParms( new  IntExtClauseC().setParms( new  AstToken().setParms( " ","extends", 0 ),  tl ) );
        }

        // Step 3: or simply concatenate tl with the existing list

        else {
            AST_TypeNameList etnl =
                ( AST_TypeNameList ) arg[1].arg[0].arg[0];
            etnl.add( tl, "," );
        }
    }

    // this mangles the interface name and returns it

    public String getAndMangleName() {
        // get the name of the interface, mangle it, and return the
        // mangled name

        String name = mangleName( getName() );
        setName( name );
        return name;
    }

    public String getName() {
        return arg[0].tok[0].tokenName();
    }

    private void setName( String name ) {
        ( ( AstToken ) arg[0].tok[0] ).setName( name );
    }
}

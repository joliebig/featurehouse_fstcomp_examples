

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

   /** production
   InterfaceMemberDeclarations
: (InterfaceMemberDeclaration)+
;
   *
   * @layer<CompInt>
   */

public class InterfaceMemberDeclarations {

    public  AstList makeList( AstNode n ) {
        InterfaceMemberDeclarations l = 
            new  InterfaceMemberDeclarations();
        l.add( new  InterfaceMemberDeclarationsElem().setParms( n ) );
        return l;
    }

    public void compose( AstNode etree ) {
        // Composition algorithm is reasonably straightforward.
        // (a) collect all the signatures of interface members 
        //     of the base into a hash table.
        // (b) for each member of the extension, see if there is a
        //     corresponding member in the base.  if so, compose them --
        //     and remove the member from the extension.
        // (c) the remaining members on the extension list are to be
        //     added to the base list.
        // (d) optionally typeSort the list of composed declarations

        // Step 1: collect signatures of base interface members into a 
        //         hash table

        Hashtable          h = new Hashtable();
        AstCursor     c = new  AstCursor();

        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {
            ( ( InterfaceMemberDeclaration ) c.node ).add2Hash( h );
        }
 
        // Step 2: for each member of the extension, see if it is present
        //         in the base

        InterfaceMemberDeclaration d;

        for ( c.FirstElement( etree ); c.MoreElement(); c.NextElement() ) {
            if ( ( ( InterfaceMemberDeclaration ) c.node ).actOnHash( h ) )
                c.Delete();
        }

        // Step 3: the remaining members on the extension list should
        //         be added to the base list

        this.add( ( AstList ) etree );

        // Step 4: optionally typeSort the list of declarations

        if ( Main.typeSort )
            typeSort( new  InterfaceMemberDeclarations() );
    }
}



import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

//-------------------- COMMON MODIFIER LIST UTILITIES ------------------

   /** production:

   AST_Modifiers
  : ( Modifier )+
  ;

   Modifier<br>
      : ABSTRACT::ModAbstract<br>
      | FINAL::ModFinal<br>
      | PUBLIC::ModPublic<br>
      | PROTECTED::ModProtected<br>
      ...<br>
      ;<br>
   *
   * @layer<Java>
   */

public class AST_Modifiers {

    // search modifier list of base for modifier x.
    // return true if present
 
    public boolean findModifier( Modifier x ) {
        return findModifier( x.GetName() );
    }

    public boolean findModifier( String mod ) {
        AstCursor     c = new  AstCursor();
        AST_Modifiers b = ( AST_Modifiers ) this;

        for ( c.FirstElement( b ); c.MoreElement(); c.NextElement() ) {
            if ( ( ( Modifier ) ( c.node ) ).GetName().equals( mod ) )
                return true;
        }
        return false;
    }

    public void remModifier( Modifier x ) {
        remModifier( x.GetName() );
    }

    public void remModifier( String xName ) {
        AstCursor     c = new  AstCursor();
        AST_Modifiers b = ( AST_Modifiers ) this;

        for ( c.FirstElement( b ); c.MoreElement(); c.NextElement() ) {
            if ( ( ( Modifier ) ( c.node ) ).GetName().equals( xName ) )
                c.Delete();
        }
    }

    // to create a modifier for Public, use:
    // new $TEqn.ModPublic().setParms( 
    //          new $TEqn.AstToken().setParms(" ","public", 0)) 
    // check with the java grammar to see the correct naming
     
    public void addModifier( Modifier m ) {

        // Step 1: if modifier already is present, do nothing

        if ( findModifier( m ) )
            return;

        // Step 2: create a list of length one with m on it

        AST_Modifiers l = new  AST_Modifiers();
        l.add( new  AST_ModifiersElem().setParms( m ) );

        // Step 3: add the modifier to the list

        this.add( l );
    }
}

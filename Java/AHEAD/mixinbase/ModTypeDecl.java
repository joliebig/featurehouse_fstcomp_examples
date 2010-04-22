

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class ModTypeDecl {

    // replaces extension TypeDeclaration with Java counterpart

    public  TypeDeclaration prepareReplace( JTSParseTree t ) {
        UnmodifiedTypeDeclaration utd;

        utd = ( UnmodifiedTypeDeclaration ) arg[1];
        utd.Replace( utd.prepareReplace( t ) );
        return ( TypeDeclaration ) this;
    }

    public boolean isExtension() {
        return ( ( UnmodifiedTypeDeclaration ) arg[1] ).isExtension();
    }

    public void extensionOf( String name ) {
        ( ( UnmodifiedTypeDeclaration ) arg[1] ).extensionOf( name );
    }

    public String getAndMangleName() {
        return ( ( UnmodifiedTypeDeclaration ) arg[1] ).getAndMangleName();
    }

    public String getName() {
        return ( ( UnmodifiedTypeDeclaration ) arg[1] ).getName();
    }

    // set the modifier list to be {abstract}

    public void setAbstractModifier() {
        String com = getComment();
        ModAbstract ma = new  ModAbstract().setParms( new  AstToken().setParms( " ","abstract", 0 ) );
        arg[0].Replace( new  AST_Modifiers().add( new  AST_ModifiersElem().setParms( ma ) ) );
        arg[0].setComment( com );
        arg[1].setComment( " " );
    }

    // set the modifier list to m

    public void addModifiers( AST_Modifiers m ) {

        // Step 1: if m is null, return immediately

        if ( m == null )
            return;

        // Step 2: if original modifierlist is empty
        //         move the comment to the new modifier list

        AST_Modifiers a = ( AST_Modifiers ) arg[0].arg[0];
        String com = getComment();
        if ( a == null ) {
            setComment( " " );
            arg[0].Replace( m );
            setComment( com );
            return;
        }

        // Step 2: original list is not empty -- go ahead and
        //         add each new modifier, one at a time

        AstCursor c = new  AstCursor();
        for ( c.FirstElement( m ); c.MoreElement(); c.NextElement() )
            a.addModifier( ( Modifier ) c.node );
        arg[0].setComment( com );
        arg[1].setComment( " " );
    }

    public  AST_Modifiers getModifier() {
        return ( AST_Modifiers ) arg[0].arg[0];
    }
}

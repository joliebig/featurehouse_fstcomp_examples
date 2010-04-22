

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class TypeDeclaration {

    // replaces extension TypeDeclaration with Java counterpart

    public  TypeDeclaration prepareReplace( JTSParseTree t ) {
        AstNode.override( "TypeDeclaration.prepareReplace", this );
        return null; // pacify whiney compiler
    }

    // is type decl an extension or not?

    public boolean isExtension() {
        AstNode.override( "TypeDeclaration.isExtension", this );
        return true; // pacify whiney compiler
    }

    public void extensionOf( String name ) {
        AstNode.override( "TypeDeclaration.extensionOf", this );
    }

    public String getAndMangleName() {
        AstNode.override( "TypeDeclaration.getAndMangleName", this );
        return null; // pacify whiney compiler
    }

    public String getName() {
        AstNode.override( "TypeDeclaration.getName", this );
        return null; // pacify whiney compiler
    }

    public void addModifiers( AST_Modifiers m ) {
        AstNode.override( "TypeDeclaration.addModifiers", this );
    }

    public void setAbstractModifier() {
        AstNode.override( "TypeDeclaration.setAbstractModifier", this );
    }

    public  AST_Modifiers getModifier() {
        AstNode.override( "TypeDeclaration.getModifier", this );
        return null;
    }
}

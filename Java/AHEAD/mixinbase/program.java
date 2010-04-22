

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class program {

    // has file already been formatted for mixin composition?

    public boolean alreadyPrepared( JTSParseTree t ) {
        // arg[2].arg[0] is the optionally specified AST_Class declaration

        AST_Class ac = ( AST_Class ) arg[2].arg[0];

        if ( ac == null ) {
            AstNode.error( "file has no base or refinement declaration" );
            return true;
        }

        return ac.alreadyPrepared( t );
    }

    // updates firstType, lastType data members of JTSParseTree given
    // TypeDeclaration r

    private void updateTypes( JTSParseTree t,  TypeDeclaration r ) {
        if ( t.firstType == null )
            t.firstType = r;
        t.lastType = r;
    }

    private void initTypes( JTSParseTree t ) {
        t.firstType = null;
        t.lastType  = null;
    }

    // this method is called if it has already been prepared -- this
    // is possible only if mixin was used to compose a set of layers
    // previously.

    public void processAlreadyPrepared( JTSParseTree t ) {

        // the primary objective of this method is to set the firstType
        // and lastType values of t.  As a secondary objective, we do
        // some simple error checking.

        // Step 1: iterate through the AST_Class list. This list must 
        //         be an alternating set of SoUrCe and ModTypeDecl
        //         declarations.  Along the way, update firstType, lastType.

        AstCursor k = new  AstCursor();
        AST_Class sd = ( AST_Class ) arg[2].arg[0];
        for ( k.FirstElement( sd ); k.MoreElement(); k.NextElement() ) {

            if ( k.node instanceof  SourceDecl ) {

                // now extract the name of the layer (aspect) so that we can 
                // tag each of its AST nodes with the appropriate name

                String lName = ( ( AST_QualifiedName ) k.node.arg[0] ).GetName();
                k.NextElement();
                updateTypes( t, ( TypeDeclaration ) k.node );
                k.node.setSource( lName );
            }
            else
                AstNode.error( "file was incorrectly created/composed" );
        }
    }

    // prepare file for mixin composition
    // this amounts to introduceing SoUrCe declarations and
    // rewriting the TypeDeclaration extensions into its java equivalent

    public void prepare( JTSParseTree t ) {
        AST_Class sd;
        TypeDeclaration repl;

        // Step 1: first check to see if the parsed file is already 
        //         prepared.  We will know this if "SoUrCe" nodes exist.

        initTypes( t );
        if ( alreadyPrepared( t ) ) {
            processAlreadyPrepared( t );
            return;
        }

        // Step 2: so the file needs to be prepared.  This means that
        //         there is one TypeDeclaration.  We need to place a 
        //         SoUrCe statement in front of it, and convert it
        //         into a normal class, interface, etc. declaration.
        //         along the way, remember the first and last type 
        //         declarations on the list.  Doing so is useful as
        //         we will need these references later to do mixin
        //         parameter substitution

        AstCursor k = new  AstCursor();
        int count = 0;
        sd = ( AST_Class ) arg[2].arg[0];
        for ( k.FirstElement( sd ); k.MoreElement(); k.NextElement() ) {
            // Step 2.1: keep a count of the number of type declarations.
            //           also, generate a relative URI for the universal
                //                          to increase portability of source filename.

            count++;
            String universalName =  Main.file2uri( t.filepath ) ;

            // Step 2.2: before we proceed, make sure that the element
                                                //           we are examining is indeed a ModTypeDecl.  If not,
                                                //           the .jak file is in the wrong format.

            if ( ! ( k.node instanceof  ModTypeDecl ) )
                AstNode.fatalError( k.node.findToken(), ".jak file has incorrect format: " +
                                                                                    "expected a modified type declaration (ModTypeDecl) but found " + 
                                                                                                 k.node.getClass().getName() );

            // Step 2.3: is the type declaration an extension or not?

            String isRoot = "";
            if ( ! ( ( ModTypeDecl ) k.node ).isExtension() )
                isRoot = "RooT";

            // Step 2.4 form the SoUrCe declaration 

            sd =  AST_Class.MakeAST( "\n\nSoUrCe " + isRoot + " " + 
                   getSource() + " " +
                   "\"" + universalName + "\";" );
            k.AddBefore( sd );
            repl = ( ( TypeDeclaration ) k.node ).prepareReplace( t );
           
            // Step 2.5: now remember the first and last type declaration.
            //           this information is critical for instantiating
            //           mixin layer parameters

            updateTypes( t, repl );
        }
  
        // Step 3: if there is more than one type declaration, it's an error 
        if ( count==0 )
            AstNode.error( "file has no base or refinement declaration" );
        else
            if ( count >1 )
                AstNode.error( "file has 2 or more root or refinement declarations" );
    }

    // compose base tree with extension tree 

    public void compose( AstNode etree,  JTSParseTree base,
            JTSParseTree ext ) {
        // composition involves
        // (a) doing nothing about aspect declaration
        // (b) compose the imports lists
        // (c) compose the AST_Class declarations

        // Step 1: do preliminary testing
        //         make sure argument is correct type

        program e = ( program ) etree;

        // Step 2: compose the imports list and AST_Class declarations

        arg[1].compose( e.arg[1], base, ext );
        arg[2].compose( e.arg[2], base, ext );

    /*
                // Step 3: add a blank line to separate imports and packages

                if (arg[1].arg[0] != null)
                   arg[1].arg[0].prependComment("\n");
    */
    }

    // can remove into its own layer -- stolen from preprocess

    public boolean isExtension() {
        if ( arg[2].arg[0] == null )
            // missing TypeDeclaration
            AstNode.fatalError( "file has no base or refinement declaration" );
        return ( ( AST_Class ) arg[2].arg[0] ).isExtension();
    }
}

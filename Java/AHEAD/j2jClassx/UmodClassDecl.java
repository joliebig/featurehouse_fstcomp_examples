

import java.util.*;
import java.io.*;
import Jakarta.util.*;

//------------------------ j2jClassx layer -------------------
//       encapsulates refinement of classes and anything
//       to do with their composition.  in this case, the j2j tool
//       requires some rewrites of classes *prior* to their
//       reduction.  Also, the j2j tool will be able to parse extensions
//       to classes, but will flag them as errors.

public class UmodClassDecl {

    String previous = "";

    public void harvestConstructors( int stage ) {

        // Step 0: do nothing if we are inside quoted text

        if ( stage != 0 ) {
            super.harvestConstructors( stage );
            return;
        }

        // Step 1: copy the inheritedCons of kernelConstants.j2jbase -- all the
        //         constructors we have seen up until now are 
        //         the constructors that we want to inherit

        copyConstructors();

        // Step 2: now harvest the constructors of the classBody
        //         this is where propagations of constructors UP
        //         the hierarchy can take place

        arg[3].harvestConstructors( stage );

        // Step 3: add reference to this type declaration

        kernelConstants.globals().j2jbase.previousTypeDecls.add( this );
    }

    private void finishReduction( AstProperties props ) {
        super.reduce2java( props );
        props.setProperty( "MixinDeclName", "" );
        props.setProperty( "SuperName", "" );
		  props.setProperty( "ThisName", previous );
    }

    public void reduce2java( AstProperties props ) {

        // Step 1: remember the name of the class that is being extended
        //         this is needed for correctly expanding Super() constructs
        //         inside static methods.

        String extendsName = "";
        ExtendsClause ec = ( ExtendsClause ) arg[1].arg[0];
        if ( ec != null )
            extendsName = ec.GetName();
        props.setProperty( "SuperName", extendsName );

        // Step 2: do a normal reduction if we haven't seen SoUrCe decls

        if ( props.getProperty( "SoUrCe" ) == null ) {
            finishReduction( props );
            return;
        }

        // Step 3: we could be reaching this reduction method because
        //         we have translated state machines or whatever into
        //         a class.  We don't perform double harvesting and
        //         rewriting, because this will generate errors.
        //         So if a non-null MixinDeclName is returned, we do
        //         nothing except a normal reduction
		  //         

        String name = ( String ) props.getProperty( "MixinDeclName" );
        if ( ! (name == null || name.equals( "" ) 
		          || (up instanceof NClassDecl) ) ) {
            finishReduction( props );
            return;
        }

        // Step 4: remember the name of the class that is being reduced
 
        String className = arg[0].tok[0].tokenName();
        props.setProperty( "MixinDeclName", className );
		  previous = (String) props.getProperty( "ThisName" );
		  if (previous == null) previous = "";
		  props.setProperty( "ThisName", Util2.unmangleId(className) );

        // Step 5: make sure that an FieldDecl is present

        if ( arg[3].arg[0].arg[0] == null ) {

            // can't set it because there is an empty ClassBody
            // so here's the hack -- we'll add an empty AST_FieldDecl
            // so that we can set its boolean

            arg[3].arg[0].Replace( new  AST_FieldDecl() );
        }
 
        // Step 6: add the ConstructorMarker

        AST_FieldDecl f = ( AST_FieldDecl ) arg[3].arg[0].arg[0];
        f.addMarker( inheritedCons );
  
        // Step 7: now reduce normally.  Clear refineSet as this
        //         keeps track of constructor refinements in this class.

        kernelConstants.globals().j2jbase.refinedSet.clear();

        // Step 8: finish the reduction

        finishReduction( props );
    }
}

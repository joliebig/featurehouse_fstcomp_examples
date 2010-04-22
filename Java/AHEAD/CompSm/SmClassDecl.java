

import java.util.*;
import java.io.*;

public class SmClassDecl {

    public Hashtable h = new Hashtable();

    // piggyback onto checkForErrors the checking for 
    // errors in replicated definitions within a single file.
    // the error checking is performed on State definitions
    // and Transition definitions

    public void checkForErrors( int state, String file ) {
        super.checkForErrors( state, file );
        StatesList slb = ( StatesList ) arg[2].arg[0];
        if ( slb!=null )
            slb.add2Hash( h, kernelConstants.globals().currentFileName );
        ESList esb = ( ESList ) arg[3].arg[0];
        if ( esb!=null )
            esb.add2Hash( h, kernelConstants.globals().currentFileName );
    }
     
    public void compose( AstNode etree ) {

        // Step 0: Do some standard error checking - etree is of type
        //         SmClassDecl.  

        SmClassDecl e = ( SmClassDecl ) etree;

        // Step 1: compose the root clauses

		  arg[0].compose( e.arg[0] );

        // Step 2: compose and otherwise clauses.  Just merge the lists

        arg[1].compose( e.arg[1] );

        // Step 3: foreach key in the extension's hashtable, try to 
        //         add it to the base hash table.  If there is a collision
        //         then we have an error.

        for ( Enumeration c = e.h.keys(); c.hasMoreElements(); ) {
            String key = ( String ) c.nextElement();
            String source = ( String ) e.h.get( key );
            String result = ( String ) h.get( key );
            if ( result==null )
                h.put( key,source );
            else
                // so there is a state already defined in base with 'key' as its name
                // if either the extension or base has correctly defined the
                // state, then don't issue a warning
                if ( ! ( result.equals( "unknown layer" ) || source.equals( "unknown layer" ) ) )
                    AstNode.error( key + " is defined both in " + source + " and in " + result );
        }

        // Step 4: all that remains is to compose AST_FieldDecls.  What's
        //         unusual here is that not only do we compose the base and
        //         extension AST_FieldDecls, but we also have to pass in
        //         a LinkedList of ASTs of extension method bodies that
        //         are not part of the extension AST_FieldDecl proper.
        //         so ASTs like edge actions, edge boolean tests, exit
        //         methods must first be harvested onto this list before
        //         we can officially compose.  The reason for doing so
        //         is that these extra method bodies may reference methods
        //         of the "base" class via the Base() construct.  These
        //         references must be rewritten.

        //         The sources of harvest include the: OtherwiseClause,
        //         and ESList.  no class bodies are present in StatesLists

        LinkedList ll = new LinkedList();

        OtherwiseClauses oc = ( OtherwiseClauses ) e.arg[1].arg[0];
        if ( oc!=null )
            oc.harvestAst( ll );
        ESList es = ( ESList ) e.arg[3].arg[0];
        if ( es!=null )
            es.harvestAst( ll );

        // Step 5: compose the methods and data members of AST_FieldDecl
        //         in the approved manner

        arg[4].arg[0] = 
             AST_FieldDecl.compose( arg[4].arg[0], e.arg[4].arg[0], ll );

        // Step 6: the last step in composition is to assume that there
        //         are no semantic errors and compose directly the
        //         stateslist and eslist declarations

        arg[2].compose( e.arg[2] );
        arg[3].compose( e.arg[3] );
    }
}

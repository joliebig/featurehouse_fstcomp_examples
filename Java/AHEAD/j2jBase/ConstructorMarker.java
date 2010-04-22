

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

   // this class is interesting. It extends the set of nodes that
   // can appear in a parse tree, but the parser can't generate this
   // node.  It is used to mark the point where inherited constructors
   // are inserted.

// a blank token is added so that addComment routine will still work.
// strange case that, if not present, would generate an error.

public class ConstructorMarker extends  ClassBodyDeclaration {

    final boolean order[] = { true };
    final static String me = "insert_inherited_constructors_here;";
    conTable inheritedCons; // points to constructors to inherit

    public ConstructorMarker( conTable inheritedCons ) {
        this.inheritedCons = inheritedCons;
        arg = new  AstNode[0];
        tok = new  AstTokenInterface[1];
        tok[0] = new  AstToken().setParms( "","",0 ); // blank token
    }

    // added to pacify compiler

    public boolean[] printorder() {
        return order;
    }

    // some standard routines just in case
    // note: I added the clone method because without it, I was
    // getting "can't clone errors" or instantiation exceptions.
    // I have no idea why, so this hack fixes it.

    public Object clone() {
        return new  ConstructorMarker( inheritedCons );
    }

    public void harvestConstructors( int stage ) {}
    public String toString() {
        return me;
    }
    public void print() {
        System.out.println( me );
    }
    public void print( AstProperties props ) {
        props.print( me );
    }

    // this is the key method: it adds the inherited constructors to 
    // the output -- only if MixinDeclName has been set.  It is 
    // set only if we have seen a SoUrCe declaration

    public void reduce2java( AstProperties props ) {

        // Step 0: do nothing if we have not seen a SoUrCe decl

        if ( props.getProperty( "SoUrCe" )==null )
            return;

        // Step 1: add a comment that says we're about to inherit constructors

        if ( inheritedCons.size()!= 0 ) {
            props.println( "" );
            props.println( "      // inherited constructors" );
            props.println( "" );
        }

        // Step 2: foreach inherited constructor, change its name
        //         and output it -- there should be no need to reduce.
        //         and actually, if we did reduce, we might get
        //         errors because we're referencing super
        //         note: the addition of constructor refinements adds the
        //         following twist.  Previously, we generated constructors
        //         that were always printed.  With constructor refinements,
        //         we only print if the constructors were not refined.  In
        //         this way we either print a default refinement or a user-
        //         specified refinement, but not both.

        String className = ( String ) props.getProperty( "MixinDeclName" );
        for ( Enumeration e = inheritedCons.keys(); e.hasMoreElements(); ) {
            String sig = ( String ) e.nextElement();
            if ( kernelConstants.globals().j2jbase.refinedSet.contains( sig ) )
                continue;
            ConDecl c = ( ConDecl ) inheritedCons.get( sig );
            c.changeNameTo( className );
            c.print( props );
        }
    }
}

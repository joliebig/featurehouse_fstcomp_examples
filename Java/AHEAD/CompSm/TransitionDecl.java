

import java.util.*;
import java.io.*;

public class TransitionDecl {

    public void harvestAst( LinkedList ll ) {
        // harvest the conditions and block for TransitionDecl
        ll.add( arg[3] );
        ll.add( arg[4] );
    }

    public void add2Hash( Hashtable h, String source ) {

        // Step 1: Standard stuff for error checking -- add an element
        //         to the hash table and make sure that the edge was not
        //         defined before.

        String edgeName = arg[0].tok[0].tokenName();
        String sig = "transition " + edgeName;
        String result = ( String ) h.get( sig );
        if ( result == null )
            h.put( sig, source );
        else
            AstNode.error( tok[0], "duplicate " + sig + " declarations in " + result + 
                        " and " + source );

        // Step 2: now check if the states are known -- if not, issue a warning

        if ( arg[1] instanceof  SmSName ) {
            String startState = "state " + arg[1].arg[0].tok[0].tokenName();
            verifyState( h, startState, sig );
        }
        String endState = "state " + arg[2].tok[0].tokenName();
        verifyState( h, endState, sig );
    }
}



import java.util.*;
import java.io.*;

public class Es {

    public void add2Hash( Hashtable h, String source ) {
        AstNode.override( "add2Hash", this );
    }

    public void harvestAst( LinkedList ll ) {
        AstNode.override( "harvestAst", this );
    }

    public void verifyState( Hashtable h, String inWhat ) {
        String statekey = "state " + arg[0].tok[0].tokenName();
        verifyState( h, statekey, inWhat );
    }

    public void verifyState( Hashtable h, String statekey, String inWhat ) {

        // Step 1: ignore references to implicitly defined states

        if ( statekey.equals( "state start" ) || statekey.equals( "state stop" ) )
            return;

        // Step 2: now check if the state has been defined -- otherwise
        //         issue a warning and enter a dummy definition for the state

        if ( h.get( statekey ) == null ) {
            AstNode.warning( arg[0].tok[0], statekey + " in " + inWhat + 
                         " declaration has not yet been defined" );
            h.put( statekey, "unknown layer" );
        }
    }

    public void verifyTransition( Hashtable h, String inWhat ) {
        String trans = "transition " + arg[0].tok[0].tokenName();

        // now check if the transition has been defined -- otherwise
        // issue a warning

        if ( h.get( trans ) == null ) {
            AstNode.warning( arg[0].tok[0], trans + " in " + inWhat + 
                         " declaration has not yet been defined" );
            h.put( trans, "unknown layer" );
        }
    }
}

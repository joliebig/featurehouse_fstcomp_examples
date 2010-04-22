

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class StatesClause {

    // call this method to define a state

    public static  stateInfo defineState( String stateName, boolean nested,  AstTokenInterface t ) {
        stateInfo s;

        // Step 1: create a stateInfo object for this state 
        //         and make sure it is unique.  If so, add it
  
        s = new  stateInfo( stateName, nested );
        if ( kernelConstants.globals().sm4vars.Sm.StateCont.find( s ) != null )
            AstNode.fatalError( t, Utility.SourceName()+
                            "duplicate state name " + stateName );
        kernelConstants.globals().sm4vars.Sm.StateCont.add( s );

        // Step 2: assign state a number

        kernelConstants.globals().sm4vars.Sm.state_constants =  kernelConstants.globals().sm4vars.Sm.state_constants +
         "   final static int " + s.name + " = stateNumGenerator++; \n";

        return s;
    }
}

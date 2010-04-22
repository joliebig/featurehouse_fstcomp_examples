

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// transInfo is instantiated per transition declaration.

class transInfo extends contInfo {
    public String         name; // name of transition
    public String         start; // name of start state
    public String         end; // name of end state
    public  AST_Exp  condition_ast; // transition condition
    public  AST_Stmt action_ast; // transition action
    public String         branches; // branch code
    public boolean        defined_in_file; // trans decl in current file?

    public transInfo( String name, String startName, String endName ) {
        this.name      = name;
        start          = startName;
        end            = endName;
        condition_ast  = null;
        action_ast     = null;
        String whichExit = startName.equals( "*" ) ? 
                            "dispatch.exit(" : startName+"_exit(";
		  String whichReturn = (startName.equals( "*" )?
		                      "return false; " : "return; ") + "}\n";
        branches       = "      if ( " + name + "_test(" + 
             kernelConstants.globals().sm4vars.Sm.argdecl + ") )"
            + " { " + whichExit +  kernelConstants.globals().sm4vars.Sm.argdecl + "); "
            + name + "_action(" +  kernelConstants.globals().sm4vars.Sm.argdecl + "); " 
            + endName + "_enter(" +  kernelConstants.globals().sm4vars.Sm.argdecl + "); "
            + whichReturn;
        inherited      = false;
        defined_in_file= true;
    }

    // used for serialization - throw away anything that is not needed.
    // this object is serialized, and is read in during the processing
    // of parent state machines in inheritance hierarchies.  Not all
    // information is needed -- most is thrown away.
    // REMEMBER: if an AST is to be saved/serialized, it must be
    // Detached from the tree

    public void truncate() {
        inherited = true;
        // leave name
        // leave start
        // leave end
        condition_ast = null;
        action_ast = null;
        // leave action_method_name
        if ( !start.equals( "*" ) ) // leave branches as is if from *
            branches = null;
        defined_in_file = false;
    }

    // only for debugging

    public void print() {
        System.out.println( "   Transition name : " + name + " ast " + name );
        System.out.println( "   start("+start+") end("+end+")" );
        System.out.println( "   Condition: "+condition_ast );
        System.out.println( "   Action: "+action_ast );
        System.out.println( "   Branches: "+branches );
        System.out.println( "   Inherited: " + inherited );
        System.out.println( "   Defined-in-file: " + defined_in_file );
        System.out.println();
    }

    public boolean equals( Object x ) {
        if ( x instanceof  transInfo )
            return ( ( transInfo ) x ).name.equals( name );
        return false;
    }

    static  transInfo verifyTransName( String ename, String which, 
                                               AstTokenInterface t ) {
        transInfo e;

        SmIterator i =  kernelConstants.globals().sm4vars.Sm.TransCont.iterator();
        for ( e = ( transInfo ) i.firstObj(); 
               e != null; 
               e = ( transInfo ) i.nextObj() ) {
            if ( e.name.equals( ename ) )
                return e;
        }
        
        AstNode.fatalError( t, Utility.SourceName()+ 
                         "Unrecognized transition " + ename + " in " + which );
        return /* should never get here */ null;
    }
}

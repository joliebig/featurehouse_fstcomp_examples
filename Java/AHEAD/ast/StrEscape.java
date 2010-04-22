

import java.io.PrintWriter;

//**************************************************
// StrEscape extension class -- should never appear on a list
//**************************************************
   
public class StrEscape {
    public void reduce2java( AstProperties props ) {
        PrintWriter pw = ( PrintWriter ) props.getProperty( "output" );
        AstToken atok = ( AstToken ) tok[0];

        pw.print( atok.white_space );
        pw.print( "\"" );
        arg[0].reduce2java( props );
        pw.print( "\"" );
    /* $str outside AST constructor should be retired -- but not yet
       delete the above code after bootstrapping
    $TEqn.AstNode.error( tok[0], 
           "$str construct found outside of AST constructor");
    */
    }

    public void reduce2ast( AstProperties props ) {
        PrintWriter pw = ( PrintWriter ) props.getProperty( "output" );
        Integer oldLevel = ( Integer ) props.getProperty( "AstLevel" );

        // Decrement AstLevel marker
        if ( oldLevel != null ) {
            if ( oldLevel.intValue() ==1 )
                props.removeProperty( "AstLevel" );
            else
                props.setProperty( "AstLevel", new Integer( oldLevel.intValue()-1 ) );
        }
        else
            // no way reduce2ast node can be called without an AstLevel marker
            AstNode.fatalError( tok[0], "AstLevel marker missing!!!" );

        // output string literal in AST form
        String qual =  kernelConstants.LangName;
        String result = "(" + qual + "StrLit) new " + qual + 
                       "StrLit().setParms( new " + qual + 
                                 "AstToken().setParms(\"\", '\"' + " + arg[0] + " + '\"',0))";
        pw.print( result );

        // Restore AstLevel marker (increment)
        props.setProperty( "AstLevel", oldLevel );
    }
}

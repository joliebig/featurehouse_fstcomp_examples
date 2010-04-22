

import java.io.PrintWriter;

//**************************************************
// ExpEscape extension class
//**************************************************
    
public class ExpEscape 
    implements  EscapeMarker { // not a list
    String L =  kernelConstants.LangName;
    String beginParen = "(" + L +"ExprPre) new " + 
        L +"ExprPre().setParms( new " + 
        L +"AstToken().setParms(\" \",\"(\", 0),";
    String endParen =  ", new " +
        L + "AstToken().setParms(\"\",\")\", 0))";
        
    public void reduce2ast( AstProperties props ) {
        PrintWriter pw = ( PrintWriter ) props.getProperty( "output" );
        pw.println( beginParen );
        reduce2astEscape( props, "Expression" );
        pw.println( endParen );
    }
}



import java.io.PrintWriter;

//**************************************************
// ClsEscape extension class
//**************************************************
    
public class ClsEscape 
    implements  EscapeMarker,  IsList {
        
    public void reduce2ast( AstProperties props ) {
        reduce2astEscape( props, "AST_Class" );
    }
}



import java.io.PrintWriter;

//**************************************************
// AiEscape extension class
//**************************************************
    
public class AiEscape 
    implements  EscapeMarker,  IsList {
        
    public void reduce2ast( AstProperties props ) {
        reduce2astEscape( props, "AST_ArrayInit" );
    }
}

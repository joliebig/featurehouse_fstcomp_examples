

import java.io.PrintWriter;

//**************************************************
// StmEscape extension class
//**************************************************
    
public class StmEscape 
    implements  EscapeMarker,  IsList {
        
    public void reduce2ast( AstProperties props ) {
        reduce2astEscape( props, "AST_Stmt" );
    }
}

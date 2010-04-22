

import java.io.PrintWriter;

//**************************************************
// NameIdEscape extension class
//**************************************************
    
public class NameIdEscape 
    implements  EscapeMarker,  IsList {
        
    public void reduce2ast( AstProperties props ) {
        PrintWriter pw = ( PrintWriter ) props.getProperty( "output" );
        pw.print( "( " );
        reduce2astEscape( props, "AST_QualifiedName" );
        pw.print( " ).makeQName()" );
    }
}

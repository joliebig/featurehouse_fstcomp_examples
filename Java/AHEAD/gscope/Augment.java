

import java.io.PrintWriter;

//**************************************************
// Augment extension class
//**************************************************
    
public class Augment {
    public void reduce2java( AstProperties props ) {
        PrintWriter ps;

        ps = ( PrintWriter ) props.getProperty( "output" );
        ps.print( "_E.addId(" );
        arg[0].reduce2java( props );
        ps.print( ");\n" );
    }
}

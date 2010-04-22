

import java.io.PrintWriter;

//**************************************************
// class AstToken extension
//**************************************************
    
public class AstToken
    implements AstTokenInterface {

    //**************************************************
    // reduce2ast()
    //**************************************************
    public void reduce2ast( AstProperties props ) {
        PrintWriter ps;
        Environment env;

        ps = ( PrintWriter ) props.getProperty( "output" );
        env = ( Environment ) props.getProperty( "env" );
        ps.print( "new " +  kernelConstants.LangName + "AstToken().setParms(\"" +
                     makeString( white_space ) + "\",\"" +
                     makeString( name ) + "\", 0)" );

    // Mangling is done at run-time only.
    // Logic has moved to NameId class.
    }
}

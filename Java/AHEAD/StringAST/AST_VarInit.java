

import java.io.*;

public class AST_VarInit {
    static public  AST_VarInit MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_VarInit) parser.parse ("AST_VarInit") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

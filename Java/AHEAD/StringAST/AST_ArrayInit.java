

import java.io.*;

public class AST_ArrayInit {
    static public  AST_ArrayInit MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_ArrayInit) parser.parse ("AST_ArrayInit") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

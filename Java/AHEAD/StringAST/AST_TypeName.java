

import java.io.*;

public class AST_TypeName {
    static public  AST_TypeName MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_TypeName) parser.parse ("AST_TypeName") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}



import java.io.*;

public class AST_Imports {
    static public  AST_Imports MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_Imports) parser.parse ("AST_Imports") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

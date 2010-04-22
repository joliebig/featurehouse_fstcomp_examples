

import java.io.*;

public class AST_Stmt {
    static public  AST_Stmt MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
            return (AST_Stmt) parser.parse ("AST_Stmt") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

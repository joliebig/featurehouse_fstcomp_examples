

import java.io.*;

public class AST_ExpStmt {
    static public  AST_ExpStmt MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_ExpStmt) parser.parse ("AST_ExpStmt") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

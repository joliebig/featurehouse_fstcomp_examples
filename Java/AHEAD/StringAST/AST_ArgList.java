

import java.io.*;

public class AST_ArgList {
    static public  AST_ArgList MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_ArgList) parser.parse ("AST_ArgList") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

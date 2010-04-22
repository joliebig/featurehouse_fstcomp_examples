

import java.io.*;

public class AST_ParList {
    static public  AST_ParList MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_ParList) parser.parse ("AST_ParList") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

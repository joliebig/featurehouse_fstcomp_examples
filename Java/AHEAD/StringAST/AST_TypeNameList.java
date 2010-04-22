

import java.io.*;

public class AST_TypeNameList {
    static public  AST_TypeNameList MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_TypeNameList) parser.parse ("AST_TypeNameList") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

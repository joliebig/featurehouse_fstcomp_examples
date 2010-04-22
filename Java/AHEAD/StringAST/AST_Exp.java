

import java.io.*;

/**************************************************
/*  this layer includes all string-to-ast constructors
/*  its capabilities are not factored into the AST layer
/*  because the current AST layer is too closely fused 
/*  with the generation scoping layer. Ultimately, it should
/*  be merged with AST
/*************************************************
* @layer<StringAST>
*/

public class AST_Exp {
    static public  AST_Exp MakeAST( String in ) {
        try {
	    Parser parser = Parser.getInstance (new StringReader (in)) ;
	    return (AST_Exp) parser.parse ("AST_Exp") ;
        }
        catch ( ParseException pe ) {
            AstNode.fatalError( "string-to-ast parse error: " + in );
	    return null ;
        }
    }
}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class IOEBod extends IOEBody {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public ExclusiveOrExpression getExclusiveOrExpression () {
        
        return (ExclusiveOrExpression) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public IOEBod setParms (AstToken tok0, ExclusiveOrExpression arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "|" */
        arg [0] = arg0 ;            /* ExclusiveOrExpression */
        
        InitChildren () ;
        return (IOEBod) this ;
    }

}

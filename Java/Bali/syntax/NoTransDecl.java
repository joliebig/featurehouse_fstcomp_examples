// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class NoTransDecl extends NoTransitionClause {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public Block getBlock () {
        
        return (Block) arg [0] ;
    }

    public AstToken getUNRECOGNIZABLE_STATE () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public NoTransDecl setParms (AstToken tok0, Block arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* UNRECOGNIZABLE_STATE */
        arg [0] = arg0 ;            /* Block */
        
        InitChildren () ;
        return (NoTransDecl) this ;
    }

}

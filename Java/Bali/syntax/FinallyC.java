// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class FinallyC extends Finally {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public Block getBlock () {
        
        return (Block) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public FinallyC setParms (AstToken tok0, Block arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "finally" */
        arg [0] = arg0 ;            /* Block */
        
        InitChildren () ;
        return (FinallyC) this ;
    }

}

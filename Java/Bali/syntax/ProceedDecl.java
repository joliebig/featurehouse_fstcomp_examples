// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class ProceedDecl extends PrimaryPrefix {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public Arguments getArguments () {
        
        return (Arguments) arg [0] ;
    }

    public AstToken getPROCEED () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public ProceedDecl setParms (AstToken tok0, Arguments arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* PROCEED */
        arg [0] = arg0 ;            /* Arguments */
        
        InitChildren () ;
        return (ProceedDecl) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class BangLA extends CastLookaheadChoices {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 1 ;

    public boolean[] printorder () {
        
        return new boolean[] {true} ;
    }

    public BangLA setParms (AstToken tok0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "!" */
        
        InitChildren () ;
        return (BangLA) this ;
    }

}

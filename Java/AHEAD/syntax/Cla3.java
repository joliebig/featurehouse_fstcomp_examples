// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class Cla3 extends CastLookahead {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_QualifiedName getAST_QualifiedName () {
        
        return (AST_QualifiedName) arg [0] ;
    }

    public CastLookaheadChoices getCastLookaheadChoices () {
        
        return (CastLookaheadChoices) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true, false} ;
    }

    public Cla3 setParms
    (AstToken tok0, AST_QualifiedName arg0, AstToken tok1, CastLookaheadChoices arg1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "(" */
        arg [0] = arg0 ;            /* AST_QualifiedName */
        tok [1] = tok1 ;            /* ")" */
        arg [1] = arg1 ;            /* CastLookaheadChoices */
        
        InitChildren () ;
        return (Cla3) this ;
    }

}

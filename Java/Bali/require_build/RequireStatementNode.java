// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class RequireStatementNode extends RequireStatement {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public RequireRules getRequireRules () {
        
        return (RequireRules) arg [0] ;
    }

    public AstToken get_REQUIRE () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public RequireStatementNode setParms
    (AstToken tok0, RequireRules arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* _REQUIRE */
        arg [0] = arg0 ;            /* RequireRules */
        tok [1] = tok1 ;            /* ";" */
        
        InitChildren () ;
        return (RequireStatementNode) this ;
    }

}

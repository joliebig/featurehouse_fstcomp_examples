// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class ElseClauseC extends ElseClause {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public Statement getStatement () {
        
        return (Statement) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public ElseClauseC setParms (AstToken tok0, Statement arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "else" */
        arg [0] = arg0 ;            /* Statement */
        
        InitChildren () ;
        return (ElseClauseC) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class ForStmt extends ForStatement {

    final public static int ARG_LENGTH = 4 ;
    final public static int TOK_LENGTH = 5 ;

    public Expression getExpression () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (Expression) node : null ;
    }

    public ForInit getForInit () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (ForInit) node : null ;
    }

    public ForUpdate getForUpdate () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (ForUpdate) node : null ;
    }

    public Statement getStatement () {
        
        return (Statement) arg [3] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, true, false, true, false, true, false, true, false} ;
    }

    public ForStmt setParms
    (AstToken tok0, AstToken tok1, AstOptNode arg0, AstToken tok2, AstOptNode arg1, AstToken tok3, AstOptNode arg2, AstToken tok4, Statement arg3)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "for" */
        tok [1] = tok1 ;            /* "(" */
        arg [0] = arg0 ;            /* [ ForInit ] */
        tok [2] = tok2 ;            /* ";" */
        arg [1] = arg1 ;            /* [ Expression ] */
        tok [3] = tok3 ;            /* ";" */
        arg [2] = arg2 ;            /* [ ForUpdate ] */
        tok [4] = tok4 ;            /* ")" */
        arg [3] = arg3 ;            /* Statement */
        
        InitChildren () ;
        return (ForStmt) this ;
    }

}

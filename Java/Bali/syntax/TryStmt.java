// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class TryStmt extends TryStatement {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 1 ;

    public AST_Catches getAST_Catches () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (AST_Catches) node : null ;
    }

    public Block getBlock () {
        
        return (Block) arg [0] ;
    }

    public Finally getFinally () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (Finally) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, false} ;
    }

    public TryStmt setParms
    (AstToken tok0, Block arg0, AstOptNode arg1, AstOptNode arg2) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "try" */
        arg [0] = arg0 ;            /* Block */
        arg [1] = arg1 ;            /* [ AST_Catches ] */
        arg [2] = arg2 ;            /* [ Finally ] */
        
        InitChildren () ;
        return (TryStmt) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class AST_ParListC extends JakartaSST {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_ParList getAST_ParList () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (AST_ParList) node : null ;
    }

    public AstToken getPLST_BEGIN () {
        
        return (AstToken) tok [0] ;
    }

    public AstToken getPLST_END () {
        
        return (AstToken) tok [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, true} ;
    }

    public AST_ParListC setParms (AstToken tok0, AstOptNode arg0, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* PLST_BEGIN */
        arg [0] = arg0 ;            /* [ AST_ParList ] */
        tok [1] = tok1 ;            /* PLST_END */
        
        InitChildren () ;
        return (AST_ParListC) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class SmClassDecl extends SmClassBody {

    final public static int ARG_LENGTH = 5 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_FieldDecl getAST_FieldDecl () {
        
        AstNode node = arg[4].arg [0] ;
        return (node != null) ? (AST_FieldDecl) node : null ;
    }

    public ESList getESList () {
        
        AstNode node = arg[3].arg [0] ;
        return (node != null) ? (ESList) node : null ;
    }

    public OtherwiseClauses getOtherwiseClauses () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (OtherwiseClauses) node : null ;
    }

    public RootClause getRootClause () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (RootClause) node : null ;
    }

    public StatesList getStatesList () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (StatesList) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, false, false, false, true} ;
    }

    public SmClassDecl setParms
    (AstToken tok0, AstOptNode arg0, AstOptNode arg1, AstOptNode arg2, AstOptNode arg3, AstOptNode arg4, AstToken tok1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "{" */
        arg [0] = arg0 ;            /* [ RootClause ] */
        arg [1] = arg1 ;            /* [ OtherwiseClauses ] */
        arg [2] = arg2 ;            /* [ StatesList ] */
        arg [3] = arg3 ;            /* [ ESList ] */
        arg [4] = arg4 ;            /* [ AST_FieldDecl ] */
        tok [1] = tok1 ;            /* "}" */
        
        InitChildren () ;
        return (SmClassDecl) this ;
    }

}

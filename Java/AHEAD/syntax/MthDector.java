// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class MthDector extends MethodDeclarator {

    final public static int ARG_LENGTH = 3 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_ParList getAST_ParList () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (AST_ParList) node : null ;
    }

    public Dims getDims () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (Dims) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, true, false, true, false} ;
    }

    public MthDector setParms
    (QName arg0, AstToken tok0, AstOptNode arg1, AstToken tok1, AstOptNode arg2)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* QName */
        tok [0] = tok0 ;            /* "(" */
        arg [1] = arg1 ;            /* [ AST_ParList ] */
        tok [1] = tok1 ;            /* ")" */
        arg [2] = arg2 ;            /* [ Dims ] */
        
        InitChildren () ;
        return (MthDector) this ;
    }

}

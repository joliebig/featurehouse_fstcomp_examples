// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class TestDecl extends Es {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 2 ;

    public AST_Exp getAST_Exp () {
        
        return (AST_Exp) arg [1] ;
    }

    public AstToken getEDGETEST () {
        
        return (AstToken) tok [0] ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, true} ;
    }

    public TestDecl setParms
    (AstToken tok0, QName arg0, AST_Exp arg1, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* EDGETEST */
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* AST_Exp */
        tok [1] = tok1 ;            /* ";" */
        
        InitChildren () ;
        return (TestDecl) this ;
    }

}

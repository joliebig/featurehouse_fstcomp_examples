// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class EnterDecl extends Es {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 ;

    public Block getBlock () {
        
        return (Block) arg [1] ;
    }

    public AstToken getENTER () {
        
        return (AstToken) tok [0] ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false} ;
    }

    public EnterDecl setParms (AstToken tok0, QName arg0, Block arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* ENTER */
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* Block */
        
        InitChildren () ;
        return (EnterDecl) this ;
    }

}

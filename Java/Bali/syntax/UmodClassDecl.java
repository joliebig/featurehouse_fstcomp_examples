// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class UmodClassDecl extends UnmodifiedClassDeclaration {

    final public static int ARG_LENGTH = 4 ;
    final public static int TOK_LENGTH = 1 ;

    public ClassBody getClassBody () {
        
        return (ClassBody) arg [3] ;
    }

    public ExtendsClause getExtendsClause () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (ExtendsClause) node : null ;
    }

    public ImplementsClause getImplementsClause () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (ImplementsClause) node : null ;
    }

    public QName getQName () {
        
        return (QName) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, false, false} ;
    }

    public UmodClassDecl setParms
    (AstToken tok0, QName arg0, AstOptNode arg1, AstOptNode arg2, ClassBody arg3)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "class" */
        arg [0] = arg0 ;            /* QName */
        arg [1] = arg1 ;            /* [ ExtendsClause ] */
        arg [2] = arg2 ;            /* [ ImplementsClause ] */
        arg [3] = arg3 ;            /* ClassBody */
        
        InitChildren () ;
        return (UmodClassDecl) this ;
    }

}

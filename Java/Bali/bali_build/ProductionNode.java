// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class ProductionNode extends Production {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public Lookahead getLookahead () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (Lookahead) node : null ;
    }

    public Rewrite getRewrite () {
        
        return (Rewrite) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public ProductionNode setParms (AstOptNode arg0, Rewrite arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* [Lookahead] */
        arg [1] = arg1 ;            /* Rewrite */
        
        InitChildren () ;
        return (ProductionNode) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class ArrayInitializerElem extends AstListNode {

    public AST_VarInit getAST_VarInit () {
        
        return (AST_VarInit) arg [0] ;
    }

    public ArrayInitializerElem setParms (AST_VarInit arg0) {
        
        super.setParms (arg0) ;     /* AST_VarInit */
        return (ArrayInitializerElem) this ;
    }

    public ArrayInitializerElem setParms (AstToken tok0, AST_VarInit arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "," */
        return setParms (arg0) ;    /* AST_VarInit */
    }

}

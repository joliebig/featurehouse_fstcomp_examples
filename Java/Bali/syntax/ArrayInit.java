// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class ArrayInit extends AST_ArrayInit {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public ArrayInitializer getArrayInitializer () {
        
        return (ArrayInitializer) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false} ;
    }

    public ArrayInit setParms (ArrayInitializer arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* ArrayInitializer */
        
        InitChildren () ;
        return (ArrayInit) this ;
    }

}

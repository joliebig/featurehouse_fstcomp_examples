// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class JavacodeNode extends JavacodeProduction {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 ;

    public ScanBlock getScanBlock () {
        
        return (ScanBlock) arg [0] ;
    }

    public AstToken get_JAVACODE () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false} ;
    }

    public JavacodeNode setParms (AstToken tok0, ScanBlock arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* _JAVACODE */
        arg [0] = arg0 ;            /* ScanBlock */
        
        InitChildren () ;
        return (JavacodeNode) this ;
    }

}

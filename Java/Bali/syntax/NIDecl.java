// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class NIDecl extends InterfaceMemberDeclaration {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public NestedInterfaceDeclaration getNestedInterfaceDeclaration () {
        
        return (NestedInterfaceDeclaration) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false} ;
    }

    public NIDecl setParms (NestedInterfaceDeclaration arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* NestedInterfaceDeclaration */
        
        InitChildren () ;
        return (NIDecl) this ;
    }

}

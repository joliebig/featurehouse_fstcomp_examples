// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class ModTypeDecl extends TypeDeclaration {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public AST_Modifiers getAST_Modifiers () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (AST_Modifiers) node : null ;
    }

    public UnmodifiedTypeDeclaration getUnmodifiedTypeDeclaration () {
        
        return (UnmodifiedTypeDeclaration) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public ModTypeDecl setParms (AstOptNode arg0, UnmodifiedTypeDeclaration arg1)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* [ AST_Modifiers ] */
        arg [1] = arg1 ;            /* UnmodifiedTypeDeclaration */
        
        InitChildren () ;
        return (ModTypeDecl) this ;
    }

}

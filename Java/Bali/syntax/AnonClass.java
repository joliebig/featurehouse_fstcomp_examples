// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer syntax ;

public class AnonClass extends AllocExprChoices {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public Arguments getArguments () {
        
        return (Arguments) arg [0] ;
    }

    public ClassBody getClassBody () {
        
        AstNode node = arg[1].arg [0] ;
        return (node != null) ? (ClassBody) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false} ;
    }

    public AnonClass setParms (Arguments arg0, AstOptNode arg1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* Arguments */
        arg [1] = arg1 ;            /* [ ClassBody ] */
        
        InitChildren () ;
        return (AnonClass) this ;
    }

}

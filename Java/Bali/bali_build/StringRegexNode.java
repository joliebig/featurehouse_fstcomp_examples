// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class StringRegexNode extends Regex {

    final public static int ARG_LENGTH = 1 /* Kludge! */ ;
    final public static int TOK_LENGTH = 1 ;

    public AstToken getSTRING () {
        
        return (AstToken) tok [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true} ;
    }

    public StringRegexNode setParms (AstToken tok0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* STRING */
        
        InitChildren () ;
        return (StringRegexNode) this ;
    }

}

// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

layer require ;

public class RequireRulesElem extends AstListNode {

    public RequireRule getRequireRule () {
        
        return (RequireRule) arg [0] ;
    }

    public RequireRulesElem setParms (AstToken tok0, RequireRule arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "," */
        return setParms (arg0) ;    /* RequireRule */
    }

    public RequireRulesElem setParms (RequireRule arg0) {
        
        super.setParms (arg0) ;     /* RequireRule */
        return (RequireRulesElem) this ;
    }

}

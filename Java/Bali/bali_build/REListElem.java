// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class REListElem extends AstListNode {

    public RegexBlock getRegexBlock () {
        
        return (RegexBlock) arg [0] ;
    }

    public REListElem setParms (AstToken tok0, RegexBlock arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "|" */
        return setParms (arg0) ;    /* RegexBlock */
    }

    public REListElem setParms (RegexBlock arg0) {
        
        super.setParms (arg0) ;     /* RegexBlock */
        return (REListElem) this ;
    }

}

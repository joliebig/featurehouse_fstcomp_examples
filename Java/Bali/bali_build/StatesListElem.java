// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.



public class StatesListElem extends AstListNode {

    public StateName getStateName () {
        
        return (StateName) arg [0] ;
    }

    public StatesListElem setParms (AstToken tok0, StateName arg0) {
        
        tok = new AstToken [1] ;
        tok [0] = tok0 ;            /* "," */
        return setParms (arg0) ;    /* StateName */
    }

    public StatesListElem setParms (StateName arg0) {
        
        super.setParms (arg0) ;     /* StateName */
        return (StatesListElem) this ;
    }

}

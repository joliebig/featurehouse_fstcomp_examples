

import java.io.PrintWriter;

// Start of classes from CodeTemplate.KernelBase

//**************************************************
// class AstNode extension
//**************************************************
    
public abstract class AstNode {
     //**************************************************
     // markStack()
     //**************************************************
     /*
     public $TEqn.AstNode markStack() {
         stackMarker = aliasStack.size();
         return(($TEqn.AstNode) this);
     }

     public static $TEqn.AstNode markStack( int size, $TEqn.AstNode n ) {
          n.stackMarker = size;
          return n;
     }
    */

     //**************************************************
     // Execute doPatch() of each entry on the stack until
     // the stack size is reduce to that previously marked.
     //**************************************************
    public  AstNode patch() {
        Object obj;

        while ( aliasStack.size() > stackMarker ) {
            obj = null;
            try {
                obj = aliasStack.pop();
                if ( obj instanceof  NameId ) {
                    NameId nie = ( NameId ) obj;
                    nie.doPatch();
                }
                else
                    if ( obj instanceof  MthIscape ) {
                        MthIscape cie = ( MthIscape ) obj;
                        cie.doPatch();
                    }
                    else
                        if ( obj instanceof  ClsIscape ) {
                            ClsIscape ci2e = ( ClsIscape ) obj;
                            ci2e.doPatch();
                        }
                        else
                            if ( obj instanceof  PlstIscape ) {
                                PlstIscape pie = ( PlstIscape ) obj;
                                pie.doPatch();
                            }
                            else
                                AstNode.fatalError( "Invalid object on alias stack\n("+
                                                                                                                                                                obj.getClass().getName()+")" );
            }
            catch ( ClassCastException e ) {
                AstNode.fatalError( e.toString() );
            }
            catch ( Exception e ) {}
        }
        return ( ( AstNode ) this );
    }
}

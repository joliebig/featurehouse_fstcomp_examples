

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class ConSSuper {

    public void baseRewrite( Hashtable hb, Hashtable he, int stage ) {
        // this is a piggy-back onto baseRewrite.
             // when we encounter Super(-+-).(...) in jampack, we must perform
             // the following translation.  if there is a constructor already
             // present in hb with the signature (-+-), then we convert
             // Super(-+-).(...) into this(...).  Otherwise, we leave
             // the declaration alone

        // Step 0: do nothing if we are in the middle of an AST Constructor
                        
        if ( stage != 0 ) {
            super.baseRewrite( hb,he,stage );
            return;
        }

        // Step 1: get signature of Super().() being called
        //         Super(a,b,c).(c,d) - signature is "m(a,b,c",
                       //         where m is the name of the constructor

        String typeSig = "";
        if ( arg[0].arg[0] != null )
            typeSig = ( ( AST_TypeNameList ) arg[0].arg[0] ).signature();
        String sig = up.up.arg[1].tok[0].tokenName() + "(" + typeSig;

        // Step 2: see if this signature is in the base table (meaning
        // that we are inheriting this constructor via refinement)

        Object o = hb.get( sig );

        // Step 3: if o is null, do nothing.  if not, create ConThis
        // instance and replace this node with generated ConThis
                        
        if ( o!=null ) {
            ConThis c = new  ConThis().setParms( new  AstToken().setParms( getComment(),"this", 0 ),
                                           ( Arguments ) arg[1],
                                           new  AstToken().setParms( "",";", 0 ) );
            Replace( c );
        }
    }
}

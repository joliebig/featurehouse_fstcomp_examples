

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class MethodDcl {
    public void execute( int stage ) {
        if ( stage != 0 ) {
            super.executeBypass( stage );
            return;
        }
        ;

        // Step 1: form signature of method

        String methType = ( ( AST_TypeName ) arg[1] ).GetSignature();
        String methSig  = ( ( MethodDeclarator ) arg[2] ).GetSignature();
		  String dims     = ( ( MethodDeclarator ) arg[2] ).GetDims();
        String sig = methSig + " " + methType + dims;

        // Step 2: determine if method refines its parent method.
        //         This is done by setting PrimaryPrefix's signature,
        //         and traversing the body of the method.

        PrimaryPrefix.parentSig = methSig;
        String defines =  MMGlobals.Defines;
        try {
            arg[4].execute( stage );
        }
        catch ( ResultException e ) {
            defines =  MMGlobals.Refines;
        }

        // Step 3: create an MMOutput object with this signature
        //         for now, guess that it defines, not refines

        MMOutput o = new  MMOutput().init( sig, 
                                 MMGlobals.Method,
                                defines );
        o.setlines( this.getFirstLineNum(), this.getLastLineNum() );
		  o.setModifiers( (AstOptNode) arg[0] );

		  // Step 3: now harvest the throws clause

		  if (arg[3].arg[0] != null) {
		     NamedVector nv = new NamedVector( MMGlobals.Throws );
			  arg[3].arg[0].harvestAST_QualifiedNames( nv );
			  o.union(nv);

		  }

        // Step 5: now add o to the main object.  If the method
		  //         was already defined (as can happen in a mixin-produced
		  //         file), add the throws clauses

        MMOutput m =  Main.mmresult;
		  if (m.nested.containsKey(sig)) {
		     ((MMOutput) m.nested.get(sig)).merge(o);
		  }
		  else {
           m.nested.putUnique( sig, o );
		  }
    }
}

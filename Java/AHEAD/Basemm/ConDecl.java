

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class ConDecl {
    public void execute( int stage ) {
        if ( stage != 0 ) {
            super.executeBypass( stage );
            return;
        }
        ;

        // Step 1: form signature of constructor

        String name = Util2.unmangleId( ((QName) arg[1]).GetName() );
        String sig = name + "(";
        if ( arg[2].arg[0]!=null )
            sig = sig + ( ( AST_ParList ) arg[2].arg[0] ).GetSignature();
        sig = sig + ")";

        // Step 2: create an MMOutput object with this signature.
        //         constructors don't refine, only define.

        MMOutput o = new  MMOutput().init( sig, 
                                 MMGlobals.Constructor, 
                                 MMGlobals.Defines );
        o.setlines( this.getFirstLineNum(), this.getLastLineNum() );
		  o.setModifiers( (AstOptNode) arg[0] );

		  // Step 3: now harvest the throws clause

		  if (arg[3].arg[0] != null) {
		     NamedVector nv = new NamedVector( MMGlobals.Throws );
			  arg[3].arg[0].harvestAST_QualifiedNames( nv );
			  o.union(nv);
		  }

        // Step 4: now add o to the main object.  If the method
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

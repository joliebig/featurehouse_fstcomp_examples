

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

// the following classes deal with Field signature harvesting

public class FldVarDec     {

    // harvest [mods] AST_TypeName AST_VarDecl;

    public void execute( int stage ) {
        if ( stage != 0 ) {
            super.executeBypass( stage );
            return;
        }
        ;

        // Step 1: get the name of the type

        String typename = ( ( AST_TypeName ) arg[1] ).GetSignature();

		  // Step 2: harvest modifiers in temporary MMOutput object

		  MMOutput modifiers = new MMOutput();
		  modifiers.setModifiers( (AstOptNode) arg[0] );

        // Step 3: harvest identifiers on declarator list, and for
        //         each identifier, create an MMOutput object and
        //         add it to the main object

        MMOutput m =  Main.mmresult;
        AstCursor c = new  AstCursor();
        for ( c.FirstElement( arg[2] ); c.MoreElement(); c.NextElement() ) {
		      VariableDeclarator vd = ((VariableDeclarator) c.node);
            String varname = vd.GetSignature();
				String dims    = vd.GetDims();
            String sig = varname + " " + typename + dims;

            MMOutput o = new  MMOutput().init( sig, 
                                        MMGlobals.Field,
                                                       MMGlobals.Defines );
            o.setlines( this.getFirstLineNum(), this.getLastLineNum() );
				o.copyModifiers( modifiers );

				// now remember final values

				if ((modifiers.modifiers & MMGlobals.ModFinal) != 0) {
				   String initedValue = vd.getValue();
					if (initedValue != null) {
                  NamedVector nv = new NamedVector( "finalValue" );
					   nv.add( initedValue );
						o.union( nv );
					}
				}
            m.nested.putUnique( sig, o );
        }
    }
}

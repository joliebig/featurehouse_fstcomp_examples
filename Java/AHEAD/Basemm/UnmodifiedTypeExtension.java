

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class UnmodifiedTypeExtension {

    // does the common work of harvesting output of tool for
    // UnmodifiedTypeExtensions

    public void harvest( String name, String type, String info ) {
        MMOutput m =  Main.mmresult;
        m.setName( name );
        m.setType( type );
        m.setDefn( MMGlobals.Refines );
        m.setlines( -1, -1 ); // entire file
		  // go up 2 levels (parent is Ute, grandparent is ModTypeDecl
		  m.setModifiers( (AstOptNode) up.up.arg[0] );

        NamedVector nv = new  NamedVector( info );
        if ( arg[1].arg[0] != null )
            arg[1].arg[0].harvestAST_QualifiedNames( nv );
        if( !nv.isEmpty() )
            m.union( nv );
    }
}

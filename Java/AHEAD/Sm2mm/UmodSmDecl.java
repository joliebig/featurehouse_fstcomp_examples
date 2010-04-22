

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodSmDecl {
    public void execute( int stage ) {
        if ( stage!=0 ) {
            super.execute( stage );
            return;
        }

        NamedVector nv;

        MMOutput m =  Main.mmresult;
        m.setType( MMGlobals.StateMachine );
        m.setName( ( ( QName ) arg[0] ).GetName() );
        m.setlines( -1, -1 ); // entire file
		  m.setModifiers( (AstOptNode) up.arg[0] );

        // do the SmExtendsClause first

        if ( arg[1].arg[0] == null )
            m.setDefn( MMGlobals.Defines );
        else {
            m.setDefn( MMGlobals.Extends );
            String name = ( arg[1].arg[0] instanceof  SmExtends ) ?
                           MMGlobals.Smachines :
                           MMGlobals.Classes;
            nv = new  NamedVector( name );
            arg[1].arg[0].harvestAST_QualifiedNames( nv );
            m.union( nv );

            // do the same for harvesting the super"class" name

            nv = new  NamedVector( "super" );
            arg[1].arg[0].harvestAST_QualifiedNames( nv );
            m.union( nv );

        }

        // now do the implements claus         

        if ( arg[2].arg[0] != null ) {
            nv = new  NamedVector( MMGlobals.Interfaces );
            arg[2].arg[0].harvestAST_QualifiedNames( nv );
            m.union( nv );
        }

        // now harvest the contents of the state machine

        arg[3].execute( stage );
    }
}

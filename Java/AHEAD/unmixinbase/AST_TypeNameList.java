

import java.util.*;
import Jakarta.util.*;
import java.io.*;

//---------- code for processing an AST_TypeNameList

public class AST_TypeNameList {

    // removes name from AST_TypeNameList if it is present.
    // returns modified list as result -- or null if the resulting
    // list is empty.

    public  AST_TypeNameList removeName( String name ) {
        int counter = 0;
        AST_QualifiedName a = null;

        AstCursor c = new  AstCursor();
        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) {

            // if there is a cast error, it's because there should never
            // be anything other than TNClass pointing to an AST_QualifiedName

            try {
                TNClass t = ( TNClass ) c.node;
                QNameType q = ( QNameType ) t.arg[0];
                a = ( AST_QualifiedName ) q.arg[0];
            }
            catch ( Exception e ) {
                AstNode.fatalError( "something other than AST_QualifiedName " + e.getMessage() );
            }
            if ( a.GetName().equals( name ) )
                c.Delete();
            else
                counter++;
        }

        return ( counter != 0 ) ? ( AST_TypeNameList ) this : null ;
    }
}

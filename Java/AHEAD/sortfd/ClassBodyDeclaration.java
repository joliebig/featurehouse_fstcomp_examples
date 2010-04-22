

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class ClassBodyDeclaration
          implements Comparator {

    public String sortkey;

    public void setSortKey() {
        AstNode.override( "ClassBodyDeclaration.setSortKey", this );
    }

    public void setSortKey( String basis ) {
        // Step 1: optional modifiers are always the first argument of 
        //         a ClassBodyDeclaration

        AST_Modifiers m = ( AST_Modifiers ) arg[0].arg[0];

        // Step 2: determine if "static" is present, if so,
        //         adjust base

        if ( m != null && m.findModifier( "static" ) )
            sortkey = "00" + basis;
        else
            sortkey = basis;
    }

    public int compare( Object o1, Object o2 ) {
        ClassBodyDeclaration b1 = ( ClassBodyDeclaration ) o1;
        ClassBodyDeclaration b2 = ( ClassBodyDeclaration ) o2;
        return b1.sortkey.compareTo( b2.sortkey );
    }

    public boolean equal( Object o ) {
        ClassBodyDeclaration b = ( ClassBodyDeclaration ) o;
        return ( sortkey.equals( b.sortkey ) );
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class MethodDeclarator {

    // this is a cheap (and not very accurate way) to get the names
    // of methods.  It can have comments in front.  For now, this
    // will do.  The reason for this hack is that in JTS metaprogramming,
    // we don't exactly preserve the syntax of parse trees.  When we
    // generate an identifier as a name of a method, we return an
    // AST_QualifiedName, instead of a QName.  This makes it difficult
    // to know what exactly is the class of a child subtree.  (yes, we
    // can determine it at run-time, but we should know it at compile
    // time.  For now, this will do.

    public String GetName() {
        String nameWithComments = arg[0].toString();
        return trim( nameWithComments );
    }

    // returns trailing string of non-blank characters

    private String trim( String x ) {
        int i = x.lastIndexOf( ' ' );
        if ( i == -1 )
            return x;
        else
            return x.substring( i+1 );
    }
}

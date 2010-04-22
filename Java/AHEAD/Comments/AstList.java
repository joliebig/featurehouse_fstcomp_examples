

import java.util.*;
import java.io.*;

public abstract class AstList {

    // find the first element on the list that has a token,
    // and return that token

    public  AstToken findToken() {
        AstToken t;

        for ( AstNode l=arg[0]; l != null; l = l.right )
            if ( l.arg[0] != null ) {
                t = l.arg[0].findToken();
                if ( t != null )
                    return t;
            }
        return null;
    }
}

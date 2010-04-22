

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public abstract class AstNode {
    public int getFirstLineNum() {
        boolean order[];
        int     t, n, i, line;

        order = printorder();
        t = 0;
        n = 0;
         
        for ( i=0; i<order.length; i++ ) {
            // if order[i] is true; visit token else visit nonterminal

            Object o = order[i] ? ( Object ) tok[t] : ( Object ) arg[n];
            if ( o == null )
                continue;

            line = order[i] ? tok[t++].lineNum() : arg[n++].getFirstLineNum();
            if ( line != -1 )
                return line;
        }
        return -1;
    }

    public int getLastLineNum() {
        boolean order[];
        int     t, n, i, line;

        order = printorder();
        t = ( tok != null ) ? tok.length-1 : -1;
        n = ( arg != null ) ? arg.length-1 : -1;
         
        for ( i=order.length-1; i>=0; i-- ) {
            // if order[i] is true; visit token else visit nonterminal

            Object o = order[i] ? ( Object ) tok[t] : ( Object ) arg[n];
            if ( o == null )
                continue;

            line = order[i] ? tok[t--].lineNum() : arg[n--].getLastLineNum();
            if ( line != -1 )
                return line;
        }
        return -1;
    }
}

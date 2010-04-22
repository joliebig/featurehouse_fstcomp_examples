

import java.util.*;
import java.io.*;

//----------------- Comments Layer -----------

public abstract class AstNode {

    // findToken returns the left-most token of a parse tree.
    // this is the token on which comments for that parse tree
    // can be extracted and attached.  If findToken returns null,
    // then the parse tree has no such token, and thus no comment
    // is associated with (or can be assigned to) such a tree.

    public  AstToken findToken() {
        AstToken result;

        int t = 0; // terminal index;
        int n = 0; // non-terminal index;
        boolean order[] = printorder();

        for ( int i=0; i < order.length; i++ ) {

            // if order[i] is true, a terminal is next; 
            // else a non-terminal

            if ( order[i] ) {
                // Terminal can be a token or an optional token 
                // only two possibilities

                if ( tok[t] instanceof  AstToken )
                    return ( AstToken ) tok[t];
                else {
                    result = ( ( AstOptToken ) tok[t++] ).findToken();
                    if ( result != null )
                        return result;
                }
            }
            else {
                // non-terminal
                result = ( ( AstNode ) arg[n++] ).findToken();
                if ( result != null )
                    return result;
            }
        }

        // couldn't find a token in this tree, return null
        return null;
    }

    // sets the white_space of tree to c

    public void setComment( String c ) {
        AstToken t = findToken();
        if ( t == null )
            ferror( "setComment" );
        t.white_space = c;
    }

    // gets the white_space of tree

    public String getComment() {
        AstToken t = findToken();
        if ( t == null )
            ferror( "getComment" );
        return t.white_space;
    }

    // appends c to the white_space of tree

    public void appendComment( String c ) {
        AstToken t = findToken();
        if ( t == null )
            ferror( "appendComment" );
        t.white_space = t.white_space + c;
    }

    // prefaces c to the white_space of tree

    public void prependComment( String c ) {
        AstToken t = findToken();
        if ( t == null )
            ferror( "prefaceComment" );
        t.white_space = c + t.white_space;
    }

    // adds new line if one wasn't present

    public void addNewLine() {
        AstToken t = findToken();
        if ( t == null )
            ferror( "addNewLine" );
        if ( t.white_space.charAt( 0 ) != '\n' )
            t.white_space = '\n'+ t.white_space;
    }

    private void ferror( String etype ) {
        AstNode.fatalError( etype + " performed on tree that has no tokens:"
                            + toString() );
    }

}

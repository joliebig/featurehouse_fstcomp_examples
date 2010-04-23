

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An <code>AstVisitor</code> is a {@link Dispatcher} that contains methods to
 * visit nodes in an abstract syntax tree.  Specializations should implement
 * <code>visit</code> methods for more specific types of AST nodes.
 *
 * @layer<visitor>
 */
    
public abstract class AstVisitor extends Dispatcher {

    /**
     * A generic method that dispatches <code>this</code> to the list
     * elements, thus implementing a pre-order visitation of the tree.
     *
     * @layer<visitor>
     */
    public void visit( AstList list ) {
        for ( AstNode n = list.arg [0] ; n != null ; n = n.right )
            if ( n.arg [0] != null )
                dispatch( n.arg [0] ) ;
    }

    /**
     * A generic method that dispatches <code>this</code> to its children,
     * thus implementing a pre-order visitation of the tree.
     *
     * @layer<visitor>
     */
    public void visit( AstNode node ) {
        if ( node.arg != null )
            for ( int n = 0 ; n < node.arg.length ; ++n )
                if ( node.arg [n] != null )
                    dispatch( node.arg [n] ) ;
    }

    /**
     * A generic method that dispatches <code>this</code> on its optional
     * member, thus implementing a pre-order visitation of the tree.
     *
     * @layer<visitor>
     */
    public void visit( AstOptNode node ) {
        if ( node.arg [0] != null )
            dispatch( node.arg [0] ) ;
    }
}

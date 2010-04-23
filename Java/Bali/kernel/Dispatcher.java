

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements a <em>Dispatcher</em> design pattern by using reflection to
 * determine the most specific runtime method signature appropriate for
 * visiting an object.  This avoids the use of double-dispatch which is
 * fairly intrusive.
 *
 * @layer<visitor>
 */
    
public abstract class Dispatcher {

    /**
     * Using reflection, this method calls a specific <code>visit</code>
     * method based on dynamic argument type.
     *
     * @throws IllegalStateException
     * if trying to dispatch to <code>visit</code> causes an exception
     * (the original exception is saved as the "cause").
     *
     * @layer<visitor>
     */
    public void dispatch( Object object ) {
        try {
            Class klass = ( object != null ) ? object.getClass() : null ;
            Method method = getVisitMethod( klass ) ;
            method.invoke( this, new Object[] {object} ) ;
        }
        catch ( Exception exception ) {
            error( object, exception ) ;
        }
    }

    /**
     * This <code>visit</code> method is for the case where an exception
     * occurs while trying to visit an object.  This can occur in the
     * <code>visit</code> method during reflection and a derived method
     * may also call this method for unexpected exceptions.
     * 
     * @throws IllegalStateException for all arguments.
     *
     * @layer<visitor>
     */
    public void error( Object object, Throwable thrown ) {
        String msg = "thrown when visiting " + object.getClass() ;
        IllegalStateException exception = new IllegalStateException( msg ) ;
        exception.initCause( thrown ) ;
        throw exception ;
    }

    /**
     * Implements a search for a {@link Method} named <code>visit</code>
     * with an argument matching one of the superclasses of the argument.
     * Has a cache for to reduce extra searches.
     *
     * @param baseClass
     * a non-null reference to the {@link Class} of an object to visit.
     *
     * @layer<visitor>
     */
    private Method getVisitMethod( Class baseClass ) {

        // Check the cache first just in case the search has been done:
        //
        Method method = ( Method ) methodMap.get( baseClass ) ;
        if ( methodMap.containsKey( baseClass ) )
            return method ;

        // First, loop through the superclasses:
        //
        Class superClass = baseClass ;
        while ( method == null && superClass != null )
            try {
                classArray [0] = superClass ;
                method = getClass().getMethod( "visit", classArray ) ;
            }
            catch ( NoSuchMethodException exception ) {
                superClass = superClass.getSuperclass() ;
            }

        // Otherwise, try the interfaces:
        //
        if ( method == null ) {
            Class[] interfaces = baseClass.getInterfaces() ;
            for ( int n = 0 ; n < interfaces.length ; ++n )
                try {
                    classArray [0] = interfaces [n] ;
                    method = getClass().getMethod( "visit", classArray ) ;
                    break ;
                }
                catch ( NoSuchMethodException exception ) {
                /* Body deliberately left empty (loop handles this). */
                }
        }
                
        // Save the result in the cache, then return it:
        // (may be null!)
        //
        methodMap.put( baseClass, method ) ;

        return method ;
    }

    final private Class[] classArray = new Class [1] ;
    final private Map methodMap = new HashMap() ;
}

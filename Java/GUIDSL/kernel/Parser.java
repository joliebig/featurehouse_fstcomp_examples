

import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.InputStream ;
import java.io.InputStreamReader ;
import java.io.Reader ;
import java.io.UnsupportedEncodingException ;

import java.lang.reflect.Method ;

import java.util.HashMap ;
import java.util.Map ;

//-----------------------------------------------------------------------//
// Parser facade below:
//-----------------------------------------------------------------------//

/**
 * Facade for underlying {@link BaliParser} that provides factory methods
 * named <code>getInstance</code> for obtaining a <em>singleton</em>
 * instance of the parser.  The factory methods should be used instead of
 * constructors or the {@link BaliParser#ReInit()} methods.
 *
 * @layer<kernel>
 */
final public class Parser {

    //-----------------------------------------------------------------------//
    // Static factory methods for instantiation:
    //-----------------------------------------------------------------------//

    /**
     * Return singleton <code>Parser</code> in current state.
     *
     * @layer<kernel>
     */
    public static Parser getInstance() {
        return parser ;
    }

    /**
     * Re-initialize singleton <code>Parser</code> to start parsing from
     * a given {@link File}, returning the updated <code>Parser</code>.
     *
     * @layer<kernel>
     */
    public static Parser getInstance( File inputFile )
    throws FileNotFoundException {
        return getInstance( new FileInputStream( inputFile ) ) ;
    }

    /**
     * Re-initialize singleton <code>Parser</code> to start parsing from
     * an {@link InputStream}, returning the updated <code>Parser</code>.
     *
     * @layer<kernel>
     */
    public static Parser getInstance( InputStream stream ) {
        try {
            return getInstance( new InputStreamReader( stream,"ISO-8859-1" ) );
        }
        catch ( UnsupportedEncodingException except ) {
            return getInstance( new InputStreamReader( stream ) ) ;
        }
    }

    /**
     * Re-initialize singleton <code>Parser</code> to start parsing from
     * a {@link Reader} object, returning the updated <code>Parser</code>.
     *
     * @layer<kernel>
     */
    public static Parser getInstance( Reader reader ) {

        if ( parser == null )
            parser = new Parser( reader ) ;
        else
            parser.reset (reader) ;

        return parser ;
    }

    //-----------------------------------------------------------------------//
    // Parser instance methods:
    //-----------------------------------------------------------------------//

    /**
     * Returns the result of the last parse, <code>null</code> if none.
     **/
    public AstNode getParse () {
	return lastParse ;
    }

    /**
     * Parses the input against the specified non-terminal rule.  This method
     * doesn't require that a successful parse must be terminated by an
     * end-of-file.  Instead, any non-matching input is allowed.
     *
     * @return an {@link AstNode} with the result of the parse.
     * @throws ParseException if the parse fails.
     **/
    public AstNode parse (String rule) throws ParseException {

	try {
	    Method method = (Method) ruleMap.get (rule) ;
	    if (null == method) {
		Class klass = baliParser.getClass() ;
		method = klass.getMethod (rule, class0) ;
		if (! AstNode.class.isAssignableFrom (method.getReturnType()))
		    throw new IllegalArgumentException (rule + " not found") ;
		ruleMap.put (rule, method) ;
	    }
	    return (AstNode) method.invoke (baliParser, object0) ;

	} catch (RuntimeException re) {
	    throw re ;
	} catch (Exception except) {
	    ParseException pe = baliParser.generateParseException() ;
	    pe.initCause (except) ;
	    throw pe ;
	}
    }

    /**
     * Parses the input against the start rule, then parses an end-of-file.
     *
     * @return an {@link AstNode} with the result of the parse.
     *
     * @throws ParseException
     * if input doesn't match the start rule or if it's not terminated by an
     * end-of-file.
     **/
    public AstNode parseAll () throws ParseException {
	lastParse = BaliParser.getStartRoot (baliParser) ;
	return lastParse ;
    }

    /**
     * Parses an end-of-file from the input.
     *
     * @throws ParseException if input is not at end of file.
     **/
    public void parseEOF () throws ParseException {
	baliParser.requireEOF() ;
    }

    /**
     * Resets the parser's input to another {@link Reader}.
     **/
    private void reset (Reader reader) {
	baliParser.ReInit (reader) ;
    }

    //-----------------------------------------------------------------------//

    /**
     * Private constructor to create singleton of <code>Parser</code>.
     *
     * @layer<kernel>
     */
    private Parser( Reader reader ) {
	if (null == reader)
	    throw new NullPointerException ("\"reader\" is null") ;
	this.baliParser = new BaliParser (reader) ;
	this.lastParse = null ;
	this.ruleMap = new HashMap() ;
    }

    private BaliParser baliParser ;
    private AstNode lastParse ;
    private Map ruleMap ;

    final private static Class[] class0 = new Class [0] ;
    final private static Object[] object0 = new Object [0] ;
    private static Parser parser = null ;

}

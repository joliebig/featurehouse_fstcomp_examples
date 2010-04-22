

public class RefCons  {

    // refines qname( parlist ) { body }  is rewritten to
	 // qname( parlist ) { super( arglist ); body }

    public void reduce2java( AstProperties props ) {
        String className = arg[0].tok[0].tokenName();
        String params = "";
		  AST_ParList parlist = (AST_ParList) arg[1].arg[0];
        if ( parlist != null )
            params = parlist.onlyParams();

        props.print( getComment() + arg[0].getComment() + className + "(" );
        arg[1].print( props ); // arguments
        props.print( ") " );
        props.print( "{ super(" + params + "); " );
        arg[2].reduce2java( props ); // refinement code
        props.println( " }" );
    }
}



class stateInfo {

    // override original definition by extending the method so
    // that if the edge isn't found, it is declared.

    static  stateInfo verifyStateName( String sname, String which,
              AstTokenInterface t ) {
        stateInfo s;

        SmIterator i =  kernelConstants.globals().sm4vars.Sm.StateCont.iterator();
        for ( s = ( stateInfo ) i.firstObj(); 
               s != null; 
               s = ( stateInfo ) i.nextObj() ) {
            if ( s.name.equals( sname ) )
                return s;
        }
   
	     // if we can't find it, we'll declare a dummy, unnested, state
		  // in the state container

		  s = new stateInfo( sname, false );
		  s.inherited = true;
        kernelConstants.globals().sm4vars.Sm.StateCont.add( s );

		  return s;
    }

    // since we don't serialize anything, we don't need to truncate
	 public void truncate() { }
}

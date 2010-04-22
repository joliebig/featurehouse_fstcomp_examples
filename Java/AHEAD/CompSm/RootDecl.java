

class RootDecl {

    public void compose( AstNode etree ) {
	    // Step 1: do some error checking

		 RootDecl rd = (RootDecl) etree;

		 // Step 2: composition of DelivClauses is simple replacement

		 arg[0].Replace( rd.arg[0] );

		 // Step 3: there is no composition of NoTransitionClauses

       AstNode a = rd.arg[1].arg[0];
		 if (a != null) 
          AstNode.error( a.tok[0], "State machine refinement cannot "
			                + "have a NoTransition Clause");
    }
}

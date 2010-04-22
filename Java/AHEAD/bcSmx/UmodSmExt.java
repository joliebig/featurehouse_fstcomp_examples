

public class UmodSmExt {

    // refines statemachine qname implements smbody reduces to
	 // statemachine qname extends stub.qname implements smbody 

    public void reduce2java( AstProperties props ) {

       // Step 1: before we reduce a UmodSmExt, we have to make sure
		 //         that there is a DelivClause present -- otherwise
		 //         we can't proceed with the reduction.
		 //         arg[2] -- SmClassBody
		 //         arg[0].arg[0] -- RootClause

		 if (arg[2].arg[0].arg[0] == null) 
		    AstNode.fatalError( "Refinement of State Machine missing "
			    + "Delivery_parameters declaration" );

		 // Step 2: get the name of the state machine

		 String name = arg[0].tok[0].tokenName();
		 String decl = "State_machine " + name + 
		               " extends " + kernelConstants.stub + "." + name +
							" {} ";

		 // Step 3: now manufacture a parse tree for the above
		 //         declaration, remembering that the root of the
		 //         tree is really a list.  the first element on
		 //         the list is of type UmodSmDecl
       //         c.arg[0].arg[0] is of type ModTypeDecl
		 //         " ".arg[1] is of type UmodSmDecl
		 
		 AST_Class c = AST_Class.MakeAST( decl );
		 UmodSmDecl umsd = (UmodSmDecl) c.arg[0].arg[0].arg[1];

       // Step 4: finish constructing the tree and reduce it
		 
		 umsd.arg[2] = arg[1];  // implements clause
		 umsd.arg[3] = arg[2];  // SmClassBody
		 umsd.reduce2java( props );
   }
}

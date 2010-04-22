

public class BasePre {
    public void reduce2java( AstProperties props ) {

        // Step 0: make sure that there is indeed a super class

        String superName = ( String ) props.getProperty( "SuperName" );
        if ( superName == null || superName.equals( "" ) ) {
            AstNode.warning( tok[0], 
                   "Use of Super(...).meth(...) in class with no super class" );
        }
        String cmt = getComment();

        // Step 1: one of two possible reductions are performed.
        //         if BasePre belongs to a static method, then
        //         the reduction of Super(...).meth(...) is 
        //         superclass.meth(...).  Else it is
        //         super.meth(...).

        if ( props.containsProperty( "isStatic" ) ) {
            props.print( cmt + superName + "." );
            arg[1].reduce2java( props );
            return;
        }

        // Step 2: else reduce "Super(...).meth(...)" to "super.meth()"

        props.print( cmt + "super." );
        arg[1].reduce2java( props );
    }
}

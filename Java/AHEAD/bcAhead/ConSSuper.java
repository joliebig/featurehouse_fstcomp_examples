

public class ConSSuper {

    // reduction of Super( type-list).( arg-list ) is simple:
    // replace an instance of the above with:
    //        super( arg-list )

    public void reduce2java( AstProperties props ) {
        props.print( getComment() + "super" );
        arg[1].reduce2java( props );
        props.print( ";" );
    }
}

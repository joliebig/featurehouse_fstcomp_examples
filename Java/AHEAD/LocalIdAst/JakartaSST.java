

// the following classes are examples of rewrites that
// note boundaries between quoted text and executable text.
// These rewrites are necessary only if both the LocalId layer
// and the ast layer are present; they are absent otherwise.

public class JakartaSST {
    public void mangleLocalIds( int stage ) {
        super.mangleLocalIds( stage+1 );
    }
}

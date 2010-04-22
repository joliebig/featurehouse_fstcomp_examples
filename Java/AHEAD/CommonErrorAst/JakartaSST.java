

// this layer is needed only if CommonError and ast are present.
// it encapsulates rewrites that mark the boundary between
// quoted text and unquoted text.  CommonErrors apply only
// to unquoted text.

public class JakartaSST {
    public void checkForErrors( int stage, String file ) {
        super.checkForErrors( stage+1, file );
    }
}



import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class AST_FieldDecl {
    public void mangleConstructors() {
        AstCursor c = new  AstCursor();
        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() )
            if ( c.node instanceof  ConstructorDeclaration )
                ( ( ConstructorDeclaration ) c.node ).mangleConstructor();
    }
}

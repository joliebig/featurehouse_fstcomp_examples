

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class AstOptNode {
    public void execute( int stage ) {
        if ( arg[0]!=null )
            arg[0].execute( stage );
    }
}

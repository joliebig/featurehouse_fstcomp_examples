

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class Initializer {
    public void execute( int stage ) {
        if ( stage != 0 ) {
            super.executeBypass( stage );
            return;
        }
        ;

    // do nothing when harvesting initializers
    }
}

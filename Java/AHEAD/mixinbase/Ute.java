

import java.util.*;
import Jakarta.util.*;
import java.io.*;

public class Ute {

    // replace extension syntax with corresponding java syntax

    public  UnmodifiedTypeDeclaration prepareReplace( JTSParseTree t ) {
        UnmodifiedTypeExtension ute = 
                    ( UnmodifiedTypeExtension ) arg[0];
        return ute.prepareReplace( t );
    }

    public boolean isExtension() {
        return true;
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class Ute {
    public String GetName() {
        return ( ( UnmodifiedTypeExtension ) arg[0] ).GetName();
    }

    public boolean isExtension() {
        return true;
    }
    public String GetType() {
        return ( ( UnmodifiedTypeExtension ) arg[0] ).GetType();
    }
      
    public void compose( AstNode etree ) {

        kernelConstants.globals().isBaseAnInterface = false;
        arg[0].compose( etree.arg[0] );
    }
}

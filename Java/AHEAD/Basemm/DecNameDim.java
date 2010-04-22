

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class DecNameDim {
    public String GetSignature() {

        // Step 1: get QName -- don't tack on dimensions, as this
		  //                      isn't the signature

        String result = ( ( QName ) arg[0] ).GetName();
        return result;
    }

    public String GetDims() {
        if ( arg[1].arg[0] != null )
            return ( ( Dims ) arg[1].arg[0] ).GetSignature();
        else
            return "";
    }
}

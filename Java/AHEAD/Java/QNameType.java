

import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

public class QNameType {
    public String GetName() {
        return ( ( AST_QualifiedName ) arg[0] ).GetName();
    }

	 public String Signature() {
	    // extract dimension signature before name
	    String dsig = "";
       Dims d = (Dims) arg[1].arg[0];
	    if (d != null)
		    dsig = d.Signature();

	    return GetName() + dsig; 
	 }
}

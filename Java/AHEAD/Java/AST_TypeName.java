

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

public class AST_TypeName {
    public String GetName() {
        AstNode.override( "GetName", this );
        return null; // pacify whiney compiler
    }

    public String Signature() {
        AstNode.override( "Signature", this );
        return null; // pacify whiney compiler
    }
}

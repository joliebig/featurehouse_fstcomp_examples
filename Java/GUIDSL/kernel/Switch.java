

import Jakarta.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Vector;

public class Switch extends CommandLineArg implements Cloneable {
    String description;
    String[] args; // names or instance bindings

    public Switch( String _id, String _description, String[] argNames,
              boolean _optional, int _layer ) {
        name = _id;
        description = _description;
        args = argNames;
        optional = _optional;
        
    }

    public Object clone() throws CloneNotSupportedException {
        return ( super.clone() );
    }
}

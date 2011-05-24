

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

//**************************************************
// interface AstTokenInterface:
//**************************************************
    
public interface AstTokenInterface {
    public void print( AstProperties props );
    public void print();
    public void reduce2java( AstProperties props );
    public Object clone();
    public String tokenName();
    public String getTokenName();
    public void setTokenName( String replacement );
    public boolean Equ( AstTokenInterface x );
    public void printWhitespaceOnly( AstProperties props );
}

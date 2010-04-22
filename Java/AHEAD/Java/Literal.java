

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

//**************************************************
// Literal extension class
//**************************************************
    
public class Literal {
    public static Literal Make( String id ) {
        AstToken t = new  AstToken().setParms( " ", id, 0 );
        return ( new  StrLit().setParms( t ) );
    }

    public static Literal Make( int id ) {
        AstToken t = new  AstToken().setParms( " ",
                                                 new Integer( id ).toString(),
                                                           0 );
        return new  IntLit().setParms( t );
    }

    public static Literal makeLiteral( String str ) {
        AstToken t = new  AstToken().setParms( "", str, 0 );
        return ( new  StrLit().setParms( t ) );
    }
}

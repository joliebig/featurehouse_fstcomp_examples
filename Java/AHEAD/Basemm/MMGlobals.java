

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class MMGlobals {
    static public final String Class        = "C";
    static public final String Interface    = "I";

    static public final String Constructor  = "K";
    static public final String Method       = "M";
    static public final String Field        = "F";

    static public final String Defines      = "D";
    static public final String Extends      = "E";
    static public final String Refines      = "R";

    static public final int    ModAbstract  = 01;
    static public final int    ModFinal     = 02;
    static public final int    ModPublic    = 04;
    static public final int    ModProtected = 010;
    static public final int    ModPrivate   = 020;
    static public final int    ModStatic    = 040;
    static public final int    ModTransient = 0100;
    static public final int    ModVolatile  = 0200;
    static public final int    ModNative    = 0400;
    static public final int    ModSynchronized = 01000;
    
    static public final String Interfaces   = "Interfaces";
    static public final String Classes      = "Classes";
	 static public final String Throws       = "Throws";

    final public static String INDENT = "    " ;

    public static void main( String[] args ) {} // for external class generation
}

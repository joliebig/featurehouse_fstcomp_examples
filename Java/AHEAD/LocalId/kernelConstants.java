

import java.util.Hashtable;
import Jakarta.util.Util2;
import java.io.*;

public class kernelConstants {
    // hash table contains (id, mangledId) pairs.
    // mangledId = id + $$ + _source (i.e., layername)
    // hash table initialized in AST_Class

    static public Hashtable localId_ht;
}

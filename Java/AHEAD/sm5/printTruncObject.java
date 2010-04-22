

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// all state, transition objects are printTruncObjects, where a 
// printTruncObject is an object that can be printed and truncated

interface printTruncObject {
    void print();
    void truncate();
}

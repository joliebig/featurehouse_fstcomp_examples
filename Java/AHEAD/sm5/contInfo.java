

import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// all harvested info on transitions, states are instances of contInfo

abstract class contInfo 
                   implements Serializable, printTruncObject {
    public boolean        inherited; // is object inherited
    // from previous run?
    abstract public void print();
    abstract public void truncate();
}



import java.util.Hashtable;
import java.util.Vector;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

class SmIterator {
      
    // iteration idiom:
    // SmContainer e;
    // Object o;

    // for (SmIterator i = e.iterator(), o = i.firstObj(); 
    //      o != null; o = i.nextObj()) { }

    // override previous definition -- never look beyond first container

    public Object resultObj() {
        if ( searchContainer != null &&
                searchPos >= searchContainer.v.size() ) 
            return null;
        return searchContainer.v.get( searchPos );
    }
}

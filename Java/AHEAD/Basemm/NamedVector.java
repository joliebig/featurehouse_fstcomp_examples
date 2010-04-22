

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class NamedVector extends HashSet {
    protected String name;
   
    public NamedVector( String name ){
        this.name = name;
    }
   
    public String getName() {
        return name;
    }
   
    public void setName( String name ) {
        this.name = name;
    }

    public void print( String indent ) {

        if ((name.equals("finalValue") || name.equals("Classes"))
             && !Main.printClean )
            return;

        System.out.print( indent + name + " :( " );

        List lines = new ArrayList( this ) ;
        Collections.sort( lines ) ;

        for ( Iterator p = lines.iterator() ; p.hasNext() ; )
            System.out.print( " " + ( String ) p.next() ) ;
        System.out.println(" ):");

    }
      
    // equals needed by HashSet

    public boolean equals( Object o ) {
        if ( o instanceof  NamedVector )
            return ( ( NamedVector ) o ).name.equals( name );
        return false;
    }
}

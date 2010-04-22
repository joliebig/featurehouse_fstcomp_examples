

import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

// this layer encapsulates the grammar rule and translation
// of "layer xxx;" to "package xxx;"

public class AspectStm         {

    public static final String  packID = "$pack";

    public void reduce2java( AstProperties props ) {
        props.print( getComment() + "package" );

        // here's were we determine the default value of packName
        // if nothing was specified on the command line, we use
        // the entire name that is given in the layer declaration 

        // NOTE the following behavior:  when jak2java is to process
        // a list of files, Main.packName is set here ONCE and Main.main
        // is called once.  Once Main.packName is set, it is used 
        // FOR ALL SUBSEQUENT FILE TRANSLATIONS.

        if (Main.packName.equals(""))
            Main.packName = ((AST_QualifiedName) arg[0]).GetName();

        // here's where we broadcast this on properties

        props.setProperty( AspectStm.packID, Main.packName );
        arg[0].reduce2java( props );
        props.print( ";" );
    }
}

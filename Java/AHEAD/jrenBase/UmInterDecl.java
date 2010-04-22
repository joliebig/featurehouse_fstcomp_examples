

import java.util.*;
import java.io.*;

public class UmInterDecl  {

    public void reduce2java( AstProperties props ) {

	    String depth = (String) props.getProperty("inside");

		 // rename only the outer-most interface
		 if (depth == null) {
          if (arg[0] instanceof NameId) {
             String n = arg[0].tok[0].getTokenName();
             arg[0].tok[0].setTokenName(n + kernelConstants.renameId);
          }
			 props.setProperty("inside", "");
          super.reduce2java(props);
			 props.removeProperty("inside");
		 }
		 else {
		    // inside outer-most interface, increment length of depth
			 // which indicates the level of nesting, then decrement
			 props.setProperty("inside", depth + " ");
			 super.reduce2java(props);
			 props.setProperty("inside", depth);
		 }
	 }
}

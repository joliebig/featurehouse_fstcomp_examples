

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

public class AST_QualifiedName {
    // the following routines are used for converting strings (or string
    // arrays) into AST_QualifiedName trees.  If an array of strings is
    // given as an argument (id[0]="x"; id[1]="y"; id[2]="z"), what is
    // returned is id{x.y.z}id

    public static  AST_QualifiedName Make( String id ) {
        AST_QualifiedName a = new  AST_QualifiedName();
        AstToken dot = new  AstToken().setParms( "",".",0 );
        NameId nid = new  NameId().setParms( new  AstToken().setParms( " ",id,0 ) );
        a.add( new  AST_QualifiedNameElem().setParms( dot, nid ) );
        return ( a );
    }

    public static  AST_QualifiedName Make( String[] id ) {
        AST_QualifiedName a = new  AST_QualifiedName();
        AstToken dot;
        NameId nid;
        int i;

        for ( i = 0; i<id.length; i++ ) {
            dot = new  AstToken().setParms( "",".",0 );
            nid = new  NameId().setParms( new  AstToken().setParms( " ",id[i],0 ) );
            a.add( new  AST_QualifiedNameElem().setParms( dot, nid ) );
        }
        return ( a );
    }

    public String GetName() {
        AstCursor c    = new  AstCursor();
        String    name = null;

        for ( c.First( this ); c.More(); c.PlusPlus() ) {
            if ( c.node instanceof  NameId ) {
                if ( name != null )
                    name = name + "." + c.node.tok[0].tokenName();
                else
                    name = c.node.tok[0].tokenName();
            }
        }
        return name;
    }

    // gets first QName of AST_QualifiedName

    public String getPrefixName() {
       String name = GetName();
       int i = name.indexOf(".");
       if (i>=0)
          name = name.substring(0,i);
       return name;
    }

    // sets first QName of AST_QualifiedName

    public void setPrefixName( String n ) {
        AstCursor c = new AstCursor();
        for ( c.FirstElement( this ); c.MoreElement(); c.NextElement() ) 
           if (c.node instanceof NameId) {
              c.node.tok[0].setTokenName( n );
              return;
           }
           else
           if (c.node instanceof AST_QualifiedName) 
              ((AST_QualifiedName) c.node).setPrefixName(n);
           else
              AstNode.fatalError("first name of layer decl is not an Identifier"); 
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util2;
import java.io.*;

public class UmodClassDecl {
    public String GetName() {
        return arg[0].tok[0].tokenName();
    }

    public String GetType() {
        return "class";
    }
           
    public void compose( AstNode etree ) {
        // etree is of type Ute -- convert this into UmodClassExt
        // composition of class declarations involves three steps.
        // (a) etree is of type Ute -- translate into UmodClassExt
        // (b) compose implements lists
        // (c) compose classbody

        // Step 0: set flag for error checking

        kernelConstants.globals().compclass.isBaseAClass = true;

        // Step 1: convert etree and check to see if 
        //         names are the same

        UmodClassExt e = ( UmodClassExt ) etree.arg[0];
        String bname = arg[0].tok[0].tokenName();
        String ename = e.arg[0].tok[0].tokenName();
        if ( !bname.equals( ename ) )
            AstNode.fatalError( tok[0], 
                         "attempting to compose files with different names: " +
                     bname + "  " + ename );

        // Step 2: if both the base implements list and extension
        //         implements list are not empty, then compose them
        //         otherwise set the base to be the non-empty list
        //         of the two.

        arg[2].compose( e.arg[1] );

        // Step 3: compose corresponding AST_FieldDeclarations -- there
        //         are no additional extension code fragments, so pass in
        //         a null linked list

        arg[3].arg[0].arg[0] = 
                AST_FieldDecl.compose( arg[3].arg[0].arg[0], 
                                     e.arg[2].arg[0].arg[0], null );
    }
/*
      // called when the base class/extension is empty ({})
      // it merely checks if references to Base() are correct --

      private void checkExtension( $TEqn.AstNode n ) {
          
         // Step 1: collect signatures of all class member declarations
         //         in both the base and extension declarations
         Hashtable          hb = new Hashtable();
         Hashtable          he = new Hashtable();
         $TEqn.AstCursor     c = new $TEqn.AstCursor();

         // Step 2: n is a ClassBody, n.arg[0] is an AstOptNode
         //         n.arg[0].arg[0] is a AST_FieldDecl

         $TEqn.AST_FieldDecl e = ($TEqn.AST_FieldDecl) n.arg[0].arg[0];
         for ( c.FirstElement(e); c.MoreElement(); c.NextElement() ) { 
             (($TEqn.ClassBodyDeclaration) c.node).add2Hash(he);
         }

         // Step 2: invoke baseRewrite to rewrite all "Base(...).method(...)"
         //         invocations in the extension

         e.baseRewrite( hb, he, 0 );
      }
      */
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class UmodClassDecl {

    // this method is called on parse tress of the SoUrCe file
    // the idea is for this method to propagate the contents of
    // this UmodClassDecl AST to the file referenced in the SoUrCe file.

    public void propagateChanges() {
        String myName = null;
        String referencedName = null;

        // Step 1: fetch the referenced file, parse it, and locate
        //         the lone ModTypeDecl declaration

        UnMixinUtil u = new  UnMixinUtil();

        // Step 2: do some error checking.  The ModTypeDecl must reference
        //         a UmodClassDecl or UmodClassExt with the same QName as 
        //         our parse tree

        //         note u.location is of type ModTypeDecl, whose first
        //         argument is an UnmodifiedTypeDeclaration -- in our
        //         case, it should be a UmodClassDecl

        if ( kernelConstants.globals().unmixin.base ) {
            // testing for UmodClassDecl

            if ( u.location.arg[1] instanceof  UmodClassDecl ) {
                referencedName = u.location.arg[1].arg[0].tok[0].tokenName();
                myName = arg[0].tok[0].tokenName();
                if ( !referencedName.equals( myName ) )
                    AstNode.fatalError( tok[0],
                                                 "expecting class " + myName + " but got "
                         + referencedName +
                         " file " + 
                                                              kernelConstants.globals().unmixin.fileName + 
                                                             " not updated" );
            }
            else
                AstNode.fatalError( tok[0],
                                "expecting UmodClassDecl but got " +
                        u.location.arg[1].getClass().getName() + 
                        " file " + 
                                                 kernelConstants.globals().unmixin.fileName + 
                                                " not updated" );
        }
        else {
            // testing for UmodClassExt

            if ( u.location.arg[1] instanceof  Ute &&
                u.location.arg[1].arg[0] instanceof  UmodClassExt ) {
                referencedName = u.location.arg[1].arg[0].arg[0].tok[0].tokenName();
                myName = arg[0].tok[0].tokenName();
                if ( !referencedName.equals( myName ) )
                    AstNode.fatalError( tok[0],
                                             "expecting class extension" + myName + 
                     " but got " + referencedName +
                     " file " + 
                                              kernelConstants.globals().unmixin.fileName + 
                                             " not updated" );
            }
            else
                AstNode.fatalError( tok[0],
                            "expecting Ute (or rather UmodClassExt) but got " +
                    u.location.arg[1].getClass().getName() + 
                    " file " + 
                                     kernelConstants.globals().unmixin.fileName + 
                                    " not updated" );
        }
  
        // Step 3: we're ready to make the changes!
        //         see if there is an extends clause -- there shouldn't
        //         be if a class appears to extend itself.  (This is 
        //         possible because of name mangling -- when names are
        //         unmangled, a class appears to extend itself).

        ExtendsClause k = ( ExtendsClause ) arg[1].arg[0];
        if ( k != null ) {
            String xname = ( ( AST_QualifiedName ) arg[1].arg[0].arg[0] ).GetName();
            if ( xname.equals( myName ) )
                k = null;
        }

        boolean updated = false;
        if ( kernelConstants.globals().unmixin.base )
            updated = ( ( UmodClassDecl ) u.location.arg[1] ).
               propagateChanges( k, ( ImplementsClause ) arg[2].arg[0], 
                                ( ClassBody ) arg[3] );
        else
            updated = ( ( UmodClassExt ) u.location.arg[1].arg[0] ).
               propagateChanges( ( ImplementsClause ) arg[2].arg[0], 
                                ( ClassBody ) arg[3] );

        // Step 4: last thing we do -- propagate the changes back to the file

        if ( updated )
            u.output();
    }

    // this method is called on the original AST, not the one with
    // SoUrCe declarations

    public boolean propagateChanges( ExtendsClause k,
                                     ImplementsClause i, 
                                     ClassBody b ) {
        // explicitly call to achieve side effects
        boolean u = oneChange( arg[1],k );
        u = oneChange( arg[2],i ) || u;
        u = oneChange( arg[3],b ) || u;
        return u;
    }

    public static boolean oneChange( AstNode old,  AstNode nu ) {
        boolean updated = false;
        if ( nu == null ) {
            if ( !old.toString().trim().equals( "" ) ) {
                old.Delete();
                updated = true;
            }
        }
        else {
            if ( nu!=null && ! ( old.toString().trim().equals( nu.toString().trim() ) ) ) {
                old.Replace( nu );
                updated = true;
            }
        }
        return updated;
    }
}



import java.util.*;
import Jakarta.util.FixDosOutputStream;
import java.io.*;

public class j2jBasedata {
    public boolean SourceDeclSeen;
    public int     SourceDeclCounter;
    public Vector  previousTypeDecls;
    public  conTable                  inheritedCons;
    public  UnmodifiedTypeDeclaration currentTypeDecl;

    // contains signatures of all constructors seen in this file.
    // useful only for mixin-produced files.
    public Hashtable constructorTable;

    // initialized at beginning of class-like declaration
    public HashSet refinedSet = new HashSet();
}

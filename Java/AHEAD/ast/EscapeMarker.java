

import java.io.PrintWriter;

// this interface is a marker that indicates if an object
// represents an instance of a code escape clause
// it is used in reduce2astEscape in AstList
// the $TEqn.IsList marker indicates that the escaped trees
// are indeed lists, and not ast nodes.  Again, this distinction
// is needed in reduce2astEscape

public interface EscapeMarker {}

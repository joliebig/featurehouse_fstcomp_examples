

public class Dims {

    public String Signature() {
	    String sig = "";
	    AstCursor c = new AstCursor();
		 for (c.FirstElement(this); c.MoreElement(); c.NextElement())
		   sig=sig+"[]";
		 return sig;
    }
}

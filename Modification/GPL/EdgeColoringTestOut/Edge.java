package GPL; 
import java.util.LinkedList; 
public  class  Edge  extends Neighbor  implements EdgeIfc {
	 public Vertex start;

	 public void EdgeConstructor( Vertex the_start, Vertex the_end ) { start = the_start; end = the_end; }

	 public void adjustAdorns( EdgeIfc the_edge ) { }

	 public void setWeight(int weight) { }

	 public int getWeight( ) { return 0; }

	 public Vertex getOtherVertex( Vertex vertex ) { if( vertex == start ) { return end; } else if(vertex == end) { return start; } else { return null; } }

	 public Vertex getStart( ) { return start; }

	 public Vertex getEnd( ) { return end; }

	private int color;

	public int getColor() {return color;}

	public void setColor(int color) {this.color = color;}

	 public void display__wrappee__Degree( ) { System.out.println( " start=" + start.getName() + " end=" + end.getName( ) ); }

	public void display() {System.out.print(" color=" + color);display__wrappee__Degree();}


}

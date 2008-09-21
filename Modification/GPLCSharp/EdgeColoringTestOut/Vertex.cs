//import java.util.Iterator;
using System.Collections; 
using System.Collections.Generic; 
public class  Vertex {
	 public  List<Neighbor> neighbors;  public  string name;  public  Vertex( ) { VertexConstructor( ); }  public  VertexConstructor__wrappee__UndirectedWithEdges( ) { name = null; neighbors = new List<Neighbor>( ); }  public  void VertexConstructor( ) { VertexConstructor__wrappee__UndirectedWithEdges( ); visited = false; }  public  Vertex assignName( string name ) { this.name = name; return ( Vertex ) this; }  public  string GetName( ) { return this.name; }  public  List<Neighbor> GetNeighborsObj( ) { return neighbors; }  public  Iterator<Vertex> GetNeighbors() { return new Iterator<Vertex, Neighbor>(neighbors.GetEnumerator(), TransformNeighborToEnd); }  public static  Vertex TransformNeighborToEnd(Neighbor val) { return val.end; }  public  display__wrappee__UndirectedWithEdges( ) { System.Console.Out.Write( " Node " + name + " connected to: " ); for ( Iterator<Vertex> vxiter = GetNeighbors( ); vxiter.hasNext( ); ) { System.Console.Out.Write( vxiter.next().GetName() + ", " ); } System.Console.Out.WriteLine( ); }  public  void display( ) { if ( visited ) System.Console.Out.Write( "  visited" ); else System.Console.Out.WriteLine( " !visited " ); display__wrappee__UndirectedWithEdges( ); }  public  void AddNeighbor( Neighbor n ) { neighbors.Add( n ); }  public  Iterator<EdgeIfc> GetEdges() { return new Iterator<EdgeIfc, Neighbor>(neighbors.GetEnumerator(), TransformNeighborToEdge); }  public static  EdgeIfc TransformNeighborToEdge(Neighbor val) { return val.edge; }  public  bool visited;  public  void init_vertex( WorkSpace w ) { visited = false; w.init_vertex( ( Vertex ) this ); }  public  void nodeSearch( WorkSpace w ) { Vertex v; w.preVisitAction( ( Vertex ) this ); if ( visited ) return; visited = true; for ( Iterator<Vertex> vxiter = GetNeighbors(); vxiter.hasNext(); ) { v = vxiter.next( ); w.checkNeighborAction( ( Vertex ) this, v ); v.nodeSearch( w ); } w.postVisitAction( ( Vertex ) this ); }
}


using System.Collections; 
using System.Collections.Generic; 
public class  Graph {
	 private  List<Vertex> vertices;  private  List<EdgeIfc> edges;  public static readonly  bool isDirected = false;  private  Dictionary<string, Vertex> verticesMap;  public  Graph() { vertices = new List<Vertex>(); edges = new List<EdgeIfc>(); verticesMap = new Dictionary<string, Vertex>( ); }  public  void run( Vertex s ) {}  public  void sortEdges(System.Collections.Generic.IComparer<EdgeIfc> c) { edges.Sort(c); }  public  void sortVertices(System.Collections.Generic.IComparer<Vertex> c) { vertices.Sort(c); }  public  EdgeIfc AddEdge(Vertex start, Vertex end) { Edge theEdge = new Edge(); theEdge.EdgeConstructor( start, end ); edges.Add( theEdge ); start.AddNeighbor( new Neighbor( end, theEdge ) ); end.AddNeighbor( new Neighbor( start, theEdge ) ); return theEdge; }  public  void AddVertex( Vertex v ) { vertices.Add( v ); verticesMap.Add( v.name, v ); }  public  Vertex findsVertex( string theName ) { Vertex theVertex; if ( theName==null ) return null; return (Vertex)verticesMap[theName]; }  public  Iterator<Vertex> GetVertices() { return new Iterator<Vertex>(vertices.GetEnumerator()); }  public  Iterator<EdgeIfc> GetEdges() { return new Iterator<EdgeIfc>(edges.GetEnumerator()); }  public  EdgeIfc findsEdge( Vertex theSource, Vertex theTarGet ) { EdgeIfc theEdge; for( Iterator<EdgeIfc> edgeiter = theSource.GetEdges(); edgeiter.hasNext(); ) { theEdge = edgeiter.next(); if ( ( theEdge.GetStart().GetName().Equals( theSource.GetName() ) && theEdge.GetEnd().GetName().Equals( theTarGet.GetName() ) ) || ( theEdge.GetStart().GetName().Equals( theTarGet.GetName() ) && theEdge.GetEnd().GetName().Equals( theSource.GetName() ) ) ) return theEdge; } return null; }  public  void display() { System.Console.Out.WriteLine( "******************************************" ); System.Console.Out.WriteLine( "Vertices " ); for ( Iterator<Vertex> vxiter = GetVertices(); vxiter.hasNext() ; ) vxiter.next().display(); System.Console.Out.WriteLine( "******************************************" ); System.Console.Out.WriteLine( "Edges " ); for ( Iterator<EdgeIfc> edgeiter = GetEdges(); edgeiter.hasNext(); ) edgeiter.next().display(); System.Console.Out.WriteLine( "******************************************" ); }  public  void GraphSearch( WorkSpace w ) { Iterator<Vertex> vxiter = GetVertices( ); if ( vxiter.hasNext( ) == false ) { return; } while( vxiter.hasNext( ) ) { Vertex v = vxiter.next( ); v.init_vertex( w ); } for( vxiter = GetVertices( ); vxiter.hasNext( ); ) { Vertex v = vxiter.next( ); if ( !v.visited ) { w.nextRegionAction( v ); v.nodeSearch( w ); } } }
}

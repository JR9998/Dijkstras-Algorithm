//John Reyes
import java.util.*;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

/**
 * Dijkstra's algorithm.
 */
public class Dijkstra {

	private Graph graph_; // the graph
	private EdgeWeight w_; // accessor for edge weights
	private Map<Vertex, Double> dist = new HashMap<>(); //Hashmap for storing the distance  
	private Map<Vertex, Edge> prev = new HashMap<>(); //Hashmap for storing the edge coming into the vertex
	private PriorityQueue<Vertex> pq = new PriorityQueue<>(new VertComparator()); //PriorityQueue used for the vertices 

	/**
	 * Functional interface for edge weights.
	 */
	public interface EdgeWeight {

		/**
		 * Get the weight for edge e.
		 * 
		 * @param e
		 *          edge to get the weight for
		 * @return weight for edge e
		 */
		double weight ( Edge e );
	}

	/**
	 * Ordering of vertices within the priority queue.
	 */
	private class VertComparator implements Comparator<Vertex> {
		@Override
		public int compare ( Vertex u, Vertex v ) {
			if(dist.get(u) < dist.get(v)) { //u comes before v
				return -1; 
			} else if(dist.get(u) > dist.get(v)) { //u comes after v
				return 1; 
			} else { //they're tied 
				return 0; 
			}
			//throw new UnsupportedOperationException("not yet implemented!");
		}//end of constructor
	}//end of VertComparator class

	/**
	 * Create an instance of Dijkstra's algorithm to compute shortest paths from s
	 * to all other vertices. Assumes that the weight for edge e is
	 * (Double)e.getObject().
	 * 
	 * @param g
	 *          the graph
	 */
	public Dijkstra ( Graph g ) {
		this(g,e -> ((Double) e.getObject()).doubleValue());
	}

	/**
	 * Create an instance of Dijkstra's algorithm to compute shortest paths from s
	 * to all other vertices.
	 * 
	 * @param g
	 *          the graph
	 * @param w
	 *          function taking an Edge and returning the weight for the edge (as
	 *          a double)
	 */
	public Dijkstra ( Graph g, EdgeWeight w ) {
		graph_ = g;
		w_ = w;
	}

	/**
	 * Initialize the algorithm.
	 * 
	 * @param s
	 *          starting vertex
	 */

	public void init ( Vertex s ) {
		for(Vertex current:graph_.vertices()) { //iterate through all vertices in the graph
			dist.put(current, Double.MAX_VALUE); //set all distances to max value 
			prev.put(current, null); //set each prev value to null
		}

		dist.put(s, 0.0); 

		for(Vertex current:graph_.vertices()) { //iterate through all vertices again
			pq.add(current); //add each vertex in the graph to our priority queue
		}
		//throw new UnsupportedOperationException("not yet implemented!");
		
	}//end of init method

	/**
	 * Compute shortest paths from s to all vertices. Requires init(s) to have
	 * been called first.
	 */
	public void run () {
		run(null);
	}

	/* 
	 * 
	 * algorithm dijkstra(G,s):
  for all v in V do
    dist[v] ← ∞
    prev[v] = null

  dist[s] ← 0

  PQ ← makeQueue(V)

  while PQ is not empty do
    v ← PQ.removeMin()
    for each incident edge e=(v,u) 
      if dist[u] > dist[v]+w(v,u) then
        dist[u] = dist[v]+w(v,u)
        PQ.decreaseKey(u)
        prev[u] = e               // was prev[u] = v
	 */

	/**
	 * Compute shortest path from vertex s to vertex f. Requires init(s) to have
	 * been called first.
	 * 
	 * @param f
	 *          finish vertex; if null, compute the shortest path to all vertices
	 */
	public void run ( Vertex f ) {
		while(!pq.isEmpty()) { 
			Vertex v = pq.poll(); //remove the first vertex with the shortest distance 
			if(v == f && v != null) { //break after shortest path to f is found 
				break;
			}
			for(Edge e:graph_.incidentEdges(v)) { 
				Vertex adj = graph_.opposite(v, e); //get the vertex that's adjacent to v

				if(dist.get(adj) > dist.get(v) + w_.weight(e)) { 
					dist.put(adj, dist.get(v) + w_.weight(e)); //update the distance 
					pq.remove(adj);//remove the adjacent vertex from the queue 
					pq.add(adj); //reinsert the adjacent vertex into the queue 
					prev.put(adj, e); //update the previous vertex 
				}
			}//end of for loop
		}//end of the while loop

		//throw new UnsupportedOperationException("not yet implemented!");
	}//end of run method 

	/**
	 * Get the vertices on the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 * 
	 * @param f
	 *          finish vertex
	 * @return the vertices on the shortest path from vertex s to vertex f
	 */
	public Iterable<Vertex> getPathVertices ( Vertex f ) {
		LinkedList<Vertex> vertices = new LinkedList<>(); //LinkedList to hold the vertices traveled through 
		Vertex current = f; //the current vertex that we're at 
		while(current != null) { 
			vertices.add(current); //add the vertex to our list 
			Edge edge = prev.get(current); 
			if(edge == null) { 
				break;
			}
			current = graph_.opposite(current, edge); //update our vertex
		}
		Collections.reverse(vertices); //reverse the list for proper order
		return vertices;
		//throw new UnsupportedOperationException("not yet implemented!");
	}//end of getPathVertices method

	/**
	 * Get the edges on the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 * 
	 * @param f
	 *          finish vertex
	 * @return the edges on the shortest path from vertex s to vertex f
	 */
	public Iterable<Edge> getPathEdges ( Vertex f ) {
		LinkedList<Edge> edges = new LinkedList<>(); //LinkedList to hold the edges we took 
		Vertex current = f; //current vertex 
		Edge edge = prev.get(current); //the edge that leads to the current vertex
		while(prev.get(current) != null) { 
			edges.add(edge); //add the edge our list 
			if(edge == null) { 
				break; 
			}
			current = graph_.opposite(current, edge); //update our vertex
			edge = prev.get(current); //update our edge
		}
		Collections.reverse(edges); //reverse the list for proper order
		return edges; 
		//throw new UnsupportedOperationException("not yet implemented!");
	}//end of getPathEdges method

	/**
	 * Get the length of the shortest path from vertex s to vertex f. Requires
	 * run() or run(f) to have been called first.
	 * 
	 * @param f
	 *          finish vertex
	 * @return the length of the shortest path from vertex s to vertex f
	 */
	public double getDist ( Vertex f ) {
		return dist.get(f); //returns the distance label for f 
	//	throw new UnsupportedOperationException("not yet implemented!");
	}//end of getDist method

}//end of class

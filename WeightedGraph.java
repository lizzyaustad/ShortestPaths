import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * An undirected, unweighted, simple graph.  The vertex set is static; edges
 * may be added but not removed.
 *
 * @param K the type of the vertices
 * @author Jim Glenn
 * @version 0.1 2015-10-27
 */

public class WeightedGraph<K> implements Graph<K>
{
    /**
     * This graph's vertex set.
     */
    private Set<K> verts;

    /**
     * This graph's adjacency lists.
     */
    private Map<K, List<Edge>> adjLists;

    public Set<K> getVerts() {
        return verts;
    }

    public Map<K, List<Edge>> getAL() {
        return adjLists;
    }

    /**
     * Creates a graph with the given vertex set and no edges.
     *
     * @param v a collection of vertices
     */
    public WeightedGraph(Collection<K> v)
    {
        // make ourselves a private copy of the vertex set
        verts = new HashSet<K>(v);

        // set up empty adkacency lists for each vertex
        adjLists = new HashMap<K, List<Edge>>();
        for (K src : verts)
        {
            adjLists.put(src, new ArrayList<Edge>());
        }
    }

    /**
     * Adds the given edge to this graph if it does not already exist.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     */
    public void addEdge(String u, String v, int w)
    {
        
    if (u.equals(v))
        {
        throw new IllegalArgumentException("adding self loop");
        }

    // get u's adjacency list
    List<Edge> adj = adjLists.get(u);

    boolean adjContainsV = false;
    for (int i =0; i<adj.size(); i++) {
        
        if ((adj.get(i).v).equals(v)) {
            adjContainsV = true;
        }
    }
    
    // check for edge already being there
    if (!adjContainsV)
        {
        // edge is not already there -- add to both adjacency lists
        Edge n = new Edge(v,w);
        
        adj.add(n);
        adjLists.get(v).add(new Edge(u,w)); 
        }
    }

    /**
     * Determines if the given edge is present in this graph.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     * @return true if and only if the edge (u, v) is in this graph
     */
    public boolean hasEdge(String u, String v)
    {
        return adjLists.get(u).contains(v);
    }

    /**
    * Returns the edge from the given pairs
    * @param u a vertex in this graph
    * @param v a vertex in this graph
    * @return w the weight of the edge between the two
    */
    public int getWeight(String u, String next) {
        
        List<Edge> l = adjLists.get(u);
            for (int i=0; i<l.size(); i++) {
                if((l.get(i).v).equals(next)) {
                    
                    return l.get(i).w;
                }
            }
        
        return 9999999;
    }

    /**
     * Returns an iterator over the vertices in this graph.
     *
     * @return an iterator over the vertices in this graph
     */
    public Iterator<K> iterator()
    {
        return (new HashSet<K>(verts)).iterator();
    }

    /**
     * Returns an iterator over the neighbors of the vertices in this graph.
     *
     * @param v a vertex in this graph
     * @return an iterator over the vertices in this graph
     */
    public Iterable<Edge> neighbors(String v)
    {
        return new ArrayList<Edge>(adjLists.get(v));
    }

    /**
     * Returns a printable represenation of this graph.
     *
     * @return a printable representation of this graph
     */
    public String toString()
    {
        return adjLists.toString();
    }

    
}
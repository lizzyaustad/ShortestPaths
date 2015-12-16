import java.util.*;

public class ShortestPathsMain
{
	

    public static void main(String[] args)
    {
	Scanner in = new Scanner(System.in);

	// read the first line with the dimensions of the grid
	int width = in.nextInt();
	int height = in.nextInt();
	int n = in.nextInt();

	// THIS WILL MAKE AN ARRAY (okay, a list of lists since Java won't allow
	// arrays of generics) OF GRAPHS FOR THE INDIVIDUAL CELLS --
	// g.get(r).get(c) IS THE GRAPH FOR THE CELL IN ROW r COLUMN c

	// make an empty graph for each cell
	List<List<WeightedGraph<String>>> g = new ArrayList<List<WeightedGraph<String>>>();
	for (int r = 0; r < height; r++)
	    {
		List<WeightedGraph<String>> row = new ArrayList<WeightedGraph<String>>();
		for (int c = 0; c < width; c++)
		    {
			// make the list of vertices in this cell starting
			// with the corners...
			List<String> verts = new ArrayList<String>();
			verts.add("g" + r + "." + c); // upper left
			verts.add("g" + (r + 1) + "." + c); // lower left
			verts.add("g" + r + "." + (c + 1)); // upper right
			verts.add("g" + (r + 1) + "." + (c + 1)); // lower right

			//...then the interior vertices
			for (int k = 0; k < n; k++)
			    {
				verts.add("v" + r + "." + c + "." + k);
			    }

			// add that graph!
			row.add(new WeightedGraph<String>(verts));
		    }
		g.add(row);
	    }

	// loop over edges to add
	String from;
	while (!(from = in.next()).equals("queries"))
	    {
		String to = in.next();
		int w = in.nextInt();
		
		// the to vertex is always in the interior of the cell
		assert to.charAt(0) == 'v';

		// figure out from the to vertex which cell we're in
		StringTokenizer tok = new StringTokenizer(to.substring(1), ".");	
		int r = Integer.parseInt(tok.nextToken());
		int c = Integer.parseInt(tok.nextToken());
		
		// add the edge to the correct cell
		
		g.get(r).get(c).addEdge(from, to, w);
	    }

	// MAKE YOUR CORNER GRAPH HERE (might want the ability to label edges with paths
	// they represent)

	    WeightedGraph<String> cg;
	    List<String> corners = new ArrayList<String>();

	    for (int r = 0; r <= height; r++) {
			//List<WeightedGraph<String>> cornerRow = new ArrayList<WeightedGraph<String>>();
			for (int c = 0; c <= width; c++) {
						
				corners.add("g" + r + "." + c); 
			}
		}
				//add the corners to a separate list
		cg = new WeightedGraph<String>(corners);
		

	// process the queries
	while (in.hasNext())
	    {
		from = in.next();
		String to = in.next();

		// determine what cells we're in
		StringTokenizer tok = new StringTokenizer(from.substring(1), ".");
		int fromR = Integer.parseInt(tok.nextToken());
		int fromC = Integer.parseInt(tok.nextToken());

		tok = new StringTokenizer(to.substring(1), ".");
		int toR = Integer.parseInt(tok.nextToken());
		int toC = Integer.parseInt(tok.nextToken());
		
		String[] fromCorners = {"g" + fromR + "." + fromC,
					"g" + (fromR + 1) + "." + fromC,
					"g" + fromR + "." + (fromC + 1),
					"g" + (fromR + 1) + "." + (fromC + 1)};
		String[] toCorners = {"g" + toR + "." + toC,
				      "g" + (toR + 1) + "." + toC,
				      "g" + toR + "." + (toC + 1),
				      "g" + (toR + 1) + "." + (toC + 1)};

		// COMPUTE THE SHORTEST PATHS FROM from AND to TO THEIR CORNERS AND PERHAPS
		// UPDATE THE CORNER GRAPH ACCORDINGLY (existing Graph needs to know all
		// vertices when it is created; need some way to deal with that or an addVertex
		// method)

		HashMap<String, Integer> weights = new HashMap<String, Integer>();
		for (int r=0; r<height; r++) {
			
			for (int c=0; c<width; c++) {
				String[] m = new String[4];
				m[0] = ("g" + r + "." + c);
				m[1] = ("g" + (r+1) + "." + c);
				m[2] = ("g" + r + "." + (c+1));
				m[3] = ("g" + (r+1) + "." + (c+1));
				

				for (int i=0; i<3; i++) {
					weights = dijkstra(g.get(r).get(c), m[i]).distances; 

					int end = 3;
					while (end>i) {
						
						cg.addEdge(m[i], m[end], weights.get(m[end]));
						
						end--;
					}
				}
			}
		}   

		cg.getVerts().add(from);
		cg.getVerts().add(to);   
		cg.getAL().put(from, new ArrayList<Edge>());		
		cg.getAL().put(to, new ArrayList<Edge>());
		
		weights = dijkstra(g.get(fromR).get(fromC), from).distances; 

		for (int i=0; i<4; i++) {
			cg.addEdge(from, fromCorners[i], weights.get(fromCorners[i]));
		}

		weights = dijkstra(g.get(toR).get(toC), to).distances; 

		for (int i=0; i<4; i++) {
			cg.addEdge(to, toCorners[i], weights.get(toCorners[i]));
		}

		HashStructure answer = dijkstra(cg, from);
		weights = answer.distances;
		HashMap<String,String> path = answer.path;
			
		System.out.println(weights.get(to) + " " + getPath(from, to, path));
		//cg.remove(from);
		}
	}
		// RUN DIJKSTRA'S ON THE CORNER GRAPH
		public static HashStructure dijkstra(WeightedGraph<String> g, String s){
			
			HashMap<String, Integer> d = new HashMap<String, Integer>();
			HashMap<String, String> pred = new HashMap<String, String>();
			
			
			for (String i : g) {
				d.put(i, Integer.MAX_VALUE);
			}
			
			d.put(s,0);

			PriorityQueue<String, Integer> q = new PriorityQueue<String,Integer>();
			
			
			for (String i : g) {
				q.addItem(i, d.get(i));
			}
				

			while (q.getSize() > 0) {
				int minP = q.peekPriority();
				String u = q.removeItem(); 
				Iterator<Edge> neighbors = g.neighbors(u).iterator();

				while (neighbors.hasNext()) {
					Edge nb = neighbors.next();
					String n = nb.v;

					int w = g.getWeight(u, n); //the weight of the edge from u to n

					if (q.contains(n)) {
						
						if((minP+w) < d.get(n)) {
							q.decreasePriority(n, minP + w);
							d.remove(n);
							d.put(n, minP + w);
							pred.remove(n);
							pred.put(n,u);
						}
					}
				}
				
			}
			HashStructure hs = new HashStructure (d, pred);
			return hs;	
		}
					

		// RECONSTRUCT COMPLETE PATH FROM THE PATH OF CORNERS
		public static String getPath(String from, String to, HashMap<String, String> path) {
			String route = "[" + from + ", ";
			for (String i : path.keySet()) {
				//route += i;
				//route += ", ";
			}
			route += to + "]";
			return route;
		}

		// UNDO THE UPDATES (maybe add removeVertex and/or removeEdge to WeightedGraph)
	    
}
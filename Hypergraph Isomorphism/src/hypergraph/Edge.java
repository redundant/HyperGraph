package hypergraph;

import java.util.*;

public class Edge {
	private Set<Integer> vertices;
        private Hypergraph graph;
	
	
	public Edge(Hypergraph g, ArrayList<Integer> verts){
		vertices = new HashSet<Integer>();
                graph = g;
		
		Iterator<Integer> it = verts.iterator();
		while(it.hasNext()){
			Integer j = it.next();
                        Vertex v = g.getVertex(j);
			vertices.add(j);
			v.addEdge(this);
		}
	}
        
        public Set<Integer> getVertices(){
            return vertices;
        }
}

package hypergraph;

import java.util.*;

public class Vertex {

	private String label;
	private ArrayList<Edge> edges;
	
	public Vertex(){
		label = new String(); 
		edges = new ArrayList<Edge>();
		}
	
	public void setLabel(String s){label = s;}
	public void addEdge(Edge e){edges.add(e);}
	
	public String getlabel(){return label;}
	public int getEdgecount(){return edges.size();}
	public Edge getEdge(int i){return edges.get(i);}
	
}

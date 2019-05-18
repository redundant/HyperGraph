package hypergraph;
import Flag.PermIterator;
import java.io.PrintWriter;
import java.util.*;

public class Hypergraph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	
	public Hypergraph(){
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	public void addVertex(Vertex v){vertices.add(v);}
	public void labelVertex(int j, String l){vertices.get(j).setLabel(l);}
        public Vertex getVertex(int j){return vertices.get(j);}
	public Edge getEdge(int j){return edges.get(j);}
        
	public void addEdge(ArrayList<Integer> vertices){
		Edge e = new Edge(this, vertices);
		edges.add(e);
	}
        
        public Hypergraph cloneGraph(){
            Hypergraph g = new Hypergraph();
            
            for(int i = 0; i < vertices.size(); i++){
                Vertex v = new Vertex();
                v.setLabel(vertices.get(i).getlabel());
                g.addVertex(v);
            }
            
            for(int i = 0; i < edges.size(); i++){
                Set<Integer> sverts = edges.get(i).getVertices();
                Integer[] temp = sverts.toArray(new Integer[0]);
                ArrayList<Integer> alverts = new ArrayList<Integer>();
                alverts.addAll(Arrays.asList(temp));
                
                g.addEdge(alverts);

            }
            
            return g;
        }
        
        public Integer[] getNumberofIsos(){
            Integer[] temp = new Integer[2];
            temp[0]=0;
            temp[1]=factorial(this.getVertexSize());
            
            Iterator<int[]> it = new PermIterator(this.getVertexSize());
            
            while(it.hasNext()){
                int[] perm = it.next();
                if(isIso(perm)){
                    temp[0]++;
                }
            }
            
            return temp;
        }
        
        public boolean isIso(int[] perm){
            boolean good = true;
            for(Edge e: edges){
                boolean egood = false;
                for(Edge f: edges){
                    Set<Integer> fvert = f.getVertices();
                    Set<Integer> iverts = new HashSet<Integer>();
                    
                    for(Integer i: e.getVertices()){
                        iverts.add(perm[i]);
                    }
                    
                    if(fvert.containsAll(iverts)){
                        egood = true;
                    }
                        
                }
                if(egood == false){
                    good = false;
                    break;
                }
            }
            
            return good;
        }
        
        public int getVertexSize(){
            return vertices.size();
        }
        
        public int getEdgeSize(){
            return edges.size();
        }
        
        // expands with one unlabeled vertex in all possible ways.
        public ArrayList<Hypergraph> expand(){
            ArrayList<Hypergraph> temp = new ArrayList<Hypergraph>();
            
            Hypergraph g = this.cloneGraph();
            
            Vertex v = new Vertex();
            v.setLabel("");
            g.addVertex(v);
            
            temp.add(g);
            
            for(int i= 0; i < vertices.size(); i++){
                for(int j = i+1; j < vertices.size(); j++){
                    int old = temp.size();
                    for(int k = 0; k < old; k++){
                        ArrayList<Integer> newedge = new ArrayList<Integer>();
                        newedge.add(i);
                        newedge.add(j);
                        newedge.add(vertices.size());
                        Hypergraph tempg = temp.get(k).cloneGraph();
                        tempg.addEdge(newedge);
                        temp.add(tempg);
                    }
                }
            }
            
            return temp;
        }
        
        public Hypergraph takeComplement(){
            
            Hypergraph temp = new Hypergraph();
            
            for(Vertex v: vertices){
                Vertex u = new Vertex();
                u.setLabel(v.getlabel());
                temp.addVertex(u);
            }
            
            for(int i = 0; i < vertices.size(); i++){
                for (int j = 0; j < i; j++){
                    for(int k = 0; k < j; k++){
                        HashSet<Integer> verts = new HashSet<Integer>();
                        verts.add(i);
                        verts.add(j);
                        verts.add(k);
                        boolean add = true;
                        for(Edge e: edges){
                            if(e.getVertices().containsAll(verts)){
                                add = false;
                                break;
                            }
                        }
                        if(add){
                            ArrayList<Integer> vertarr = new ArrayList<Integer>();
                            for(Integer m: verts){
                                vertarr.add(m);
                            }
                            temp.addEdge(vertarr);
                        }
                        
                    }
                }
            }
            
            return temp;
        }
	
        public boolean isIMic(Hypergraph g){
            if(g.getVertexSize() != getVertexSize()){
                return false;
            }
            
            if(g.getEdgeSize() != getEdgeSize()){
                return false;
            }
            
            PartialIsomorphism im = new PartialIsomorphism(getVertexSize());
            
            return isIMicRecurse(g, im);
        }
        
        private boolean isIMicRecurse(Hypergraph g, PartialIsomorphism im){
            ArrayList<Integer> unmapped = (ArrayList<Integer>)im.getUnmappedVertices();
            ArrayList<Integer> mapping = (ArrayList<Integer>)im.getMappedVertices();
            
            int last = mapping.size()-1;
            
            if(last >=0){
                int mapsto = mapping.get(last);

                Vertex lastV = g.getVertex(last);
                Vertex lastMap = getVertex(mapsto);
                // check if last mapped has same number of edges and same label

                if(!lastV.getlabel().equals(lastMap.getlabel())){
                    return false;
                }

                if(lastV.getEdgecount() != lastMap.getEdgecount()){
                    return false;
                }

                for(int i = 0; i < lastV.getEdgecount(); i++){
                    Edge e = lastV.getEdge(i); // edge here contains a list of integers
                    Set<Integer> verts = e.getVertices();
                    
                    Set<Integer> imageVerts = new HashSet<Integer>();
                    
                    for(int k : verts){
                        if(k < mapping.size()){
                            imageVerts.add(mapping.get(k));
                        }
                    }
                    
                    boolean good = false;
                    
                    for(int j = 0; j < lastMap.getEdgecount(); j++){
                         Edge codomainEdge = lastMap.getEdge(j);
                         Set<Integer> codomainSet = codomainEdge.getVertices();
                         if(codomainSet.containsAll(imageVerts)){
                             good = true;
                             break;
                         }
                    }
                    
                    if(good == false)
                        return false;
                }
                
            }
            
            if(mapping.size()==this.getVertexSize())
                return true;
            
            for (int j =0; j < unmapped.size(); j++/*Iterator<Integer> it = unmapped.iterator(); it.hasNext();*/) {
                int i = unmapped.get(j);
                im.map(i);

                if(isIMicRecurse(g,im)==true){
                    return true;
                }
                else{
                    im.unmap(i);
                }
            
            }
            return false;
        }

    public void writeToFile(PrintWriter out) {
        out.println(vertices.size());
        if(edges.isEmpty()){
            out.println("empty");
        }
        
        else{
            Iterator<Edge> eit = edges.iterator();
            while(eit.hasNext()){
                Edge e = eit.next();
                Iterator<Integer> it = e.getVertices().iterator();
                
                while(it.hasNext()){
                    
                    out.print(it.next());
                    if(it.hasNext())
                        out.print(",");
                }
                if(eit.hasNext())
                    out.print(" ");
            }
            out.print("\n");
        }
       
                
        
    }
    
    
    
    //VERIFY THIS WORKS
    public Hypergraph inducedSubgraph(ArrayList<Integer> nvertices){
        Hypergraph temp = new Hypergraph();
        
        for(int i = 0; i < nvertices.size(); i++){
            Vertex v = new Vertex();
            v.setLabel(vertices.get(nvertices.get(i)).getlabel()); // keep label
            temp.addVertex(v);
        }
        
        for(Edge e: edges){
            Set verts = e.getVertices();
            if(nvertices.containsAll(verts)){
                ArrayList<Integer> t= new ArrayList<Integer>();
                for(int j = 0; j < nvertices.size();j++){
                    if(verts.contains(nvertices.get(j))){
                        t.add(j);
                    }
                }
                temp.addEdge(t);
            }
        }
        
        return temp;
    }

    private Integer factorial(int vertexSize){ 
            
        if(vertexSize < 0){
         System.out.print("WTF");
        }
        
        if(vertexSize == 0){
            return 1;
        }
        
        else{
            return vertexSize*factorial(vertexSize-1);
        }
    }
        
}


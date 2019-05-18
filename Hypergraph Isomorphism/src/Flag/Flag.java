/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;

import hypergraph.Edge;
import hypergraph.Hypergraph;
import hypergraph.Vertex;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author tnell
 */
public class Flag {
    // Only defined for 0 flags
    public Integer[] getNumberofIsos(){
        return model.getNumberofIsos();
    }
    
    private boolean isIdentityIso(Hypergraph desiredType, Hypergraph localType) {
        if(desiredType.getVertexSize()!= localType.getVertexSize()){
            return false;
        }
        
        if(desiredType.getEdgeSize() != localType.getEdgeSize()){
            return false;
        }
        
        boolean iso = true;
        for(int i = 0; i < desiredType.getEdgeSize(); i++){
            Edge u = desiredType.getEdge(i);
            boolean edgeWorks = false;
            for(int j = 0; j < localType.getEdgeSize(); j++){
                Edge v = localType.getEdge(j);
                
                if(u.getVertices().equals(v.getVertices())){ // check this works
                    edgeWorks = true;
                }
            }
            
            if(edgeWorks == false){
                iso = false;
                break;
            }
        }
        
        return iso;
        
    }

    Hypergraph type;
    Hypergraph model;
    ArrayList<Integer> map;
    
    public Flag(ArrayList<Integer> f, Hypergraph t, Hypergraph m){
        map = f;
        type = t;
        model = m;
        
        for(int i = 0; i < map.size(); i++){
            model.getVertex(map.get(i)).setLabel(new Integer(i).toString());
        }
    }
    
    public Flag cloneFlag(){
        return new Flag(map, type.cloneGraph(),model.cloneGraph());
    }
    
    public void writeToFile(PrintWriter out){
        if(!map.isEmpty()){        
            
            out.print(map.get(0));
            
            for(int i = 1; i < map.size(); i++){
                out.print(","+ map.get(i));
            }
            
            
            out.print("\n");
            type.writeToFile(out);
        }
        else{
            out.println("empty");
        }
        model.writeToFile(out); 
        out.print("\n");
    }
    
    // Only well defined behavior for zero flags
    
    public Flag Complement(){
        return new Flag(map, type, model.takeComplement());
    }
    
    public ArrayList<Flag> expand(){
        ArrayList<Flag> temp = new ArrayList<Flag>();
        ArrayList<Hypergraph> expandedmodels = model.expand();
        
        for(Hypergraph h: expandedmodels){
            temp.add(new Flag(map, type, h));
        }
        
        return temp;
    }
    
    // better version, aware of subflag restrictions and more memory efficient
   // starts at the jth and goes to the kth including j but not k
    public ArrayList<Flag> smartExpand(ArrayList<Flag> forbidden, int j, int k){
        ArrayList<Flag> expanded = new ArrayList<Flag>();
        
        Flag baseflag = this.cloneFlag();
        int size = model.getVertexSize();
        baseflag.addVertex();
        
        
        for(int i = j; i < k; i++){
            Flag tempf = baseflag.cloneFlag();
            ArrayList<ArrayList<Integer> > edges = pickEdges(tempf.getModel().getVertexSize(),i);
            Hypergraph tempg = tempf.getModel();
            for(ArrayList<Integer> e: edges){
                e.add(size);
                tempg.addEdge(e);
            }
            
            boolean good = true;
            
            if(tempf.isforbidden(forbidden)){
                continue; // forget about it
            }
            
            for(Flag f: expanded){
                if(f.isIMic(tempf)){
                    good = false;
                    break;
                }
            }
            
            if(good == true){
                expanded.add(tempf);
                //System.out.println("Added "+i);
            }
        }
        
        return expanded;
    }
    
    private boolean isforbidden(ArrayList<Flag> forbidden){
        boolean good = true;
        if(forbidden != null){
            for(Flag f: forbidden){
                if(this.hasSubgraph(f)){
                    good = false;
                    break;
                }
            }
        }
        return !good;
    }
    
    // returns an arraylist containing which new edges to use will break if you go too big
    // too big in this case means n \ge 8 might have fixed it by going to long
    private ArrayList<ArrayList<Integer> > pickEdges(int n, long m){
        ArrayList<ArrayList< Integer> > newEdges = new ArrayList<ArrayList< Integer> >();
        
        if(n >=7){
            for(long i = 0; i < 64; i++){
                    if(((m >> i)& 1) == 1){
                        newEdges.add(enumeratePair(n,i));
                    }
            }
        }
        else{
            for(long i = 0; i < 32; i++){
                    if(((m >> i)& 1) == 1){
                        newEdges.add(enumeratePair(n,i));
                    }
            }
        
        }
        return newEdges;
    }
    
    //gets the ith pair with both strictly less than n
    // and first coordinate less than the second coordinate
   
    private ArrayList<Integer> enumeratePair(int n, long i){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        
        i++;
        int j = 1;
        
       while(j<=i){
            i = i-j;
            j++;
       }
       
       if(i==0){
           j--;
       }
       
       temp.add(j);
       temp.add((int)i);
        
        return temp;
    }
   
    
    public boolean isIMic(Flag other){
        return model.isIMic(other.getModel());
    }

    private Hypergraph getModel() {
        return model;
    }
    
    public Hypergraph getType(){
        return type;
    }
    
    public ArrayList<Integer> getMap(){
        return map;
    }
    
    public boolean hasSubgraph(Flag f){
        int flagsize = model.getVertexSize();
        int othersize = f.getModel().getVertexSize();
        
        ArrayList<ArrayList <Integer> > subsets = getSubsets(flagsize,othersize);
        
        for(ArrayList<Integer> subset: subsets){
            Flag nFlag = this.inducedFlag(subset);
            if(f.isIMic(nFlag)){
                return true;
            }
                
        }
        
        return false;
    }
    
    // computes the coefficient of a downward averaged f, where f is assumed to be a zero flag. I only need it in this case
    // and can't find a better name
    public Integer[] downwardAverage(Flag f){
        Integer[] temp = new Integer[2];
        int flagsize = model.getVertexSize();
        int othersize = f.getModel().getVertexSize();
        
        temp[0] = 0;
        temp[1] = binomial(flagsize,othersize);
        
        ArrayList<ArrayList <Integer> > subsets = getSubsets(flagsize,othersize);
        
        
        for(ArrayList<Integer> subset: subsets){
            Flag nFlag = this.inducedFlag(subset);
            if(f.isIMic(nFlag)){
                temp[0]++;
            }
                
        }
        
        return temp;
    }
    
    // Behavior here is only defined when used on a 0 flag with two flags on the same type
    // with the same size ... suboptimal coding ftw 
    public Integer[] downAveragedProduct(Flag first, Flag second){
        Integer[] temp = new Integer[2];
        temp[0]=0;
        
        int flagsize = this.getModel().getVertexSize();
        int omodelsize = first.getModel().getVertexSize();
        int typesize = first.getType().getVertexSize();
        
        temp[1]=binomial(flagsize,typesize)*factorial(typesize)// pick an injective map
                *binomial(flagsize-typesize,2*(omodelsize-typesize))* // pick the other vertices considered
                binomial(2*(omodelsize-typesize),omodelsize-typesize) //pick where they end up i.e. break into two sets
 
                ;
        
        
        ArrayList<ArrayList<Integer> > subsets = getSubsets(flagsize,2*omodelsize-typesize);
        ArrayList<ArrayList< Integer> > typesets = getSubsets(2*omodelsize-typesize,typesize);
        
        for(ArrayList<Integer> subset: subsets){
            
            Flag local = this.inducedFlag(subset); // this relabels the vertices so i'm no longer working in the big flags enumeration
            for(ArrayList<Integer> typeset: typesets){
                
                ArrayList<Integer> unused = new ArrayList<Integer>();
                
                for(int i = 0; i < subset.size(); i++){
                    if(!typeset.contains(i))
                        unused.add(i);
                }
                
                ArrayList<ArrayList<Integer> > partitions = getSubsets(unused.size(), omodelsize-typesize); // check that these work out
                
                for(ArrayList<Integer> partition : partitions){
                    ArrayList<Integer> firstset = new ArrayList<Integer>();
                    ArrayList<Integer> secondset = new ArrayList<Integer>();
                    
                    for(int i = 0; i < 2*omodelsize-2*typesize; i++){
                        if(partition.contains(i)){
                            firstset.add(unused.get(i));
                        }
                        
                        else{
                            secondset.add(unused.get(i));
                        }
                    }
                   if(typesize>0){ 
                    PermIterator p = new PermIterator(typesize);
                    while(p.hasNext()){ 
                        int[] permutation = p.next();
                         ArrayList<Integer> typetemp = new ArrayList<Integer>();
                         for(int i = 0; i < permutation.length; i++){
                             typetemp.add(typeset.get(permutation[i]));
                         }

                         local.changeType(typetemp); // i now need to verify that this works with the desired map



                         Hypergraph desiredType = first.getType();
                         Hypergraph localType = local.getType();

                         if(!local.getType().isIMic(desiredType))
                             break;

                         if(isIdentityIso(desiredType,localType)){

                             Flag firstPart = local.inducedFlag(firstset);
                             Flag secondPart = local.inducedFlag(secondset);

                             if(first.isIMic(firstPart)&& second.isIMic(secondPart))
                                 temp[0]++;
                         }
                    }
                   }
                   else{
                       Flag firstPart = local.inducedFlag(firstset);
                       Flag secondPart = local.inducedFlag(secondset);
                       if(first.isIMic(firstPart)&& second.isIMic(secondPart))
                                 temp[0]++;
                   }
                }
            }
        }
        
        return temp;
        
    }
    
    private ArrayList<ArrayList<Integer> > getSubsets(int n, int size){
        
        ArrayList< ArrayList< Integer> > temp = new ArrayList<ArrayList<Integer> >();
        
        
        for(int j = 0; j < Math.pow(2,n); j++){
            ArrayList<Integer> res = new ArrayList<Integer>();
            for(int i = 0; i < 32; i++){
                if(((j >> i)& 1) == 1){
                    res.add(i);
                }
            }
            if(res.size() == size)
                temp.add(res);
        }
        
        return temp;
    }
    
    private int binomial(int n, int k){
        
        double temp = (double)factorial(n)/(double)(factorial(k)*factorial(n-k));
        
        return (int) temp;
    }
    
    private int factorial(int n){
        double temp = 1;
        
        for(int i = n; i> 1; i--){
            temp*=i;
        }
        
        return (int)temp;
    }
    
    //does not preserve order on where the map is (i.e. the type is the first k vertices)
    // Assumes vertices does NOT contain the type (i.e. appends the type to get the actual subgraph)
    // CHECK THAT THIS WORKS
    public Flag inducedFlag(ArrayList<Integer> vertices){
        
        ArrayList<Integer> subGverts = new ArrayList<Integer>();
        
        for(Integer i: map){
            subGverts.add(i);
        }
        for(Integer i: vertices){
            subGverts.add(i);
        }
        
        
        
        ArrayList<Integer> nMap = new ArrayList<Integer>();
        
        for(int i = 0; i < map.size(); i++){
            nMap.add(i);
        }
        
        return new Flag(nMap,type.cloneGraph(),model.inducedSubgraph(subGverts));
    }
    
    public void changeType(ArrayList<Integer> newMap){
        for(int i = 0; i < model.getVertexSize(); i++){
            model.getVertex(i).setLabel(""); // CHECK THIS IS THE UNLABELED STRING
        }
        
        map = newMap;
        
        type = model.inducedSubgraph(newMap);
        
        for(int i = 0; i < newMap.size(); i++){
            model.getVertex(newMap.get(i)).setLabel(new Integer(i).toString());
        }
    }

    private void addVertex() {
        Vertex v = new Vertex();
        v.setLabel("");
        model.addVertex(v);
    }

    public int getSize() {
        return model.getVertexSize();
    }

    public void addEdge(ArrayList<Integer> edge) {
        model.addEdge(edge);
    }
}

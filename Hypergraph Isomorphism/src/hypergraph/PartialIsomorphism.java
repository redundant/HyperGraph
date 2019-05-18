/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypergraph;

import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author tnell
 */
public class PartialIsomorphism {
    private ArrayList<Integer> mapping;
    private ArrayList<Integer> unmapped;
    
    public PartialIsomorphism(int n){
        mapping = new ArrayList<Integer>();
        unmapped = new ArrayList<Integer>();
        
        for(int i = 0; i < n; i++){
            unmapped.add(i);
        }
    }
    
    public ArrayList<Integer> getMappedVertices(){
        return mapping;
    }
    
    public ArrayList<Integer> getUnmappedVertices(){
        return unmapped;
    }
    
    public void map(int i){
        mapping.add(i);
        unmapped.remove(new Integer(i));
    }
    
    public void unmap(int i){
        mapping.remove(new Integer(i));
        unmapped.add(i);
        Collections.sort(unmapped);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hypergraph;

import Flag.Flag;
import java.io.*;
import java.util.*;

/**
 *
 * @author tnell
 */
public class FlagParser {
    public static ArrayList<Flag> parse(String filename){
        
        ArrayList<Flag> flags = new ArrayList<Flag>();
        try {       
          
            BufferedReader inputStream = new BufferedReader(new FileReader(filename));
            while(inputStream.ready()){
                    String s = inputStream.readLine();
                    Hypergraph type = new Hypergraph();
                    ArrayList<Integer> map = new ArrayList<Integer>();
                    if(!s.equals("empty")){
                            String[] verts1 = s.split(",");
                            for(int i = 0; i < verts1.length; i++)
                                map.add(new Integer(verts1[i]));
                            s = inputStream.readLine();
                            Integer j = new Integer(s);

                            for(int i=0; i < j; i++){
                                Vertex v = new Vertex();
                                type.addVertex(v);
                            }

                            s = inputStream.readLine();

                        if(!s.equals("empty")){    
                            String[] edges = s.split(" ");

                            for(int i = 0; i < edges.length; i++){
                                String[] verts = edges[i].split(",");
                                ArrayList<Integer> vertices = new ArrayList<Integer>();

                                for(int k =0; k < verts.length; k++){
                                    vertices.add(new Integer(verts[k]));
                                }

                                type.addEdge(vertices);

                            }
                        }
                    }
                    

                    s = inputStream.readLine();
                    Hypergraph g = new Hypergraph();
                    Integer j = new Integer(s);

                    for(int i=0; i < j; i++){
                            Vertex v = new Vertex();
                            g.addVertex(v);
                    }


                    s = inputStream.readLine();
                    if(!s.equals("empty")){    
                        String[] edges = s.split(" ");

                        for(int i = 0; i < edges.length; i++){
                            String[] verts = edges[i].split(",");
                            ArrayList<Integer> vertices = new ArrayList<Integer>();

                            for(int k =0; k < verts.length; k++){
                                vertices.add(new Integer(verts[k]));
                            }

                            g.addEdge(vertices);

                        }
                    }
                    
                    inputStream.readLine();    
                    
                    flags.add(new Flag(map, type,g));
            }
		
            } catch (Exception e) {
			// TODO Auto-generated catch block
                    e.printStackTrace();
            }
        return flags;

        }
}


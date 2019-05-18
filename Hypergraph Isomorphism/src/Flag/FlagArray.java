/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;

import hypergraph.FlagParser;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Travis
 */
public class FlagArray {
    
    Flag[] currentFlags;
    Flag[] tail;
    String filename;
    int currentchunk;
    int endchunk;
    int chunksize;
    int currentend;
    int size;
    
    public FlagArray(String id, int size){
        currentFlags = new Flag[size];
        filename = id;
        currentchunk = -1;
        chunksize = size;
        currentend = 0;
        endchunk = 0;
        tail = new Flag[size];
        size = 0;
    }
    
    //You really want this to be sequentially accessed unless you hate yourself
    public Flag getFlag(int i){
        int chunknum = i/chunksize;
        
        if(chunknum == endchunk){
            return tail[i-chunksize*chunknum];
        }
        else if(chunknum== currentchunk){
            return currentFlags[i-chunksize*chunknum];
        }
        else{
            FlagParser.parse(filename+"-"+chunknum).toArray(currentFlags);
            System.out.println("Swapped Memory");
            currentchunk = chunknum;
            return currentFlags[i-chunksize*chunknum];
        }
        
    }
    public void addFlag(Flag f){
        if(currentend < chunksize){
            tail[currentend] = f;
            size++;
            currentend++;
        }
        else{
            currentend = 0;
            
            try {
                FileWriter fout = new FileWriter(filename+"-"+endchunk);
                PrintWriter out = new PrintWriter(fout);
                for(int i = 0; i < chunksize; i++){
                    tail[i].writeToFile(out);
                }
                    
                out.close();
            } 
            catch (Exception e) {
            }
            endchunk++;
            tail = new Flag[chunksize];
            tail[currentend] = f;
            size++;
            currentend++;
        }
        
    }
    
    public int getSize(){
        return size;
    }
}

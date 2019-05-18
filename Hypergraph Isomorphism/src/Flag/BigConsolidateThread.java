/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tnell
 */
/*public class BigConsolidateThread extends WorkThread{

    FlagArray firstpart;
    FlagArray secondpart;
    FlagArray consolidated;
    int threadnum;
    Integer name;
   
    
    public BigConsolidateThread(FlagArray first, FlagArray second, int numthreads, int id){
        threadnum = numthreads;
        firstpart = first;
        secondpart = second;  
        name = id;
    }
    
    @Override
    public FlagArray getResult() {
        return consolidated;
    }
    
    public int threadnum(){
        return threadnum;
    }

    @Override
    public void run() {
        
        int chunksize = (int)Math.ceil((double)secondpart.getSize()/(double)threadnum); // check this
        int numchunks = threadnum;
        
        ArrayList<FlagArray> chunks = new ArrayList<FlagArray>();
                
        for(int i = 0; i < numchunks; i++){
            ArrayList<Flag> temp = new ArrayList<Flag>();
            for(int j = 0; j < chunksize; j++){
                if(chunksize*i+j<secondpart.getSize()){
                    temp.add(secondpart.getFlag(chunksize*i+j));
                }
            }

            //chunks.add(temp);
        }
        threadnum = chunks.size();
        
        ArrayList<Thread> threads = new ArrayList<Thread>();
        ArrayList<ConsolidateThread> objects = new ArrayList<ConsolidateThread>();
        
        //for(ArrayList<Flag> f: chunks){
            //ConsolidateThread thread = new ConsolidateThread(firstpart, f,1000,name.toString());
            name++;
            Thread temp = new Thread(thread);
            temp.start();
            threads.add(temp);
            objects.add(thread);
        }
        
        for(Thread t: threads){
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(BigConsolidateThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(ConsolidateThread t: objects){
            ArrayList<Flag> temp = t.getResult();
            for(Flag f: temp){
                consolidated.addFlag(f);
            }
        }
    }
    
}
*/
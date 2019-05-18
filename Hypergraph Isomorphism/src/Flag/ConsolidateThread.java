/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;

/**
 *
 * @author tnell
 */
public class ConsolidateThread extends WorkThread{
    
    FlagArray secondpart;
    FlagArray firstpart;
    String name;
    
    public ConsolidateThread(FlagArray first, FlagArray second, String id){
        secondpart=second;
        firstpart=first;
        name = id;
    }
    
    @Override
    public void run() {
        int size = firstpart.getSize();
        for(int i = 0; i < secondpart.getSize(); i++){
            Flag f = secondpart.getFlag(i);
            boolean add = true;
            
            for(int j = 0; j < size; j++){ // anything added will be novel
                Flag g = firstpart.getFlag(j);
                if(f.isIMic(g)){
                    add = false;
                    break;
                }
            }
            
            if(add == true){
                firstpart.addFlag(f);
            }
            if(i%200 == 0){
                System.out.println(secondpart.getSize()-i+" Remaining in Thread "+ name);
            }
        }
    }
    
    @Override
    public FlagArray getResult(){
        return firstpart;
    }

    @Override
    public int threadnum() {
        return 1;
    }

    @Override
    public String getid() {
        return name;
    }
    
}

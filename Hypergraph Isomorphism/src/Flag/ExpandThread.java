/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author tnell
 */
public class ExpandThread extends WorkThread{

    ArrayList<Flag> toExpand;
    ArrayList<Flag> forbidden;
    FlagArray expanded;
    String name;
    int beg;
    int end;
    
    public ExpandThread(ArrayList<Flag> smallflags, ArrayList<Flag> forbiddenflags, int i, int j, int size, String id){
        toExpand = smallflags;
        forbidden = forbiddenflags;
        expanded = new FlagArray(id,size);
        name = id;
        beg = i;
        end = j;
    }
    
    @Override
    public void run() {
            
            Iterator<Flag> git = toExpand.listIterator();
            int size = toExpand.size();
            while (git.hasNext()) {
                ArrayList<Flag> temp = git.next().smartExpand(forbidden,beg,end);

                Iterator<Flag> tempit = temp.listIterator();
                while (tempit.hasNext()) {
                    Flag g = tempit.next();
                    
                    boolean quit = false;
                    
                    for(int i = 0; i < expanded.getSize(); i++){
                        if(expanded.getFlag(i).isIMic(g)){
                            quit = true;
                            
                            break;
                        }
                    }
                    
                    if (quit == false) {
                        expanded.addFlag(g);
                    }
                }
                System.out.println(--size+" Remaining in Thread "+ name);
            }

        }
    
    @Override
    public FlagArray getResult(){
        return expanded;
    }

    @Override
    public int threadnum() {
        return 1;
    }
    
    public String getid(){
        return name;
    }
}


import Flag.*;
import hypergraph.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args first parameter is type of computation i.e. expand, ...
     * 
     * 
     * expand, flags to expand, file to output them in, number of threads, size of cache, forbiddenflags
     */
    public static void main(String[] args) {
        if(args[0].contains("tio")){
            ArrayList<Flag> flags = FlagParser.parse(args[1]);
            
            for(Flag f: flags){
                System.out.println(f.getNumberofIsos()[0]);
            }
        }
        
        if(args[0].contains("victory")){
            ArrayList<Flag> flag = FlagParser.parse(args[1]);
            ArrayList<Flag> even = FlagParser.parse(args[2]);
            
            for(Flag f: flag){
                double result = 0;
                for(Flag g: even){
                    Integer[] temp = f.downwardAverage(g);
                    result+= (double)temp[0]/temp[1];
                }
                System.out.println(result);
            }
        }
        
        if (args[0].contains("product")) {
            ArrayList<Flag> flags = FlagParser.parse(args[1]);

            ArrayList<Flag> zeroflags = FlagParser.parse(args[2]);
            boolean mathematicaoutput = false;
            if (args[3].contains("math")) {
                mathematicaoutput = true;
            }

            /*try{
             FileWriter fout = new FileWriter(args[2]);
             PrintWriter out = new PrintWriter(fout);
             int i = 0;
             for(Flag zf: zeroflags){
             System.out.println(i++);
             Integer[] temp = new Integer[2];
             Integer[] temparr = zf.downwardAverage(flags.get(0));
             temp[0]=temparr[0];
             temp[1]=temparr[1];
             temparr = zf.downwardAverage(flags.get(2));
             temp[0]+=temparr[0];
             temparr = zf.downwardAverage(flags.get(4));
             temp[0]+=temparr[0];
             out.println(temp[0]+"/"+temp[1]);
             out.flush();
             }
             }
             catch(Exception e){
             // do nothing MUAHAHAHAHAHAH   
             }
             */

            // make the matrices you want
            ArrayList<ArrayList<ArrayList<Integer[]>>> matrix = new ArrayList<ArrayList<ArrayList<Integer[]>>>();
            for (Flag zf : zeroflags) {

                ArrayList<ArrayList<Integer[]>> temp1 = new ArrayList<ArrayList<Integer[]>>();
                for (Flag f : flags) {
                    ArrayList<Integer[]> temp2 = new ArrayList<Integer[]>();
                    for (Flag g : flags) {

                        temp2.add(zf.downAveragedProduct(f, g));
                    }
                    temp1.add(temp2);
                }
                matrix.add(temp1);
            }

            if (!mathematicaoutput) {
                int i = 1;
                for (ArrayList<ArrayList< Integer[]>> flag : matrix) {
                    try {

                        FileWriter fout = new FileWriter(args[2] + i++);
                        PrintWriter out = new PrintWriter(fout);
                        for (ArrayList<Integer[]> row : flag) { // with the flag will almost do mathematica output, but has one extra ,

                            for (int j = 0; j < row.size(); j++) {
                                Integer[] entry = row.get(j);
                                if (j != 0) {
                                    out.print(",");
                                }
                                out.print(entry[0] + "/" + entry[1]);

                            }
                            out.println("");
                            out.flush();
                        }

                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (mathematicaoutput) {
                try {
                    FileWriter fout = new FileWriter(args[3]);
                    PrintWriter out = new PrintWriter(fout);
                    for (int i = 0; i < matrix.size(); i++) {
                        ArrayList<ArrayList< Integer[]>> flag = matrix.get(i);
                        out.println("type5number" + i + "={");
                        for (int k = 0; k < flag.size(); k++) {
                            ArrayList<Integer[]> row = flag.get(k);
                            out.println("{");
                            for (int j = 0; j < row.size(); j++) {
                                Integer[] entry = row.get(j);
                                if (j != 0) {
                                    out.print(",");
                                }
                                out.print(entry[0] + "/" + entry[1]);

                            }
                            out.print("}");
                            if (k != flag.size() - 1) {
                                out.print(",");
                            }
                            out.println("");
                            out.flush();
                        }
                        out.println("}");

                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // This is the sanity check
                    /*(int k = 0;
             for(Flag zf: zeroflags){

             double i = 0;
             for(Flag f: flags)
             for(Flag g: flags){
             Integer[] temp = zf.downAveragedProduct(f, g);
             i += ((double)temp[0])/temp[1];
             }

             Flag f = flags.get(0).inducedFlag(new ArrayList<Integer>());
             Integer[] temp = zf.downAveragedProduct(f, f);
             double j = ((double)temp[0])/temp[1];

             if(Math.abs(i-j)>.00001){
             System.out.println("HOUSTON"+ k);
             break;
             }
             k++;

             }
             */





        }
        
        if(args[0].contains("test")){
            ArrayList<Flag> flags = FlagParser.parse(args[1]);
            ArrayList<Flag> forbidden = null;
            if(2<args.length){
                forbidden = FlagParser.parse(args[2]);
            }
            
            ArrayList<Flag> temp = flags.get(0).smartExpand(forbidden, 0, 1024);
            System.out.println(temp.size());
        }
        
        if(args[0].contains("search")){
            ArrayList<Flag> flags = FlagParser.parse(args[1]);

            ArrayList<Flag> forbidden = FlagParser.parse(args[2]);
            
            ArrayList<ArrayList<Integer> > possEdges = new ArrayList<ArrayList<Integer> >(); // going over 32 is bad news bears
            
            if(args.length > 3){
                try{
                    BufferedReader inputStream = new BufferedReader(new FileReader(args[3]));
                    
                    while(inputStream.ready()){

                        String s = inputStream.readLine();
                        String[] temp = s.split(" ");

                        for(String t : temp){
                        String[] ints = t.split(",");
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        for(String r: ints){
                            list.add(Integer.parseInt(r));
                        }
                        possEdges.add(list);
                        }
                    }
                   
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                ArrayList<Flag> temp = new ArrayList<Flag>();
                for(Flag f: flags){
                    for(int i = 0; i < Math.pow(2,possEdges.size()); i++){
                        Flag g = f.cloneFlag();
                        ArrayList<ArrayList<Integer> > edges = getEdges(possEdges,i);
                        for(ArrayList<Integer> edge: edges){
                            g.addEdge(edge);
                        }
                        temp.add(g);
                    }
                }
                flags = temp;
            }
            
            for(int i = 0; i < flags.size(); i++){
                boolean good = true;
                Flag f = flags.get(i);
                for(Flag g: forbidden){
                    if(f.hasSubgraph(g)){
                        good = false;
                        break;
                    }
                }
                if(good){
                    System.out.println("Flag "+i+" is "+ good);
                }
            }
        }
        
        if (args[0].contains("expand")) {
            try {
                //expand out collection of flag into the next size
                ArrayList<Flag> flags = FlagParser.parse(args[1]);
                FlagArray expanded;
                ArrayList<Flag> forbidden = null; // forbidden subgraph not working for flags
                Integer id = 0;
                
                if(5<args.length){
                    forbidden = FlagParser.parse(args[5]);
                }
                
                int numcores = Integer.parseInt(args[3]);
                
                int chunksize = (int)Math.ceil((double)flags.size()/(double)numcores); // check this
                int numchunks = (int)Math.ceil((double)flags.size()/(double)chunksize);
                
                ArrayList<ArrayList<Flag> > chunks = new ArrayList<ArrayList<Flag > >();
                
                for(int i = 0; i < numchunks; i++){
                    ArrayList<Flag> temp = new ArrayList<Flag>();
                    for(int j = 0; j < chunksize; j++){
                        if(chunksize*i+j<flags.size()){
                            temp.add(flags.get(chunksize*i+j));
                        }
                    }
                    
                    chunks.add(temp);
                }
                
                ArrayList<Thread> threads = new ArrayList<Thread>();
                ArrayList<WorkThread> objects = new ArrayList<WorkThread>();
                int flagsize = flags.get(0).getSize();
                for(ArrayList<Flag> f: chunks){
                    WorkThread thread = (WorkThread) new ExpandThread(f,forbidden, 0, (int)Math.pow(2,flagsize*(flagsize-1)/2),Integer.parseInt(args[4]), id.toString());
                    System.out.println("Starting Expand Thread with id " + id);
                    id++;
                    objects.add(thread);
                    Thread temp = new Thread(thread);
                    threads.add(temp);
                    temp.start();
                }
                //int activethreads = threads.size();
                Thread.sleep(2000); // make sure all the threads come to life before I begin polling. I should fix that to use callbacks later
                ArrayList<Integer> done = new ArrayList<Integer>();
                while(threads.size()>1){
                    try {
                        
                        while(done.size()<2){
                            Thread.sleep(600);
                            for(int i = 0; i < threads.size(); i++){
                                if(done.size() >1){
                                    break;
                                }
                                if(! done.contains(i)){
                                    if(!threads.get(i).isAlive()) {
                                        done.add(i);
                                    }
                                }
                            }
                        }
                        
                        Collections.sort(done);
                        
                        //threads.get(0).join();
                        //threads.get(1).join();
                        //activethreads = activethreads - objects.get(0).threadnum() - objects.get(1).threadnum();
                        //int maxnewthreads = numcores - activethreads;
                        //FlagArray tflags = objects.get(1).getResult();
                        //chunksize = (int)Math.ceil((double)tflags.getSize()/(double)maxnewthreads); // not used in the same way as before, but similarly
                        //numchunks = (int)Math.ceil((double)tflags.getSize()/(double)chunksize);
                        //int newthreads = Math.min(numchunks, (int)Math.ceil(3.5*(double)maxnewthreads/(double)(threads.size()-1)));//try to be uniform in number of threads
                        //activethreads = activethreads + newthreads;
                        //System.out.println("New job started with "+newthreads +" threads");
                        //System.out.println(numcores-activethreads + " many open cores");
                        System.out.println("Starting Consolidate Thread of " +objects.get(done.get(0)).getid()+" "+objects.get(done.get(1)).getid());
                        WorkThread thread = (WorkThread) new ConsolidateThread(objects.get(done.get(0)).getResult(),objects.get(done.get(1)).getResult(),Integer.toString(id));
                        id++;
                        int tempint = done.get(0);
                        threads.remove(tempint);
                        threads.remove(done.get(1)-1);// everything is shifted by 1
                        objects.remove(tempint);
                        objects.remove(done.get(1)-1);// ditto
                        done.remove(0);
                        done.remove(0);
                        Thread temp = new Thread(thread);
                        threads.add(temp);
                        objects.add(thread);
                        temp.start();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                threads.get(0).join();
                expanded = objects.get(0).getResult();
                
                
                /*while (git.hasNext()) {
                    ArrayList<Flag> temp = git.next().expand();
                    Iterator<Flag> tempit = temp.listIterator();
                    while (tempit.hasNext()) {
                        Flag g = tempit.next();
                        
                        Flag t = g.cloneFlag(); 
                        t.changeType(new ArrayList<Integer>()); // get the underlying zero flag
                        
                        boolean quit = false;
                        
                        if(forbidden !=null){
                            for(Flag f: forbidden){
                                if(t.downwardAverage(f)[0]!=0)
                                    quit=true;
                            }
                        }

                        Iterator<Flag> expit = expanded.listIterator();

                        while (expit.hasNext() && !quit) {

                            if (expit.next().isIMic(g)) {
                                quit = true;
                            }
                        }
                        if (quit == false) {
                            expanded.add(g);
                        }
                    }

                }*/
                System.out.println(expanded.getSize());
                try {
                    FileWriter fout = new FileWriter(args[2]);
                    PrintWriter out = new PrintWriter(fout);
                    for(int i = 0; i < expanded.getSize(); i++){
                        Flag f = expanded.getFlag(i);
                        f.writeToFile(out);
                    }
                    out.close();
                } catch (Exception e) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        if(args[0].contains("selfcomp")){
            ArrayList<Flag> flag = FlagParser.parse(args[1]);
            ArrayList<Flag> forbidden = FlagParser.parse(args[2]);
            ArrayList<Flag> even = FlagParser.parse(args[3]);
            
            ArrayList<Flag> best = new ArrayList<Flag>(); // contains current acheivers of best value.
            best.add(flag.get(0));
            Integer[] optimal = new Integer[2];
            optimal[0]=1;
            optimal[1]=1;
            for(Flag f: flag){
                ArrayList<Flag> temp = f.smartExpand(forbidden, 0, (int)Math.pow(2,f.getSize()*((double)f.getSize()-1)/2));
                for(Flag g: temp){
                    Integer[] density = new Integer[2];
                    density[0]=0;
                    density[1]=1;
                    for(Flag h: even){
                        density = addRational(density, g.downwardAverage(h));          
                    }
                    if(compareRational(density,optimal)){
                            if(g.isIMic(g.Complement())){
                                optimal = density;
                                best = new ArrayList<Flag>();
                                best.add(g);
                            }
                    }
                    else if (equalRational(density,optimal)){
                        if(g.isIMic(g.Complement())){
                            best.add(g);
                        }
                    }
                }
            }
            
            System.out.println((double)optimal[0]/optimal[1]);
            
            try {
                FileWriter f = new FileWriter("outputlolz");
                PrintWriter out = new PrintWriter(f);
                
                for(Flag g: best){
                    g.writeToFile(out);
                }
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(args[0].contains("optimize")){
            ArrayList<Flag> flag = FlagParser.parse(args[1]);
            ArrayList<Flag> forbidden = FlagParser.parse(args[2]);
            ArrayList<Flag> even = FlagParser.parse(args[3]);
            
            ArrayList<Flag> best = new ArrayList<Flag>(); // contains current acheivers of best value.
            best.add(flag.get(0));
            Integer[] optimal = new Integer[2];
            optimal[0]=1;
            optimal[1]=1;
            
            Integer[] autorat = new Integer[2];
            autorat[0] = 1;
            autorat[1] = 1;
            for(Flag f: flag){
                ArrayList<Flag> temp = f.smartExpand(forbidden, 0, (int)Math.pow(2,f.getSize()*((double)f.getSize()-1)/2));
                for(Flag g: temp){
                    Integer[] density = new Integer[2];
                    density[0]=0;
                    density[1]=1;
                    
                    Integer[] numAuto = g.getNumberofIsos();
                    
                    for(Flag h: even){
                        density = addRational(density, g.downwardAverage(h));          
                    }
                    
                    /**/
                    if(g.isIMic(g.Complement())){
                    /**/
                    if(compareRational(density,optimal)){
                            
                            optimal = density;
                            autorat = numAuto;
                            best = new ArrayList<Flag>();
                            best.add(g);
                            
                    }
                    
                    if(compareRational(autorat,numAuto) && equalRational(density,optimal)){
                        best = new ArrayList<Flag>();
                        autorat = numAuto;
                        best.add(g);
                    }
                    
                    else if (equalRational(density,optimal) && equalRational(numAuto,autorat)){
                        best.add(g);
                    }
                    /**/}/**/
                }
            }
            
            System.out.println((double)optimal[0]/optimal[1]);
            System.out.println((double) autorat[0]/autorat[1]);
            try {
                FileWriter f = new FileWriter("outputlolz");
                PrintWriter out = new PrintWriter(f);
                
                for(Flag g: best){
                    g.writeToFile(out);
                }
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static Integer[] addRational(Integer[] first, Integer[] second){
        Integer[] temp = new Integer[2];
        temp[1] = first[1]*second[1];
        temp[0] = first[0]*second[1]+first[1]*second[0];
        
        return temp;
    }
    
    private static boolean compareRational(Integer[] first, Integer[] second){
        return first[0]*second[1]<second[0]*first[1];
    }
    
    private static boolean equalRational(Integer[] first, Integer[] second){
        return first[0]*second[1]==second[0]*first[1];
    }
    
    
    private static ArrayList<ArrayList<Integer> > getEdges(ArrayList<ArrayList<Integer> > edges,int n){
        ArrayList<ArrayList<Integer> > temp = new ArrayList<ArrayList<Integer> >();
        
        for(int i = 0; i < edges.size(); i++){
            if(((n >> i)& 1) == 1){
                temp.add(edges.get(i));
                
            }
        }
        return temp;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Flag;


/**
 *
 * @author tnell
 */
public abstract class WorkThread implements Runnable{
    public abstract FlagArray getResult();
    @Override
    public abstract void run();
    public abstract int threadnum();
    public abstract String getid();
}

package pacman;

/**

 @author holliefuller
 */
public interface Subject {

    //basic setup for Subject portion of observer DP
    public void register(Observer obj);

    public void unregister(Observer obj);

    public void notifyObservers();

    public Object getUpdate(Observer obj);

}


import java.util.Vector;
import java.util.List;
 
public class Queue {
	final private List v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new Vector();
	}
	
	public Object next() {
		return v.remove(0);
	}

	public void add(Object o) {
		v.add(o);
	}
	
	public boolean hasMoreElements() {
		return (!v.isEmpty());
	}

	public List asVector() {
		return v;
	}
	
}

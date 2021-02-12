/* Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.Vector;
import java.util.List;
 
public class Queue {
	private List v;
	
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
		return v.size() != 0;
	}

	public List asVector() {
		return v;
	}
	
}

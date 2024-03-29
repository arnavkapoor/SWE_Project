
/**
 * Class that represents control desk
 *
 */

import org.apache.log4j.Logger;

import java.util.*;
import java.io.*;

class ControlDesk extends Thread {
	static Logger log = Logger.getLogger(ControlDesk.class.getName());
	/** The collection of Lanes */
	final private Set lanes;

	/** The party wait queue */
	final private Queue partyQueue;

	/** The number of lanes represented */
	final private int numLanes;
	
	/** The collection of subscribers */
	final private List subscribers;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numlanes	the numbler of lanes to be represented
     *
     */

	public ControlDesk(int numLanes, int framesAllowed, boolean tieBreakerAllowed) {
		this.numLanes = numLanes;
		lanes = new HashSet(numLanes);
		partyQueue = new Queue();

		subscribers = new Vector();

		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane(framesAllowed, tieBreakerAllowed));
		}
		
		this.start();

	}
	
	/**
	 * Main loop for ControlDesk's thread
	 * 
	 */
	@Override public void run() {
		while (true) {
			
			assignLane();
			
			try {
				sleep(250);
			} catch (Exception e) {}
		}	
	}
		

    /**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName	The NickName of the Bowler
     *
     * @return a Bowler object.
     *
     */

	private Bowler registerPatron(String nickName) {
		Bowler patron = null;

		try {
			// only one patron / nick.... no dupes, no checks

			patron = BowlerFile.getBowlerInfo(nickName);

		} catch (FileNotFoundException e) {
			log.error("Error..." + e);
		} catch (IOException e) {
			log.error("Error..." + e);
		}

		return patron;
	}

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

	public void assignLane() {
		Iterator it = lanes.iterator();

		while (it.hasNext() && partyQueue.hasMoreElements()) {
			Lane curLane = (Lane) it.next();

			if (!curLane.isPartyAssigned()) {
				log.info("ok... assigning this party");
				curLane.assignParty(((Party) partyQueue.next()));
			}
		}
		publish(new ControlDeskEvent(getPartyQueue()));
	}

    /**
     */

	public void viewScores(Lane ln) {
		// TODO: attach a LaneScoreView object to that lane
	}

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

	public void addPartyQueue(List partyNicks) {
		List partyBowlers = new Vector();
		for (int i = 0; i < partyNicks.size(); i++) {
			Bowler newBowler = registerPatron(((String) partyNicks.get(i)));
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);
		partyQueue.add(newParty);
		publish(new ControlDeskEvent(getPartyQueue()));
	}

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
     * @return a Vecotr of Strings
     *
     */

	public List getPartyQueue() {
		List displayPartyQueue = new Vector();
		for ( int i=0; i < ( (List)partyQueue.asVector()).size(); i++ ) {
			String nextParty =
				((Bowler) ((List) ((Party) partyQueue.asVector().get( i ) ).getMembers())
					.get(0))
					.getNickName() + "'s Party";
			displayPartyQueue.add(nextParty);
		}
		return displayPartyQueue;
	}

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     * 
     * @return an int containing the number of lanes represented
     *
     */

	public int getNumLanes() {
		return numLanes;
	}

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}

    /**
     * Broadcast an event to subscribing objects.
     * 
     * @param event	the ControlDeskEvent to broadcast
     *
     */

	public void publish(ControlDeskEvent event) {
		Iterator eventIterator = subscribers.iterator();
		while (eventIterator.hasNext()) {
			(
				(ControlDeskObserver) eventIterator
					.next())
					.receiveControlDeskEvent(
				event);
		}
	}

    /**
     * Accessor method for lanes
     * 
     * @return a HashSet of Lanes
     *
     */

	public Set getLanes() {
		return lanes;
	}
}

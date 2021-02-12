
/**
 *  Container that holds bowlers
 *
 */

import java.util.*;

public class Party {

	/** Vector of bowlers in this party */	
    final private List myBowlers;
	
	/**
	 * Constructor for a Party
	 * 
	 * @param bowlers	Vector of bowlers that are in this party
	 */
		
    public Party( List bowlers ) {
		myBowlers = new Vector(bowlers);
    }

	/**
	 * Accessor for members in this party
	 * 
	 * @return 	A vector of the bowlers in this party
	 */

    public List getMembers() {
		return myBowlers;
    }

}

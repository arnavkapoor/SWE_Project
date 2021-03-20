
import java.util.*;
import java.util.Random;


import org.apache.log4j.Logger;


public class Lane extends Thread implements PinsetterObserver {	
	private Party party;
	final private Pinsetter setter;
	final private Map scores;
	final private List subscribers;

	private boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;
	private boolean extraChance;

	private int[] curScores;
	private int[][] cumulScores;
	private boolean canThrowAgain;
	
	private int[][] finalScores;
	private int gameNumber;
	private boolean tieBreakerAllowed;
	private int framesAllowed;
	
	private int highestPlayer;
	private int secondHighestPlayer;

	private int penalizeNextFrame;

	private Bowler currentThrower;			// = the thrower who just took a throw
	static Logger log = Logger.getLogger(Lane.class.getName());

	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane(int fa, boolean tba) { 
		setter = new Pinsetter();
		scores = new HashMap();
		subscribers = new Vector();

		gameIsHalted = false;
		partyAssigned = false;
		extraChance = false;

		gameNumber = 0;
		penalizeNextFrame = 0;
		framesAllowed = fa - 1;
		tieBreakerAllowed = tba;


		setter.subscribe( this );
		
		this.start();
	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	@Override public void run() {
		
		while (true) {
			if (partyAssigned && !gameFinished) {	// we have a party on this lane, 
								// so next bower can take a throw
			
				while (gameIsHalted) {
					try {
						sleep(10);
					} catch (Exception e) {}
				}


				if (bowlerIterator.hasNext()) {
					currentThrower = (Bowler)bowlerIterator.next();

					canThrowAgain = true;
					tenthFrameStrike = false;
					ball = 0;
					while (canThrowAgain) {
						setter.ballThrown(currentThrower);		// simulate the thrower's ball hitting
						ball++;
					}
					
					if (frameNumber == framesAllowed){
						finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][framesAllowed];
						try{
							Calendar calendar = Calendar.getInstance();

							String dateString = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + " " +
									calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
									calendar.get(Calendar.YEAR);

							ScoreHistoryFile.addScore(currentThrower.getNick(), dateString, Integer.toString(cumulScores[bowlIndex][framesAllowed]));
						} catch (Exception e) {log.error("Exception in addScore. "+ e );}
					}

					
					setter.reset();
					bowlIndex++;
					
				} else {
					frameNumber++;
					resetBowlerIterator();
					bowlIndex = 0;
					if (frameNumber > framesAllowed) {
						gameFinished = true;
						gameNumber++;
					}
				}
			} else if (partyAssigned && gameFinished) {
				if(tieBreakerAllowed){
					handleEndGame();
				}
				EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
				int result = egp.getResult();
				egp.distroy();
				egp = null;

				
				log.error("result was: " + result);
				
				if (result == 1) {					// yes, want to play again
					resetScores();
					resetBowlerIterator();
					
				} else if (result == 2) {// no, dont want to play another game
					List printVector;
					EndGameReport egr = new EndGameReport( ((Bowler)party.getMembers().get(0)).getNickName() + "'s Party", party);
					printVector = egr.getResult();
					partyAssigned = false;
					Iterator scoreIt = party.getMembers().iterator();
					party = null;
					partyAssigned = false;
					
					publish(lanePublish());
					
					int myIndex = 0;
					while (scoreIt.hasNext()){
						Bowler thisBowler = (Bowler)scoreIt.next();
						ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
						sr.sendEmail(thisBowler.getEmail());
						for (Object o : printVector) {
							if (thisBowler.getNick().equals((String) o)) {
								log.error("Printing " + thisBowler.getNick());
								sr.sendPrintout();
							}
						}

					}
				}
			}
			
			
			try {
				sleep(10);
			} catch (Exception e) {}
		}
	}

	private void handleEndGame() {

		int totalBowlers = party.getMembers().size();
		int max = 0, smax = 0, sind = 0, find = 0, i;

		// findSecondHighestPlayer()
		for(i=0; i<totalBowlers; i++) {
			if(cumulScores[i][9]>max) {
				smax = max;
				sind = find;
				max = cumulScores[i][9];
				find = i;
			}
			else if(cumulScores[i][9]>smax) {
				smax = cumulScores[i][9];
				sind = i;
			}
		}

		highestPlayer = find;
		secondHighestPlayer = sind;

		extraChance = true;
		for(i=0; i<sind; i++) {
			currentThrower = (Bowler)bowlerIterator.next();
		}
		Random rand = new Random();
		int randomNum = rand.nextInt((10 - 5) + 1) + 5;
		handleExtraChance(randomNum);
	}

	private void handleExtraChance(int pinsDown) {
		int highestScore = cumulScores[highestPlayer][9];
		int secondHighestScore = cumulScores[secondHighestPlayer][9] + pinsDown;


		if(highestScore <= secondHighestScore) {
        //if(true) {
			Vector bowlers = new Vector(party.getMembers());
			Vector partyNicks = new Vector();
			partyNicks.add(((Bowler) bowlers.get(highestPlayer)).getNickName());
			partyNicks.add(((Bowler) bowlers.get(secondHighestPlayer)).getNickName());


			ControlDesk newControlDesk = new ControlDesk(1, 3, false);
			ControlDeskView newCDV = new ControlDeskView( newControlDesk, 2);
			newControlDesk.subscribe( newCDV );

			newControlDesk.addPartyQueue(partyNicks);
		} else {
			System.out.println("The second highest player is not able to cross the highest player");
			System.out.println("So the game ends here");
		}
		return ;
	}
	
	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	@Override public void receivePinsetterEvent(PinsetterEvent pe) {
		
			if (pe.pinsDownOnThisThrow() >=  0) {            // this is a real throw
				markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

				// next logic handles the ?: what conditions dont allow them another throw?
				// handle the case of 10th frame first
				if (frameNumber == 9) {
					if (pe.totalPinsDown() == 10) {
						setter.resetPins();
						if (pe.getThrowNumber() == 1) {
							tenthFrameStrike = true;
						}
					}

					if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && !(tenthFrameStrike))) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}

					if (pe.getThrowNumber() == 3) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				} else { // its not the 10th frame

					if (pe.pinsDownOnThisThrow() == 10) {        // threw a strike
						canThrowAgain = false;
						//publish( lanePublish() );
					} else if (pe.getThrowNumber() == 2) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				}
			}
			else {
				return; //  this is not a real throw, probably a reset
			}
	}
	
	/** resetBowlerIterator()
	 * 
	 * sets the current bower iterator back to the first bowler
	 * 
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	private void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 * 
	 * resets the scoring mechanism, must be called before scoring starts
	 * 
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	private void resetScores() {

		for (Object o : party.getMembers()) {
			int[] toPut = new int[25];
			for (int i = 0; i != 25; i++) {
				toPut[i] = -1;
			}
			scores.put(o, toPut);
		}
		
		
		
		gameFinished = false;
		frameNumber = 0;
	}
		
	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;
		
		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128];
		gameNumber = 0;
		
		resetScores();
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = (int[]) scores.get(Cur);

	
		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		getScore( Cur, frame );
		publish( lanePublish() );
	}

	void handleGutter(int[] curScore, int i, int current){
		int mx = -10;
		for(int j=0;j < i;j++) mx = Math.max(mx, curScore[j]);
		if(curScore[i-1] == 0 && curScore[i] == 0){
			if(i == 1) {
				// penalise 1/2 points of next frame
				penalizeNextFrame = 1;
			}
			else if(mx > 0){
				// penalise 1/2 points of highest score
				cumulScores[bowlIndex][i/2] -= (mx/2);
			}
		}
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return		The new lane event
	 */
	private LaneEvent lanePublish(  ) {
		return new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber+1, curScores, ball, gameIsHalted);
	}

	/** getScore()
	 *
	 * Method that calculates a bowlers score
	 * 
	 * @param Cur		The bowler that is currently up
	 * @param frame	The frame the current bowler is on
	 * 
	 * @return			The bowlers total score
	 */
	private int getScore( Bowler Cur, int frame) {
		int[] curScore;

		int strikeballs = 0;
		int totalScore = 0;

		curScore = (int[]) scores.get(Cur);
		for (int i = 0; i != 10; i++){
			cumulScores[bowlIndex][i] = 0;
		}


		int current = 2*(frame - 1)+ball-1;
		//Iterate through each ball until the current one.

		for (int i = 0; i != current+2; i++) {
			// If the first 2 consecutive times
			//are at the start of the game, player would be penalized 1/2 of the points that is scored in
			//the next frame.
			if(penalizeNextFrame == 1){
				penalizeNextFrame = 0;
				int mx = -10;
				for(int j=0;j < i;j++) mx = Math.max(mx, curScore[j]);
				if(mx > 0) {
					cumulScores[bowlIndex][i/2] -= (mx/2);
				}
			}

			// Check if 2 consecutive gutters are bowled
			if(i%2 == 1) handleGutter(curScore, i, current);
			//Spare:
			if (i % 2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19) {
				//This ball was a the second of a spare.
				//Also, we're not on the current ball.
				//Add the next ball to the ith one in cumul.
				cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
			} else if (i < current && i % 2 == 0 && curScore[i] == 10 && i < 18) {
				strikeballs = 0;
				//This ball is the first ball, and was a strike.
				//If we can get 2 balls after it, good add them to cumul.
				if (curScore[i + 3] != -1 || curScore[i + 4] != -1) {
					strikeballs = 2;

					cumulScores[bowlIndex][i / 2] += 10;
					if (i / 2 > 0) {
						cumulScores[bowlIndex][i / 2] += curScore[i + 2] + cumulScores[bowlIndex][(i / 2) - 1];
					} else {
						cumulScores[bowlIndex][i / 2] += curScore[i + 2];
					}

					if (curScore[i + 3] != -1) {
						cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
					} else {
						cumulScores[bowlIndex][(i / 2)] += curScore[i + 4];
					}
				} else {
					break;
				}
			} else {
				//We're dealing with a normal throw, add it and be on our way.
				if (i < 18) {
					if (i / 2 == 0 || i % 2 == 1) {
						if (curScore[i] != -1)
							cumulScores[bowlIndex][i / 2] += curScore[i];
					} else {
						if (curScore[i] != -1) {
							cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1] + curScore[i];
						} else {
							cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1];
						}
					}
				} else {
					if (i == 18) {
						cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
					}
					cumulScores[bowlIndex][9] += curScore[i];
				}
			}
		}

		return totalScore;
	}




	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}
	
	/** isGameFinished
	 * 
	 * @return true if the game is done, false otherwise
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param subscribe	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** unsubscribe
	 * 
	 * Method that unsubscribes an observer from this object
	 * 
	 * @param removing	The observer to be removed
	 */
	
	public void unsubscribe( LaneObserver removing ) {
		subscribers.remove( removing );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

	public void publish( LaneEvent event ) {
		if(  !(subscribers.isEmpty())) {
			Iterator eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		publish(lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish(lanePublish());
	}

}

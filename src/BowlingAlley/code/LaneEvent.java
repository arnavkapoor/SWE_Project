import java.util.Map;

public class LaneEvent {

	final private Party p;
	int frame;
	int ball;
	Bowler bowler;
	int[][] cumulScore;
	Map score;
	int index;
	int frameNum;
	int[] curScores;
	boolean mechProb;
	
	public LaneEvent(Party pty, int theIndex, Bowler theBowler, int[][] theCumulScore, Map theScore, int theFrameNum, int[] theCurScores, int theBall, boolean mechProblem) {
		p = pty;
		index = theIndex;
		bowler = theBowler;
		cumulScore = theCumulScore;
		score = theScore;
		curScores = theCurScores;
		frameNum = theFrameNum;
		ball = theBall;	
		mechProb = mechProblem;
	}
	
	public boolean isMechanicalProblem() {
		return mechProb;
	}
	
	public int getFrameNum() {
		return frameNum;
	}
	
	public Map getScore( ) {
		return score;
	}


	public int[] getCurScores(){ 
		return curScores;
	}
	
	public int getIndex() {
		return index;
	}

	public int getFrame( ) {
		return frame;
	}

	public int getBall( ) {
		return ball;
	}
	
	public int[][] getCumulScore(){
		return cumulScore;
	}

	public Party getParty() {
		return p;
	}
	
	public Bowler getBowler() {
		return bowler;
	}

};
 

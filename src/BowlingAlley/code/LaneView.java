/*
 *  constructs a prototype Lane View
 *
 */

import org.apache.log4j.Logger;
import com.formdev.flatlaf.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.UIManager;
public class LaneView implements LaneObserver, ActionListener {

	//private int roll;


	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;

	JButton maintenance;
	Lane lane;
	static Logger log = Logger.getLogger(LaneView.class.getName());
	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {


		initDone = false;
		bowlers = (Vector) party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}
		GetEmoticonFactory emotefact = new GetEmoticonFactory();
		Emoticon maleemoticon = emotefact.getEmote("Male");
		Emoticon femaleemoticon = emotefact.getEmote("Female");
		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			if(((Bowler) bowlers.get(i)).getGender().equalsIgnoreCase("M")) {
				pins[i].setBorder(
						BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
								((Bowler) bowlers.get(i)).getNick()));
			}
			else if (((Bowler) bowlers.get(i)).getGender().equalsIgnoreCase("F")){
				pins[i].setBorder(
						BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
								((Bowler) bowlers.get(i)).getNick()));
			}
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel(" ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	@Override public void receiveLaneEvent(LaneEvent le) {
		if (lane.isPartyAssigned()) {
			int numBowlers = le.getParty().getMembers().size();
			while (!initDone) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}
			if (le.getFrameNum() == 1
				&& le.getBall() == 0
				&& le.getIndex() == 0) {
				log.info("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");
				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);

				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");

				frame.pack();

			}

			int[][] lescores = le.getCumulScore();
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= le.getFrameNum() - 1; i++) {
					if (lescores[k][i] != 0)
						scoreLabel[k][i].setText(
							(Integer.valueOf(lescores[k][i])).toString());
				}
				for (int i = 0; i < 21; i++) {
					GetEmoticonFactory emotefact = new GetEmoticonFactory();
					if (((int[]) ((HashMap) le.getScore())
						.get(bowlers.get(k)))[i]
						!= -1)
						if (((int[]) ((HashMap) le.getScore())
							.get(bowlers.get(k)))[i]
							== 10
							&& (i % 2 == 0 || i == 19)) {
							ballLabel[k][i].setText("X"); // strike
							Emoticon happyemoticon = emotefact.getEmote("HAPPY");
							Emoticon appemoticon = emotefact.getEmote("APPRECIATE");
							Emoticon envyemoticon = emotefact.getEmote("ENVY");
							Emoticon maleemoticon = emotefact.getEmote("Male");
							Emoticon femaleemoticon = emotefact.getEmote("Female");
							if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("M")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + happyemoticon.getEmoticon()));
							}
							else if (((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("F")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + happyemoticon.getEmoticon()));
							}
							for(int z=k+1; z < numBowlers; z++) {
								if(((Bowler) le.getParty().getMembers().get(z)).getGender().equalsIgnoreCase("M")) {
									pins[z].setBorder(
											BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
													((Bowler) le.getParty().getMembers().get(z)).getNick() + appemoticon.getEmoticon()));
								}
								else if(((Bowler) le.getParty().getMembers().get(z)).getGender().equalsIgnoreCase("F")) {
									pins[z].setBorder(
											BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
													((Bowler) le.getParty().getMembers().get(z)).getNick() + appemoticon.getEmoticon()));
								}
							}

							for(int z=0; z < k; z++) {
								if(((Bowler) le.getParty().getMembers().get(z)).getGender().equalsIgnoreCase("M")) {
									pins[z].setBorder(
											BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
													((Bowler) le.getParty().getMembers().get(z)).getNick() + envyemoticon.getEmoticon()));
								}
								else if(((Bowler) le.getParty().getMembers().get(z)).getGender().equalsIgnoreCase("F")) {
									pins[z].setBorder(
											BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
													((Bowler) le.getParty().getMembers().get(z)).getNick() + envyemoticon.getEmoticon()));
								}
							}
						}
						else if (
							i > 0
								&& ((int[]) ((HashMap) le.getScore())
									.get(bowlers.get(k)))[i]
									+ ((int[]) ((HashMap) le.getScore())
										.get(bowlers.get(k)))[i
									- 1]
									== 10
								&& i % 2 == 1) {
							ballLabel[k][i].setText("/");
							Emoticon happyemoticon = emotefact.getEmote("HAPPY");
							Emoticon maleemoticon = emotefact.getEmote("Male");
							Emoticon femaleemoticon = emotefact.getEmote("Female");
							if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("M")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + happyemoticon.getEmoticon()));
							}
							else if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("F") ) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + happyemoticon.getEmoticon()));
							}
						}
						else if (((int[]) ((HashMap) le.getScore())
								.get(bowlers.get(k)))[i]
								< 4) {
							ballLabel[k][i].setText(
									(Integer.valueOf(((int[]) ((HashMap) le.getScore())
											.get(bowlers.get(k)))[i]))
											.toString());
							Emoticon embarrassemoticon = emotefact.getEmote("EMBARRASS");
							Emoticon maleemoticon = emotefact.getEmote("Male");
							Emoticon femaleemoticon = emotefact.getEmote("Female");
							if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("M")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + embarrassemoticon.getEmoticon()));
							}
							else if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("F")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + embarrassemoticon.getEmoticon()));
							}
						}
						else if ( ((int[])((HashMap) le.getScore()).get(bowlers.get(k)))[i] == -2 ) {
							ballLabel[k][i].setText("F"); // check extensively and remove it.
						} else {
							ballLabel[k][i].setText(
									(Integer.valueOf(((int[]) ((HashMap) le.getScore())
											.get(bowlers.get(k)))[i]))
											.toString());
							Emoticon normalemoticon = emotefact.getEmote("NORMAL");
							Emoticon maleemoticon = emotefact.getEmote("Male");
							Emoticon femaleemoticon = emotefact.getEmote("Female");
							if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("M")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(maleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + normalemoticon.getEmoticon()));
							}
							else if(((Bowler) le.getParty().getMembers().get(k)).getGender().equalsIgnoreCase("F")) {
								pins[k].setBorder(
										BorderFactory.createTitledBorder(femaleemoticon.getEmoticon() +
												((Bowler) le.getParty().getMembers().get(k)).getNick() + normalemoticon.getEmoticon()));
							}
						}
				}
			}

		}
	}

	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
	}

}

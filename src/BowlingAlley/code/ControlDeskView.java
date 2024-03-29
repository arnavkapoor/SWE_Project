
/**
 * Class for representation of the control desk
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

	private JButton addParty;
	private JButton finished;
	private JButton assign;
	private JButton scoreHistory;
	private JFrame win;
	private JList partyList;
	
	/** The maximum  number of members in a party */
	final private int maxMembers;
	
	final private ControlDesk controlDesk;

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */
	private void setWindow() {
		win = new JFrame("Control Desk");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
	}

	private JPanel setPanel() {
		int numLanes = controlDesk.getNumLanes();
		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Controls Panel
		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new GridLayout(3, 1));
		controlsPanel.setBorder(new TitledBorder("Controls"));

		addParty = new JButton("Add Party");
		JPanel addPartyPanel = new JPanel();
		addPartyPanel.setLayout(new FlowLayout());
		addParty.addActionListener(this);
		addPartyPanel.add(addParty);
		controlsPanel.add(addPartyPanel);

		scoreHistory = new JButton("Score History");
		JPanel scoreHistoryPanel = new JPanel();
		scoreHistoryPanel.setLayout(new FlowLayout());
		scoreHistory.addActionListener(this);
		scoreHistoryPanel.add(scoreHistory);
		controlsPanel.add(scoreHistoryPanel);


		// assign = new JButton("Assign Lanes");
		// JPanel assignPanel = new JPanel();
		// assignPanel.setLayout(new FlowLayout());
		// assign.addActionListener(this);
		// assignPanel.add(assign);
		// controlsPanel.add(assignPanel);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);
		controlsPanel.add(finishedPanel);

		// Lane Status Panel
		JPanel laneStatusPanel = new JPanel();
		laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
		laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

		Set lanes = controlDesk.getLanes();
		Iterator it = lanes.iterator();
		int laneCount=0;
		while (it.hasNext()) {
			Lane curLane = (Lane) it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);
			((Pinsetter)curLane.getPinsetter()).subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Queue"));

		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		//		partyPanel.add(partyList);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");
		return colPanel;
	}

	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;

		setWindow();


		JPanel colPanel = setPanel();

		win.getContentPane().add("Center", colPanel);

		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	/**
	 * Handler for actionEvents
	 *
	 * @param e	the ActionEvent that triggered the handler
	 *
	 */

	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addParty)) {
			AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
		}
		// if (e.getSource().equals(assign)) {
		// 	controlDesk.assignLane();
		// }
		if (e.getSource().equals(finished)) {
			win.hide();
			System.exit(0);
		}
		if (e.getSource().equals(scoreHistory)) {
			AddScoreHistoryView addscores = new AddScoreHistoryView(this);
			// win.hide();
			// System.exit(0);
			System.out.println("OK");
		}

	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue((Vector) addPartyView.getParty());
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param ce	the ControlDeskEvent that triggered the handler
	 *
	 */

	@Override public void receiveControlDeskEvent(ControlDeskEvent ce) {
		partyList.setListData(((Vector) ce.getPartyQueue()));
	}
}

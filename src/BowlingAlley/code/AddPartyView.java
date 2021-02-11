/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.List;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	final private int maxSize;

	private JFrame win;
	private JButton addPatron;
	private JButton	newPatron;
	private JButton remPatron;
	private JButton finished;
	private JList partyList;
	private JList allBowlers;
	private List party;
	private List bowlerdb;

	final private ControlDeskView controlDesk;

	private String selectedNick;
	private String selectedMember;
	static Logger log = Logger.getLogger(AddPartyView.class.getName());

	private JPanel setPanels() {
		final JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Your Party"));

		party = new Vector();
		List empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty.toArray());
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			log.error("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb.toArray());
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));


		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel = new JPanel();
		addPatronPanel.setLayout(new FlowLayout());
		addPatron.addActionListener(this);
		addPatronPanel.add(addPatron);

		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = new JPanel();
		remPatronPanel.setLayout(new FlowLayout());
		remPatron.addActionListener(this);
		remPatronPanel.add(remPatron);

		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = new JPanel();
		newPatronPanel.setLayout(new FlowLayout());
		newPatron.addActionListener(this);
		newPatronPanel.add(newPatron);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);
		return colPanel;
	}

	public void centerWindow() {
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
				((screenSize.width) / 2) - ((win.getSize().width) / 2),
				((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();
	}

	public void setWindow() {
		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
	}
	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		setWindow();
		final JPanel colPanel = setPanels();
		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		centerWindow();

	}

	private void actionPerformedAddPatron() {
		if (party.contains(selectedNick)) {
			log.error("Member already in Party");
		} else {
			party.add(selectedNick);
			partyList.setListData(party.toArray());
		}
	}

	private void actionPerformedFinished() {
		if ( party != null && !party.isEmpty()) {
			controlDesk.updateAddParty( this );
		}
		win.hide();
	}

	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addPatron) && (selectedNick != null && party.size() < maxSize) ) {
			actionPerformedAddPatron();
		}
		if (e.getSource().equals(remPatron) && selectedMember != null) {
			party.remove(selectedMember);
			partyList.setListData(party.toArray());
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView( this );
		}
		if (e.getSource().equals(finished)) {
			actionPerformedFinished();
		}

	}

/**
 * Handler for List actions
 * @param e the ListActionEvent that triggered the handler
 */

	@Override public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember =
				((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

/**
 * Accessor for Party
 */

	public List getNames() {
		return party;
	}

/**
 * Called by NewPatronView to notify AddPartyView to update
 * 
 * @param newPatron the NewPatronView that called this method
 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
			if ( checkBowler == null ) {
				BowlerFile.putBowlerInfo(
					newPatron.getNick(),
					newPatron.getFull(),
					newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb.toArray());
				party.add(newPatron.getNick());
				partyList.setListData(party.toArray());
			} else {
				log.error( "A Bowler with that name already exists." );
			}
		} catch (Exception e2) {
			log.error("File I/O Error");
		}
	}

/**
 * Accessor for Party
 */

	public List getParty() {
		return party;
	}

}

/**
 * Class for GUI components need to add a patron
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class NewPatronView implements ActionListener {


	final private JFrame win;
	final private JButton abort;
	final private JButton finished;
	final private JTextField nickField;
	final private JTextField fullField;
	final private JTextField emailField;
	final private JTextField genderField;
	private String nick;
	private String full;
	private String email;
	private String gender;

	private boolean done;

	final private AddPartyView addParty;

	public NewPatronView(AddPartyView v) {

		addParty=v;	
		done = false;

		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Patron Panel
		JPanel patronPanel = new JPanel();
		patronPanel.setLayout(new GridLayout(3, 1));
		patronPanel.setBorder(new TitledBorder("Your Info"));

		JPanel nickPanel = new JPanel();
		nickPanel.setLayout(new FlowLayout());
		JLabel nickLabel = new JLabel("Nick Name");
		nickField = new JTextField("", 15);
		nickPanel.add(nickLabel);
		nickPanel.add(nickField);

		JPanel fullPanel = new JPanel();
		fullPanel.setLayout(new FlowLayout());
		JLabel fullLabel = new JLabel("Full Name");
		fullField = new JTextField("", 15);
		fullPanel.add(fullLabel);
		fullPanel.add(fullField);

		JPanel emailPanel = new JPanel();
		emailPanel.setLayout(new FlowLayout());
		JLabel emailLabel = new JLabel("E-Mail");
		emailField = new JTextField("", 15);
		emailPanel.add(emailLabel);
		emailPanel.add(emailField);

		JPanel genderPanel = new JPanel();
		genderPanel.setLayout(new FlowLayout());
		JLabel genderLabel = new JLabel("Gender (M/F)");
		genderField = new JTextField("", 15);
		genderPanel.add(genderLabel);
		genderPanel.add(genderField);

		patronPanel.add(nickPanel);
		patronPanel.add(fullPanel);
		patronPanel.add(emailPanel);
		patronPanel.add(genderPanel);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));


		finished = new JButton("Add Patron");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		abort = new JButton("Abort");
		JPanel abortPanel = new JPanel();
		abortPanel.setLayout(new FlowLayout());
		abort.addActionListener(this);
		abortPanel.add(abort);

		buttonPanel.add(abortPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(abort)) {
			done = true;
			win.hide();
		}

		if (e.getSource().equals(finished)) {
			nick = nickField.getText();
			full = fullField.getText();
			email = emailField.getText();
			gender = genderField.getText();
			done = true;
			addParty.updateNewPatron( this );
			win.hide();
		}

	}

	public boolean done() {
		return done;
	}

	public String getNick() {
		return nick;
	}

	public String getFull() {
		return full;
	}

	public String getEmail() {
		return email;
	}

	public String getGender() { return gender; }

}

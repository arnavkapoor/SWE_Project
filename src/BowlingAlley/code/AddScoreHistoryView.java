

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddScoreHistoryView implements ActionListener {

	// max number of Patrons
	// final private int maxSize;
		static JFrame win;
		private JButton topPlayers;
		private JButton nickMax;
		private JButton nickMin;
		private JTextField nickText;
		private TextArea output;
		public boolean hidden;
		private QueryHandler queryHandler;
		private int queryOption;
		final private ControlDeskView controlDesk;


	public AddScoreHistoryView(ControlDeskView controlDesk ) {

			this.controlDesk = controlDesk;

			queryHandler = new QueryHandler();
			hidden = false;


			win = new JFrame("Database Query Window");
			win.getContentPane().setLayout(new BorderLayout());
			((JPanel) win.getContentPane()).setOpaque(false);

			topPlayers = new JButton("Top Players");
			topPlayers.addActionListener(this);

			nickText = new JTextField("Nick Here",10);

			nickMax = new JButton("Check Max Score");
			nickMax.addActionListener(this);

			nickMin = new JButton("Check Min Score");
			nickMin.addActionListener(this);

			JPanel panel1 = new JPanel();
			JPanel panel2 = new JPanel();
			panel2.setLayout(new GridLayout(4,1));

			output = new TextArea("Output Comes Here");
			panel1.add(output);
			output.setBounds(10,30,300,300);
			panel2.add(topPlayers);
			panel2.add(nickText);
			panel2.add(nickMax);
			panel2.add(nickMin);

			panel1.add(panel2);

			//
			win.getContentPane().add("Center", panel1);

			win.pack();

			// Center Window on Screen
			Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
			win.setLocation(
					((screenSize.width) / 2) - ((win.getSize().width) / 2),
					((screenSize.height) / 2) - ((win.getSize().height) / 2));
			win.show();

		}


	public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(topPlayers)) {
				try {
					String outputVals = queryHandler.getQueryOutput("trash",1);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
			else if (e.getSource().equals(nickMax)) {
				try {
					String outputVals = queryHandler.getQueryOutput(nickText.getText(),2);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
			else if (e.getSource().equals(nickMin)) {
				try {
					String outputVals = queryHandler.getQueryOutput(nickText.getText(),3);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
		}

		public void hide() {
			win.hide();
			hidden = true;
		}

		public void show() {
			win.show();
			hidden = false;
		}

}



/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.io.File;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddScoreHistoryView implements ActionListener {

	// max number of Patrons
	// final private int maxSize;
	static JFrame main_window;
	private JButton topPlayers;
	private JButton nickMax;
	private JButton nickMin;
	private JTextField nickText;
	private TextArea output;
	private JComboBox dropdownnick;

	public boolean hidden;
	private SimpleQuery queryHandler;
	private int queryOption;
	final private ControlDeskView controlDesk;

	private static String BOWLING_DATA = "BOWLERS.DAT";

	public List<String> read_bowler_info(String fname){
		File myObj = new File("BOWLERS.DAT");
		List<String> bowler_names = new ArrayList<>();

		Scanner myReader = null;
		try {
			myReader = new Scanner(myObj);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();

		}
		String[] lines = new String[0];
		try {
			lines = Files.readAllLines(new File(fname).toPath()).toArray(new String[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(String line: lines) {
			String[] rowVals = line.split("\\s+");
			bowler_names.add(rowVals[0]);
		}
		return  bowler_names;
	}

	public AddScoreHistoryView(ControlDeskView controlDesk ) {

			this.controlDesk = controlDesk;

			queryHandler = new SimpleQuery();
			hidden = false;


			main_window = new JFrame("Database Query Window");
			main_window.getContentPane().setLayout(new BorderLayout());
			((JPanel) main_window.getContentPane()).setOpaque(false);

			topPlayers = new JButton("Top Scoring Game Ever");
			topPlayers.addActionListener(this);

//			nickText = new JTextField("Nick Here",10);
//			list_of_bowlers = read_bowler_info(BOWLING_DATA);
			List<String> bowler_db = read_bowler_info(BOWLING_DATA);
			String[] bowler_db_arr = Arrays.copyOf(bowler_db.toArray(), bowler_db.size(),
				String[].class);
			dropdownnick = new JComboBox(bowler_db_arr);

			nickMax = new JButton("Check Maximum Score for Player");
			nickMax.addActionListener(this);

			nickMin = new JButton("Check Minimum Score for Player");
			nickMin.addActionListener(this);

			JPanel panel1 = new JPanel();
			JPanel panel2 = new JPanel();
			panel2.setLayout(new GridLayout(6,1));

			output = new TextArea("Query Results");
			panel1.add(output);

			output.setBounds(10,30,250,100);
			panel2.add(topPlayers);


			panel2.add(dropdownnick);

			panel2.add(nickMax);
			panel2.add(nickMin);

			panel1.add(panel2);

			//
			main_window.getContentPane().add("Center", panel1);

			main_window.pack();

			// Center Window on Screen
			Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
			main_window.setLocation(
					((screenSize.width) / 2) - ((main_window.getSize().width) / 2),
					((screenSize.height) / 2) - ((main_window.getSize().height) / 2));
			main_window.show();

		}


	public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(topPlayers)) {
				try {
					String outputVals = queryHandler.getQueryOutput("random",1);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
			else if (e.getSource().equals(nickMax)) {
				try {
					String outputVals = queryHandler.getQueryOutput(dropdownnick.getSelectedItem().toString(),2);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
			else if (e.getSource().equals(nickMin)) {
				try {
					String outputVals = queryHandler.getQueryOutput(dropdownnick.getSelectedItem().toString(),3);
					output.setText(outputVals);
				}
				catch(IOException error) {
					error.printStackTrace();
				}
			}
		}

		public void hide() {
			main_window.hide();
			hidden = true;
		}

		public void show() {
			main_window.show();
			hidden = false;
		}

}

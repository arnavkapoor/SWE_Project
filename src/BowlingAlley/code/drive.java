//import java.util.Vector;
//import java.io.*;
//import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.formdev.flatlaf.*;

import javax.swing.*;

public class drive {

	public static void main(String[] args) {
		FlatDarkLaf.install();
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		InitConfig ic = new InitConfig();
		ic.getGameParameters();

		int numLanes = ic.numLanes;
		int maxPatronsPerParty= ic.maxPatrons;

		ControlDesk controlDesk = new ControlDesk( numLanes, 10, true );
		ControlDeskView cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
		controlDesk.subscribe( cdv );

	}
}

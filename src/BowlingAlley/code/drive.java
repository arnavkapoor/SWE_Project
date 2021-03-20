//import java.util.Vector;
//import java.io.*;
//import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.formdev.flatlaf.*;
public class drive {

	public static void main(String[] args) {
		FlatDarkLaf.install();
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		int numLanes = 3;
		int maxPatronsPerParty=5;

		ControlDesk controlDesk = new ControlDesk( numLanes, 10, true );
		ControlDeskView cdv = new ControlDeskView(controlDesk, maxPatronsPerParty);
		controlDesk.subscribe( cdv );

	}
}

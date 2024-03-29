

/**
 * Class for interfacing with Bowler database
 *
 */

import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

final class BowlerFile {

	static Logger log = Logger.getLogger(BowlerFile.class.getName());
	/** The location of the bowler database */
	private static String BOWLER_DAT = "BOWLERS.DAT";

    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     *
     * @return a Bowler object
     * 
     */
	private BowlerFile() {}

	public static Bowler getBowlerInfo(String nickName)
		throws IOException, FileNotFoundException {

		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");
			if (nickName.equals(bowler[0])) {
				log.info(
					"Nick: "
						+ bowler[0]
						+ " Full: "
						+ bowler[1]
						+ " email: "
						+ bowler[2]
						+ "gender: "
						+ bowler[3]);
				return (new Bowler(bowler[0], bowler[1], bowler[2], bowler[3]));
			}
		}
		log.info("Nick not found...");
		return null;
	}

    /**
     * Stores a Bowler in the database
     *
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
     *
     */

	public static void putBowlerInfo(
		String nickName,
		String fullName,
		String email,
		String gender)
		throws IOException, FileNotFoundException {

		String data = nickName + "\t" + fullName + "\t" + email + "\t" + gender + "\n";

		RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     * 
     */

	public static List getBowlers()
		throws IOException, FileNotFoundException {

		List allBowlers = new Vector();

		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");
			//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
			allBowlers.add(bowler[0]);
		}
		return allBowlers;
	}

}
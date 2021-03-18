import java.util.*;
import java.io.*;
import java.io.File;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QueryHandler {

    private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

    public static void QueryHandler() {
        SCOREHISTORY_DAT = "./SCOREHISTORY.DAT";
    }

    public String getQueryOutput(String nick, int option)
            throws IOException, FileNotFoundException {

        // File parsing
        String localDir = System.getProperty("user.dir");
        System.out.println(localDir);
        File myObj = new File("SCOREHISTORY.DAT");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        String[] lines = Files.readAllLines(new File(SCOREHISTORY_DAT).toPath()).toArray(new String[0]);

        int nickMax = 0;
        int nickMin = 1000;
        int topMax = 0;
        String topNick = "\0";
        System.out.println(lines);

        // Generates output based on the query
        for(String line: lines) {
            System.out.println(line);

            String[] rowVals = line.split("\\s+");

            // Overall Top Player
            if(topMax < Integer.parseInt(rowVals[3])) {
                topMax = Integer.parseInt(rowVals[3]);
                topNick = rowVals[0];
            }

            if(nick.equals(rowVals[0])) {

                if(nickMax < Integer.parseInt(rowVals[3])) {
                    nickMax = Integer.parseInt(rowVals[3]);
                }

                if(nickMin > Integer.parseInt(rowVals[3])) {
                    nickMin = Integer.parseInt(rowVals[3]);
                }

            }

        }

        String outputVals = "Error Output\n";

        // Returns output based on the query
        if(option == 1) {
            outputVals = topNick+" "+topMax+"\n";
        }
        else if(option == 2) {
            outputVals = nick+" "+nickMax+"\n";
        }
        else if(option == 3) {
            outputVals = nick+" "+nickMin+"\n";
        }

        return outputVals;
    }
}
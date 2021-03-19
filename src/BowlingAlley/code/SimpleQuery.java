import java.util.*;
import java.io.*;
import java.io.File;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleQuery {

    private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

    public String getQueryOutput(String nick, int option)
            throws IOException, FileNotFoundException {

        // File parsing
        String localDir = System.getProperty("user.dir");

        File myObj = new File("SCOREHISTORY.DAT");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        String[] lines = Files.readAllLines(new File(SCOREHISTORY_DAT).toPath()).toArray(new String[0]);

        int userwise_max = 0;
        int userwise_min = 1000;
        int overall_max = 0;
        String topNick = " ";
        int is_valid = 0;


        for(String line: lines) {


            String[] rowVals = line.split("\\s+");
            if(overall_max < Integer.parseInt(rowVals[3])) {
                overall_max = Integer.parseInt(rowVals[3]);
                topNick = rowVals[0];
            }

            if(nick.equals(rowVals[0])) {
                is_valid = 1;
                if(userwise_max < Integer.parseInt(rowVals[3])) {
                    userwise_max = Integer.parseInt(rowVals[3]);
                }

                if(userwise_min > Integer.parseInt(rowVals[3])) {
                    userwise_min = Integer.parseInt(rowVals[3]);
                }

            }

        }

        String outputVals = "Error Output\n";

        if(option == 1) {
            outputVals = topNick+" had the highest score of "+overall_max+"\n";
        }
        else if(option == 2) {
            if(is_valid == 1)
                outputVals = nick+" had a max score of  "+userwise_max+"\n";
            else
                outputVals = "This user " + nick +  " has not played before";
        }
        else if(option == 3) {
             if(is_valid == 1)
                   outputVals = nick+" had a min score of "+userwise_min+ "\n";
             else
                    outputVals = "This user " + nick + " has not played before";
        }

        return outputVals;
    }
}
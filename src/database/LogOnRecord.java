package database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LogOnRecord {
    private static final String logFile = "login_activity.txt";

    public LogOnRecord() {}

    public static void generateLogOnFile(String username, boolean successful) {
        try (FileWriter fileWriter = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            printWriter.println(ZonedDateTime.now() + " " + username + (successful ? " Success" : " Fail"));
        }
        catch (IOException ioException) {
            System.out.println("Error generating log file data. " + ioException.getMessage());
        }
    }


}

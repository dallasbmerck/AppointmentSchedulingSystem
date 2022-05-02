package database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Records the log on attempts for the java application and stores them into login_activity.txt file.
 *
 * @author Dallas Merck
 */
public class LogOnRecord {
    private static final String logFile = "login_activity.txt";

    /**
     * Generates the log information for log on attempts.
     * @param username Username of the user logging on.
     * @param successful Boolean to determine whether log on was successful.
     */
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

package database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class LogOnRecord {
    private static final String log = "login_activity.txt";


    public static void generateLogOnFile(String username, Boolean success) throws IOException {
        try {
            BufferedWriter logOnRec = new BufferedWriter(new FileWriter(log, true));
            //logOnRec.append(ZonedDateTime.now(ZoneOffset.UTC).toString()).append(" UTC - LogOn Attempt - Username: ").append(username).append(" LogOn Successful: ").append(success.toString()).append("\n");
            logOnRec.append(ZonedDateTime.now(ZoneOffset.UTC).toString()).append(" UTC - LogOn Attempt - Username: ").append(username).append(" LogOn Successful: ").append(success.toString()).append("\n");
            logOnRec.flush();
            logOnRec.close();
        } catch
        (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}

package model;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;

public class LogOn {
    private static User userLoggedOn;
    private static Locale usersLocale;
    private static ZoneId usersTimeZone;

    public LogOn() {}

    public static User getUserLoggedOn() {
        return userLoggedOn;
    }

    public static Locale getUsersLocale() {
        return usersLocale;
    }

    public static ZoneId getUsersTimeZone() {
        return usersTimeZone;
    }
//public static boolean logOnAttempt(String usernameInput, String userPasswordInput) throws SQLException {

//}

}

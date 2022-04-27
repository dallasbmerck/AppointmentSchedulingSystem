package model;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public static boolean logOnAttempt(String usernameInput, String userPasswordInput) throws SQLException {
        Connection connection = DatabaseConnection.initiateConnection();
        PreparedStatement SQLCommand = connection.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
        SQLCommand.setString(1, usernameInput);
        SQLCommand.setString(2, userPasswordInput);
        System.out.println("Executing SQL Query...");
        ResultSet resultSet = SQLCommand.executeQuery();
        if(!resultSet.next()) {
            SQLCommand.close();
            return false;
        }
        else {
            userLoggedOn = new User(resultSet.getString("User_Name"), resultSet.getInt("User_ID"));
            usersLocale = Locale.getDefault();
            usersTimeZone = ZoneId.systemDefault();
            SQLCommand.close();
            return true;
        }
    }

    public static void userLogOff() {
        userLoggedOn = null;
        usersLocale = null;
        usersTimeZone= null;
    }

}

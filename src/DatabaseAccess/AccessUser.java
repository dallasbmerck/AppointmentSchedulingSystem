package DatabaseAccess;

import database.DatabaseConnection;
import database.LogOnRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.*;
import java.time.ZoneId;
import java.util.Locale;

public class AccessUser {
    private static User userLoggedOn;
    private static Locale usersLocale;
    private static ZoneId usersTimeZone;

    public static User getUserLoggedOn() {
        return userLoggedOn;
    }

    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> userID = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT DISTINCT " +
                "User_ID FROM users;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            userID.add(resultSet.getInt("User_ID"));
        }
        SQLCommand.close();
        return userID;
    }

    public static Boolean attemptLogin(String username, String password) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE " +
                "User_Name = ? AND Password = ?");
        ps.setString(1, username);
        ps.setString(2, password);
        System.out.println("Executing command...");
        ResultSet resultSet = ps.executeQuery();
        if(!resultSet.next()) {
            ps.close();
            return false;
        }
        else {
            userLoggedOn = new User(resultSet.getString("User_Name"), resultSet.getInt("User_ID"));
            usersLocale = Locale.getDefault();
            usersTimeZone = ZoneId.systemDefault();
            ps.close();
            return true;
        }
    }

    public static Locale getUsersLocale() {
        return usersLocale;
    }

    public static ZoneId getUsersTimeZone() {
        return usersTimeZone;
    }
/*
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
    } */

    public static void userLogOff() {
        userLoggedOn = null;
        usersLocale = null;
        usersTimeZone= null;
    }
}

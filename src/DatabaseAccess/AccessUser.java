package DatabaseAccess;

import database.DatabaseConnection;
import database.LogOnRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.*;
import java.time.ZoneId;
import java.util.Locale;

/**
 * AccessUser class used to write methods to move data to and from MySQL Database via Connection for all users.
 *
 * @author Dallas Merck
 */
public class AccessUser {
    //Used for methods below.
    private static User userLoggedOn;
    private static Locale usersLocale;
    private static ZoneId usersTimeZone;

    /**
     * Getter for the logged on user.
     * @return userLoggedOn.
     */
    public static User getUserLoggedOn() {
        return userLoggedOn;
    }

    /**
     * Observable list that gets all User_ID.
     * @return userID.
     * @throws SQLException SQLException.
     */
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

    /**
     * Gets the username and password information to compare to the users input during a login attempt.
     * @param username Username of the user.
     * @param password Password of the user.
     * @return Boolean true or false.
     * @throws SQLException SQLException.
     */
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

    /**
     * Getter for the users Locale.
     * @return usersLocale.
     */
    public static Locale getUsersLocale() {
        return usersLocale;
    }

    /**
     * Getter for the users time zone on their machine accessing the program.
     * @return usersTimeZone.
     */
    public static ZoneId getUsersTimeZone() {
        return usersTimeZone;
    }

    /**
     * Log out the user.
     */
    public static void userLogOff() {
        userLoggedOn = null;
        usersLocale = null;
        usersTimeZone= null;
    }

    /**
     * Observable list that gets all User_ID from the MySQL Database.
     * @return userID.
     * @throws SQLException SQLException.
     */
    public static ObservableList<Integer> getAllUsersID() throws SQLException {
        ObservableList<Integer> userID = FXCollections.observableArrayList();
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT DISTINCT User_ID FROM users;");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            userID.add(rs.getInt("User_ID"));
        }
        ps.close();
        return userID;
    }
}

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String serverURL = "wgudb.ucertify.com";
    private static final int port = 3306;
    private static String DBName;
    private static String DBUsername;
    private static String DBPassword;
    private static String jdbcURL;
    private static String jdbcDriver = "com.mysql.jdbc.Driver";
    public static Connection connection;

    public DatabaseConnection() {}

    public static void getConnection() {
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcURL, DBUsername, DBPassword);
        }
        catch (SQLException e) {
            System.out.println(e.toString() + e.getSQLState());
        }
        catch (ClassNotFoundException c) {
            System.out.println(c.getMessage());
        }
    }
    public static void endConnection() {
        try {
            connection.close();
        } catch (Exception exception) {

        }
    }
    public static Connection initiateConnection() {
        return connection;
    }

    public static void setDataBaseName(String nameInput) {
        DBName = nameInput;
    }

    public static void setDataBaseUserName(String usernameInput) {
        DBUsername = usernameInput;
    }

    public static void setDataBasePassword(String passwordInput) {
        DBPassword = passwordInput;
    }

    public static void setJDBCURL(String urlInput) {
        jdbcURL = urlInput;
    }
}

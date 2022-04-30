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
    public static Connection connection;

    public DatabaseConnection() {}

    public static Connection initiateConnection() {
        try {
            String JDBCMYSQLDriver = "com.mysql.jdbc.Driver";
            Class.forName(JDBCMYSQLDriver);
            connection = DriverManager.getConnection(jdbcURL, DBUsername, DBPassword);
            System.out.println("Connection was succesful.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return connection;
    }
    public static void endConnection() {
        try {
            connection.close();
        } catch (Exception exception) {

        }
    }
    public static Connection getConnection() {
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

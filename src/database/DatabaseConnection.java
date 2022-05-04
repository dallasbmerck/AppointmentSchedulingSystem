package database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DatabaseConnection
 *
 * @author Dallas Merck
 */
public abstract class DatabaseConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    private static Connection connection;

    /**
     * Connects to the SQL Database.
     * @return connection.
     */
    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        //return connection;
    }

    /**
     * Connection class method to get the connection. Does not reconnect each time it is called.
     * @return the connection.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the MySQL Database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
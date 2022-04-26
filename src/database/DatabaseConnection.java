package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String protocol = "jdbc";
    private static final String IPAddress = "//wgudb.ucertify.com:3306";
    private static final String DBName = "client_schedule";
    private static final String vendorName = ":mysql:";
    private static final String DBUsername = "sqlUser";
    private static final String DBPassword = "Passw0rd!";
    private static final String jdbcURL = protocol + vendorName + IPAddress + DBName;
    private static final String JDBCMYSQLDriver = "com.mysql.jdbc.Driver";
    public static Connection connection;

    public static Connection initiateConnection() {
        try {
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
}

package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AccessCustomer {

    public static ObservableList<Integer> getAllCustomerID() throws SQLException {
        ObservableList<Integer> customerID = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT DISTINCT Customer_ID FROM customers;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            customerID.add(resultSet.getInt("Customer_ID"));
        }
        SQLCommand.close();
        return customerID;
    }

    public static Boolean addCustomers(String country, String division, String name, String address, String postCode,
                                       String phoneNumber, Integer divisionID) throws SQLException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement(
                "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date," +
                        "Created_By, Last_Update, Last_Updated_By, Division_ID) \n " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        SQLCommand.setString(1, name);
        SQLCommand.setString(2, address);
        SQLCommand.setString(2, postCode);
        SQLCommand.setString(4, phoneNumber);
        SQLCommand.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString();
        SQLCommand;
        SQLCommand;
        SQLCommand;
    }

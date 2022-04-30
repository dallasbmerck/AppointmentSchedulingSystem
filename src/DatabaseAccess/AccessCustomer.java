package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.LogOn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AccessCustomer {

    public static ObservableList<Integer> getAllCustomersID() throws SQLException {
        ObservableList<Integer> customerID = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT DISTINCT Customer_ID FROM customers;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            customerID.add(resultSet.getInt("Customer_ID"));
        }
        SQLCommand.close();
        return customerID;
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT cx.Customer_ID," +
                "c.Customer_Name, c.Address, c.Postal_Code, c.Phone, c.Division_ID, f.Division, f.COUNTRY_ID, co.Country " +
                "FROM customers as c INNER JOIN first_level_divisions as f on c.Division_ID INNER JOIN countries as co ON " +
                "f.COUNTRY_ID = co.Country_ID");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer customerID = resultSet.getInt("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");
            String customerAddress = resultSet.getString("Address");
            String customerPostCode = resultSet.getString("Postal_Code");
            String customerPhone = resultSet.getString("Phone");
            String customerDivision = resultSet.getString("Division");
            Integer divisionID = resultSet.getInt("Division_ID");
            String customerCountry = resultSet.getString("Country");

            Customer newCustomer = new Customer(customerID, customerName, customerAddress, customerPostCode,
                    customerPhone, customerDivision,divisionID,customerCountry);

            allCustomers.add(newCustomer);
        }
        SQLCommand.close();
        return allCustomers;
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
        SQLCommand.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter));
        SQLCommand.setString(6, LogOn.getUserLoggedOn().getUserName());
        SQLCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter));
        SQLCommand.setString(8, LogOn.getUserLoggedOn().getUserName());
        SQLCommand.setInt(9, divisionID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static Boolean updateCustomers(String divisionID, String name, String address, String postCode,
            String phoneNumber, Integer customerID) throws SQLException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("UPDATE customers" +
                "SET Customer_Name=?, Address=?, Postal_Code=?, Phone=? Last_Update=?, Last_Updated_By=?, Division_ID=?" +
                "WHERE Customer_ID = ?");

        SQLCommand.setString(1, name);
        SQLCommand.setString(2, address);
        SQLCommand.setString(3, postCode);
        SQLCommand.setString(4, phoneNumber);
        SQLCommand.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter));
        SQLCommand.setString(6, LogOn.getUserLoggedOn().getUserName());
        SQLCommand.setInt(7, getDivisionID(divisionID));
        SQLCommand.setInt(8, customerID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException exception){
            exception.printStackTrace();
            SQLCommand.close();
            return false;
        }
    }

    public static Integer getDivisionID(String divisionID) throws SQLException {
        Integer id = 0;
        PreparedStatement SQlCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT Division," +
                " Division_ID FROM first_level_divisions WHERE Division = ?");
        SQlCommand.setString(1, divisionID);
        ResultSet resultSet = SQlCommand.executeQuery();

        while (resultSet.next()) {
            id = resultSet.getInt("Division_ID");
        }
        SQlCommand.close();
        return id;
    }

    public static ObservableList<String> getFirstLevelDivisions(String country) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT co.Country, " +
                "co.Country_ID, d.Division FROM countries as co on RIGHT OUTER JOIN first_level_divisions AS d ON " +
                "co.Country_ID = d.Country_ID WHERE co.Country = ?");

        SQLCommand.setString(1, country);
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            divisions.add(resultSet.getString("Division"));
        }
        SQLCommand.close();
        return divisions;
    }

    public static Boolean deleteSelectedCustomer(Integer customerID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("DELETE FROM customers" +
                "WHERE Customer_ID = ?");

        SQLCommand.setInt(1, customerID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT DISTINCT " +
                "Country FROM countries");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            countries.add(resultSet.getString("Country"));
        }
        SQLCommand.close();
        return countries;
    }
    }

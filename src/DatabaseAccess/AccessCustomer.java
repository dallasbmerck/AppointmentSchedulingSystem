package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * * AccessCustomer class used to write methods to move data to and from MySQL Database via Connection for all customers.
 */
public class AccessCustomer {

    /**
     * Observable list user to return all Customer_ID from database.
     * @return customerID.
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllCustomersID() throws SQLException {
        ObservableList<Integer> customerID = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT DISTINCT Customer_ID FROM customers;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            customerID.add(resultSet.getInt("Customer_ID"));
        }
        SQLCommand.close();
        return customerID;
    }

    /**
     * Observable list user to return all customer data from the database.
     * @return allCustomers.
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT c.Customer_ID, c.Customer_Name, " +
                "c.Address, c.Postal_Code, c.Phone, c.Division_ID, f.Division, f.Country_ID, co.Country FROM customers " +
                "as c INNER JOIN first_level_divisions as f ON c.Division_ID = f.Division_ID INNER JOIN countries as co " +
                "ON f.Country_ID = co.Country_ID;");
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
                    customerPhone, customerDivision, divisionID, customerCountry);

            allCustomers.add(newCustomer);
        }
        SQLCommand.close();
        return allCustomers;
    }

    /**
     * Used to add customers and their attributes into the MySQL Database.
     * @param country Customer Country.
     * @param division Customer Division.
     * @param name Customer_Name.
     * @param address Customer Address.
     * @param postCode Customer Postal_Code.
     * @param phoneNumber Customer Phone.
     * @param divisionID Customer Division_ID.
     * @return Boolean true of false.
     * @throws SQLException
     */
    public static Boolean addCustomers(String country, String division, String name, String address, String postCode,
                                       String phoneNumber, Integer divisionID) throws SQLException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement(
                "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date," +
                        "Created_By, Last_Update, Last_Updated_By, Division_ID) \n " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        SQLCommand.setString(1, name);
        SQLCommand.setString(2, address);
        SQLCommand.setString(2, postCode);
        SQLCommand.setString(4, phoneNumber);
        SQLCommand.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(6, AccessUser.getUserLoggedOn().getUserName());
        SQLCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(8, AccessUser.getUserLoggedOn().getUserName());
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

    /**
     * Used to update customer data in the database.
     * @param division Division of customer.
     * @param name Customer_Name.
     * @param address Address of customer.
     * @param postCode Postal_Code of customer.
     * @param phoneNumber Phone of customer.
     * @param customerID Customer_ID.
     * @return Boolean true or false.
     * @throws SQLException
     */
    public static Boolean updateCustomers(String division, String name, String address, String postCode,
            String phoneNumber, Integer customerID) throws SQLException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("UPDATE customers" +
                "SET Customer_Name=?, Address=?, Postal_Code=?, Phone=? Last_Update=?, Last_Updated_By=?, Division_ID=?" +
                "WHERE Customer_ID = ?");

        SQLCommand.setString(1, name);
        SQLCommand.setString(2, address);
        SQLCommand.setString(3, postCode);
        SQLCommand.setString(4, phoneNumber);
        SQLCommand.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(6, AccessUser.getUserLoggedOn().getUserName());
        SQLCommand.setInt(7, AccessCustomer.getDivisionID(division));
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

    /**
     * Used to get the Division_ID for a customer.
     * @param division Division_ID.
     * @return divisionID
     * @throws SQLException
     */
    public static Integer getDivisionID(String division) throws SQLException {
        Integer divisionID = 0;
        PreparedStatement SQlCommand = DatabaseConnection.getConnection().prepareStatement("SELECT Division," +
                " Division_ID FROM first_level_divisions WHERE Division = ?");
        SQlCommand.setString(1, division);
        ResultSet resultSet = SQlCommand.executeQuery();

        while (resultSet.next()) {
            divisionID = resultSet.getInt("Division_ID");
        }
        SQlCommand.close();
        return divisionID;
    }

    /**
     * Observable list used to access the First_Level_Divisions of customer.
     * @param country Country of customer.
     * @return divisions.
     * @throws SQLException
     */
    public static ObservableList<String> getFirstLevelDivisions(String country) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT c.Country, " +
                "c.Country_ID, d.Division FROM countries as c RIGHT OUTER JOIN first_level_divisions AS d ON " +
                "c.Country_ID = d.Country_ID WHERE c.Country = ?");

        SQLCommand.setString(1, country);
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            divisions.add(resultSet.getString("Division"));
        }
        SQLCommand.close();
        return divisions;
    }

    /**
     * Deletes a selected customer from the database.
     * @param customerID Customer_ID.
     * @return Boolean true or false.
     * @throws SQLException
     */
    public static Boolean deleteSelectedCustomer(Integer customerID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("DELETE FROM customers" +
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

    /**
     * Observable list used to get all countries from the countries table.
     * @return countries.
     * @throws SQLException
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT DISTINCT " +
                "Country FROM countries;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            countries.add(resultSet.getString("Country"));
        }
        SQLCommand.close();
        return countries;
    }
    }

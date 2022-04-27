package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessContact {
    public static Integer getContactID(String contactName) throws SQLException {
        Integer id = -1;
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT Contact_ID, " +
                "Contact_Name FROM contacts WHERE Contact_Name = ?");
        SQLCommand.setString(1, contactName);
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            id = resultSet.getInt("Contact_ID");
        }
        SQLCommand.close();
        return id;
    }

    public static ObservableList<String> getContactName() throws SQLException {
        ObservableList<String> contactName = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT DISTINCT " +
                "Contact_Name FROM contacts;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            contactName.add(resultSet.getString("Contact_Name"));
        }
        SQLCommand.close();
        return contactName;
    }

    public static ObservableList<String> getContactAppointments(String contactID) throws SQLException {
        ObservableList<String> appointments = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT * FROM" +
                "appointments WHERE Contact_ID = ?");
        SQLCommand.setString(1, contactID);
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            String appointmentID = resultSet.getString("Appointment_ID");
            String title = resultSet.getString("Title");
            String type = resultSet.getString("Type");
            String startTime = resultSet.getString("Start");
            String endTime = resultSet.getString("End");
            String customerID = resultSet.getString("Customer_ID");






            //FINISH THIS SECTION
        }
        SQLCommand.close();
        return appointments;
    }

}

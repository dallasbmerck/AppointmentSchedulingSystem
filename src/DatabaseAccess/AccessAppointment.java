package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AccessAppointment {

    public static ObservableList<Appointment> showAllAppointments() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT * FROM " +
            "appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp start = resultSet.getTimestamp("Start");
            Timestamp end = resultSet.getTimestamp("End");
            Timestamp dateCreated = resultSet.getTimestamp("Created_Date");
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer userID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, start, end, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, userID, contactID, contactName);

            appointments.add(newAppointment);
        }
        SQLCommand.close();
        return appointments;
    }

    public static Boolean updateAppointment(Integer apptIDInput, String titleInput, String descriptionInput, String )
}

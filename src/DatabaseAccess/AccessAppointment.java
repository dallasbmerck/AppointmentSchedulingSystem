package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public static Boolean addAppointment(String titleInput, String descriptionInput, String locationInput, String typeInput,
                                         ZonedDateTime startInput, ZonedDateTime endInput, String createdByInput, String lastUpdatedByInput,
                                         Integer customerIDInput, Integer userIDInput, Integer contactIDInput) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("INSERT INTO appointments " +
                "Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, " +
                "Customer_ID, User_ID, Contact_ID VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startInputString = startInput.format(dateTimeFormatter).toString();
        String endInputString = endInput.format(dateTimeFormatter).toString();

        SQLCommand.setString(1, titleInput);
        SQLCommand.setString(2, descriptionInput);
        SQLCommand.setString(3, locationInput);
        SQLCommand.setString(4, typeInput);
        SQLCommand.setString(5, startInputString);
        SQLCommand.setString(6, endInputString);
        SQLCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(8, createdByInput);
        SQLCommand.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(10, lastUpdatedByInput);
        SQLCommand.setInt(11, customerIDInput);
        SQLCommand.setInt(12, userIDInput);
        SQLCommand.setInt(13, contactIDInput);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            SQLCommand.close();
            return false;
        }

    }

    public static Boolean updateAppointment(Integer apptIDInput, String titleInput, String descriptionInput, String locationInput,
                                            String typeInput, ZonedDateTime startInput, ZonedDateTime endInput, String
                                            lastUpdateByInput, Integer customerIDInput, Integer userIDInput, Integer contactIDInput) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("UPDATE appointments " +
                "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?, Last_Update_By=?, " +
                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startInputString = startInput.format(dateTimeFormatter).toString();
        String endInputString = endInput.format(dateTimeFormatter).toString();

        SQLCommand.setString(1, titleInput);
        SQLCommand.setString(2, descriptionInput);
        SQLCommand.setString(3, locationInput);
        SQLCommand.setString(4, typeInput);
        SQLCommand.setString(5, startInputString);
        SQLCommand.setString(6, endInputString);
        SQLCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter).toString());
        SQLCommand.setString(8, lastUpdateByInput);
        SQLCommand.setInt(9, customerIDInput);
        SQLCommand.setInt(10, userIDInput);
        SQLCommand.setInt(11, contactIDInput);
        SQLCommand.setInt(12, apptIDInput);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            SQLCommand.close();
            return false;
        }
    }

    public static Boolean deleteAppointment(Integer appointmentID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("DELETE FROM appointments " +
                "WHERE Appointment_ID = ?");
        SQLCommand.setInt(1, appointmentID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            SQLCommand.close();
            return false;
        }
    }

    public static Boolean deleteAllAppointmentsByCustomerID(Integer customerID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("DELETE FROM appointments " +
                "WHERE Customer_ID = ?");
        SQLCommand.setInt(1, customerID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            SQLCommand.close();
            return false;
        }
    }
}

package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.LogOn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static ObservableList<Appointment> filterAppointmentsByDate(ZonedDateTime start, ZonedDateTime end) throws SQLException {

        ObservableList<Appointment> filterAppointments = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT * FROM " +
                "appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start BETWEEN ? AND ?");

        String startString = start.format(dateTimeFormatter);
        String endString = end.format(dateTimeFormatter);

        SQLCommand.setString(1, startString);
        SQLCommand.setString(2, endString);

        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp starTime = resultSet.getTimestamp("Start");
            Timestamp endTime = resultSet.getTimestamp("End");
            Timestamp dateCreated = resultSet.getTimestamp("Created_Date");
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer userID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, starTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, userID, contactID, contactName);

            filterAppointments.add(newAppointment);
        }
        SQLCommand.close();
        return filterAppointments;
    }

    public static ObservableList<Appointment> filterAppointmentsByCustomerID(Integer customerIDInput, LocalDate appointmentDate) throws SQLException {
        ObservableList<Appointment> filterCustomerAppointment = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT * FROM appointments " +
                "as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0 AND Customer_ID = ?");

        SQLCommand.setInt(1, customerIDInput);
        SQLCommand.setString(2, String.valueOf(appointmentDate));

        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp starTime = resultSet.getTimestamp("Start");
            Timestamp endTime = resultSet.getTimestamp("End");
            Timestamp dateCreated = resultSet.getTimestamp("Created_Date");
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer userID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, starTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, userID, contactID, contactName);

            filterCustomerAppointment.add(newAppointment);
        }
        SQLCommand.close();
        return filterCustomerAppointment;
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

    public static ObservableList<Appointment> appointmentWithin15MinOfLogOn() throws SQLException {
        ObservableList<Appointment> apptsIn15 = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime timeOfLogOn = LocalDateTime.now();
        ZonedDateTime timezone = timeOfLogOn.atZone(LogOn.getUsersTimeZone());
        ZonedDateTime utc = timezone.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime timeDelta = utc.plusMinutes(15);

        String start = utc.format(dateTimeFormatter).toString();
        String end = timeDelta.format(dateTimeFormatter).toString();
        Integer userID = LogOn.getUserLoggedOn().getUserID();

        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT * FROM " +
                "appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start BETWEEN ? AND " +
                "? AND User_ID = ?");

        SQLCommand.setString(1, start);
        SQLCommand.setString(2, end);
        SQLCommand.setInt(3, userID);

        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp startTime = resultSet.getTimestamp("Start");
            Timestamp endTime = resultSet.getTimestamp("End");
            Timestamp dateCreated = resultSet.getTimestamp("Created_Date");
            String createdBy = resultSet.getString("Created_By");
            Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer currentUserID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, startTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, currentUserID, contactID, contactName);

            apptsIn15.add(newAppointment);
        }
        SQLCommand.close();
        return apptsIn15;

    }

    public static ObservableList<String> createReportTypeAndDate() throws SQLException {
        ObservableList<String> report = FXCollections.observableArrayList();

        report.add("Total Appointments by Type and Month:\n");

        PreparedStatement SQLCommandType = DatabaseConnection.initiateConnection().prepareStatement(" SELECT Type, " +
                "COUNT(Type) as \"Total\" FROM appointments GROUP BY Type");
        PreparedStatement SQLCommandMonth = DatabaseConnection.initiateConnection().prepareStatement("SELECT " +
                "MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month");

        ResultSet resultSetType = SQLCommandType.executeQuery();
        ResultSet resultSetMonth = SQLCommandMonth.executeQuery();

        while (resultSetType.next()) {
            String type = "Type: " + resultSetType.getString("Month") + " Count: " + resultSetType.getString("Total") + "\n";
            report.add(type);
        }
        while (resultSetMonth.next()) {
            String month = "Month: " + resultSetMonth.getString("Month") + " Count: " + resultSetMonth.getString("Total") + "\n";
            report.add(month);
        }

        SQLCommandType.close();
        SQLCommandMonth.close();
        return report;
    }
}

package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * AccessAppointment class used to write methods to move data to an from MySQL Database via Connection for all appointments.
 *
 * @author Dallas Merck
 */
public class AccessAppointment {

    /**
     * Observable list to gather all appointment data and add it to the appointment table view.
     * @return appointments list.
     * @throws SQLException SQLException.
     */
    public static ObservableList<Appointment> showAllAppointments() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM " +
            "appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID;");

        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime dateCreated = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
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

    /**
     * Observable list to filter appointments by week and month using their data and converting to UTC.
     * @param start Start range for the appointments.
     * @param end End range for the appointments.
     * @return filterAppointments list.
     * @throws SQLException SQLException.
     */
    public static ObservableList<Appointment> filterAppointmentsByDate(ZonedDateTime start, ZonedDateTime end) throws SQLException {

        ObservableList<Appointment> filterAppointments = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM " +
                "appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start BETWEEN ? AND ?;");

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
            LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime dateCreated = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer userID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, startTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, userID, contactID, contactName);

            filterAppointments.add(newAppointment);
        }
        SQLCommand.close();
        return filterAppointments;
    }

    /**
     * Observable list that filters appointments by customer ID.
     * @param customerIDInput Customer_ID related to an appointment.
     * @param appointmentDate The date of the appointment.
     * @return filterCustomerAppointment list.
     * @throws SQLException SQLException.
     */
    public static ObservableList<Appointment> filterAppointmentsByCustomer(LocalDate appointmentDate) throws SQLException {
        ObservableList<Appointment> filterCustomerAppointment = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM appointments " +
                "as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0;");

        SQLCommand.setString(1, appointmentDate.toString());
        //SQLCommand.setInt(2, customerIDInput);

        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            Integer apptID = resultSet.getInt("Appointment_ID");
            String title =  resultSet.getString("Title");
            String  description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime dateCreated = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer userID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, startTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, userID, contactID, contactName);

            filterCustomerAppointment.add(newAppointment);
        }
        SQLCommand.close();
        return filterCustomerAppointment;
    }

    /**
     * Is used to query the MySql Database to find all appointments and add them into the scheduling program.
     *
     * @param titleInput Title of the appointment.
     * @param descriptionInput Description of the appointment.
     * @param locationInput Location of the appointment.
     * @param typeInput Type of appointment.
     * @param startInput Start time of the appointment.
     * @param endInput End time of the appointment.
     * @param createdByInput User_ID of the user who created the appointment.
     * @param lastUpdatedByInput User_ID of the user who last updated the appointment.
     * @param customerIDInput The Customer_ID that the appointment is scheduled for.
     * @param userIDInput User_ID of user who the appointment is assigned to.
     * @param contactIDInput Contact_ID for the appointment.
     * @return Boolean true or false.
     * @throws SQLException SQLException.
     */
    public static Boolean addAppointment(String titleInput, String descriptionInput, String locationInput, String typeInput,
                                         ZonedDateTime startInput, ZonedDateTime endInput, String createdByInput, String lastUpdatedByInput,
                                         Integer customerIDInput, Integer userIDInput, Integer contactIDInput) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, " +
                "Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

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
            return false;
        }

    }

    /**
     * Used to update appointment details in the database by populating all data and allowing it to be adjusted.
     * @param apptIDInput The Appointment_ID.
     * @param titleInput The appointment Title.
     * @param descriptionInput The appointment Description.
     * @param locationInput The appointment Location.
     * @param typeInput The appointment Type.
     * @param startInput Appointment Start.
     * @param endInput Appointment End.
     * @param lastUpdateByInput Appointment Last_Updated_By.
     * @param customerIDInput Appointment Customer_ID.
     * @param userIDInput Appointment User_ID.
     * @param contactIDInput Appointment Contact_ID.
     * @return Boolean true or false.
     * @throws SQLException SQLException.
     */
    public static Boolean updateAppointment(Integer apptIDInput, String titleInput, String descriptionInput, String locationInput,
                                            String typeInput, ZonedDateTime startInput, ZonedDateTime endInput, String
                                            lastUpdateByInput, Integer customerIDInput, Integer userIDInput, Integer contactIDInput) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("UPDATE appointments " +
                "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?, Last_Updated_By=?, " +
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

    /**
     * Deletes a selected appointment from the database.
     * @param appointmentID Appointment_ID of the selected appointment.
     * @return Boolean true or false.
     * @throws SQLException SQLException.
     */
    public static Boolean deleteAppointment(Integer appointmentID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("DELETE FROM appointments " +
                "WHERE Appointment_ID = ?;");
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

    /**
     * Deletes an appointment by reference of a Customer_ID.
     * @param customerID Customer_ID for the appointment.
     * @return Boolean true of false.
     * @throws SQLException SQLException.
     */
    public static Boolean deleteAllAppointmentsByCustomerID(Integer customerID) throws SQLException {
        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("DELETE FROM appointments " +
                "WHERE Customer_ID = ?");
        SQLCommand.setInt(1, customerID);

        try {
            SQLCommand.executeUpdate();
            SQLCommand.close();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * Observable list to store appointments that are to be attended within 15 minutes of a user logging on.
     * @return Observable list apptsIn15.
     * @throws SQLException SQLException.
     */
    public static ObservableList<Appointment> appointmentWithin15MinOfLogOn() throws SQLException {
        ObservableList<Appointment> apptsIn15 = FXCollections.observableArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime timeOfLogOn = LocalDateTime.now();
        ZonedDateTime timezone = timeOfLogOn.atZone(AccessUser.getUsersTimeZone());
        ZonedDateTime utc = timezone.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime timeDelta = utc.plusMinutes(15);

        String start = utc.format(dateTimeFormatter);
        String end = timeDelta.format(dateTimeFormatter);
        Integer userID = AccessUser.getUserLoggedOn().getUserID();

        PreparedStatement SQLCommand = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM " +
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
            LocalDateTime startTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime dateCreated = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime lastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdateBy = resultSet.getString("Last_Updated_By");
            Integer customerID = resultSet.getInt("Customer_ID");
            Integer currentUserID = resultSet.getInt("User_ID");
            Integer contactID = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");

            Appointment newAppointment = new Appointment(apptID, title, description, location, type, startTime, endTime, dateCreated,
                    createdBy, lastUpdate, lastUpdateBy, customerID, currentUserID, contactID, contactName);

            apptsIn15.add(newAppointment);
        }
        return apptsIn15;

    }


    /**
     * Observable list that is user to create reports from the appointment's type or date.
     * @return report observable list.
     * @throws SQLException SQLException.
     */
    public static ObservableList<String> createReportTypeAndDate() throws SQLException {
        ObservableList<String> report = FXCollections.observableArrayList();

        report.add("Total Appointments by Type and Month:\n");

        PreparedStatement SQLCommandType = DatabaseConnection.getConnection().prepareStatement(" SELECT Type, " +
                "COUNT(Type) as \"Total\" FROM appointments GROUP BY Type;");
        PreparedStatement SQLCommandMonth = DatabaseConnection.getConnection().prepareStatement("SELECT " +
                "MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month;");

        ResultSet resultSetType = SQLCommandType.executeQuery();
        ResultSet resultSetMonth = SQLCommandMonth.executeQuery();

        while (resultSetType.next()) {
            String type = "Type: " + resultSetType.getString("Type") + " Count: " + resultSetType.getString("Total") + "\n";
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

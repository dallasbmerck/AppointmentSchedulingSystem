package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessContact;
import DatabaseAccess.AccessCustomer;
import DatabaseAccess.AccessUser;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * UpdateAppointmentController allows for appointment data to be augmented in the UpdateAppointmentsPage.fxml.
 */
public class UpdateAppointmentController implements Initializable {
    public DatePicker datePicker;
    public TextField startTextBox;
    public TextField endTextBox;
    public TextField apptIDTextBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public ComboBox<String> contactComboBox;
    public ComboBox<Integer> customerIDTextBox;
    public ComboBox<Integer> userIDComboBox;
    public TextField typeTextBox;
    public TextField locationTextBox;
    public Label dateLabel;
    public Label startLabel;
    public Label endLabel;
    public Label timezoneLabel;
    public Label businessHoursLabel;
    public Label appIDLabel;
    public Label titleLabel;
    public Label descriptionLabel;
    public Label locationLabel;
    public Label contactLable;
    public Label typeLabel;
    public Label customerIDLabel;
    public Label userIDLabel;
    public Button saveButton;
    public Button backButton;
    public Button clearButton;

    /**
     * Called by other methods to change the screen when a button is pressed.
     * @param actionEvent Button is clicked.
     * @param path Path that the screen change takes.
     * @throws IOException
     */
    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    /**
     * Validates the operational hours in regards to an appointment.
     * @param start Start of the appointment.
     * @param end End of the appointment.
     * @param appointmentDate Date of the appointment.
     * @return Boolean true or false.
     */
    public Boolean validateOperationHours(LocalDateTime start, LocalDateTime end, LocalDate appointmentDate) {
        ZonedDateTime zonedStart = ZonedDateTime.of(start, AccessUser.getUsersTimeZone());
        ZonedDateTime zonedEnd = ZonedDateTime.of(end, AccessUser.getUsersTimeZone());

        ZonedDateTime operationStart = ZonedDateTime.of(appointmentDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));
        ZonedDateTime operationEnd = ZonedDateTime.of(appointmentDate ,LocalTime.of(22,0), ZoneId.of("America/New_York"));

        return !zonedStart.isBefore(operationStart) && !zonedStart.isAfter(operationEnd) && !zonedEnd.isBefore(operationStart) &&
                !zonedEnd.isAfter(operationEnd) && !zonedStart.isAfter(zonedEnd);

    }

    /**
     * Ensures that customer appointments do not become double booked.
     * @param customerID Customer_ID.
     * @param start Start.
     * @param end End.
     * @param date Date.
     * @return Boolean true or false.
     * @throws SQLException
     */
    public Boolean overlappingCustomerAppointments(Integer customerID, LocalDateTime start, LocalDateTime end, LocalDate date) throws SQLException {
        ObservableList<Appointment> overlap = AccessAppointment.filterAppointmentsByCustomerID(date, customerID);

        if (overlap.isEmpty()) {
            return true;
        }
        else {
            for (Appointment overlappingAppt : overlap) {
                LocalDateTime overlapStart = overlappingAppt.getStartDateTime().toLocalDateTime();
                LocalDateTime overlapEnd = overlappingAppt.getEndDateTime().toLocalDateTime();

                if (overlapStart.isBefore(start) && overlapEnd.isAfter(end)) {
                    return false;
                }
                if (overlapStart.isBefore(end) && overlapStart.isAfter(start)) {
                    return false;
                }
                return !overlapEnd.isBefore(end) || !overlapEnd.isAfter(start);
            }
        }
        return true;
    }

    /**
     * Saves the updated appointment when the user clicks the save button.
     * @param actionEvent Save button is clicked.
     * @throws SQLException
     * @throws IOException
     */
    public void clickSaveButton(ActionEvent actionEvent) throws SQLException, IOException {
        Boolean validOverlap;
        Boolean validOperationHours;
        String errorMessage = "";

        String apptTitle = titleTextBox.getText();
        String apptDescription = descriptionTextBox.getText();
        String apptLocation = locationTextBox.getText();
        String apptContactName = contactComboBox.getValue();
        String apptType = typeTextBox.getText();
        Integer apptCustomerID = customerIDTextBox.getValue();
        Integer apptUserID = userIDComboBox.getValue();
        LocalDate apptDate = datePicker.getValue();
        LocalDateTime apptStart = null;
        LocalDateTime apptEnd = null;
        ZonedDateTime zonedStart;
        ZonedDateTime zonedEnd;

        Integer apptContactID = AccessContact.getContactID(apptContactName);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            apptStart = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTextBox.getText(), dateTimeFormatter));
        }
        catch (DateTimeParseException exception) {
            errorMessage += "Invalid start time. Please use (HH:MM) format.\n";
        }
        try {
            apptEnd = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTextBox.getText(), dateTimeFormatter));
        }
        catch (DateTimeParseException exception) {
            errorMessage += "Invalid end time. Please use (HH:MM) format.\n";
        }
        if (apptTitle.isBlank() || apptDescription.isBlank() || apptLocation.isBlank() || apptContactName == null ||
                apptType.isBlank() || apptCustomerID == null || apptUserID == null || apptEnd == null || apptStart == null) {
            errorMessage += "Please enter a valid values into all fields.\n";

            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, errorMessage, ok);
            invalid.showAndWait();
            return;
        }
        validOperationHours = validateOperationHours(apptStart, apptEnd, apptDate);
        validOverlap = overlappingCustomerAppointments(apptCustomerID, apptStart, apptEnd, apptDate);

        if (!validOperationHours) {
            errorMessage += "Invalid hours of operation. (8:00 AM to 10:00 PM EST)\n";
        }
        if (!validOverlap) {
            errorMessage += "You cannot overlap Customer Appointments.\n";
        }
        System.out.println(errorMessage);

        if (!validOverlap || !validOperationHours) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, errorMessage, ok);
            invalid.showAndWait();
        }
        else {
            zonedStart = ZonedDateTime.of(apptStart, AccessUser.getUsersTimeZone());
            zonedEnd = ZonedDateTime.of(LocalDateTime.from(apptEnd), AccessUser.getUsersTimeZone());
            String username = AccessUser.getUserLoggedOn().getUserName();

            zonedStart = zonedStart.withZoneSameInstant(ZoneOffset.UTC);
            zonedEnd = zonedEnd.withZoneSameInstant(ZoneOffset.UTC);

            Boolean successfulAdd = AccessAppointment.addAppointment(apptTitle, apptDescription, apptLocation, apptType,
                    zonedStart, zonedEnd, username, username, apptCustomerID, apptUserID, apptContactID);

            if (successfulAdd) {
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "The appointment has been scheduled successfully.", ok);
                a.showAndWait();
                screenChange(actionEvent, "/view/AppointmentsPage.fxml");
            }
            else {
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert a2 = new Alert(Alert.AlertType.WARNING, "Unable to schedule appointment.", ok);
                a2.showAndWait();
            }

        }
    }

    /**
     * Changes the screen to AppointmentsPage.fxml when the user clicks the back button.
     * @param actionEvent Back button is clicked.
     * @throws IOException
     */
    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    /**
     * Clears all text and combo boxes when the user clicks the Clear button.
     */
    public void clickClearButton() {
        datePicker.getEditor().clear();
        startTextBox.clear();
        endTextBox.clear();
        titleTextBox.clear();
        descriptionTextBox.clear();
        locationTextBox.clear();
        contactComboBox.getSelectionModel().clearSelection();
        typeTextBox.clear();
        customerIDTextBox.getSelectionModel().clearSelection();
        userIDComboBox.getSelectionModel().clearSelection();

    }

    /**
     * Adds the data to the text and combo boxes.
     * @param selectedAppointment selectedAppointment.
     * @throws SQLException
     */
    public void addData(Appointment selectedAppointment) throws SQLException {
        try {
            LocalDate dateOfAppt = selectedAppointment.getStartDateTime().toLocalDateTime().toLocalDate();
        }
        catch (NullPointerException n) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.WARNING, "Select a date.", ok);
            a.showAndWait();
        }
        ZonedDateTime startUTC = selectedAppointment.getStartDateTime().toInstant().atZone(ZoneOffset.UTC);
        ZonedDateTime endUTC = selectedAppointment.getEndDateTime().toInstant().atZone(ZoneOffset.UTC);

        ZonedDateTime localStart = startUTC.withZoneSameInstant(AccessUser.getUsersTimeZone());
        ZonedDateTime localEnd = endUTC.withZoneSameInstant(AccessUser.getUsersTimeZone());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String stringLocalStart = localStart.format(dateTimeFormatter);
        String stringLocalEnd = localEnd.format(dateTimeFormatter);

        apptIDTextBox.setText(selectedAppointment.getApptID().toString());
        titleTextBox.setText(selectedAppointment.getApptTitle());
        descriptionTextBox.setText(selectedAppointment.getApptDescription());
        locationTextBox.setText(selectedAppointment.getApptLocation());
        contactComboBox.setItems(AccessContact.getContactName());
        contactComboBox.getSelectionModel().select(selectedAppointment.getApptContactName());
        typeTextBox.setText(selectedAppointment.getApptType());
        customerIDTextBox.setItems(AccessCustomer.getAllCustomersID());
        customerIDTextBox.getSelectionModel().select(selectedAppointment.getCustomerID());
        userIDComboBox.setItems(AccessUser.getAllUserIDs());
        userIDComboBox.getSelectionModel().select(selectedAppointment.getCustomerID());
        datePicker.setValue(selectedAppointment.getStartDateTime().toLocalDateTime().toLocalDate());
        startTextBox.setText(stringLocalStart);
        endTextBox.setText(stringLocalEnd);

    }

    /**
     * Initializes the UpdateAppointmentsPage.fxml when the page opens.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timezoneLabel.setText(AccessUser.getUsersTimeZone().toString());
    }
}

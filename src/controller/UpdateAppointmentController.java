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
    public Boolean overlappingCustomerAppointmentsUpdate(int customerID, LocalDateTime start, LocalDateTime end, int apptID) throws SQLException {
        ObservableList<Appointment> overlap = AccessAppointment.filterAppointmentsByCustomerID(customerID);

        for (Appointment overlappingAppt : overlap) {
            if (overlappingAppt.getApptID() == apptID) {
                continue;
            }
            LocalDateTime overlapStart = overlappingAppt.getStartDateTime();
            LocalDateTime overlapEnd = overlappingAppt.getEndDateTime();
            //System.out.println(overlappingAppt.getStartDateTime());
            if (overlapStart.equals(start) || overlapEnd.equals(end)) {
                //System.out.println("overlap");
                return true;
            }
            else if (overlapStart.isBefore(start) && overlapEnd.isAfter(end)){
                return true;
            }
            else if (overlapStart.isAfter(start) && overlapStart.isBefore(end)) {
                return true;
            }
            else if (overlapStart.isBefore(start) && overlapEnd.isAfter(start)) {
                return true;
            }
            else if (overlapStart.isAfter(start) && overlapEnd.isBefore(start)) {
                return true;
            }
        }
        return false;
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

        int apptID = Integer.parseInt(apptIDTextBox.getText());
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
        validOverlap = overlappingCustomerAppointmentsUpdate(apptCustomerID, apptStart, apptEnd, apptID);

        if (!validOperationHours) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, "Appointment must be scheduled within operation hours! " +
                    "Please adjust your appointment time and make sure that the start time is before the end time.", ok);
            invalid.showAndWait();
        }
        else if (validOverlap) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, "You cannot overlap customer appointments! Please select another time to schedule the appointment.", ok);
            invalid.showAndWait();
        }
        else {
            //zonedStart = ZonedDateTime.of(apptStart, AccessUser.getUsersTimeZone());
            //zonedEnd = ZonedDateTime.of(apptEnd, AccessUser.getUsersTimeZone());
            String username = AccessUser.getUserLoggedOn().getUserName();

            //zonedStart = zonedStart.withZoneSameLocal(ZoneOffset.UTC);
            //zonedEnd = zonedEnd.withZoneSameLocal(ZoneOffset.UTC);

            Boolean successfulAdd = AccessAppointment.updateAppointment(apptID, apptTitle, apptDescription, apptLocation,
                    apptType, apptStart, apptEnd, username, apptCustomerID, apptUserID,
                    apptContactID);

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
            LocalDate dateOfAppt = selectedAppointment.getStartDateTime().toLocalDate();
        }
        catch (NullPointerException n) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.WARNING, "Select a date.", ok);
            a.showAndWait();
        }
        ZonedDateTime startUTC = selectedAppointment.getStartDateTime().atZone(ZoneOffset.UTC);
        ZonedDateTime endUTC = selectedAppointment.getEndDateTime().atZone(ZoneOffset.UTC);

        ZonedDateTime localStart = startUTC.withZoneSameLocal(AccessUser.getUsersTimeZone());
        ZonedDateTime localEnd = endUTC.withZoneSameLocal(AccessUser.getUsersTimeZone());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String stringLocalStart = localStart.format(dateTimeFormatter);
        String stringLocalEnd = localEnd.format(dateTimeFormatter);

        apptIDTextBox.setText(String.valueOf(selectedAppointment.getApptID()));
        titleTextBox.setText(selectedAppointment.getApptTitle());
        descriptionTextBox.setText(selectedAppointment.getApptDescription());
        locationTextBox.setText(selectedAppointment.getApptLocation());
        contactComboBox.setItems(AccessContact.getContactName());
        contactComboBox.getSelectionModel().select(selectedAppointment.getApptContactName());
        typeTextBox.setText(selectedAppointment.getApptType());
        customerIDTextBox.setItems(AccessCustomer.getAllCustomersID());
        customerIDTextBox.setValue(selectedAppointment.getCustomerID());
        userIDComboBox.setItems(AccessUser.getAllUserIDs());
        userIDComboBox.getSelectionModel().select(selectedAppointment.getUserID());
        datePicker.setValue(selectedAppointment.getStartDateTime().toLocalDate());
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

package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessContact;
import DatabaseAccess.AccessCustomer;
import DatabaseAccess.AccessUser;
import javafx.collections.FXCollections;
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
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class AddAppointmentsController implements Initializable {
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

    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void clickSaveButton(ActionEvent actionEvent) throws SQLException, IOException {
        Boolean validStart = true;
        Boolean validEnd = true;
        Boolean validOverlap = true;
        Boolean validOperationHours = true;
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
        ZonedDateTime zonedStart = null;
        ZonedDateTime zonedEnd = null;

        Integer apptContactID = AccessContact.getContactID(apptContactName);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            apptStart = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTextBox.getText(), dateTimeFormatter));
            validStart = true;
        } catch (DateTimeParseException exception) {
            validStart = false;
            errorMessage += "Invalid start time. Please use (HH:MM) format.\n";
        }
        try {
            apptEnd = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTextBox.getText(), dateTimeFormatter));
            validEnd = true;
        } catch (DateTimeParseException exception) {
            validEnd = false;
            errorMessage += "Invalid end time. Please use (HH:MM) format.\n";
        }
        if (apptTitle.isBlank() || apptDescription.isBlank() || apptLocation.isBlank() || apptContactName == null ||
                apptType.isBlank() || apptCustomerID == null || apptUserID == null || apptStart == null || apptEnd == null) {
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

        if (!validOverlap || !validOperationHours || !validStart || !validEnd) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, errorMessage, ok);
            invalid.showAndWait();
        } else {
            zonedStart = ZonedDateTime.of(apptStart, AccessUser.getUsersTimeZone());
            zonedEnd = ZonedDateTime.of(apptEnd, AccessUser.getUsersTimeZone());
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
            } else {
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert a2 = new Alert(Alert.AlertType.WARNING, "Unable to schedule appointment.", ok);
                a2.showAndWait();
            }

        }
    }

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
                if (overlapEnd.isBefore(end) && overlapEnd.isAfter(start)) {
                    return false;
                }
                else {
                    return true;
                }
            }
        }
        return true;
    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    public Boolean validateOperationHours(LocalDateTime start, LocalDateTime end, LocalDate appointmentDate) {
        ZonedDateTime zonedStart = ZonedDateTime.of(start, AccessUser.getUsersTimeZone());
        ZonedDateTime zonedEnd = ZonedDateTime.of(end, AccessUser.getUsersTimeZone());

        ZonedDateTime operationStart = ZonedDateTime.of(appointmentDate, LocalTime.of(8, 0), ZoneId.of("America/New_York"));
        ZonedDateTime operationEnd = ZonedDateTime.of(appointmentDate, LocalTime.of(22, 0), ZoneId.of("America/New_York"));

        if (zonedStart.isBefore(operationStart) || zonedStart.isAfter(operationEnd) || zonedEnd.isBefore(operationEnd) ||
        zonedEnd.isAfter(operationEnd) || zonedStart.isAfter(zonedEnd)) {
            return false;
        }
        else {
            return true;
        }

    }

    public void clickClearButton() {
        typeTextBox.clear();
        datePicker.getEditor().clear();
        startTextBox.clear();
        endTextBox.clear();
        apptIDTextBox.clear();
        titleTextBox.clear();
        descriptionTextBox.clear();
        contactComboBox.getSelectionModel().clearSelection();
        customerIDTextBox.getSelectionModel().clearSelection();
        userIDComboBox.getSelectionModel().clearSelection();
        locationTextBox.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timezoneLabel.setText("Your Time Zone:" + AccessUser.getUsersTimeZone());


        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate apptDatePicker, boolean empty) {
                super.updateItem(apptDatePicker, empty);
                setDisable(
                        empty || apptDatePicker.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                apptDatePicker.getDayOfWeek() == DayOfWeek.SUNDAY || apptDatePicker.isBefore(LocalDate.now()));
            }
        });

        try {
            customerIDTextBox.setItems(AccessCustomer.getAllCustomersID());
            userIDComboBox.setItems(AccessUser.getAllUserIDs());
            contactComboBox.setItems(AccessContact.getContactName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


package controller;

import DatabaseAccess.AccessContact;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.LogOn;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddAppointmentsController {
    public DatePicker datePicker;
    public TextField startTextBox;
    public TextField endTextBox;
    public TextField apptIDTextBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public ComboBox contactComboBox;
    public ComboBox customerIDTextBox;
    public ComboBox userIDComboBox;
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

    public void clickSaveButton(ActionEvent actionEvent) {
        Boolean validStart = true;
        Boolean validEnd = true;
        Boolean validOverlap = true;
        Boolean validOperationHours = true;
        String errorMessage = "";

        String apptTitle = titleTextBox.getText();
        String apptDescription = descriptionTextBox.getText();
        String apptLocation = locationTextBox.getText();
        String apptContactName = (String) contactComboBox.getValue();
        String apptType = typeTextBox.getText();
        Integer apptCustomerID = (Integer) customerIDTextBox.getValue();
        Integer apptUserID = (Integer) userIDComboBox.getValue();
        LocalDate apptDate = datePicker.getValue();
        LocalDateTime apptStart = null;
        LocalDateTime apptEnd = null;
        ZonedDateTime zonedStart = null;
        ZonedDateTime zonedEnd = null;

        Integer apptContactID = AccessContact.getContactID(apptContactName);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            apptStart = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(startTextBox.getText(), dateTimeFormatter));
            validEnd = true;
        }
        catch (DateTimeParseException exception) {
            validStart = false;
            errorMessage += "Invalid start time. Please use (HH:MM) format.\n"
        }
        try {
            apptEnd = LocalDateTime.of(datePicker.getValue(), LocalTime.parse(endTextBox.getText(), dateTimeFormatter));
            validEnd = true;
        }
        catch (DateTimeParseException exception) {
            validEnd = false;
            errorMessage += "Invalid end time. Please use (HH:MM) format.\n"
        }
        if (apptTitle.isBlank() || apptDescription.isBlank() || apptLocation.isBlank() || apptContactName == null ||
        apptType.isBlank() || apptCustomerID == null || apptUserID == null || apptEnd == null || apptStart == null) {
            errorMessage += "Please enter a valid values into all fields.\n";

            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, errorMessage, ok);
            invalid.showAndWait();
            return;
        }
        validOperationHours = val
    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    public Boolean validateOperationHours(LocalDateTime start, LocalDateTime end, LocalDate appointmentDate) {
        ZonedDateTime zonedStart = ZonedDateTime.of(start, LogOn.getUsersTimeZone());
        ZonedDateTime zonedEnd = ZonedDateTime.of(end, LogOn.getUsersTimeZone());

        ZonedDateTime operationStart = ZonedDateTime.of(appointmentDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));

    }

    public void clickClearButton(ActionEvent actionEvent) {
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
}

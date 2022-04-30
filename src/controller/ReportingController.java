package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessContact;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ReportingController {
    public TextArea reportsTextArea;
    public RadioButton reportMandTbutton;
    public ToggleGroup toggleGroup;
    public RadioButton contactScheduleButton;
    public Button backButton;
    public RadioButton timePerContactButton;

    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void clickReportMandTbutton() throws SQLException {
        reportMandTbutton.setSelected(true);
        contactScheduleButton.setSelected(false);
        timePerContactButton.setSelected(false);

        ObservableList<String> report = AccessAppointment.createReportTypeAndDate();

        for (String r : report) {
            reportsTextArea.appendText(r);
        }
    }

    public void clickContactScheduleButton() throws SQLException {
        contactScheduleButton.setSelected(true);
        reportMandTbutton.setSelected(false);
        timePerContactButton.setSelected(false);

        ObservableList<String> contact = AccessContact.getContactName();

        for (String c : contact) {
            String id = AccessContact.getContactID(c).toString();
            reportsTextArea.appendText("Contact Name: " + c + " Contact ID: " + id + "\n");
            ObservableList<String> appointments = AccessContact.getContactAppointments(id);
            if (appointments.isEmpty()) {
                reportsTextArea.appendText("No appointments scheduled for Contact ID: " + id + "\n");
            }
            for (String a : appointments) {
                reportsTextArea.appendText(a);
            }
        }
    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    public void clickTimePerContactButton() throws SQLException {
        timePerContactButton.setSelected(true);
        reportMandTbutton.setSelected(false);
        contactScheduleButton.setSelected(false);

        ObservableList<String> timePerContact = AccessContact.getContactName();

        for (String t : timePerContact) {
            String id = AccessContact.getContactID(t).toString();
            reportsTextArea.appendText("Contact Name: " + t + "Contact ID: " + id + "\n");
            reportsTextArea.appendText("Total time scheduled for Contact: " + AccessContact.calculateAppointmentTime(id) + "\n");
        }
    }
}

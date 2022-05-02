package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessUser;
import database.LogOnRecord;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    public Label ApplicationTitleLabel;
    public Label UserNameLabel;
    public Label PasswordLabel;
    public TextField UsernameText;
    public TextField PasswordText;
    public Button ResetButton;
    public Button SignInButton;
    public Button ExitButton;
    public Label ZoneIDLabel;

    private String errorHead;
    private String errorTitle;
    private String errorText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale userLocale = Locale.getDefault();
        Locale.setDefault(userLocale);
        ZoneIDLabel.setText(ZoneId.systemDefault().toString());
        resourceBundle = ResourceBundle.getBundle("language/login", userLocale);
        ApplicationTitleLabel.setText(resourceBundle.getString("ApplicationTitleLabel"));
        UserNameLabel.setText(resourceBundle.getString("UserNameLabel"));
        PasswordLabel.setText(resourceBundle.getString("PasswordLabel"));
        SignInButton.setText(resourceBundle.getString("SignInButton"));
        ResetButton.setText(resourceBundle.getString("ResetButton"));
        ExitButton.setText(resourceBundle.getString("ExitButton"));
    }

    public void PressResetButton() {
        UsernameText.clear();
        PasswordText.clear();
    }

    public void PressSignInButton(ActionEvent actionEvent) throws SQLException, IOException {
        String username = UsernameText.getText();
        String password = PasswordText.getText();
        boolean valid = AccessUser.attemptLogin(username, password);
        LogOnRecord.generateLogOnFile(username, valid);
        if (valid) {
            ObservableList<Appointment> upcoming = DatabaseAccess.AccessAppointment.appointmentWithin15MinOfLogOn();
            if (!upcoming.isEmpty()) {
                for (Appointment a : upcoming) {
                    String notify = "Upcoming appointment with ID: " + a.getApptID() + " starts at " + a.getBeginDateTime().toString();
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.WARNING, notify, ok);
                    alert.showAndWait();
                }
            }
            else {
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "There are no upcoming appointments within the next 15 minutes.", ok);
                a.showAndWait();
            }
            screenChange(actionEvent, "/view/AppointmentsPage.fxml");
        }
        else {
            Locale userLocale = Locale.getDefault();
            ResourceBundle rb = ResourceBundle.getBundle("language.login");
            ButtonType ok = new ButtonType(rb.getString("okButton"), ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.WARNING, rb.getString("logOnFailButton"), ok);
            a.showAndWait();
        }
    }


    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void PressExitButton() {
        AccessUser.userLogOff();
        System.exit(0);
    }
}
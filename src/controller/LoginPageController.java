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

/**
 * LoginPageController class takes input from a user and checks username and password against values in the MySQL Database.
 * Allows for timezone and language to be auto generated given parameters by the users machine settings.
 */
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

    /**
     * Initializes the LoginPageController when the program is launched and gets the Locale and language information to display to the user
     * @param url url
     * @param resourceBundle resourceBundle
     */
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

    /**
     * Clears the username and password text boxes when the user clicks the clear button.
     */
    public void PressResetButton() {
        UsernameText.clear();
        PasswordText.clear();
    }

    /**
     * Takes the username and password input from the user and validates it against the MySQL Database values to authorize the user login.
     * If valid, it opens the AppointmentsPage.fxml.
     * @param actionEvent Sign In button is clicked.
     * @throws SQLException SQLException.
     * @throws IOException IOException.
     */
    public void PressSignInButton(ActionEvent actionEvent) throws SQLException, IOException {
        String username = UsernameText.getText();
        String password = PasswordText.getText();
        boolean valid = AccessUser.attemptLogin(username, password);
        LogOnRecord.generateLogOnFile(username, valid);
        if (valid) {
            ObservableList<Appointment> upcoming = DatabaseAccess.AccessAppointment.appointmentWithin15MinOfLogOn();
            if (!upcoming.isEmpty()) {
                for (Appointment a : upcoming) {
                    String notify = "Upcoming appointment with ID: " + a.getApptID() + " starts at " + a.getStartDateTime().toString();
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

    /**
     * Called by other methods to change the screen when a button is pressed.
     * @param actionEvent Button is clicked.
     * @param path Path the screen change takes.
     * @throws IOException IOException.
     */
    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    /**
     * Exits the program when the Exit button is clicked by a user.
     */
    public void PressExitButton() {
        AccessUser.userLogOff();
        System.exit(0);
    }
}
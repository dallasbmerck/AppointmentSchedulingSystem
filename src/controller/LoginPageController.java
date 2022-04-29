package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale userLocale = Locale.getDefault();
        ZoneIDLabel.setText(ZoneId.systemDefault().toString());
        resourceBundle = ResourceBundle.getBundle("laguage/LoginPage");
        ApplicationTitleLabel.setText(resourceBundle.getString(ApplicationTitleLabel));
    }

    public void PressResetButton(ActionEvent actionEvent) throws IOException {
        UsernameText.clear();
        PasswordText.clear();
    }

    public void PressSignInButton(ActionEvent actionEvent) {
    }

    public void PressExitButton(ActionEvent actionEvent) throws IOException {

    }
}
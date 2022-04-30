package controller;

import DatabaseAccess.AccessCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    public Label customerIDLabel;
    public Label countryLabel;
    public Label divisionLabel;
    public Label nameLabel;
    public Label addressLabel;
    public Label postCodeLabel;
    public Label phoneLabel;
    public TextField customerIDTextBox;
    public ComboBox<String> countryComboBox;
    public ComboBox<String> divisionComboBox;
    public TextField nameTextBox;
    public TextField addressTextBox;
    public TextField postCodeTextBox;
    public TextField phoneTextBox;
    public Button saveButton;
    public Button clearButton;
    public Button backButton;

    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void clickSaveButton(ActionEvent actionEvent) throws SQLException, IOException {
        String customerCountry = countryComboBox.getValue();
        String customerDivision = divisionComboBox.getValue();
        String customerName = nameTextBox.getText();
        String customerAddress = addressTextBox.getText();
        String customerPostCode = postCodeTextBox.getText();
        String customerPhone = phoneTextBox.getText();

        if (customerCountry.isBlank() || customerDivision.isBlank() || customerName.isBlank() || customerAddress.isBlank() ||
        customerPostCode.isBlank() || customerPhone.isBlank()) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.WARNING, "Please enter a valid value into each field.", ok);
            a.showAndWait();
        }

        Boolean succesfullyAddedCustomer = AccessCustomer.addCustomers(customerCountry, customerDivision,customerName, customerAddress,
                customerPostCode, customerPhone, AccessCustomer.getDivisionID(customerDivision));

        if(succesfullyAddedCustomer) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "The Customer has been added successfully.", ok);
            a.showAndWait();
            clickClearButton(actionEvent);
            screenChange(actionEvent, "/view/CustomerPage.fxml");
        }
        else {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a2 = new Alert(Alert.AlertType.WARNING, "Unable to add customer.", ok);
            a2.showAndWait();
        }
    }

    public void clickClearButton(ActionEvent actionEvent) {
        countryComboBox.getItems().clear();
        divisionComboBox.getItems().clear();
        nameTextBox.clear();
        addressTextBox.clear();
        postCodeTextBox.clear();
        phoneTextBox.clear();
    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/CustomerPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(AccessCustomer.getAllCountries());
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        countryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                divisionComboBox.getItems().clear();
                divisionComboBox.setDisable(true);
            }
            else {
                divisionComboBox.setDisable(false);
                try {
                    divisionComboBox.setItems(AccessCustomer.getFirstLevelDivisions(countryComboBox.getValue()));
                }
                catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }
}

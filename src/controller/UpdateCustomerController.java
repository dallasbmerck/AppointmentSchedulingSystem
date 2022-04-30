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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {
    public Label customerIDLabel;
    public Label countryLabel;
    public Label divisionLabel;
    public Label nameLabel;
    public Label addressLabel;
    public Label postCodeLabel;
    public Label phoneLable;
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
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void addData(Customer customer) throws SQLException {
    countryComboBox.setItems(AccessCustomer.getAllCountries());
    countryComboBox.getSelectionModel().select(customer.getCustomerCountryName());
    divisionComboBox.setItems(AccessCustomer.getFirstLevelDivisions(customer.getCustomerCountryName()));
    divisionComboBox.getSelectionModel().select(customer.getCustomerDivision());
    customerIDTextBox.setText(customer.getCustomerID().toString());
    nameTextBox.setText(customer.getCustomerName());
    addressTextBox.setText(customer.getCustomerAddress());
    postCodeTextBox.setText(customer.getCustomerPostCode());
    phoneTextBox.setText(customer.getCustomerPhoneNumber());
    }

    public void clickSaveButton() throws SQLException {
        String customerCountry = countryComboBox.getValue();
        String customerDivision = divisionComboBox.getValue();
        String customerName = nameTextBox.getText();
        String customerAddress = addressTextBox.getText();
        String customerPostCode = postCodeTextBox.getText();
        String customerPhone = phoneTextBox.getText();
        Integer customerID = Integer.parseInt(customerIDTextBox.getText());

        if (customerCountry.isBlank() || customerDivision.isBlank() || customerName.isBlank() || customerAddress.isBlank() ||
        customerPostCode.isBlank() || customerPhone.isBlank()) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a = new Alert(Alert.AlertType.WARNING, "Please enter valid values into all customer fields.", ok);
            a.showAndWait();
        }

        Boolean saveCustomerSuccess = AccessCustomer.updateCustomers(customerDivision, customerName, customerAddress,
                customerPostCode, customerPhone, customerID);

        if (saveCustomerSuccess) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a2 = new Alert(Alert.AlertType.CONFIRMATION, "Customer values have been updated successfully.", ok);
            a2.showAndWait();
        }
        else {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a3 = new Alert(Alert.AlertType.WARNING, "Customer values have NOT been updated.", ok);
            a3.showAndWait();
        }
    }

    public void clickClearButton() {
        countryComboBox.getSelectionModel().clearSelection();
        divisionComboBox.getSelectionModel().clearSelection();
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
        } );
    }
}

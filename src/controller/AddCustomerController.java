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
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * AddCustomerController class used to control input and output to the AddCustomerPage.fxml
 */
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

    /**
     * Used to show a new screen when an action event is executed by the user.
     * @param actionEvent Button click.
     * @param path Path taken by the screen change.
     * @throws IOException IOException.
     */
    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    /**
     * Saves the new customer to the MySQL Database when the save button is pressed.
     * @param actionEvent Save button is clicked.
     * @throws SQLException SQLException.
     * @throws IOException IOException.
     */
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
            clickClearButton();
            screenChange(actionEvent, "/view/CustomerPage.fxml");
        }
        else {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert a2 = new Alert(Alert.AlertType.WARNING, "Unable to add customer.", ok);
            a2.showAndWait();
        }
    }

    /**
     * Clears all data fields in the AddCustomerPage.fxml when the user presses the cleat button.
     */
    public void clickClearButton() {
        countryComboBox.getItems().clear();
        divisionComboBox.getItems().clear();
        nameTextBox.clear();
        addressTextBox.clear();
        postCodeTextBox.clear();
        phoneTextBox.clear();
    }

    /**
     * Changes the screen back to the CustomerPage.fxml when the user clicks the back button.
     * @param actionEvent Back button is clicked.
     * @throws IOException IOException.
     */
    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/CustomerPage.fxml");
    }

    /**
     * Initializes the AddCustomerPage.fxml when it the screen is changed to it.
     *
     * Lambda expression on line 131 that adds a listener to the country combobox without needing to declare another method elsewhere.
     * @param url Stage path.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            countryComboBox.setItems(AccessCustomer.getAllCountries());
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        //Lambda expression 2.
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

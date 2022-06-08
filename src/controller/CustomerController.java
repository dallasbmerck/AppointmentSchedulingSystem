package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessCustomer;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * CustomerController class allows for input and output of data to the CustomerPage.fxml.
 */
public class CustomerController implements Initializable {
    public TableView<Customer> customerTableView;
    public TableColumn<Customer, Integer> customerIDCol;
    public TableColumn<Customer, String> customerNameCol;
    public TableColumn<Customer, String> countryCol;
    public TableColumn<Customer, String> divisionCol;
    public TableColumn<Customer, String> addressCol;
    public TableColumn<Customer, String> postCodeCol;
    public TableColumn<Customer, String> phoneCol;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;
    public Button backButton;

    /**
     * Gets called by other methods to switch screens in the application.
     * @param actionEvent Button is clicked.
     * @param path Path the switch takes.
     * @throws IOException IOException.
     */
    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(getClass().getResource(path));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    /**
     * Adds data to the customers table view using an observable list.
     * @param customerList Observable list of customers.
     */
    public void addDataToCustomersTable(ObservableList<Customer> customerList) throws SQLException {

        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("CustomerCountryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        postCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        customerTableView.setItems(customerList);
    }

    /**
     * Changes the screen when the user clicks the add customer button.
     * @param actionEvent Add button is clicked.
     * @throws IOException IOException.
     */
    public void clickAddButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AddCustomerPage.fxml");
    }

    /**
     * Checks to make sure the user has selected a customer and then loads the customer data into CustomerUpdatePage.fxml when the edit button is clicked.
     * @param actionEvent Edit button is clicked.
     * @throws IOException
     * @throws SQLException
     */
    public void clickEditButton(ActionEvent actionEvent) throws IOException, SQLException {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert noSelection = new Alert(Alert.AlertType.WARNING, "No customer selected to edit.", ok);
            noSelection.showAndWait();
        }
        else {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/view/CustomerUpdatePage.fxml"));
            Parent p = load.load();
            Scene s = new Scene(p);

            UpdateCustomerController controller = load.getController();
            controller.addData(customer);
            Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            newWindow.setScene(s);
        }
    }

    /**
     * Deletes a selected customer when the user clicks the delete button.
     * @throws SQLException SQLException.
     */
    public void clickDeleteButton() throws SQLException {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert noSelection = new Alert(Alert.AlertType.WARNING, "No customer selected to delete.", ok);
            noSelection.showAndWait();
        }
        else {
            ButtonType yes = ButtonType.YES;
            ButtonType no = ButtonType.NO;
            Alert deleteCustomer = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete Customer " +
                    customer.getCustomerID() + " and all of their associated appointments?", yes, no);
            Optional<ButtonType> selected = deleteCustomer.showAndWait();

            if (selected.get() == ButtonType.YES) {
                Boolean customerAppointmentDelete = AccessAppointment.deleteAllAppointmentsByCustomerID(customer.getCustomerID());
                Boolean customerDelete = AccessCustomer.deleteSelectedCustomer(customer.getCustomerID());

                if (customerAppointmentDelete && customerDelete) {
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert delete = new Alert(Alert.AlertType.CONFIRMATION, "Customer " + customer.getCustomerID() + " and all of their associated appointments have been deleted.", ok);
                    delete.showAndWait();
                }
                else {
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert delete = new Alert(Alert.AlertType.WARNING, "Unable to delete customer and their associated appointments.", ok);
                    delete.showAndWait();
                }
                try {
                    addDataToCustomersTable(AccessCustomer.getAllCustomers());
                }
                catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
    }

    /**
     * Switches the screen to AppointmentsPage.fxml when the user clicks the back button.
     * @param actionEvent Back button is pressed.
     * @throws IOException IOException.
     */
    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    /**
     * Initialized the  Customer table view with all customer from the MySQL Database.
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addDataToCustomersTable(AccessCustomer.getAllCustomers());
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}

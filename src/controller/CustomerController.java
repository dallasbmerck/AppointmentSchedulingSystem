package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessCustomer;
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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

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

    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void addDataToCustomersTable(ObservableList<Customer> list) {
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postCodeCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerTableView.setItems(list);
    }

    public void clickAddButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AddCustomerPage.fxml");
    }

    public void clickEditButton(ActionEvent actionEvent) throws IOException, SQLException {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert noSelection = new Alert(Alert.AlertType.WARNING, "No customer selected to edit.", ok);
            noSelection.showAndWait();
        }

        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/view/CustomerUpdatePage.fxml"));
        Parent p = load.load();
        Scene s = new Scene(p);

        UpdateCustomerController controller = load.getController();
        controller.addData(customer);
        Stage newWindow = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(s);
    }

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
                    customer.getCustomerID() + "and all of their associated appointments?", yes, no);
            Optional<ButtonType> selected = deleteCustomer.showAndWait();

            if (selected.get() == ButtonType.YES) {
                Boolean customerAppointmentDelete = AccessAppointment.deleteAllAppointmentsByCustomerID(customer.getCustomerID());
                Boolean customerDelete = AccessCustomer.deleteSelectedCustomer(customer.getCustomerID());

                if (customerAppointmentDelete && customerDelete) {
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert delete = new Alert(Alert.AlertType.CONFIRMATION, "The Customer and all of their associated appointments have been deleted.", ok);
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

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

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

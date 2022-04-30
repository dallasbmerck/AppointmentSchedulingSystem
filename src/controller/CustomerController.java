package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
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
        Parent p = FXMLLoader.load(getClass().getResource(path));
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

    public void clickEditButton(ActionEvent actionEvent) {
    }

    public void clickDeleteButton(ActionEvent actionEvent) {
    }

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AppointmentsPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

        }
    }
}

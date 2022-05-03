package controller;

import DatabaseAccess.AccessAppointment;
import DatabaseAccess.AccessUser;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
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
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.Timestamp;

public class AppointmentsController implements Initializable {
    public TableView<Appointment> appointmentsTableView;
    public TableColumn<Appointment, Integer> apptIDCol;
    public TableColumn<Appointment, String> titleCol;
    public TableColumn<Appointment, String> descriptionCol;
    public TableColumn<Appointment, String> locationCol;
    public TableColumn<Appointment, String> contactCol;
    public TableColumn<Appointment, String> typeCol;
    public TableColumn<Appointment, ZonedDateTime> startCol;
    public TableColumn<Appointment, ZonedDateTime> endCol;
    public TableColumn<Appointment, Integer> customerIDCol;
    public TableColumn<Appointment, Integer> userIDCol;
    public RadioButton filterMonthButton;
    public RadioButton filterWeekButton;
    public RadioButton showAllButton;
    public Label selectedTimeLabel;
    public Label showSelectedTimeLabel;
    public Button previousButton;
    public Button nextButton;
    public Button logoutButton;
    public Button customersButton;
    public Button reportsButton;
    public Button deleteApptButton;
    public Button editApptButton;
    public Button newApptButton;
    public ToggleGroup setToggle;

    ZonedDateTime start;
    ZonedDateTime end;

    public void addDataToTable(ObservableList<Appointment> list) {
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("apptContactName"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        appointmentsTableView.setItems(list);
    }

    public void screenChange(ActionEvent actionEvent, String path) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(p);
        Stage newWindow = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void toggleGroup() {
        setToggle = new ToggleGroup();
        showAllButton.setToggleGroup(setToggle);
        filterWeekButton.setToggleGroup(setToggle);
        filterMonthButton.setToggleGroup(setToggle);
    }

    public void clickFilterMonth(ActionEvent actionEvent) throws SQLException {
        filterWeekButton.setSelected(false);
        showAllButton.setSelected(false);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filterByMonth;
        start = ZonedDateTime.now(AccessUser.getUsersTimeZone());
        end = start.plusMonths(1);

        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        filterByMonth = AccessAppointment.filterAppointmentsByDate(startUTC, endUTC);

        addDataToTable(filterByMonth);

        showSelectedTimeLabel.setText(start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());
    }

    public void clickFilterWeek() throws SQLException {
        showAllButton.setSelected(false);
        filterMonthButton.setSelected(false);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filterByWeek = FXCollections.observableArrayList();
        start = ZonedDateTime.now(AccessUser.getUsersTimeZone());
        end = start.plusWeeks(1);

        ZonedDateTime startUTC = start.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUTC = end.withZoneSameInstant(ZoneOffset.UTC);

        filterByWeek = AccessAppointment.filterAppointmentsByDate(startUTC, endUTC);

        addDataToTable(filterByWeek);

        //appointmentsTableView.setItems(filterByWeek);

        showSelectedTimeLabel.setText(start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());

    }

    public void clickShowAll() {
        filterWeekButton.setSelected(false);
        filterMonthButton.setSelected(false);

        ObservableList<Appointment> allAppointments;
        try {
            allAppointments = AccessAppointment.showAllAppointments();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            DatabaseConnection.openConnection();
            try {
                allAppointments = AccessAppointment.showAllAppointments();
            }
            catch (SQLException sqlException2) {
                sqlException2.printStackTrace();
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalid = new Alert(Alert.AlertType.WARNING, "Database connection failed, please restart the application.", ok);
                invalid.showAndWait();
                return;
            }
        }
        addDataToTable(allAppointments);
        showSelectedTimeLabel.setText("Showing all Appointments.");
        start = null;
    }

    public void clickNextButton() throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filterGoForward;

        if(setToggle.getSelectedToggle() == filterWeekButton) {
            ZonedDateTime s = start.plusWeeks(1);
            ZonedDateTime e = end.plusWeeks(1);

            start = s;
            end = e;

            s = s.withZoneSameInstant(ZoneOffset.UTC);
            e = e.withZoneSameInstant(ZoneOffset.UTC);

            filterGoForward = AccessAppointment.filterAppointmentsByDate(s, e);

            addDataToTable(filterGoForward);
            showSelectedTimeLabel.setText(start.format(dateTimeFormatter) +" - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());
        }
        if (setToggle.getSelectedToggle() == filterMonthButton) {
            ZonedDateTime s = start.plusMonths(1);
            ZonedDateTime e = end.plusMonths(1);

            start = s;
            end = e;

            s = s.withZoneSameInstant(ZoneOffset.UTC);
            e = e.withZoneSameInstant(ZoneOffset.UTC);

            filterGoForward = AccessAppointment.filterAppointmentsByDate(s, e);

            addDataToTable(filterGoForward);
            showSelectedTimeLabel.setText(start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());
        }
    }

    public void clickPreviousButton() throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filterGoback;

        if(setToggle.getSelectedToggle() == filterWeekButton) {
            ZonedDateTime s = start.minusWeeks(1);
            ZonedDateTime e = end.minusWeeks(1);

            start = s;
            end = e;

            s = s.withZoneSameInstant(ZoneOffset.UTC);
            e = e.withZoneSameInstant(ZoneOffset.UTC);

            filterGoback = AccessAppointment.filterAppointmentsByDate(s, e);

            addDataToTable(filterGoback);
            showSelectedTimeLabel.setText(start.format(dateTimeFormatter) +" - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());
        }
        if (setToggle.getSelectedToggle() == filterMonthButton) {
            ZonedDateTime s = start.minusMonths(1);
            ZonedDateTime e = end.minusMonths(1);

            start = s;
            end = e;

            s = s.withZoneSameInstant(ZoneOffset.UTC);
            e = e.withZoneSameInstant(ZoneOffset.UTC);

            filterGoback = AccessAppointment.filterAppointmentsByDate(s, e);

            addDataToTable(filterGoback);
            showSelectedTimeLabel.setText(start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter) + " " + AccessUser.getUsersTimeZone());
        }
    }

    public void clickLogoutButton(ActionEvent actionEvent) throws IOException {
        ButtonType yes = ButtonType.YES;
        ButtonType no = ButtonType.NO;
        Alert logOut = new Alert(Alert.AlertType.WARNING, "Are you sure you want to log out of the customer scheduling application?", yes, no);
        Optional<ButtonType> optional = logOut.showAndWait();

        if (optional.get() == ButtonType.YES) {
            AccessUser.userLogOff();
            screenChange(actionEvent, "/view/LoginPage.fxml");
        }
    }

    public void checkAppointmentCanceled(ObservableList<Appointment> list) {
        list.forEach((appointment) -> {
            if (appointment.getApptType().equalsIgnoreCase("canceled")) {
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalid = new Alert(Alert.AlertType.WARNING, "Appointment " + appointment.getApptID() + " is canceled.", ok);
                invalid.showAndWait();
            }
        });
    }
    public void clickCustomersButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/CustomerPage.fxml");
    }

    public void clickReportsButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/ReportingPage.fxml");
    }

    public void clickDeleteApptButton() throws SQLException {
        Appointment appointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, "Select an appointment to delete.", ok);
            invalid.showAndWait();
        }
        else {
            ButtonType yes = ButtonType.YES;
            ButtonType no = ButtonType.NO;
            Alert confirmDelete = new Alert(Alert.AlertType.WARNING, "Are you certain you want to delete Appointment: " +
                     appointment.getApptID() + "?", yes, no);
            Optional<ButtonType> choice = confirmDelete.showAndWait();

            if (choice.get() == ButtonType.YES) {
                Boolean b = AccessAppointment.deleteAppointment(appointment.getApptID());
                if(b) {
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert showDelete = new Alert(Alert.AlertType.CONFIRMATION, "Appointment: " + appointment.getApptID() + " has been deleted.", ok);
                    showDelete.showAndWait();
                }
                else {
                    ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert notDeleted = new Alert(Alert.AlertType.WARNING, "Unable to delete Appointment: " + appointment.getApptID(), ok);
                    notDeleted.showAndWait();
                }
                try {
                    addDataToTable(AccessAppointment.showAllAppointments());
                }
                catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
    }

    public void clickEditApptButton(ActionEvent actionEvent) throws IOException, SQLException {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null) {
            ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalid = new Alert(Alert.AlertType.WARNING, "Select an appointment to edit.", ok);
            invalid.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateAppointmentPage.fxml"));
        Parent p = loader.load();
        Scene s = new Scene(p);

        UpdateAppointmentController controller = loader.getController();
        controller.addData(selectedAppointment);
        Stage newWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        newWindow.setScene(s);
    }

    public void clickNewApptButton(ActionEvent actionEvent) throws IOException {
        screenChange(actionEvent, "/view/AddAppointmentsPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAllButton.setSelected(true);
        toggleGroup();

        ObservableList<Appointment> allAppointments = null;
        try {
            allAppointments = AccessAppointment.showAllAppointments();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            DatabaseConnection.openConnection();
            try {
                allAppointments = AccessAppointment.showAllAppointments();
            }
            catch (SQLException sqlException2) {
                sqlException2.printStackTrace();
                ButtonType ok = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalid = new Alert(Alert.AlertType.WARNING, "Unable to connect to database. Please restart.", ok);
                invalid.showAndWait();
            }
        }
        addDataToTable(allAppointments);
        checkAppointmentCanceled(allAppointments);
    }
}

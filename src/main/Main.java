package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginPage.fxml")));
        stage.setTitle("C195 Software II - Advanced Java Concepts");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args){

        DatabaseConnection.openConnection();
        launch(args);
        DatabaseConnection.closeConnection();
    }
}

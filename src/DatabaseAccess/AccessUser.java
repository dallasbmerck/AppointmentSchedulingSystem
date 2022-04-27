package DatabaseAccess;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessUser {

    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> userID = FXCollections.observableArrayList();
        PreparedStatement SQLCommand = DatabaseConnection.initiateConnection().prepareStatement("SELECT DISTINCT " +
                "User_ID FROM users;");
        ResultSet resultSet = SQLCommand.executeQuery();

        while (resultSet.next()) {
            userID.add(resultSet.getInt("User_ID"));
        }
        SQLCommand.close();
        return userID;
    }
}

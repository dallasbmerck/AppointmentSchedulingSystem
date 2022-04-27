package model;

public class User {
    public int userID;
    public String userName;

    public User(String userNameInput, Integer userIDInput) {
        userID = userIDInput;
        userName = userNameInput;
    }


    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

}

package model;

public class User {
    private String username;
    private Integer userID;

    public User(String usernameInput, Integer userIDInput) {
        username = usernameInput;
        userID = userIDInput;
    }

    public String getUserName() {
        return username;
    }

    public Integer getUserID() {
        return userID;
    }

}

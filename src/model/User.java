package model;

/**
 * User class used to create methods to access and generate data for the MySQL Database.
 */
public class User {
    private String username;
    private Integer userID;

    /**
     * Constructor that creates a new instance of User.
     * @param usernameInput User_Name - unique.
     * @param userIDInput User_ID - primary key.
     */
    public User(String usernameInput, Integer userIDInput) {
        username = usernameInput;
        userID = userIDInput;
    }

    /**
     * Getter for the username of a user.
     * @return User_Name.
     */
    public String getUserName() {
        return username;
    }

    /**
     * Getter for the user ID of a user.
     * @return User_ID.
     */
    public Integer getUserID() {
        return userID;
    }

}

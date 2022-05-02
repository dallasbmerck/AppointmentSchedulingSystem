package model;

/**
 * Contact class used to create methods to access and generate data for contacts in the MySQL database.
 *
 * @author Dallas Merck
 */
public class Contact {
    public int ID;
    public String name;
    public String email;

    /**
     * Contact constructor that creates a new instance of contact.
     * @param ID Contact_ID.
     * @param name Contact_Name.
     * @param email Email.
     */
    public Contact(int ID, String name, String email) {
        this.ID = ID;
        this.name = name;
        this.email = email;
    }

    /**
     * Getter for the contact ID.
     * @return Contact_ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Gette for the contact name.
     * @return Contact_Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the contact email.
     * @return Email.
     */
    public String getEmail() {
        return email;
    }
}

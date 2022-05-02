package model;

/**
 * Customer class used to create methods that access and generate data for the MySQL database.
 *
 * @author Dallas Merck.
 */
public class Customer {
    private Integer ID;
    private String name;
    private String address;
    private String postCode;
    private String phoneNumber;
    private String division;
    private Integer divisionID;
    private String countryName;

    /**
     * Customer contructor that creates a new instance of customer.
     * @param IDInput Customer_ID - primary key.
     * @param nameInput Customer_Name.
     * @param addressInput Address.
     * @param postCodeInput Postal_Code.
     * @param phoneNumberInput Phone.
     * @param divisionInput Division.
     * @param divisionIDInput Division_ID -foreign key.
     * @param countryNameInput Country_ID.
     */
    public Customer(int IDInput, String nameInput, String addressInput, String postCodeInput, String phoneNumberInput,String divisionInput, int divisionIDInput, String countryNameInput) {
        ID = IDInput;
        name = nameInput;
        address = addressInput;
        postCode = postCodeInput;
        phoneNumber = phoneNumberInput;
        division = divisionInput;
        divisionID = divisionIDInput;
        countryName = countryNameInput;
    }

    /**
     * Getter for the customer ID.
     * @return Customer_ID.
     */
    public Integer getCustomerID() {
        return ID;
    }

    /**
     * Getter for the customer name.
     * @return Customer_Name.
     */
    public String getCustomerName() {
        return name;
    }

    /**
     * Getter for the customer address.
     * @return Address.
     */
    public String getCustomerAddress() {
        return address;
    }

    /**
     * Getter for the postal code.
     * @return Postal_Code.
     */
    public String getCustomerPostCode() {
        return postCode;
    }

    /**
     * Getter for the phone number
     * @return Phone.
     */
    public String getCustomerPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter for the customers division.
     * @return Division.
     */
    public String getCustomerDivision() {
        return division;
    }

    /**
     * Getter for the customer division ID.
     * @return Division_ID.
     */
    public Integer getCustomerDivisionID() {
        return divisionID;
    }

    /**
     * Getter for the customer's country.
     * @return Country_ID.
     */
    public String getCustomerCountryName() {
        return countryName;
    }
}

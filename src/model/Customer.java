package model;

/**
 * Customer class used to create methods that access and generate data for the MySQL database.
 *
 * @author Dallas Merck.
 */
public class Customer {
    private Integer customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostCode;
    private String customerPhoneNumber;
    private Integer customerDivisionID;
    private String customerDivision;
    private String countryOfCustomer;

    /**
     * Customer contructor that creates a new instance of customer.
     * @param idInput Customer_ID - primary key.
     * @param nameInput Customer_Name.
     * @param addressInput Address.
     * @param postCodeInput Postal_Code.
     * @param phoneNumberInput Phone.
     * @param divisionInput Division.
     * @param divisionIDInput Division_ID -foreign key.
     * @param countryInput Country_ID.
     */
    public Customer(Integer idInput, String nameInput, String addressInput, String postCodeInput, String phoneNumberInput,
                    String divisionInput, Integer divisionIDInput, String countryInput) {
        customerID = idInput;
        customerName = nameInput;
        customerAddress = addressInput;
        customerPostCode = postCodeInput;
        customerPhoneNumber = phoneNumberInput;
        customerDivision = divisionInput;
        customerDivisionID = divisionIDInput;
        countryOfCustomer = countryInput;
    }

    /**
     * Getter for the customer ID.
     * @return Customer_ID.
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * Getter for the customer name.
     * @return Customer_Name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Getter for the customer address.
     * @return Address.
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Getter for the postal code.
     * @return Postal_Code.
     */
    public String getCustomerPostCode() {
        return customerPostCode;
    }

    /**
     * Getter for the phone number
     * @return Phone.
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Getter for the customers division.
     * @return Division.
     */
    public String getCustomerDivision() {
        return customerDivision;
    }

    /**
     * Getter for the customer division ID.
     * @return Division_ID.
     */
    public Integer getCustomerDivisionID() {
        return customerDivisionID;
    }

    /**
     * Getter for the customer's country.
     * @return Country_ID.
     */
    public String getCustomerCountryName() {
        return countryOfCustomer;
    }
}

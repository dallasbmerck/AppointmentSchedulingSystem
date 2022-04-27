package model;

public class Customer {
    private Integer ID;
    private String name;
    private String address;
    private String postCode;
    private String phoneNumber;
    private String division;
    private Integer divisionID;
    private String countryName;

    public Customer(int IDInput, String nameInput, String addressInput, String postCodeInput, String phoneNumberInput,String divisionInput, int divisionIDInput, String countryNameInput) {
        ID = IDInput;
        name = nameInput;
        address = addressInput;
        postCode = postCodeInput;
        phoneNumber = phoneNumberInput;
        divisionID = divisionIDInput;
        countryName = countryNameInput;
    }

    public Integer getCustomerID() {
        return ID;
    }

    public String getCustomerName() {
        return name;
    }

    public String getCustomerAddress() {
        return address;
    }

    public String getCustomerPostCode() {
        return postCode;
    }

    public String getCustomerPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerDivision() {
        return division;
    }

    public Integer getCustomerDivisionID() {
        return divisionID;
    }

    public String getCustomerCountryName() {
        return countryName;
    }
}

package model;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private Integer apptID;
    private Integer customerID;
    private Integer userID;
    private Integer contactID;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private String apptCreatedBy;
    private String apptLastUpdateBy;
    private String apptContactName;
    private Timestamp beginDateTime;
    private Timestamp endDateTime;
    private Timestamp dateCreated;
    private Timestamp lastDateTimeUpdate;

    public Appointment(Integer apptIDInput, String apptTitleInput, String apptDescriptionInput, String apptLocationInput,
                       String apptTypeInput, Timestamp beginDateTimeInput, Timestamp endDateTimeInput, Timestamp dateCreatedInput,
                       String apptCreatedByInput,Timestamp lastDateTimeUpdateInput, String apptLastUpdateByInput,
                       Integer customerIDInput, Integer userIDInput, Integer contactIDInput, String apptContactNameInput) {

        apptID = apptIDInput;
        customerID = customerIDInput;
        userID = userIDInput;
        contactID = contactIDInput;
        apptTitle = apptTitleInput;
        apptDescription = apptDescriptionInput;
        apptLocation = apptLocationInput;
        apptType = apptTypeInput;
        apptCreatedBy = apptCreatedByInput;
        lastDateTimeUpdate = lastDateTimeUpdateInput;
        apptLastUpdateBy = apptLastUpdateByInput;
        customerID = customerIDInput;
        userID = userIDInput;
        contactID = contactIDInput;
        apptContactName = apptContactNameInput;
    }

    public Integer getApptID() {
        return apptID
    }

    public String getApptTitle() {
        return apptTitle;
    }

    public String getApptDescription() {
        return apptDescription;
    }

    public String getApptLocation() {
        return apptLocation;
    }

    public String getApptType() {
        return apptType;
    }

    public Timestamp getBeginDateTime() {
        return beginDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public String getApptCreatedBy() {
        return apptCreatedBy;
    }

    public Timestamp getLastDateTimeUpdate() {
        return lastDateTimeUpdate;
    }

    public String getApptLastUpdateBy() {
        return apptLastUpdateBy;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getContactID() {
        return contactID;
    }

    public String getApptContactName() {
        return apptContactName;
    }
}

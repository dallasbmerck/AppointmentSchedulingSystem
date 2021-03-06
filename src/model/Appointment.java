package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Appointment class used to create multiple methods to access or generate data related to any appointments in the MySQL database.
 *
 * @author Dallas Merck
 */
public class Appointment {
    private int apptID;
    private int customerID;
    private int userID;
    private int contactID;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private String apptCreatedBy;
    private String apptLastUpdateBy;
    private String apptContactName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime dateCreated;
    private LocalDateTime lastDateTimeUpdate;

    /**
     * Appointment constructor that creates a new instance of an appointment.
     *
     * @param apptIDInput Appointment_ID - primary key.
     * @param apptTitleInput Title.
     * @param apptDescriptionInput Description.
     * @param apptLocationInput Location.
     * @param apptTypeInput Type.
     * @param startDateTimeInput Start.
     * @param endDateTimeInput End.
     * @param dateCreatedInput  Create_Date.
     * @param apptCreatedByInput Created_By.
     * @param lastDateTimeUpdateInput Last_Update.
     * @param apptLastUpdateByInput Last_Updated_By.
     * @param customerIDInput Customer_ID.
     * @param userIDInput User_ID - foreign key.
     * @param contactIDInput Contact_ID foreign key.
     * @param apptContactNameInput Contact_Name - foreign key.
     */
    public Appointment(Integer apptIDInput, String apptTitleInput, String apptDescriptionInput, String apptLocationInput,
                       String apptTypeInput, LocalDateTime startDateTimeInput, LocalDateTime endDateTimeInput, LocalDateTime dateCreatedInput,
                       String apptCreatedByInput, LocalDateTime lastDateTimeUpdateInput, String apptLastUpdateByInput,
                       Integer customerIDInput, Integer userIDInput, Integer contactIDInput, String apptContactNameInput) {

        apptID = apptIDInput;
        apptTitle = apptTitleInput;
        apptDescription = apptDescriptionInput;
        apptLocation = apptLocationInput;
        apptType = apptTypeInput;
        startDateTime = startDateTimeInput;
        endDateTime = endDateTimeInput;
        dateCreated = dateCreatedInput;
        apptCreatedBy = apptCreatedByInput;
        lastDateTimeUpdate = lastDateTimeUpdateInput;
        apptLastUpdateBy = apptLastUpdateByInput;
        customerID = customerIDInput;
        userID = userIDInput;
        contactID = contactIDInput;
        apptContactName = apptContactNameInput;
    }

    /**
     * Getter to retrieve the Appointment_ID.
     * @return Appointment_ID.
     */
    public int getApptID() {
        return apptID;
    }

    /**
     * Getter for the Title.
     * @return Title of the appointment.
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**
     * Getter for the description of the appointment.
     * @return Description of the appointment.
     */
    public String getApptDescription() {
        return apptDescription;
    }

    /**
     * Getter for the location of the appointment.
     * @return Location of the appointment.
     */
    public String getApptLocation() {
        return apptLocation;
    }

    /**
     * Getter for the type of appointment.
     * @return Type of appointment.
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * Getter for the start of Appointment.
     * @return Start.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Getter for the end of the Appointment.
     * @return End.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Getter for the date an appointment was created.
     * @return Create_Date.
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * Getter for the creator of the appointment.
     * @return Created_By.
     */
    public String getApptCreatedBy() {
        return apptCreatedBy;
    }

    /**
     * Getter for the last time an update was made to an appointment.
     * @return Last_Update.
     */
    public LocalDateTime getLastDateTimeUpdate() {
        return lastDateTimeUpdate;
    }

    /**
     * Getter for the last user to update an appointment.
     * @return Last_Updated_By.
     */
    public String getApptLastUpdateBy() {
        return apptLastUpdateBy;
    }

    /**
     * Getter for the customers ID attached to an appointment.
     * @return Customer_ID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Getter for the user ID attached to an appointment.
     * @return User_ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Getter for the contact ID associated with an appointment.
     * @return Contact_ID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Getter for the contact name attached to an appointment.
     * @return Contact_Name.
     */
    public String getApptContactName() {
        return apptContactName;
    }
}

package model;

public class MonthlyReport {
    public int totalAppointments;
    public String apptMonth;

    public MonthlyReport(String apptMonth, int totalAppointments) {
        this.apptMonth = apptMonth;
        this.totalAppointments = totalAppointments;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public String getApptMonth() {
        return apptMonth;
    }


}

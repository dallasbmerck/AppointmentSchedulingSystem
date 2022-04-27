package model;

public class Reports {
    private String countryName;
    private int totalByCountry;

    public Reports(String countryName, int totalByCountry) {
        this.countryName = countryName;
        this.totalByCountry = totalByCountry;
    }

    public int getTotalByCountry() {
        return totalByCountry;
    }

    public String getCountryName() {
        return countryName;
    }


}

package org.homefix.homefix;

import java.util.ArrayList;

public class ServiceProvider {
    private String email,companyName,companyAddress,companyPhoneNumber,companyDescription,Rating;
    private boolean isLiscenced;
    private ArrayList<String> Availabilities,Ratings,Bookings;

    public ServiceProvider(String email){
        this.email=email;
        this.companyName=this.companyAddress=this.companyPhoneNumber=this.companyDescription=this.Rating="";
        this.isLiscenced=false;
        Availabilities=Ratings=Bookings= new ArrayList<>();

    }

    public String getEmail() {
        return email;
    }

    public String getRating() {return Rating;}

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public boolean isLiscenced() {
        return isLiscenced;
    }

    public ArrayList<String> getAvailabilities() {
        return Availabilities;
    }

    public ArrayList<String> getRatings() {
        return Ratings;
    }

    public ArrayList<String> getBookings() {
        return Bookings;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setRating(String Rating){this.Rating=Rating;}

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public void setLiscenced(boolean liscenced) {
        isLiscenced = liscenced;
    }

    public void addAvailability(String input){
        Availabilities.add(input);
    }
    //Add Rating Method
    //Add Booking Method
}

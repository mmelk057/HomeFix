package src;

import java.util.ArrayList;

public class HomeOwner extends User {
    private String address;
    private long homePhone;
    private ArrayList<Service> chosenServices;

    public HomeOwner(String address,long homePhone){
        this.address = address;
        this.homePhone = homePhone;
    }

    public boolean selectServiceProvider(ServiceProvider sp){
        return true; //temporary
    }

    public boolean addServiceProvider(ServiceProvider sp){
        return true; //temporary
    }

    public boolean deleteServiceProvider(ServiceProvider sp){
        return true; //temporary
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String newAddress){
        address=newAddress;
    }

    public long getHomePhone(){
        return homePhone;
    }

    public void setHomePhone(long newPhone){
        homePhone= newPhone;
    }

}

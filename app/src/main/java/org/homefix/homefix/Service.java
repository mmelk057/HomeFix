package src;

import java.util.ArrayList;

public class Service {
    private ServiceProvider serviceProv;
    private ArrayList<HomeOwner> homeOwners;
    private String serviceName;
    private String info;
    private boolean approved;
    private boolean isActive;
    private boolean isAvailable;

    public Service(String serviceName,String info,boolean approved){
        this.serviceName = serviceName;
        this.info = info;
        this.approved=approved;
    }
    public Service(String serviceName,String info) {
        this.serviceName = serviceName;
        this.info = info;
        this.approved = false;
    }

    public boolean containsHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean addHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean removeHomeOwner(HomeOwner ho){
        return true; //temporary
    }

    public boolean deleteService(){
        return true; //temporary
    }

    public boolean isServiceAvailable(){
        return true; //temporary
    }

    public ServiceProvider getServiceProvider(){
        return serviceProv;
    }

    public String getServiceName(){
        return serviceName;
    }

    public void setServiceName(String newName){
        serviceName=newName;
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String newInfo){
        info = newInfo;
    }
}

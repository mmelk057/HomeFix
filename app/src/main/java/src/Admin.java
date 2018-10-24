package src;

import java.util.ArrayList;

public class Admin extends Person {
    private ArrayList<Service> services;
    public boolean addServiceToList(Service service){
        return true;
    }
    public boolean removeServiceFromList(Service service){
        return true;
    }
}

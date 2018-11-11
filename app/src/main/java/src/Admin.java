package src;

import org.homefix.homefix.Service;

public class Admin extends User{
    private String username;
    private String password;

    public Admin(String username,String password){
        this.username = username;
        this.password = password;
    }

    public boolean addServiceToDatabase(Service service){
        return true;
    }
    public boolean removeServiceFromDatabase(Service service){
        return true;
    }
}

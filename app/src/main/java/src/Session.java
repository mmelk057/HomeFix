package src;

public class Session {
    private String username;
    private String password;

    public Session(String username,String password){
        this.username=username;
        this.password=password; //encrypt password in SHA256
        // Store encrypted password in DB
    }
    public boolean authenticateExistingUser(String username,String password){
        return true; //temporary
    }
    public boolean createNewUser(String username,String password){
        return true; //temporary
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password; //returns encrypted password. NEVER THE REAL ONE IN PLAIN TEXT
    }
}

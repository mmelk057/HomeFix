package org.homefix.homefix;

public class User { // TMP
    private String username; // is before @mail.com
    private String email;
    private String password;
    private String type;

    public User(String email, String password, String type){
        this.email = email;
        this.password = password;
        this.type = type;

        int indexOfAt = email.length();
        for(int i = 0; i < email.length(); i++)
            if (email.charAt(i) == '@') {
                indexOfAt = i;
                break;
            }
        this.username = email.substring(0, indexOfAt+1);
    }

    public String getUsername() {
        return username;
    }
}

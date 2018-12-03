package org.homefix.homefix;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class UserActions {

    public boolean deleteExistingKey(HashMap<Integer,String> serviceDatabase,Integer key){
        if(!serviceDatabase.containsKey(key)){
            return false;
        }
        else{
            serviceDatabase.remove(key);
            return !(serviceDatabase.containsKey(key));
        }
    }

    public boolean updateValue(HashMap<Integer,String> serviceDatabase,Integer key,String value){
        if (!serviceDatabase.containsKey(key)){
            return false;
        }
        else{
            serviceDatabase.replace(key,value);
            return serviceDatabase.containsKey(key);
        }

    }

    public boolean addUserToDatabase(ArrayList<String> database,String user){
        database.add(user);
        return database.contains(user);
    }

    public boolean emailValidater(String email){
        return email.contains("@") && (email.contains(".com")||email.contains(".ca"));
    }

    public int serviceListSize(ArrayList<String> serviceList){
        return serviceList.size();
    }

    public boolean passwordsAreEqual(String p1,String p2){
        return p1.equals(p2);
    }

    public boolean getUsersName(HashMap<Integer,String> userDatabase,Integer key,String value){
        if(userDatabase.containsKey(key)){
            if(userDatabase.get(key).equals(value)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean dateValidater(String date){
        SimpleDateFormat parser = new SimpleDateFormat("YYYY-MM-DD");
        try {
            Date validDate = parser.parse(date);
            return true;
        }
        catch(ParseException pe){
            return false;
        }
    }

}

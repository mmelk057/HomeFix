package org.homefix.homefix;

import android.app.Activity;
import android.provider.ContactsContract;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;

public class UnitTests {

    @Test
    public void deleteAnExistantKey(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int Key1 = 12345;
        mp.put(Key1,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Key Deleted",new UserActions().deleteExistingKey(mp,Key1),true);
    }

    @Test
    public void deletingANonExistantKey(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int NonExistantKey = 12345;
        mp.put(000000,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Key Not Deleted",new UserActions().deleteExistingKey(mp,NonExistantKey),false);
    }

    @Test
    public void updatingAnExistantKey(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int Key1 = 12345;
        mp.put(Key1,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Key Updated",new UserActions().updateValue(mp,Key1,"TempValue3"),true);
    }

    @Test
    public void updatingANonExistantKey(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int NonExistantKey = 12345;
        mp.put(000000,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Key Not Updated",new UserActions().updateValue(mp,NonExistantKey,"TempValue3"),false);
    }

    @Test
    public void addAUserToDatabase(){
        ArrayList<String> al = new ArrayList<String>();
        String newUser = "User1";

        assertEquals("User Added",new UserActions().addUserToDatabase(al,newUser),true);
    }

    @Test
    public void validEmail(){
        String email = "String@gmail.ca";

        assertEquals("Email is Valid",new UserActions().emailValidater(email),true);
    }

    @Test
    public void validEmailV2(){
        String email = "String@gmail.com";

        assertEquals("Email is Valid",new UserActions().emailValidater(email),true);
    }

    @Test
    public void invalidEmail(){
        String email = "Stringgmail.ca";

        assertEquals("Email is Invalid",new UserActions().emailValidater(email),false);
    }

    @Test
    public void emptyServiceList(){
        ArrayList<String> serviceList = new ArrayList<>();
        int expectedSize =0;

        assertEquals("ServiceList is empty!",new UserActions().serviceListSize(serviceList),expectedSize);
    }

    @Test
    public void validServiceList(){
        ArrayList<String> serviceList = new ArrayList<>();
        serviceList.add("Gardening");
        serviceList.add("Plumbing");
        int expectedSize =2;

        assertEquals("ServiceList has two items",new UserActions().serviceListSize(serviceList),expectedSize);
    }

    @Test
    public void invalidPassword(){
        String password1 = "gary123";
        String password2 = "GARY123";

        assertEquals("Password invalid! Case Sensitive!",new UserActions().passwordsAreEqual(password1,password2),false);
    }

    @Test
    public void validPassword(){
        String password1 = "gary123";
        String password2 = "gary123";

        assertEquals("Password is valid! They match!",new UserActions().passwordsAreEqual(password1,password2),true);
    }

    @Test
    public void userNameMatches(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int Key1 = 12345;
        mp.put(Key1,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Username value matches key!",new UserActions().getUsersName(mp,Key1,"TempValue"),true);
    }

    @Test
    public void userNameDoesNotMatch(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int Key1 = 12345;
        mp.put(Key1,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Username value does not match key!",new UserActions().getUsersName(mp,Key1,"DOESNOTEXIST"),false);
    }

    @Test
    public void userNameDoesNotMatchV2(){
        HashMap<Integer,String> mp = new HashMap<Integer,String>();
        int Key1 = 12345;
        mp.put(000000,"TempValue");
        mp.put(678910,"TempValue2");

        assertEquals("Key does not even exist!",new UserActions().getUsersName(mp,Key1,"TempValue"),false);
    }

    @Test
    public void dateIsValid(){
        String date ="2018-12-03";

        assertEquals("Date is Valid!",new UserActions().dateValidater(date),true);
    }

    @Test
    public void dateIsInvalid(){
        String date ="1124201";

        assertEquals("Date is invalid!",new UserActions().dateValidater(date),false);
    }

}

package org.homefix.homefix;

import android.app.Activity;
import android.provider.ContactsContract;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert.*;
import org.junit.Test;

import java.net.PasswordAuthentication;

import static junit.framework.TestCase.assertEquals;

public class UnitTests {

    @Test
    public void testValidUsername(){
        Database db = new Database(FirebaseDatabase.getInstance().getReference("User"));
        String s = "@gmail.com";
        String s2 = "@gmail.com";

        assertEquals("validateUserName Success", db.validatePassword(s,s2), false);

    }

    @Test
    public void testInvalidUsername(){
        Database db = new Database(FirebaseDatabase.getInstance().getReference("User"));
        String s = "user1@gmail.com";
        String s2 = "user2@gmail.com";

        assertEquals("validateUserName Success", db.validatePassword(s,s2), false);
    }


    @Test
    public void testValidatePassword(){
        Database db = new Database(FirebaseDatabase.getInstance().getReference("User"));
        String s = "123456";
        String s2 = "123457";

        assertEquals("validatePassword Failed", db.validatePassword(s,s2), false);

    }
    @Test
    public void testServiceInfoGetName(){
        ServiceCategory s = new ServiceCategory("Service",120, "description");
        String description = "description";

        assertEquals("getInfo Failed", s.getInfo(), description);



    }
    @Test
    public void testUserGetUsername(){
        User u = new User("fchishtie@gmail.com", "Admin");
        String username = "fchishtie";

        assertEquals("getUsername Failed", u.getUsername(), username);


    }


    @Test
    public void setDidAuth(){
        Database db = new Database(FirebaseDatabase.getInstance().getReference("User"));
        db.setDidAuth(true);
        boolean authValue=true;

        assertEquals("Auth Set",true,db.getDidAuth());
    }

    @Test void setActivities(){
        Database db = new Database(FirebaseDatabase.getInstance().getReference("User"));
        String tempActivity = "Gardening";

        assertEquals("Activity set",tempActivity+":Activity1",db.addToActivities(tempActivity));

    }


}

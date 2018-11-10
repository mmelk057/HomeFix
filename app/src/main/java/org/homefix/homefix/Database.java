package org.homefix.homefix;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


import com.google.firebase.database.DatabaseReference;

public class Database extends AppCompatActivity {

    private DatabaseReference dr;
    private EditText serviceName;
    private EditText rate;
    private EditText details;
    private String type = "";
    private EditText username;
    private EditText password;
    private String type = "";

    private void addService() {
        String id = dr.push().getKey();
        String _serviceName = serviceName.getText().toString().trim();
        String _rate = rate.getText().toString().trim();
        String _details = details.getText().toString().trim();
        Service service = new Service(_serviceName, _rate, _details);
        dr.child("Service").child(id).setValue(service);
        Toast.makeText(this, "Service added", Toast.LENGTH_LONG).show();
    }

    private void addUser(String type) {
        String id = dr.push().getKey();
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        User user = new User(email, _password, type);
        if (type.equals("HomeOwner") || type.equals("ServiceProvider")) {
            dr.child("ListOfUsers").child(id).setValue(user);
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        }
        else if (type.equals("Admin")){
            dr.child("Admin").child(id).setValue(user);
            Toast.makeText(this, "Admin added", Toast.LENGTH_LONG).show();
        }
    }

    private boolean deleteService(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(),"Service deleted",Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ListOfUsers").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_LONG).show();
        return true;
    }

    private void updateService(String name, double rate, String details){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service").child(name);
        Service service = new Service (name, rate, details);
        dR.setValue(service);
        Toast.makeText(getApplicationContext(),"Service updated",Toast.LENGTH_LONG).show();
    }

    private void updateUser(String email, String password, String type){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ListOfUsers").child(email);
        Service service = new Service(email,password,type);
        dR.setValue(service);
        Toast.makeText(getApplicationContext(),"User updated",Toast.LENGTH_LONG).show();
    }
    

}

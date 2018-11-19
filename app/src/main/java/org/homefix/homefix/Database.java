package org.homefix.homefix;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Activity activity;
    private boolean didAuth;


    // CONSTRUCTOR WHEN USING SERVICE
    public Database(DatabaseReference dr, EditText serviceName, EditText rate, EditText details, Activity activity){
        this.dr = dr;
        this.serviceName = serviceName;
        this.rate = rate;
        this.details = details;
        this.activity = activity;
        didAuth = false;
    }
    public Database(){

    }

    // CONSTRUCTOR WHEN USING USER
    public Database(DatabaseReference dr, EditText username, EditText password, String type, Activity activity){
        this.dr = dr;
        this.type = type;
        this.username = username;
        this.password = password;
        this.activity = activity;
        didAuth = false;
    }

    public void addService() {
        String id = dr.push().getKey();
        String _serviceName = serviceName.getText().toString();
        String _rate = rate.getText().toString();
        String _details = details.getText().toString();
        ServiceCategory service = new ServiceCategory(_serviceName,Double.parseDouble(_rate),_details);
        dr.child("Service").child(id).setValue(service);
//        Toast.makeText(getApplicationContext(), "Service added", Toast.LENGTH_LONG).show();
    }

    public void addUserToDatabase(String type) {
        String id = dr.push().getKey();
        String email = username.getText().toString().trim();
        User user = new User(email, type);
        dr.child("ListOfUsers").child(id).setValue(user);
//        Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
    }


    public void deleteService(final String name){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String tmpService = (String)service.child("name").getValue();
                    if (tmpService.equals(name)) {
                        dR.child(service.getKey()).removeValue(); // SHOULD REMOVE SERVICE
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public ArrayList<ServiceCategory> listServices(final String tableName){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("data").child(tableName);
        final ArrayList<ServiceCategory> services = new ArrayList<ServiceCategory>();
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String id = service.getKey();
                    String info = (String)service.child(id).child("info").getValue();
                    String name = (String)service.child(id).child("name").getValue();
                    Double rate = Double.parseDouble((String)service.child(id).child("rate").getValue());
                    if (info!=null && details != null && rate != null) {
                        ServiceCategory s = new ServiceCategory(name,rate,info);
                        services.add(s);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return services;
    }


    public void updateService(final String oldName,final String newName, final double rate, final String details){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String tmpService = (String)service.child("name").getValue();
                    String id = service.getKey();
                    if (tmpService.equals(oldName)) {
                        ServiceCategory s = new ServiceCategory(newName, rate, details); // SHOULD MAKE NEW SERVICE AND REPLACE AT ID
                        dR.child(id).setValue(s);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean authenticate(final FirebaseAuth mAuth, boolean shouldCreateNewOnNotExist){
        if(shouldCreateNewOnNotExist){
            createNewUser(mAuth);
        }
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, _password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setdidAuth(true);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("-", "signInWithEmail:success");
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("-", "signInWithEmail:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
        return true;
    }

    public boolean setdidAuth(boolean value){
        this.didAuth = value;
    }

    public boolean getdidAuth(){
        return didAuth;
    }

    private boolean createNewUser(final FirebaseAuth mAuth){
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, _password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            didAuth = true;
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("-", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            addUserToDatabase(type); // ADDING TO DB Separately
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("-", "createUserWithEmail:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
        return true;
    }

//    private void updateUser(final String email, String password, String type){
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ListOfUsers");
//        dR.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot user: dataSnapshot.getChildren()){
//                    String tempUsername = (String)user.child("email").getValue();
//                    if (tempUsername.equals(email)) {
//                        userNames.add(tempUsername);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        User user = new User(email,type);
//        dR.setValue(service);
//        Toast.makeText(getApplicationContext(),"User updated",Toast.LENGTH_LONG).show();
//    }

//    private boolean deleteUser(String id){
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("ListOfUsers").child(id);
//        dR.removeValue();
//        Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_LONG).show();
//        return true;
//    }

}

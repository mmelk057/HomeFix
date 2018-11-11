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
        String _serviceName = serviceName.getText().toString().trim();
        String _rate = rate.getText().toString().trim();
        String _details = details.getText().toString().trim();
        Service service = new Service(_serviceName, _rate, _details);
        dr.child("Service").child(id).setValue(service);
        Toast.makeText(this, "Service added", Toast.LENGTH_LONG).show();
    }

    public void addUserToDatabase(String type) {
        String id = dr.push().getKey();
        String email = username.getText().toString().trim();
        User user = new User(email, type);
        dr.child("ListOfUsers").child(id).setValue(user);
        Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
    }


    public boolean deleteService(final String name){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String tmpService = (String)service.child("name").getValue();
                    String key = service.getKey();
                    if (tmpService.equals(name)) {
                        dR.child(key).removeValue(); // SHOULD REMOVE SERVICE
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),"Service deleted",Toast.LENGTH_LONG).show();
        return true;
    }



    public void updateService(final String oldName,final final String newName, final double rate, final String details){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Service");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String tmpService = (String)service.child("name").getValue();
                    String id = service.getKey();
                    if (tmpService.equals(oldName)) {
                        Service s = new Service(newName, rate, details); // SHOULD MAKE NEW SERVICE AND REPLACE AT ID
                        dR.child("Service").child(id).setValue(s);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),"Service updated",Toast.LENGTH_LONG).show();
    }

    public boolean authenticate(final FirebaseAuth mAuth, boolean shouldCreateNewOnNotExist){
        if(shouldCreateNewOnNotExist){
            createNewUser(mAuth);
        }
        String email = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, _password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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

    void setdidAuth(boolean value){
        this.didAuth = value;
    }

    boolean getdidAuth(){
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

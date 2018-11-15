package org.homefix.homefix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private DatabaseReference firebaseReference;
    private Activity activity;
    private boolean didAuth;
    private Context currentContext;


    // CONSTRUCTOR WHEN USING SERVICE
    public Database(DatabaseReference firebaseReference, Activity activity, Context currentContext){
        this.firebaseReference = firebaseReference;
        this.activity = activity;
        this.currentContext = currentContext;
        didAuth = false;
    }

    /**
     * Adds a specific service to the Service Firebase Directory
     */
    public void addService(String serviceName,String serviceRate,String serviceDetails) {
        String id = firebaseReference.push().getKey();
        ServiceCategory service = new ServiceCategory(serviceName,Double.parseDouble(serviceRate),serviceDetails);
        firebaseReference.child(id).setValue(service);
        Toast.makeText(currentContext, "Service added", Toast.LENGTH_LONG).show();
    }

    /**
     * Adds a specific user to the User Firebase Directory
     * @param type A user can be categorized as either a Service Provider, Home Owner or Admin
     */
    public void addUserToDatabase(String username,String type) {
        String id = firebaseReference.push().getKey();
        User user = new User(username, type);
        firebaseReference.child("ListOfUsers").child(id).setValue(user);
        Toast.makeText(currentContext, "User added", Toast.LENGTH_LONG).show();
    }

    /**
     * Used for backend login authentication for Login.java activity. Verifies user's information matches with Firebase's
     * @param email User's email
     * @param password User's password
     * @param type User's Categorized type
     */
    public void loginAuthentication(final String email,final String password,final String type){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login.java", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    // If the user is not a null object, that means authentication worked!
                    if (type.equals("HomeOwner") || type.equals("ServiceProvider")) {
                        Intent toWelcomePage = new Intent(activity,Welcome.class);
                        toWelcomePage.putExtra("user",email);
                        activity.startActivity(toWelcomePage);
                        //UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(type).build(); // DISPLAY NAME IS TYPE.build();
                    }
                    else{
                        Intent toAdminOptions = new Intent(activity,AdminOptions.class);
                        activity.startActivity(toAdminOptions);
                    }
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login.java", "signInWithEmail:failure", task.getException());
                    Toast.makeText(currentContext, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /**
     * Deletes a Service From the Service Firebase Directory
     * @param name Name of the Service that is to be deleted
     */
    public void deleteService(final String name){
        final DatabaseReference dR = firebaseReference;
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
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    /**
     * Creates a new user both the authentication and regular database tables in firebase
     * @param email User's email
     * @param password User's password
     * @param type User's Categorized Type
     * @param isRegistering Whether or not this function is being used towards registering a user or not. If so, it will
     *                      start a new activity after adding new user's info to DB. If not, it will simply add user's info
     *                      to DB
     */
    public void createUser(final String email,final String password,final String type,final boolean isRegistering){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("CreateAccount.java", "createUserWithEmail:success");
                    Toast.makeText(currentContext, "Creation Successful.", Toast.LENGTH_SHORT).show();
                    //Create new user in the database table
                    addUserToDatabase(email,type); // ADDING TO DB Separately

                    // Show welcome screen (We can assume that to get to the register screen you must be either
                    // a service provider or a user
                    if (isRegistering) {
                        Intent toWelcomeScreen = new Intent(activity, Welcome.class);
                        toWelcomeScreen.putExtra("user", email);
                        activity.startActivity(toWelcomeScreen);
                    }
                }

                else {
                    // If sign in fails, display a message to the user.
                    Log.w("login.java", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(currentContext, "Creation Unsuccessful.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    /**
     * Registers a user to the app by adding their information to the Firebase Tables
     * @param email User's email
     * @param password User's password
     * @param type User's Categorized type
     */
    public void registerUser(final String email,final String password,final String type){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("CreateAccount.java", "signInWithEmail:success");
                    Toast.makeText(currentContext, "User Already Exists...", Toast.LENGTH_SHORT).show();

                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w("createaccount.java", "signInWithEmail:failure", task.getException());
                    Toast.makeText(currentContext, "User Does not Exist...Creating User", Toast.LENGTH_SHORT).show();
                    //Create new user in the authentication table
                    createUser(email,password,type,true);

                }
            }
        });
    }



    /**
     * Updates information on Firebase about a Service
     * @param oldName User's Old Name (Used to query)
     * @param newName User's new Name Selection
     * @param rate User's new Rate Selection
     * @param details User's new Details Selection
     */
    public void updateService(final String oldName,final String newName, final double rate, final String details){
        final DatabaseReference dR = firebaseReference;
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
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });

    }


    // TO BE IMPLEMENTED LATER -------------------------------


    // DELETING A USER FROM DATABASE AND AUTHENTICATION TABLE


    // -------------------------------------------------------


    /**
     * Lists all service objects in the database onto a ListView Object (Intended for AdminServices.java)
     * @param listViewId reference to the Listview object to which you would want the information to be stored on
     */
    public void listServices(final int listViewId){
        DatabaseReference dR = firebaseReference;
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                finish();
//                activity.startActivity(activity.getIntent());
                ArrayList<ServiceCategory> services = new ArrayList<>();
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String info = (String)service.child("info").getValue();
                    String name = (String)service.child("name").getValue();
                    String rate = Long.toString((long)service.child("rate").getValue());
                    if (info!=null && name!= null && rate != null) {
                        ServiceCategory s = new ServiceCategory(name,Double.parseDouble(rate),info);
                        services.add(s);
                    }
                }
                ServiceListAdapter adapt = new ServiceListAdapter(currentContext,R.layout.service_list_layout,services);
                ListView serviceList = activity.findViewById(listViewId);
                serviceList.setAdapter(adapt);
                serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent toServiceInfo = new Intent(activity,ServiceInfo.class);
                        ServiceCategory sc = (ServiceCategory) view.getTag();
                        toServiceInfo.putExtra("name",sc.getName());
                        toServiceInfo.putExtra("rate",String.valueOf(sc.getRate()));
                        toServiceInfo.putExtra("info",sc.getInfo());
                        activity.startActivity(toServiceInfo);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });

    }

}

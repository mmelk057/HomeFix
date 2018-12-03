package org.homefix.homefix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.firebase.database.DatabaseReference;

public class Database extends AppCompatActivity {

    private DatabaseReference firebaseReference;
    private Activity activity;
    private boolean didAuth;
    private Context currentContext;
    private ArrayList<String> allActivities;
    private int allActivitiesLength;

    /***
     * Constructor
     * @param firebaseReference Firebase reference to a specific node in the Database (ex. Swapp>>Users>>ListOfUsers)
     * @param activity Current activity (ex. [Activity_Name].this)
     * @param currentContext Context of current activity (ex. getBaseContext() or getApplicationContext())
     */
    public Database(DatabaseReference firebaseReference, Activity activity, Context currentContext){
        this.firebaseReference = firebaseReference;
        this.activity = activity;
        this.currentContext = currentContext;
        didAuth = false;
    }

    //DONT WORRY ABOUT THIS CONSTRUCTOR
    public Database(DatabaseReference firebaseReference) {
        this.firebaseReference=firebaseReference;
        allActivitiesLength=0;
    }

    //DONT WORRY ANOUT THIS METHOD
    public boolean getDidAuth(){
        return getDidAuth();
    }

    //DONT WORRY ABOUT THIS METHOD
    public void setDidAuth(boolean auth){
        didAuth=auth;
    }

    /**
     * Validating if two String fields are identical
     * @param pass1 Password String no.1
     * @param pass2 Password String no.2
     * @return boolean on whether the two parameter Strings are equal
     */
    public boolean validatePassword(String pass1, String pass2){
        if(!pass1.equals(pass2)){
            return false;
        }
        else{
            return true;
        }
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

    public void addReview(String email, String rating, String comment){
        String id = firebaseReference.push().getKey();
        Review review = new Review(email,rating,comment);
        firebaseReference.child(id).setValue(review);
    }

    /**
     * Adds a specific user to the User Firebase Directory
     * @param type A user can be categorized as either a Service Provider, Home Owner or Admin
     */
    public void addUserToDatabase(String username,String type) {
        //Add HomeOwner User
        if (type.equals("HomeOwner")){
            String id = firebaseReference.push().getKey();
            User user = new User(username, type);
            firebaseReference.child("ListOfUsers").child(id).setValue(user);
            Toast.makeText(currentContext, "HomeOwner added", Toast.LENGTH_LONG).show();
        }
        //Add Service Provider User
        else {
            String id = firebaseReference.push().getKey(); //Generate a random key from FireBase(ex. fyu]iwreyiu33737) then using getKey(), retrieves that newly created key
            ServiceProvider sp = new ServiceProvider(username); //Create a service provider object
            firebaseReference.child("ServiceProvider").child(id).setValue(sp); //Send the service provider object to firebase (IT will translate all the global variables within the class and their values to <Key,Value> pairs)
            firebaseReference.child("ServiceProvider").child(id).child("Availability").push().child("Time").setValue(""); // Sets a specific value for a child
            String bookingId = firebaseReference.child("ServiceProvider").child(id).child("CurrentBookings").push().getKey();
            firebaseReference.child("ServiceProvider").child(id).child("CurrentBookings").child(bookingId).child("Name").setValue("");
            firebaseReference.child("ServiceProvider").child(id).child("CurrentBookings").child(bookingId).child("Time").setValue("");
            Toast.makeText(currentContext, "Service Provider added", Toast.LENGTH_LONG).show();
            String reviewsId = firebaseReference.child("ServiceProvider").child(id).child("Reviews").push().getKey();
            firebaseReference.child("ServiceProvider").child(id).child("Reviews").child(reviewsId).child("Name").setValue("");
            firebaseReference.child("ServiceProvider").child(id).child("Reviews").child(reviewsId).child("Rating").setValue("");
            String servicesId = firebaseReference.child("ServiceProvider").child(id).child("Services").push().getKey();
            firebaseReference.child("ServiceProvider").child(id).child("Services").child(servicesId).child("name").setValue("");
            firebaseReference.child("ServiceProvider").child(id).child("Services").child(servicesId).child("rate").setValue("");
            firebaseReference.child("ServiceProvider").child(id).child("Services").child(servicesId).child("info").setValue("");
        }
    }

    /**
     * Queries a user based on his/her email, then adds an availability under his/her account
     * @param time Availability to be added
     * @param email Email that implements Comparable inferface that is used in query
     */
    public void findUserAndAddAvailability(final String time,final String email){
        final DatabaseReference dr = firebaseReference;
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    String tempUser = (String)user.child("email").getValue();
                    try {
                        if (tempUser.equals(email)) {
                            addAvailability(user.getKey(),time);
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
                dr.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    //DONT WORRY ABOUT THIS METHOD // UNIMPORTANT!!
    public String addToActivities(String input){
        allActivitiesLength+=1;
        String activitiesInput = input+":Activity"+allActivitiesLength;
        allActivities.add(activitiesInput);
        return activitiesInput;
    }

    /**
     * Adds an availability entry to a service provider's account based on his/her account key
     * @param guid global unique identifier
     * @param time availability that is to be added
     */
    public void addAvailability(final String guid,final String time){
        final DatabaseReference dR = firebaseReference.child(guid).child("Availability"); //Since the parameter's a GUID, no need to search for a user. A user's already specified!
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //Queries through all Time values within a SPECIFIED USER'S availabilities directory
                boolean isAlreadyInList = false;
                for(DataSnapshot currentElem: dataSnapshot.getChildren()){
                    if ((currentElem.child("Time").getValue()).equals(time)) {
                         isAlreadyInList=true;
                         break;
                    }
                }
                if (!isAlreadyInList){ //Makes sure that the availability is not already in the directory in Firebase!
                    firebaseReference.child(guid).child("Availability").push().child("Time").setValue(time);
                }
                dR.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }



    /**
     * Seeks for a user by email, then proceeds to list out his/her availabilities
     * @param listViewId identification integer for listview component to which the availability values will be listed on
     * @param email user's email that will be used in querying the database
     */
    public void findUserAndListAvailablities(final int listViewId,final String email){
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Starts by searching for a user
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    String tempUser = (String)user.child("email").getValue();
                    try {
                        if (tempUser.equals(email)) { //Checks to see if the user is found
                            listAvailabilities(listViewId,user.getKey()); //If the user is found, call a secondary method BELOW
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
                firebaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }



    /**
     * Lists all the availabilities onto a listview using a a guid to locate the user
     * @param listViewId identification integer for listview component to which the availability values will be listed on
     * @param guid global unique identifier
     */
    public void listAvailabilities(final int listViewId,final String guid){
        final DatabaseReference dR = firebaseReference.child(guid).child("Availability"); //The GUID parameter means that the user is already specified. This is a Secondary method
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> availabilities = new ArrayList<>(); //create a local arraylist full of availabilities
                for(DataSnapshot currentElem: dataSnapshot.getChildren()){ //Collects all the availability values under a specified user
                    if (!((String)currentElem.child("Time").getValue()).equals("")) {
                        availabilities.add((String) currentElem.child("Time").getValue());
                    }
                }
                AvailabilityListAdapter adapt = new AvailabilityListAdapter(currentContext,R.layout.simple_drop_list_layout,availabilities); //Creates a custom adapter object
                ListView serviceList = activity.findViewById(listViewId);//Create a listview object with the PARAMETER ID to point to the listview in the activity
                serviceList.setAdapter(adapt); //Set the adapter to the list view
                //Set a listnener so that whenever an object is clicked inside the listview, it reacts
                serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String time = (String)view.getTag();
                        findAndDeleteAvailability(time,guid); //if an oject is clicked it will retreieve the tag of that listview object and delete it from the Firebase DB
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    /**
     * Deletes an availiability from a specific user's directory
     * @param time Time that is to be deleted
     * @param guid Global unique identifier
     */
    public void findAndDeleteAvailability(final String time, final String guid){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).child("Availability");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Goes through all availabilities of a user
                for(DataSnapshot timeEntries: dataSnapshot.getChildren()){
                    if(((String)timeEntries.child("Time").getValue()).equals(time)){ //If it finds an availability, it will delete it
                        deleteAvailability(guid,timeEntries.getKey()); // Calls a secondary method to delete the entire entry of the availability
                        break;
                    }
                }
                dR.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    /**
     * Deletes an entry given the user's guid and the availability object's guid in Firebase
     * @param guid global unique identifier of the user
     * @param guidV2 global unique identifier of the availability
     */
    public void deleteAvailability(String guid,String guidV2){
        FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).child("Availability").child(guidV2).removeValue();
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
                    if (type.equals("HomeOwner")) {
                        Intent toWelcomePage = new Intent(activity,Welcome.class);
                        toWelcomePage.putExtra("user",email);
                        activity.startActivity(toWelcomePage);
                        //UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(type).build(); // DISPLAY NAME IS TYPE.build();
                    }
                    else if(type.equals("ServiceProvider")){
                        Intent spMainPage = new Intent(activity,ServiceProviderMain.class);
                        spMainPage.putExtra("user",email);
                        activity.startActivity(spMainPage);
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
                    try {
                        if (tmpService.equals(name)) {
                            dR.child(service.getKey()).removeValue(); // SHOULD REMOVE SERVICE
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
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
                        if (type.equals("HomeOwner")) {
                            Intent toWelcomeScreen = new Intent(activity, Welcome.class);
                            toWelcomeScreen.putExtra("user", email);
                            activity.startActivity(toWelcomeScreen);
                        }
                        else{
                            Intent spMainPage = new Intent(activity,ServiceProviderMain.class);
                            spMainPage.putExtra("user",email);
                            activity.startActivity(spMainPage);
                        }
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Goes through all the services under Swapp>>Service>>Service  database reference
                ArrayList<ServiceCategory> services = new ArrayList<>(); //array list of all ServiceCategory objects
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String info = (String)service.child("info").getValue(); //Gets the value of the info variable of a service
                    String name = (String)service.child("name").getValue(); //Gets the value of the name variable of a service
                    String rate = Long.toString((long)service.child("rate").getValue()); //Gets the value of the rate variable of a service
                    if (info!=null && name!= null && rate != null) {
                        ServiceCategory s = new ServiceCategory(name,Double.parseDouble(rate),info); //create a new Service Provider object with all the retrieved information
                        services.add(s); //add new service provider object to the arraylist
                    }
                }
                ServiceListAdapter adapt = new ServiceListAdapter(currentContext,R.layout.service_list_layout,services); // Instantiate a new custom adapter object (Check class for further information)
                ListView serviceList = activity.findViewById(listViewId); //Create a listview object with the id specified in the param (it's pointing to the listview in an activity)
                serviceList.setAdapter(adapt); //Set the adapter to the listview
                //Create a listener for the listview object
                serviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //IF AN ITEM IS CLICKED, IT WILL RETRIEVE THE TAG BEHIND THE LISTVIEW OBJECT and pair it with the intent as an "extra"
                        Intent toServiceInfo = new Intent(activity,ServiceInfo.class); //creates a new intent
                        ServiceCategory sc = (ServiceCategory) view.getTag(); //gets tag
                        toServiceInfo.putExtra("name",sc.getName()); //gets the name value of the tag and puts it as an extra
                        toServiceInfo.putExtra("rate",String.valueOf(sc.getRate())); //gets the rate value of the tag and puts it as an extra
                        toServiceInfo.putExtra("info",sc.getInfo()); //gets the info value of the tag and puts it as an extra
                        activity.startActivity(toServiceInfo); //starts a new activity based on the intent created
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });

    }


    public void listAllOfferedServices(final int listViewId,final String email){
        DatabaseReference dR = firebaseReference;
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        ServiceCategory sc = (ServiceCategory) view.getTag();
                        findProviderThenAddService(sc,email);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void findProviderThenAddService(final ServiceCategory sc,final String email){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    String tempUser = (String)user.child("email").getValue();
                    try {
                        if (tempUser.equals(email)) {
                            addServiceForProvider(sc,user.getKey());
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
                dR.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void addServiceForProvider(final ServiceCategory sc, final String guid){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).child("Services");
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAlreadyInList = false;
                for(DataSnapshot currentElem: dataSnapshot.getChildren()){
                    if ((currentElem.child("name").getValue()).equals(sc.getName())) {
                        isAlreadyInList=true;
                        break;
                    }
                }
                if (!isAlreadyInList){
                    dR.push().setValue(sc);
                }
                dR.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });

    }


    public void findServiceProviderThenListServices(final int listViewId,final String email){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    String tempUser = (String)user.child("email").getValue();
                    try {
                        if (tempUser.equals(email)) {
                            listServicesForServiceProvider(listViewId,user.getKey());
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
                dR.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void listServicesForServiceProvider(final int listViewId,final String guid){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).child("Services");
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ServiceCategory> services = new ArrayList<>();
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String info = (String)service.child("info").getValue();
                    String name = (String)service.child("name").getValue();
                    String rate = String.valueOf(service.child("rate").getValue());
                    if (!(info.equals("")) && !(name.equals("")) && !(rate.equals(""))) {
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
                        ServiceCategory sc = (ServiceCategory)view.getTag();
                        deleteServiceProviderService(sc.getName(),guid);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void deleteServiceProviderService(final String serviceName, String guid){
        final DatabaseReference dR = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).child("Services");
        dR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot service: dataSnapshot.getChildren()){
                    String tmpService = (String)service.child("name").getValue();
                    try {
                        if (tmpService.equals(serviceName)) {
                            dR.child(service.getKey()).removeValue(); // SHOULD REMOVE SERVICE
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void findAndUpdateServiceProvider(final String email){
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    String tempUser = (String)user.child("email").getValue();
                    try {
                        if (tempUser.equals(email)) {
                            updateServiceProvider(email,user.getKey());
                            break;
                        }
                    }
                    catch(NullPointerException e){
                        continue;
                    }
                }
                firebaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase_Error", "onCancelled: "+databaseError.getDetails());
            }
        });
    }

    public void updateServiceProvider(final String email,final String guid){
        EditText nameField = activity.findViewById(R.id.modify_profile_name);
        EditText addressField = activity.findViewById(R.id.modify_profile_address);
        EditText phoneNumberField=activity.findViewById(R.id.modify_phone_number);
        EditText descField =activity.findViewById(R.id.modify_profile_description);
        CheckBox yesCheck = activity.findViewById(R.id.yes_checkbox);
        CheckBox noCheck = activity.findViewById(R.id.no_checkbox);

        if (nameField.getText().toString().equals("")|| addressField.getText().toString().equals("")||phoneNumberField.getText().toString().equals("")||descField.getText().toString().equals("")){
            //Print Error Message
            Toast nullErrorMessage = Toast.makeText(currentContext,"Error: One or more fields are null",Toast.LENGTH_LONG);
            nullErrorMessage.show();
            //TOAST MESSAGE

            //WORK ON MAKING A CUSTOM TOAST MESSAGE WITH CUSTOM LAYOUT (!!)
        }
        else{
            if(yesCheck.isChecked()||noCheck.isChecked()){
                Map<String,Object> updates= new HashMap<>();
                updates.put("companyName",nameField.getText().toString());
                updates.put("companyAddress",addressField.getText().toString());
                updates.put("companyPhoneNumber",phoneNumberField.getText().toString());
                updates.put("companyDescription",descField.getText().toString());
                if (yesCheck.isChecked()){
                    updates.put("liscenced",true);
                }
                else{
                    updates.put("liscenced",false);
                }

                FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child(guid).updateChildren(updates);
                nameField.setHint(nameField.getText().toString());
                addressField.setHint(addressField.getText().toString());
                phoneNumberField.setHint(phoneNumberField.getText().toString());
                descField.setHint(descField.getText().toString());
                Intent previousIntent = activity.getIntent();
                Intent restartActivity = new Intent(activity,ServiceProviderProfilePage.class);
                restartActivity.putExtra("user",previousIntent.getStringExtra("user"));
                restartActivity.putExtra("name",nameField.getText().toString());
                restartActivity.putExtra("address",addressField.getText().toString());
                restartActivity.putExtra("desc",descField.getText().toString());
                restartActivity.putExtra("phoneNumber",descField.getText().toString());
                activity.startActivity(restartActivity);
            }
            else{
                Toast isLiscencedIsNotFilled = Toast.makeText(currentContext,"Error: Please check one of the boxes",Toast.LENGTH_LONG);
                isLiscencedIsNotFilled.show();
            }
        }



    }

}

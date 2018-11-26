package org.homefix.homefix;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import src.ServiceProvider;


public class ServiceProviderAvailabilityPage extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_set_availability_screen);

        //Set up Button Objects
        ImageButton backButton = findViewById(R.id.avail_screen_back_button);
        Button timeChoiceButton = findViewById(R.id.time_choice_button);

        //Get Previous Intent
        final Intent prevIntent = getIntent();
        final String userEmail = prevIntent.getStringExtra("user");
        final Activity currentContext = ServiceProviderAvailabilityPage.this;

        //Set up listview and listview adapter
        final ArrayList<String> availabilityTimes = new ArrayList<>();

        //Set up database reference
        final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        final Database serviceProviderDirectory = new Database(firebaseRef,ServiceProviderAvailabilityPage.this,getApplicationContext());

        Database availabilitiesDirectory = new Database(firebaseRef,ServiceProviderAvailabilityPage.this,getApplicationContext());
        availabilitiesDirectory.findUserAndListAvailablities(R.id.all_available_times,userEmail);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToProfile = new Intent(ServiceProviderAvailabilityPage.this,ServiceProviderProfilePage.class);
                backToProfile.putExtra("user",userEmail);
                if (prevIntent.getStringExtra("name")!=null){
                    backToProfile.putExtra("name",prevIntent.getStringExtra("name"));
                    backToProfile.putExtra("address",prevIntent.getStringExtra("address"));
                    backToProfile.putExtra("phoneNumber",prevIntent.getStringExtra("phoneNumber"));
                    backToProfile.putExtra("desc",prevIntent.getStringExtra("desc"));
                }
                startActivity(backToProfile);
            }
        });

        timeChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                final TimePickerDialog availabilitySelector = new TimePickerDialog(currentContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay>11) {
                            if (hourOfDay==12){
                                availabilityTimes.add(hourOfDay+":00 PM");
                                serviceProviderDirectory.findUserAndAddAvailability(hourOfDay+":00 PM",userEmail);

                            }
                            else{
                                availabilityTimes.add((hourOfDay-12)+":00 PM");
                                serviceProviderDirectory.findUserAndAddAvailability((hourOfDay-12)+":00 PM",userEmail);
                            }

                        }
                        else {
                            if (hourOfDay==0){
                                availabilityTimes.add((hourOfDay+12) + ":00 AM");
                                serviceProviderDirectory.findUserAndAddAvailability((hourOfDay+12)+":00 AM",userEmail);
                            }
                            else {
                                availabilityTimes.add(hourOfDay + ":00 AM");
                                serviceProviderDirectory.findUserAndAddAvailability(hourOfDay+":00 AM",userEmail);
                            }
                        }

                    }
                },currentDate.get(Calendar.HOUR),currentDate.get(Calendar.MINUTE),false);
                availabilitySelector.show();
            }
        });



    }
}

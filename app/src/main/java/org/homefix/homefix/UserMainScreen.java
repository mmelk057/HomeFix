package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserMainScreen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_current_bookings);

        Intent previousIntent = getIntent();
        final String email = previousIntent.getStringExtra("user");

        //Instantiating Buttons for Sign out
        Button signOut = findViewById(R.id.signOutButton2);

        //listener to return user to MainActivity when signed out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainActivity = new Intent(UserMainScreen.this,MainActivity.class);
                startActivity(backToMainActivity);
            }
        });

        //from database, retrieve list of offered services
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service").child("Service");
        Database serviceDB = new Database(dr,UserMainScreen.this,getApplicationContext());
        serviceDB.listServicesForUser(R.id.offered_Services, email);
    }
}

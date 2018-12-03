package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserAvailabilitiesActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.all__availabilities);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        Database serviceDB = new Database(dr,UserAvailabilitiesActivity.this,getApplicationContext());
        serviceDB.findUserAndListAvailablitiesForUser(R.id.timesview, "crack@gmail.com");

        Button save = findViewById(R.id.saveAvailabilities);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToServiceProvider = new Intent(UserAvailabilitiesActivity.this,UserMainScreen.class);
                Toast.makeText(UserAvailabilitiesActivity.this, "Timeslot added", Toast.LENGTH_LONG).show();
                startActivity(backToServiceProvider);
            }
        });
    }

}

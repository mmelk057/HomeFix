package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.SystemClock.sleep;


public class ServiceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_info);

        Intent previousIntent = getIntent();
        final String name = previousIntent.getStringExtra("name");
        final String rate = previousIntent.getStringExtra("rate");
        final String info = previousIntent.getStringExtra("info");

        //Set up all TextView Objects
        TextView serviceName = (TextView)findViewById(R.id.serviceName);
        TextView serviceRate = (TextView)findViewById(R.id.serviceRate);
        TextView serviceInfo = (TextView)findViewById(R.id.serviceDetails);

        //Set up all Button Objects
        ImageButton serviceInfoBack = findViewById(R.id.servInfoBackButton);
        Button editButton = findViewById(R.id.editButton);
        Button removeButton = findViewById(R.id.saveButton);

        serviceName.setText(name);
        serviceRate.setText(rate+"$/hr");
        serviceInfo.setText(info);

        serviceInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToPreviousClass = new Intent(ServiceInfo.this,AdminServices.class);
                startActivity(backToPreviousClass);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEditScreen = new Intent(ServiceInfo.this,EditService.class);
                toEditScreen.putExtra("name",name);
                toEditScreen.putExtra("rate",rate);
                toEditScreen.putExtra("info",info);
                startActivity(toEditScreen);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB Remove Service Function
                ////////////////////////////
                DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Service").child("Service");
                Database db = new Database(dr,ServiceInfo.this,getApplicationContext());
                db.deleteService(name);
                Intent backToPreviousClass = new Intent(ServiceInfo.this,AdminServices.class);
                startActivity(backToPreviousClass);
            }
        });

    }

}

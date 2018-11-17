package org.homefix.homefix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import src.ServiceProvider;

public class ServiceProviderMain extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_booking_screen);

        //Set up buttons
        Button signOutButton = findViewById(R.id.booking_screen_sign_out_button);
        Button profileButton = findViewById(R.id.service_provider_profile_button);

        //Get previous intent
        final Intent previousIntent = getIntent();
        final String userEmail = previousIntent.getStringExtra("user");

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainActivity = new Intent(ServiceProviderMain.this,MainActivity.class);
                startActivity(backToMainActivity);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfilePage = new Intent(ServiceProviderMain.this,ServiceProviderProfilePage.class);
                toProfilePage.putExtra("user",userEmail);
                if (previousIntent.getStringExtra("name")!=null){
                    toProfilePage.putExtra("name",previousIntent.getStringExtra("name"));
                    toProfilePage.putExtra("address",previousIntent.getStringExtra("address"));
                    toProfilePage.putExtra("phoneNumber",previousIntent.getStringExtra("phoneNumber"));
                    toProfilePage.putExtra("desc",previousIntent.getStringExtra("desc"));
                }
                startActivity(toProfilePage);
            }
        });
    }
}

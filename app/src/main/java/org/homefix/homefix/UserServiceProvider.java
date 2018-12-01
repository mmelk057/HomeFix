package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class UserServiceProvider extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeowner_service_provider_screen);

        //retrieve username from UserMainScreen page
        Intent previousIntent = getIntent();
        final String email = previousIntent.getStringExtra("user");

    }

}

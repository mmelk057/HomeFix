package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);

        Intent previousIntent = getIntent();
        String username = previousIntent.getStringExtra("user");
        TextView welcomeUser =(TextView)findViewById(R.id.welcomeUser);
        try {
            welcomeUser.setText("Welcome, " + username);
        }
        catch(NullPointerException e){
            System.out.println("\"Welcome User\" EditText field was attempted to be set, but could not. Stack Trace: "+e.getStackTrace());
        }

        //Instantiating Button to continue to UserMainScreen page
        Button toContinue = findViewById(R.id.continueButton);

        //listener to go to UserMainScreen page when clicked
        toContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUserMainScreen = new Intent(Welcome.this,UserMainScreen.class);
                startActivity(toUserMainScreen);
            }
        });
    }

}

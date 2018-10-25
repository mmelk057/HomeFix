package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.welcomepage);
        }
        catch(Exception e){
            System.out.println("Error Launching Welcome Page: "+e.getMessage()+"\n"+e.getStackTrace());
        }

        Intent previousIntent = getIntent();
        String username = previousIntent.getStringExtra("user");
        TextView welcomeUser =(TextView)findViewById(R.id.welcomeUser);
        try {
            welcomeUser.setText("Welcome," + username);
        }
        catch(NullPointerException e){
            System.out.println("\"Welcome User\" EditText field was attempted to be set, but could not. Stack Trace: "+e.getStackTrace());
        }
        }

}

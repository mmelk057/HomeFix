package org.homefix.homefix;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
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
        welcomeUser.setText("Welcome,"+username);
    }

}

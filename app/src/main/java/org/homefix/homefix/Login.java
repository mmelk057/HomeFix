package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Set up Login Button
        Button loginButton = (Button)findViewById(R.id.Button2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null
                Intent previousIntent = getIntent();
                if (previousIntent.getStringExtra("type").equals("admin")){
                    //Database Verification Step Required
                    //Admin
                }
                else if (previousIntent.getStringExtra("type").equals("homeOwner")){
                    //Database Verification Step Required
                    //Home Owner
                }
                else{
                    //Service Provider
                    //Database Verification Step Required
                }

                EditText username = (EditText)findViewById(R.id.editText3);
                EditText password = (EditText)findViewById(R.id.editText4);
                if (username.getText().toString().contains("@") && password.getText().length()!=0){ //If username is an email and password isn't null
                    Intent toWelcome = new Intent(Login.this,Welcome.class);
                    toWelcome.putExtra("user",username.getText().toString());
                    startActivity(toWelcome);

                }

            }
        });
    }
}

package org.homefix.homefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

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

                //splitting the username into two fields
                String email = username.getText().toString();
                if (email.contains("@") && password.getText().length()!=0){
                    String [] emailParts = email.split(Pattern.quote("@"));
                    if (emailParts.length==2) {
                        boolean areEmailSectionsValid = true;
                        for(int i=0;i<2;i++){
                            if(emailParts[i].equals("") || (emailParts[i].startsWith(".")) || (emailParts[i].endsWith("."))){  //an email split by @ cannot have either of its sections start with or end with '.'
                                areEmailSectionsValid=false;
                                break;
                            }
                            else if(emailParts[i].contains(".")){
                                String[] emailPartsV2 = emailParts[i].split(Pattern.quote("."));
                                for (int j = 0; j < emailPartsV2.length; j++) {
                                    if (emailPartsV2[j].equals("")) {
                                        areEmailSectionsValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                        if(areEmailSectionsValid){
                            Intent toWelcome = new Intent(Login.this, Welcome.class);
                            toWelcome.putExtra("user", username.getText().toString());
                            try {
                                startActivity(toWelcome);
                            } catch (Exception e) {
                                System.out.println("Error Starting Activity: " + e.getMessage() + "\n" + e.getStackTrace());

                            }

                        }
                    }
                }

            }
        });
    }
}

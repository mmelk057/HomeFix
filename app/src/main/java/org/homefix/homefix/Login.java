package org.homefix.homefix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //Set up Login Button and Back Button
        Button loginButton = findViewById(R.id.saveButton);
        ImageButton backButton = findViewById(R.id.loginBackButton);

        // DATABASE
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference("User");

        // Setting up previous intent
        final Intent previousIntent = getIntent();
        final String typeUser = previousIntent.getStringExtra("type");

        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null
                if (typeUser.equals("admin")){
                    type = "Admin";
                }
                else if (typeUser.equals("homeOwner")){
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                }
                else{
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }

                final EditText username = findViewById(R.id.editText3);
                final EditText password = findViewById(R.id.editText4);

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
                            final String email_final = username.getText().toString();
                            final String pass_final= password.getText().toString();

                            //Authenticates user's email and password

                            mAuth.signInWithEmailAndPassword(email_final,pass_final).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("login.java", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // If the user is not a null object, that means authentication worked!

                                        if (user!=null){
                                            if (type.equals("HomeOwner") || type.equals("ServiceProvider")) {
                                                Intent toWelcomePage = new Intent(Login.this,Welcome.class);
                                                toWelcomePage.putExtra("user",email_final);
                                                startActivity(toWelcomePage);
                                                //UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(type); // DISPLAY NAME IS TYPE.build();
                                            }
                                            else{
                                                Intent toAdminOptions = new Intent(Login.this,AdminOptions.class);
                                                startActivity(toAdminOptions);
                                            }
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("login.java", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeUser.equals("homeOwner") || typeUser.equals("sp")){
                    //Navigate to CreateAccount class
                    Intent backToRegister = new Intent(Login.this,CreateAccount.class);
                    backToRegister.putExtra("type",typeUser);
                    startActivity(backToRegister);
                }
                else{
                    //Navigate to Main
                    Intent backToMain = new Intent(Login.this,MainActivity.class);
                    startActivity(backToMain);
                }

            }
        });
        //BACKEND
    }


}
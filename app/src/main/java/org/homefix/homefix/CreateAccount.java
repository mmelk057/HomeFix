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


public class CreateAccount extends AppCompatActivity {
    private String type = "";
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        //Set up Login Button
        Button loginButton = findViewById(R.id.saveButton);
        Button signinButton = findViewById(R.id.signinbutton);
        ImageButton backButton = findViewById(R.id.createAccountBackButton);

        // DATABASE
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference("User");

        //Set Up Previous Intent
        final Intent previousIntent = getIntent();
        final String previousType = previousIntent.getStringExtra("type");

        // BACKEND
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take the String object from the "username" textbox - make sure it's an email
                //Take the String object from the "password" textbox - make sure it's not null

                if (previousType.equals("homeOwner")) {
                    //Database Verification Step Required
                    //Home Owner
                    type = "HomeOwner";
                } else {
                    //Service Provider
                    //Database Verification Step Required
                    type = "ServiceProvider";
                }

                EditText username = findViewById(R.id.usernamebox);
                EditText password = findViewById(R.id.passwordbox);
                EditText confirmpassword = findViewById(R.id.confirmpasswordbox);

                //splitting the username into two fields
                String email = username.getText().toString();
                if (email.contains("@") && password.getText().length() != 0) {
                    String[] emailParts = email.split(Pattern.quote("@"));
                    if (emailParts.length == 2) {
                        boolean areEmailSectionsValid = true;
                        for (int i = 0; i < 2; i++) {
                            if (emailParts[i].equals("") || (emailParts[i].startsWith(".")) || (emailParts[i].endsWith("."))) {  //an email split by @ cannot have either of its sections start with or end with '.'
                                areEmailSectionsValid = false;
                                break;
                            } else if (emailParts[i].contains(".")) {
                                String[] emailPartsV2 = emailParts[i].split(Pattern.quote("."));
                                for (int j = 0; j < emailPartsV2.length; j++) {
                                    if (emailPartsV2[j].equals("")) {
                                        areEmailSectionsValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                        boolean e = validatePassword(password.getText().toString(),confirmpassword.getText().toString());
                        if (areEmailSectionsValid && e) {
                            //Authenticate that user is not in database
                            final String email_final = username.getText().toString();
                            final String password_final = password.getText().toString();

                            mAuth.signInWithEmailAndPassword(email_final,password_final).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("CreateAccount.java", "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();

                                                //If user is null, that means we can create a new user!
                                                if (user==null){
                                                    //Create new user in the authentication table
                                                    mAuth.createUserWithEmailAndPassword(email_final,password_final).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // Sign in success, update UI with the signed-in user's information
                                                                        Log.d("CreateAccount.java", "createUserWithEmail:success");
                                                                        //Create new user in the database table
                                                                        addUserToDatabase(type,email_final); // ADDING TO DB Separately

                                                                        // Show welcome screen (We can assume that to get to the register screen you must be either
                                                                        // a service provider or a user
                                                                        Intent toWelcomeScreen = new Intent(CreateAccount.this,Welcome.class);
                                                                        toWelcomeScreen.putExtra("user",email_final);
                                                                        startActivity(toWelcomeScreen);

                                                                    }

                                                                    else {
                                                                        // If sign in fails, display a message to the user.
                                                                        Log.w("login.java", "createUserWithEmail:failure", task.getException());
                                                                        Toast.makeText(CreateAccount.this, "Creation failed.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });

                                                }
                                                else{
                                                    Log.d("CreateAccount.java","User Already exists!");
                                                }

                                            } else
                                                {
                                                // If sign in fails, display a message to the user.
                                                Log.w("createaccount.java", "signInWithEmail:failure", task.getException());
                                                Toast.makeText(CreateAccount.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                            });

                        }
                    }

                }
            }
            //BACKEND

        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CHANGING SCREEN
                Intent toLogin = new Intent(CreateAccount.this,Login.class);
                toLogin.putExtra("type",previousType);
                startActivity(toLogin);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to Main
                Intent toMain = new Intent(CreateAccount.this,MainActivity.class);
                startActivity(toMain);
            }
        });

    }

    private boolean validatePassword(String pass1, String pass2){
        if(!pass1.equals(pass2)){
            Toast.makeText(this, "Passwords entered do not match!", Toast.LENGTH_LONG);
            return false;
        }
        else{
            return true;
        }
    }



    private void addUserToDatabase(String type,String email) {
        String id = dr.push().getKey();
        User user = new User(email, type);
        dr.child("ListOfUsers").child(id).setValue(user);
        Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
    }

}
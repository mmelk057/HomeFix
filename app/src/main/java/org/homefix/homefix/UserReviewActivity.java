package org.homefix.homefix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserReviewActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write__a__review);

        //Get previous intent extras
        final Intent previousIntent = getIntent();

        //Create all Edit Text objects
        final EditText emailField,ratingField,commentField;
        emailField = findViewById(R.id.user_email); //MAKE SURE NAMES CORRELATE WITH XML Id's
        if (previousIntent.getStringExtra("Email")!=null){
            emailField.setHint(previousIntent.getStringExtra("Email"));
        }
        ratingField = findViewById(R.id.rating);
        if(previousIntent.getStringExtra("Rating")!=null){
            ratingField.setHint(previousIntent.getStringExtra("Rating"));
        }
        commentField = findViewById(R.id.comment);
        if(previousIntent.getStringExtra("Comment")!=null){
            commentField.setHint(previousIntent.getStringExtra("Comment"));
        }

        //Database Reference
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child("Reviews");
        final Database serviceProviderDirectory = new Database(firebaseRef,UserReviewActivity.this,getApplicationContext());

        //Create all Button objects
        ImageButton backButton = findViewById(R.id.review_Backbutton); //ADD BUTTONS TO XML
        Button saveButton = findViewById(R.id.review_saveButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToUserServiceProvider = new Intent(UserReviewActivity.this,UserServiceProvider.class);
                //backToUserServiceProvider.putExtra("user",email);
                if (previousIntent.getStringExtra("Email")!=null){
                    backToUserServiceProvider.putExtra("Email",previousIntent.getStringExtra("Email"));
                    backToUserServiceProvider.putExtra("Rating",previousIntent.getStringExtra("Rating"));
                    backToUserServiceProvider.putExtra("Comment",previousIntent.getStringExtra("Comment"));
                }
                startActivity(backToUserServiceProvider);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ratingInt = Integer.parseInt(ratingField.getText().toString());
                if (ratingInt>5 || ratingInt<0){
                    Toast.makeText(UserReviewActivity.this, "Please enter a whole number from 0 to 5", Toast.LENGTH_LONG).show();
                }
                if ((ratingInt<=5) && (ratingInt>=0)){
                    Toast.makeText(UserReviewActivity.this, "Review Added", Toast.LENGTH_LONG).show();
                    serviceProviderDirectory.addReview(emailField.getText().toString().trim(),ratingField.getText().toString().trim(),commentField.getText().toString().trim());
                }

            }

        });
    };


}





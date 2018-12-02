package org.homefix.homefix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UserReviewActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_a_review);

        //Get previous intent extras
        final Intent previousIntent = getIntent();

        //Create all Edit Text objects
        EditText emailField,ratingField,commentField;
        emailField = findViewById(R.id.modify_User_Email); //MAKE SURE NAMES CORRELATE WITH XML Id's
        if (previousIntent.getStringExtra("Email")!=null){
            emailField.setHint(previousIntent.getStringExtra("Email"));
        }
        ratingField = findViewById(R.id.modify_Review_rating);
        if(previousIntent.getStringExtra("Rating")!=null){
            ratingField.setHint(previousIntent.getStringExtra("Rating"));
        }
        commentField = findViewById(R.id.modify_Review);
        if(previousIntent.getStringExtra("Comment")!=null){
            numberField.setHint(previousIntent.getStringExtra("Comment"));
        }

        //Database Reference
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider").child("Reviews"));
        final Database serviceProviderDirectory = new Database(firebaseRef,UserReviewActivity.this,getApplicationContext());

        //Create all Button objects
        ImageButton backButton = findViewById(R.id.review_Backbutton); //ADD BUTTONS TO XML
        Button saveButton = findViewById(R.id.review_save_button);

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
                if (Integer.parseInt(ratingField.getText().toString())=<5 || Integer.parseInt(ratingField.getText().toString()>=0)==false){
                    Toast.makeText(currentContext, "Please enter a whole number from 0 to 5", Toast.LENGTH_LONG).show();
                }
                if (Integer.parseInt(ratingField.getText().toString())=<5 && Integer.parseInt(ratingField.getText().toString()) >=0){
                    Toast.makeText(currentContext, "Review Added", Toast.LENGTH_LONG).show();
                    serviceProviderDirectory.addReview(emailField,ratingField,commentField);
                }

            }

        });
    };
}


}


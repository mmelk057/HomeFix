package org.homefix.homefix;

import java.util.ArrayList;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Review {
    private String email;
    private String rating;
    private String comment;

    public Review(String email, String rating,String comment) {
        this.email = email;
        this.rating = rating;
        this.comment = comment;
    }

    public String getEmail (){ return email;}
    public String getRating (){ return rating;}
    public String getComment (){ return comment;}




}

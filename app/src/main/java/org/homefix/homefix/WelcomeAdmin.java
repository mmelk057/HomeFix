package org.homefix.homefix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WelcomeAdmin extends Activity {

    ListView l1;
    ArrayAdapter<String> adapter;
    String[] default_items = new String[]{"Username", "Email"};
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeadmin);
        Intent previousIntent = getIntent();
        String username = previousIntent.getStringExtra("user");
        TextView welcomeUser =(TextView)findViewById(R.id.welcomeUser);
        try {
            welcomeUser.setText("Welcome, Admin \"" + username+"\"");
        }
        catch(NullPointerException e){
            System.out.println("\"Welcome User\" EditText field was attempted to be set, but could not. Stack Trace: "+e.getStackTrace());
        }
        addAllUsers();

    }

    private void addAllUsers(){
        l1 = (ListView)findViewById(R.id.listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("ListOfUsers");
        final ArrayList<String> userNames = new ArrayList<String>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    String tempUsername = (String)user.child("email").getValue();
                    if (tempUsername!=null) {
                        userNames.add(tempUsername);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,userNames);
        l1.setAdapter(adapter);
    }

}

package org.homefix.homefix;
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

public class ServiceProviderProfilePage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provider_profile_screen);

        //Get previous intent extras
        final Intent previousIntent= getIntent();
        final String email = previousIntent.getStringExtra("user");



        //Create all Edit Text objects
        EditText nameField,addressField,numberField,descriptionField;
        nameField = findViewById(R.id.modify_profile_name);
        if (previousIntent.getStringExtra("name")!=null){
            nameField.setHint(previousIntent.getStringExtra("name"));
        }
        addressField = findViewById(R.id.modify_profile_address);
        if(previousIntent.getStringExtra("address")!=null){
            addressField.setHint(previousIntent.getStringExtra("address"));
        }
        numberField = findViewById(R.id.modify_phone_number);
        if(previousIntent.getStringExtra("phoneNumber")!=null){
            numberField.setHint(previousIntent.getStringExtra("phoneNumber"));
        }
        descriptionField = findViewById(R.id.modify_profile_description);
        if(previousIntent.getStringExtra("desc")!=null){
            descriptionField.setHint(previousIntent.getStringExtra("desc"));
        }


        //Create all TextBox objects
        final CheckBox yesChoice = findViewById(R.id.yes_checkbox);
        final CheckBox noChoice = findViewById(R.id.no_checkbox);

        //Create all Button objects
        ImageButton backButton = findViewById(R.id.service_provider_profile_back_button);
        Button saveButton = findViewById(R.id.profile_save_button);
        Button setAvailButton = findViewById(R.id.service_provider_set_avail_button);
        Button selectServButton = findViewById(R.id.service_provider_select_service_button);

        //Database Reference
        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("User").child("ServiceProvider");
        final Database serviceProviderDirectory = new Database(firebaseRef,ServiceProviderProfilePage.this,getApplicationContext());



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToServiceProviderMain = new Intent(ServiceProviderProfilePage.this,ServiceProviderMain.class);
                backToServiceProviderMain.putExtra("user",email);
                if (previousIntent.getStringExtra("name")!=null){
                    backToServiceProviderMain.putExtra("name",previousIntent.getStringExtra("name"));
                    backToServiceProviderMain.putExtra("address",previousIntent.getStringExtra("address"));
                    backToServiceProviderMain.putExtra("phoneNumber",previousIntent.getStringExtra("phoneNumber"));
                    backToServiceProviderMain.putExtra("desc",previousIntent.getStringExtra("desc"));
                }
                startActivity(backToServiceProviderMain);
            }
        });

        setAvailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAvailabilityScreen = new Intent(ServiceProviderProfilePage.this,ServiceProviderAvailabilityPage.class);
                toAvailabilityScreen.putExtra("user",email);
                if (previousIntent.getStringExtra("name")!=null){
                    toAvailabilityScreen.putExtra("name",previousIntent.getStringExtra("name"));
                    toAvailabilityScreen.putExtra("address",previousIntent.getStringExtra("address"));
                    toAvailabilityScreen.putExtra("phoneNumber",previousIntent.getStringExtra("phoneNumber"));
                    toAvailabilityScreen.putExtra("desc",previousIntent.getStringExtra("desc"));
                }
                startActivity(toAvailabilityScreen);
            }
        });

        selectServButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toServiceSelectionScreen = new Intent(ServiceProviderProfilePage.this,ServiceProviderSeviceSelectionPage.class);
                toServiceSelectionScreen.putExtra("user",email);
                if (previousIntent.getStringExtra("name")!=null){
                    toServiceSelectionScreen.putExtra("name",previousIntent.getStringExtra("name"));
                    toServiceSelectionScreen.putExtra("address",previousIntent.getStringExtra("address"));
                    toServiceSelectionScreen.putExtra("phoneNumber",previousIntent.getStringExtra("phoneNumber"));
                    toServiceSelectionScreen.putExtra("desc",previousIntent.getStringExtra("desc"));
                }
                startActivity(toServiceSelectionScreen);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceProviderDirectory.findAndUpdateServiceProvider(email);
            }
        });

        yesChoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (noChoice.isChecked()){
                    noChoice.setChecked(false);
                }
            }
        });
        noChoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(yesChoice.isChecked()){
                    yesChoice.setChecked(false);
                }
            }
        });


    }
}

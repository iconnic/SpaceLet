package com.artbycode.spacelet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewSectionActivity extends BaseActivity {
    private static AppCompatActivity activity; //private static ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_section);
        activity = this;

        initContent();
    }

    private static EditText unique,name,description,capacity,address;
    private static TextView title,complete;
    private static ImageView back;
    private void initContent(){
        unique = findViewById(R.id.unique);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        capacity = findViewById(R.id.capacity);
        address = findViewById(R.id.address);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        complete = findViewById(R.id.complete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(back,400);
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(title,400);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(complete,400);
                if(unique.getText().length()<2){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(unique,400);
                }else if(name.getText().length()<4){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(name,400);
                }else if(description.getText().length()<4){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(description,400);
                }else if(capacity.getText().length()<1){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(capacity,400);
                }else if(Integer.valueOf(capacity.getText().toString())<1){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(capacity,400);
                    capacity.setText("");
                }else  if(address.getText().length()<4){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(address,400);
                }
                else{

                    sectionId = unique.getText().toString();
                    nameDetails = name.getText().toString();
                    descriptionDetails = description.getText().toString();
                    capacityDetails = capacity.getText().toString();
                    addressDetails = address.getText().toString();

                    //for demonstration purposes, upper limit is set
                    if(Integer.valueOf(capacityDetails)>35){capacityDetails="35";}

                    uploadData();
                }
            }
        });

        APP.getAnimViews().in(back,100);
        APP.getAnimViews().in(title,200);
        APP.getAnimViews().in(unique,300);
        APP.getAnimViews().in(name,400);
        APP.getAnimViews().in(description,500);
        APP.getAnimViews().in(capacity,400);
        APP.getAnimViews().in(address,300);
        APP.getAnimViews().in(complete,200);
    }

    private static String sectionId="0",nameDetails="N/A",descriptionDetails="N/A",capacityDetails="0",addressDetails="N/A";
    private static HashMap<String,String> sectionDetails = new HashMap<>();
    private static void uploadData(){
        complete.setClickable(false);
        showLoading();
        sectionDetails.clear();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm");

        //"sectionId","status","name","description","capacity","address","imgURL"
        sectionDetails.put(APP.getSectionFields()[0],sectionId+format.format(Calendar.getInstance().getTime()));
        sectionDetails.put(APP.getSectionFields()[1],APP.getStatusAvailable()+"");
        sectionDetails.put(APP.getSectionFields()[2],nameDetails);
        sectionDetails.put(APP.getSectionFields()[3],descriptionDetails);
        sectionDetails.put(APP.getSectionFields()[4],capacityDetails);
        sectionDetails.put(APP.getSectionFields()[5],addressDetails);
        sectionDetails.put(APP.getSectionFields()[6],activity.getResources().getString(R.string.label_default));

        //firebase upload
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSection());
        ref.child(sectionDetails.get(APP.getSectionFields()[0])).setValue(sectionDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createSectionRooms();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                endLoading();
                complete.setClickable(true);
                Log.d("SECTION_CREATE_ERROR", e.getLocalizedMessage());
            }
        });

    }


    private static void createSectionRooms(){
        final HashMap<String, HashMap<String,String>> spacesAll = new HashMap<>();

        int cap = Integer.valueOf(capacityDetails);
        if(cap>0) {
            for (int i = 0; i < cap; i++){
                HashMap<String,String> spaces = new HashMap<>();
                //"spaceId","status","name","description","price","timeOccupied","imgURL"
                spaces.put(APP.getSpaceFields()[0], sectionId + " " + i);
                spaces.put(APP.getSpaceFields()[1], APP.getStatusAvailable() + "");
                spaces.put(APP.getSpaceFields()[2], activity.getResources().getString(R.string.label_space) + " " + (i + 1));
                spaces.put(APP.getSpaceFields()[3], activity.getResources().getString(R.string.label_default));
                spaces.put(APP.getSpaceFields()[4], activity.getResources().getString(R.string.label_not_available));
                spaces.put(APP.getSpaceFields()[5], activity.getResources().getString(R.string.label_not_available));
                spaces.put(APP.getSpaceFields()[6], activity.getResources().getString(R.string.label_default));

                spacesAll.put(spaces.get(APP.getSpaceFields()[0]),spaces);
            }

            //upload the spaces
            //firebase upload
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
            ref.child(sectionDetails.get(APP.getSectionFields()[0])).setValue(spacesAll).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //reset everything
                    sectionDetails.clear(); spacesAll.clear();
                    unique.setText(""); name.setText(""); description.setText(""); capacity.setText(""); address.setText("");
                    endLoading();
                    complete.setClickable(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    endLoading();
                    complete.setClickable(true);
                    Log.d("SECTION_CREATE_ERROR", e.getLocalizedMessage());
                }
            });

        }
    }

}

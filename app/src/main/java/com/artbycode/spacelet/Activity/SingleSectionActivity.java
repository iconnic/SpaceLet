package com.artbycode.spacelet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Adapter.SingleSpaceAdapter;
import com.artbycode.spacelet.Data.Space;
import com.artbycode.spacelet.R;
import com.artbycode.spacelet.SingletonSession;
import com.artbycode.spacelet.Tool.NpaGridLayoutManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleSectionActivity extends BaseActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_section);
        activity = this;

        initContent();
        initSingleSectionRecycler();
        initSection();
        initSlideSpace();
    }

    private static RelativeLayout slideSpace;
    private static TextView spaceTitle,spaceUpdate;
    private static ImageView spaceBack;
    private static EditText spaceName,spaceDescription,spacePrice;
    private void initSlideSpace(){
        slideSpace = findViewById(R.id.slide_space_details);
        spaceBack = findViewById(R.id.space_back);
        spaceTitle = findViewById(R.id.space_title);
        spaceUpdate = findViewById(R.id.space_update);
        spaceName = findViewById(R.id.space_name);
        spaceDescription = findViewById(R.id.space_description);
        spacePrice = findViewById(R.id.space_price);

        spaceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().out(slideSpace,200);
                slideSpace.setVisibility(View.GONE);
            }
        });

        spaceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(spaceUpdate,400);
                if(spaceName.getText().length()<2){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(spaceName,400);
                }else if(spaceDescription.getText().length()<4){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(spaceDescription,400);
                }else if(spacePrice.getText().length()<1){
                    APP.getToast().showToast(getApplicationContext(),getResources().getString(R.string.error_input),true);
                    APP.getAnimViews().click(spacePrice,400);
                }else{
                    if (spacePrice.getText().toString().equalsIgnoreCase("0")){
                        SingletonSession.Instance().getSpace().setPrice("Free");
                    }else{
                        SingletonSession.Instance().getSpace().setPrice(spacePrice.getText().toString());
                    }
                    SingletonSession.Instance().getSpace().setName(spaceName.getText().toString());
                    SingletonSession.Instance().getSpace().setDescription(spaceDescription.getText().toString());

                    updateSpaceDetails();
                }
            }
        });

        slideSpace.setVisibility(View.GONE);
    }

    private static void updateSpaceDetails(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
        ref.child(SingletonSession.Instance().getSection().getSectionId()).child(SingletonSession.Instance().getSpace().getSpaceId())
                .setValue(SingletonSession.Instance().getSpace()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                APP.getToast().showToast(activity,activity.getString(R.string.update_success),true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SPACE_UPDATE_ERROR", e.getLocalizedMessage());
            }
        });
    }

    public static void setSlideSpaceDetails(){
        spaceName.setText(SingletonSession.Instance().getSpace().getName());
        spaceDescription.setText(SingletonSession.Instance().getSpace().getDescription());
        spacePrice.setText(SingletonSession.Instance().getSpace().getPrice());
        slideSpace.setVisibility(View.VISIBLE);
        APP.getAnimViews().in(slideSpace,400);
    }

    private static TextView title;
    private static ImageView back;
    private void initContent(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        title.setText(SingletonSession.Instance().getSection().getName());

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(title,400);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(back,400);
                Intent intent = new Intent(activity, ViewSectionsActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        APP.getAnimViews().in(back,400);
        APP.getAnimViews().in(title,600);
    }

    private static ArrayList<Space> sectionSpaces = new ArrayList<>();
    private static void initSection(){
        bottomDialog(true,activity.getResources().getString(R.string.label_loading));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
        ref.child(SingletonSession.Instance().getSection().getSectionId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sectionSpaces.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String,String> singleSpace  = (HashMap<String, String>) snapshot.getValue();
                    Space space = new Space();
                    space.setDescription(singleSpace.get("description"));
                    space.setImgURL(singleSpace.get("imageURL"));
                    space.setName(singleSpace.get("name"));
                    space.setPrice(singleSpace.get("price"));
                    space.setSpaceId(singleSpace.get("spaceId"));
                    space.setStatus(singleSpace.get("status"));
                    space.setTimeOccupied(singleSpace.get("timeOccupied"));
                    sectionSpaces.add(space);
                    //Log.d("SINGLE_SECTION_LOADING",singleSpace.get("spaceId"));
                }

                refreshAllRecyclers();
                resetAllAdapters();
                bottomDialog(false,activity.getResources().getString(R.string.label_loading));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                bottomDialog(false,activity.getResources().getString(R.string.label_loading));
            }
        });
    }

    private static RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private static int gridnum=2;
    private void initSingleSectionRecycler(){
        recycler=(RecyclerView)findViewById(R.id.recycler); recycler.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnum, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mLayoutManager);
        adapter = new SingleSpaceAdapter(activity,sectionSpaces);

        recycler.setAdapter(adapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //SnapHelper snapHelper = new StartSnapHelper();
        //snapHelper.attachToRecyclerView(recycler);
    }

    private static void refreshAllRecyclers(){
        adapter.notifyItemRangeInserted(adapter.getItemCount(),sectionSpaces.size() - 1); adapter.notifyDataSetChanged();
    }

    private static void resetAllAdapters(){
        //adapter = new SingleSpaceAdapter(activity,sectionSpaces); recycler.setAdapter(adapter);
    }

    //change the status of the the space when it is clicked and update the recycler view
    public static void changeStatus(String spaceId,String newStatus,String time){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(SingletonSession.Instance().getSection().getSectionId()+"/"+spaceId+"/" + "status", newStatus);
        childUpdates.put(SingletonSession.Instance().getSection().getSectionId()+"/"+spaceId+"/" + "timeOccupied", time);
        ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                refreshAllRecyclers();
                resetAllAdapters();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}

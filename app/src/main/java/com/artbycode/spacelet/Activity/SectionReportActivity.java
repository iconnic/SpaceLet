package com.artbycode.spacelet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Adapter.ReportSpaceAdapter;
import com.artbycode.spacelet.Data.Space;
import com.artbycode.spacelet.R;
import com.artbycode.spacelet.SingletonSession;
import com.artbycode.spacelet.Tool.NpaGridLayoutManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SectionReportActivity extends BaseActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_report);
        activity = this;

        initRecycler();
        initSection();
        initContent();

    }

    private static TextView title,percent,sectionId,address,description,capacity;
    private static ImageView back;
    private void initContent(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        sectionId = findViewById(R.id.section_id);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        capacity = findViewById(R.id.capacity);
        percent = findViewById(R.id.percent);

        percent.setText(activity.getResources().getString(R.string.label_not_available));

        title.setText(SingletonSession.Instance().getSection().getName()+" "+activity.getResources().getString(R.string.label_reports));
        sectionId.setText(SingletonSession.Instance().getSection().getSectionId());
        address.setText(SingletonSession.Instance().getSection().getAddress());
        description.setText(SingletonSession.Instance().getSection().getDescription());
        capacity.setText(activity.getResources().getString(R.string.label_capacity)+": "+SingletonSession.Instance().getSection().getCapacity()+" "
                +activity.getResources().getString(R.string.label_not_available));

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(title,400);
            }
        });

        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APP.getAnimViews().click(percent,400);
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

        APP.getAnimViews().in(back,600);
        APP.getAnimViews().in(title,800);
    }

    private static ArrayList<Space> sectionSpaces = new ArrayList<>();
    private static ArrayList<Space> usedSpaces = new ArrayList<>();
    private static void initSection(){
        bottomDialog(true,activity.getResources().getString(R.string.label_loading));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
        ref.child(SingletonSession.Instance().getSection().getSectionId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sectionSpaces.clear(); usedSpaces.clear(); spacesUsed = 0; capacityValue = 0; count = 0;

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

                    if(singleSpace.get("status").equalsIgnoreCase(APP.getStatusOccupied())){
                        spacesUsed++;
                        //get the rooms occupied and store their details and show them in recycler
                        usedSpaces.add(space);
                    }

                    sectionSpaces.add(space);
                    count++;
                    //Log.d("SINGLE_SECTION_LOADING",singleSpace.get("status"));
                }

                calculatePercentage();
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
    private static int gridnum=1;
    private void initRecycler(){
        recycler=(RecyclerView)findViewById(R.id.recycler); recycler.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnum, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mLayoutManager);
        adapter = new ReportSpaceAdapter(activity,usedSpaces);

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
        adapter = new ReportSpaceAdapter(activity,usedSpaces); recycler.setAdapter(adapter);
    }

    private static float capacityValue=0,spacesUsed=0,count=0,spacesAvailable=0;
    private static void calculatePercentage(){
        capacityValue = Integer.valueOf(SingletonSession.getSection().getCapacity());
        spacesAvailable = capacityValue - spacesUsed;
        float percentage = ((spacesUsed/capacityValue) * 100);
        if(percentage>65){percent.setTextColor(activity.getResources().getColor(R.color.colorAccent));}
        String displayPercentage = String.format("%.2f", percentage);
        percent.setText(displayPercentage+"%");

        capacity.setText(activity.getResources().getString(R.string.label_capacity)+": "+SingletonSession.Instance().getSection().getCapacity()+" "
                +activity.getResources().getString(R.string.label_available)+": "+String.format("%.0f", spacesAvailable));
        //Log.d("SINGLE_SECTION_LOADING","Capacity: "+capacity+" SpacesUsed: "+spacesUsed+" Count: "+count);
    }

}

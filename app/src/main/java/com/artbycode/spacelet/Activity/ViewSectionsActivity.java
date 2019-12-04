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
import com.artbycode.spacelet.Adapter.MainSectionAdapter;
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

public class ViewSectionsActivity extends BaseActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sections);
        activity = this;

        initContent();
        initSectionRecycler();
        loadSections();
    }

    private static TextView title,instruction;
    private static ImageView back;
    private void initContent(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        instruction = findViewById(R.id.instruction); instruction.setVisibility(View.GONE);

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
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        APP.getAnimViews().in(back,600);
        APP.getAnimViews().in(title,800);
    }

    private static ArrayList <HashMap<String,String>> sectionsAll = new ArrayList<>();
    private static void loadSections(){
        bottomDialog(true,activity.getResources().getString(R.string.label_loading));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSection());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sectionsAll.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String,String> section  = (HashMap<String, String>) snapshot.getValue();
                    sectionsAll.add(section);
                    //Log.d("SECTION_LOADING",section.get(APP.getSectionFields()[0]).toString());
                }

                if(sectionsAll.size()<1){
                    instruction.setText(activity.getResources().getString(R.string.view_section_instructions));
                    instruction.setVisibility(View.VISIBLE);
                }
                refreshAllRecyclers();
                resetAllAdapters();
                bottomDialog(false,activity.getResources().getString(R.string.label_loading));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(sectionsAll.size()<1){
                    instruction.setText(activity.getResources().getString(R.string.error_load));
                    instruction.setVisibility(View.VISIBLE);
                    bottomDialog(false,activity.getResources().getString(R.string.label_loading));
                }
            }
        });
    }

    public static void deleteSection(String sectionId){
        showLoading();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(APP.getFirebasePathSection()+"/"+sectionId, null);
        childUpdates.put(APP.getFirebasePathSpace()+"/"+sectionId, null);
        ref.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadSections();
                endLoading();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SECTIONS_DELETE", e.getLocalizedMessage());
                endLoading();
            }
        });
    }

    private static RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private static int gridnum=1;
    private void initSectionRecycler(){
        recycler=(RecyclerView)findViewById(R.id.recycler); recycler.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnum, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mLayoutManager);
        adapter = new MainSectionAdapter(activity,sectionsAll);

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
        adapter.notifyItemRangeInserted(adapter.getItemCount(),sectionsAll.size() - 1); adapter.notifyDataSetChanged();
    }

    private static void resetAllAdapters(){
        adapter = new MainSectionAdapter(activity,sectionsAll); recycler.setAdapter(adapter);
    }
}

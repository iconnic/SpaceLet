package com.artbycode.spacelet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Adapter.FilterSectionAdapter;
import com.artbycode.spacelet.Adapter.FilterSpaceAdapter;
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

public class FilterViewActivity extends BaseActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_view);
        activity = this;

        initContent();
        initRecycler();
        initSectionRecycler();
        getAllSpaces();

    }

    private static TextView title,totalSectionstxt,totalCapacitytxt,totalAvailabletxt,totalOccupiedtxt,details;
    private static LinearLayout spinners;
    private static Spinner statusState,sectionFilter;
    private static ImageView back;
    private void initContent(){
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        details = findViewById(R.id.details);
        totalSectionstxt = findViewById(R.id.total_sections);
        totalCapacitytxt = findViewById(R.id.total_capacity);
        totalAvailabletxt = findViewById(R.id.total_available);
        totalOccupiedtxt = findViewById(R.id.total_occupied);
        spinners = activity.findViewById(R.id.spinners);
        statusState = activity.findViewById(R.id.status_state);
        sectionFilter = activity.findViewById(R.id.section_filter);

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

        sectionAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_dropdown_item, spinnerSectionValues);
        sectionFilter.setAdapter(sectionAdapter);
        sectionFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reCalculateSpinnerChoices();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        statusState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reCalculateSpinnerChoices();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        APP.getAnimViews().in(back,600);
        APP.getAnimViews().in(title,800);
    }

    private static RecyclerView recyclerSpaces;
    private static RecyclerView.Adapter adapterSpaces;
    private static int gridnumSpaces=2;
    private void initRecycler(){
        recyclerSpaces=(RecyclerView)findViewById(R.id.recycler_spaces); recyclerSpaces.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnumSpaces, LinearLayoutManager.VERTICAL,false);
        recyclerSpaces.setLayoutManager(mLayoutManager);
        adapterSpaces = new FilterSpaceAdapter(activity,combinedSpaces);

        recyclerSpaces.setAdapter(adapterSpaces);
        recyclerSpaces.addOnScrollListener(new RecyclerView.OnScrollListener(){
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

    private static void refreshAllSpacesRecyclers(){
        adapterSpaces.notifyItemRangeInserted(adapterSpaces.getItemCount(),combinedSpaces.size() - 1); adapterSpaces.notifyDataSetChanged();
    }

    private static void resetAllSpacesAdapters(){
        adapterSpaces = new FilterSpaceAdapter(activity,combinedSpaces); recyclerSpaces.setAdapter(adapterSpaces);
    }

    private static RecyclerView recyclerSection;
    private static RecyclerView.Adapter adapterSection;
    private static int gridnumSection=1;
    private void initSectionRecycler(){
        recyclerSection=(RecyclerView)findViewById(R.id.recycler_sections); recyclerSection.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnumSection, LinearLayoutManager.HORIZONTAL,false);
        recyclerSection.setLayoutManager(mLayoutManager);
        adapterSection = new FilterSectionAdapter(activity,sectionsAll);

        recyclerSection.setAdapter(adapterSection);
        recyclerSection.addOnScrollListener(new RecyclerView.OnScrollListener(){
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

    private static void refreshAllSectionRecyclers(){
        adapterSection.notifyItemRangeInserted(adapterSection.getItemCount(),sectionsAll.size() - 1); adapterSection.notifyDataSetChanged();
    }

    private static void resetAllSectionAdapters(){
        adapterSection = new FilterSectionAdapter(activity,sectionsAll); recyclerSection.setAdapter(adapterSection);
    }

    //Storage.... :/
    private static ArrayList<HashMap<String,HashMap<String,String>>> allSections = new ArrayList<>();
    private static HashMap<String,HashMap<String,String>> allSpaces = new HashMap<>();
    private static ArrayList<ArrayList<String>> allSectionKeys = new ArrayList<>();
    private static ArrayList<String> sectionTags = new ArrayList<>();
    private static void getAllSpaces(){
        bottomDialog(true,activity.getResources().getString(R.string.label_loading));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(APP.getFirebasePathSpace());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allSections.clear(); allSectionKeys.clear(); allSpaces.clear(); sectionTags.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    allSpaces  = (HashMap<String, HashMap<String,String>>) snapshot.getValue();
                    String st = snapshot.getKey();
                    sectionTags.add(st);

                    allSections.add(allSpaces);

                    ArrayList<String> hashKeys = new ArrayList<>();
                    for (String str : allSpaces.keySet()){hashKeys.add(str);}

                    allSectionKeys.add(hashKeys);
                }

                parseSpaces();
                bottomDialog(false,activity.getResources().getString(R.string.label_loading));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                bottomDialog(false,activity.getResources().getString(R.string.label_loading));
            }
        });
    }

    //main variables
    private static ArrayList <HashMap<String,String>> sectionsAll = new ArrayList<>();
    private static ArrayList<Space> combinedSpaces = new ArrayList<>();
    //variables of interest
    private static int totalSections,totalSpaces,totalOccupied,totalAvailable;
    private static ArrayList<Integer> totalSectionAvailable = new ArrayList<>();
    private static ArrayList<Integer> totalSectionOccupied = new ArrayList<>();
    private static ArrayList<Integer> totalSectionSpaces = new ArrayList<>();
    private static void parseSpaces(){
        totalSpaces=0; totalSections=0;totalOccupied=0;totalAvailable=0;
        totalSectionOccupied.clear(); totalSectionAvailable.clear(); totalSectionSpaces.clear();
        combinedSpaces.clear(); sectionsAll.clear();
        SingletonSession.Instance().getCombinedSpacesBackup().clear();

        for(int i=0;i<allSections.size();i++){
            //use this to count how many available spaces are in each section
            int count = 0;
            //separate each section and get the spaces from each and add them to a combined array
            int size = allSections.get(i).size();

            for(int j=0;j<size;j++){
                //get every instance of the space and save it to a combined list
                HashMap<String,String> singleSpace =  allSections.get(i).get(allSectionKeys.get(i).get(j));

                Space space = new Space();
                space.setDescription(singleSpace.get("description"));
                space.setImgURL(singleSpace.get("imageURL"));
                space.setName(singleSpace.get("name"));
                space.setPrice(singleSpace.get("price"));
                space.setSpaceId(singleSpace.get("spaceId"));
                space.setStatus(singleSpace.get("status"));
                space.setTimeOccupied(singleSpace.get("timeOccupied"));

                if(space.getStatus().equalsIgnoreCase(APP.getStatusOccupied())){totalOccupied++;}else{totalAvailable++; count++;}

                SingletonSession.Instance().getCombinedSpacesBackup().add(space);
                combinedSpaces.add(space);
            }

            totalSectionAvailable.add(count);
            totalSectionSpaces.add(size);
            totalSectionOccupied.add(size-count);
        }

        totalSections = allSections.size();
        totalSpaces = combinedSpaces.size();

        assignSpinners();
        showStatDetails();
        refreshAllSpacesRecyclers();
        resetAllSpacesAdapters();
        refreshAllSectionRecyclers();
        resetAllSectionAdapters();
    }

    private static ArrayList<String> spinnerSectionValues = new ArrayList<>();
    private static ArrayAdapter<String> sectionAdapter;
    private static void assignSpinners(){
        if(totalSections>0){
            spinnerSectionValues.clear();

            spinnerSectionValues.add(activity.getResources().getString(R.string.label_all_sections));
            for(int i=0;i<sectionTags.size();i++){spinnerSectionValues.add(sectionTags.get(i));}
            sectionAdapter.notifyDataSetChanged();

        }else{
            spinners.setVisibility(View.GONE);
        }
    }

    private static void reCalculateSpinnerChoices(){
        //reset the spaces connected to the recycler view
        combinedSpaces.clear();

        //all sections
        if(sectionFilter.getSelectedItemPosition()==0){
            //show everything
            if(statusState.getSelectedItemPosition()==0){
                for(int i=0;i<SingletonSession.Instance().getCombinedSpacesBackup().size();i++){
                    combinedSpaces.add(SingletonSession.Instance().getCombinedSpacesBackup().get(i));
                }
            }
            //show specific status of all combined sections
            else{
                setSpecificSpacesDisplay(0,SingletonSession.Instance().getCombinedSpacesBackup().size());
            }
        }

        //specific section
        if(sectionFilter.getSelectedItemPosition()>0){
            //find the specific section
            int sect=-1;
            for(int i=0;i<sectionTags.size();i++){
                if(sectionTags.get(i).equalsIgnoreCase(sectionFilter.getSelectedItem().toString())){
                    sect = i;
                }
            }

            //confirm the section was found (JUST A PRECAUTION)
            if(sect>-1){
                //figure where the section starts and ends in the combined spaces storage
                int startPosition = -1; int endPosition=0;
                for(int i=0;i<sect;i++){startPosition = startPosition+totalSectionSpaces.get(i);}

                //in case our section is the first section the above code may not be run leaving the start at -1, possibly
                if(startPosition<0){startPosition=0;}

                //simply add the capacity of the section to the start position to know where the section spaces end
                endPosition = startPosition + totalSectionSpaces.get(sect) + 1; //add one because a for loop terminates before the end value

                //show everything for that specific section
                if(statusState.getSelectedItemPosition()==0){
                    for(int i=startPosition;i<endPosition;i++){
                        combinedSpaces.add(SingletonSession.Instance().getCombinedSpacesBackup().get(i));
                    }
                }
                //show specific status of specific section
                else{
                    setSpecificSpacesDisplay(startPosition,endPosition);
                }
            }else{
                APP.getToast().showToast(activity,activity.getResources().getString(R.string.error_filter),true);
            }

        }

        CalculateStatDetails calculateStatDetails = new CalculateStatDetails();
        calculateStatDetails.execute();

        refreshAllSpacesRecyclers();
        resetAllSpacesAdapters();
    }

    private static void setSpecificSpacesDisplay(int startPosition,int endPosition){
        for(int i=startPosition;i<endPosition;i++){
            if(statusState.getSelectedItemPosition()==1){
                if(SingletonSession.Instance().getCombinedSpacesBackup().get(i).getStatus().equalsIgnoreCase(APP.getStatusAvailable())){
                    combinedSpaces.add(SingletonSession.Instance().getCombinedSpacesBackup().get(i));
                }
            }

            if(statusState.getSelectedItemPosition()==2){
                if(SingletonSession.Instance().getCombinedSpacesBackup().get(i).getStatus().equalsIgnoreCase(APP.getStatusOccupied())){
                    combinedSpaces.add(SingletonSession.Instance().getCombinedSpacesBackup().get(i));
                }
            }
        }
    }

    //calculate report display specifics
    private static void showStatDetails(){
        //total overall capacity and number of sections
        totalSectionstxt.setText(totalSections+"");
        totalCapacitytxt.setText(totalSpaces+"");

        //total spaces occupied and available
        totalOccupiedtxt.setText(totalOccupied+"");
        totalAvailabletxt.setText(totalAvailable+"");

        //populate the sections recycler
        for(int i=0;i<totalSectionSpaces.size();i++){
            //name,description,capacity,address
            HashMap<String,String> curSec = new HashMap<>();
            curSec.put("name",activity.getResources().getString(R.string.name_sections)+": "+sectionTags.get(i));
            curSec.put("capacity",totalSectionSpaces.get(i)+"");
            curSec.put("description",activity.getResources().getString(R.string.capacity_sections)+" "+totalSectionSpaces.get(i)+"\n"+
                    activity.getResources().getString(R.string.available_sections)+" "+totalSectionAvailable.get(i)+"\n"+
                    activity.getResources().getString(R.string.occupied_sections)+" "+totalSectionOccupied.get(i));
            curSec.put("address",activity.getResources().getString(R.string.label_occupied)+": "+
                    (String.format("%.2f",((float)totalSectionOccupied.get(i)/(float)
                            totalSectionSpaces.get(i))*100))+"%");

            sectionsAll.add(curSec);
        }
    }

    private static class CalculateStatDetails extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            //Setup preconditions if any
        }

        @Override
        protected String doInBackground(String... params) {
            //the magic happens here
            for(int i=0;i<combinedSpaces.size();i++) {

                publishProgress ((float)i/(float)combinedSpaces.size()+"% Complete");
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //Update the progress of current task
            Log.d("FILTER_PROGRESS",values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //Show the result obtained from doInBackground
            details.setText(result);
        }
    }

    /*
    private static void showStatDetailsOld(){
        //total overall capacity and number of sections
        totalSectionstxt.setText(totalSections+"");
        totalCapacitytxt.setText(totalSpaces+"");

        //total spaces occupied and available
        totalOccupiedtxt.setText(totalOccupied+"");
        totalAvailabletxt.setText(totalAvailable+"");

        String sectionTotalPercentages = "";
        //capacity of each section and number of used and available for each section (percentage % occupied)
        for(int i=0;i<totalSectionSpaces.size();i++){
            //name,description,capacity,address
            HashMap<String,String> curSec = new HashMap<>();
            curSec.put("name",activity.getResources().getString(R.string.name_sections)+": "+sectionTags.get(i));
            curSec.put("capacity",totalSectionSpaces.get(i)+"");
            curSec.put("description",activity.getResources().getString(R.string.capacity_sections)+" "+totalSectionSpaces.get(i)+"\n"+
                    activity.getResources().getString(R.string.available_sections)+" "+totalSectionAvailable.get(i)+"\n"+
                    activity.getResources().getString(R.string.occupied_sections)+" "+totalSectionOccupied.get(i));
            curSec.put("address",activity.getResources().getString(R.string.label_occupied)+": "+
                    (String.format("%.2f",((float)totalSectionOccupied.get(i)/(float)
                            totalSectionSpaces.get(i))*100))+"%");

            sectionTotalPercentages = sectionTotalPercentages + activity.getResources().getString(R.string.label_section)
                    +" "+sectionTags.get(i)+" - "+
                    (String.format("%.2f",((float)totalSectionOccupied.get(i)/(float) totalSpaces)*100)+"% "
                            +activity.getResources().getString(R.string.of_total)+"\n");

            sectionsAll.add(curSec);
        }

        float totes = ((float)totalOccupied/(float)totalSpaces)*100;
        if(totalSections>0){
            if(totes>0){}else{totes = 0.00f;}
            details.setText(
                    activity.getResources().getString(R.string.label_summary)+": "
                            +(String.format("%.2f",totes))+"% "
                            +activity.getResources().getString(R.string.overall_total_occupied)+"\n"+sectionTotalPercentages
            );
        }
        else{details.setVisibility(View.GONE);}
        //total overall time occupied

        //total time for each section
    }
    */

}

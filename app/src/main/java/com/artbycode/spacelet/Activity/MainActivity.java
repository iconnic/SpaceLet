package com.artbycode.spacelet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.artbycode.spacelet.Adapter.MainMenuAdapter;
import com.artbycode.spacelet.R;
import com.artbycode.spacelet.Tool.NpaGridLayoutManager;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        initContent();
        initMenuRecycler();
    }

    private static HashMap<String, HashMap<String,String>> items = new HashMap<>();
    private static void initContent(){
        String[] titles = activity.getResources().getStringArray(R.array.menu_titles);
        String[] names = activity.getResources().getStringArray(R.array.menu_names);
        String[] descriptions = activity.getResources().getStringArray(R.array.menu_descriptions);

        for(int i=0;i<titles.length;i++){
            HashMap<String,String> singleItem = new HashMap<>();
            singleItem.put("title",titles[i]);
            singleItem.put("name",names[i]);
            singleItem.put("description",descriptions[i]);

            items.put(i+"",singleItem);
        }
    }

    private static RecyclerView recycler;
    private static RecyclerView.Adapter adapter;
    private static int gridnum=1;
    private void initMenuRecycler(){
        recycler=(RecyclerView)findViewById(R.id.recycler); recycler.setHasFixedSize(true);
        final NpaGridLayoutManager mLayoutManager = new NpaGridLayoutManager(activity,
                gridnum, LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(mLayoutManager);
        adapter = new MainMenuAdapter(activity,items);

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
        adapter.notifyItemRangeInserted(adapter.getItemCount(),items.size() - 1); adapter.notifyDataSetChanged();
    }

    private static void resetAllAdapters(){
        adapter = new MainMenuAdapter(activity,items); recycler.setAdapter(adapter);
    }
}


package com.artbycode.spacelet.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Activity.FilterViewActivity;
import com.artbycode.spacelet.Activity.NewSectionActivity;
import com.artbycode.spacelet.Activity.ViewSectionsActivity;
import com.artbycode.spacelet.R;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder>{
    private static AppCompatActivity activity;
    private int lastPosition = -1;
    private static HashMap<String,HashMap<String,String>> items = new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid; TextView name,description,title;
        ViewHolder(View itemView, int ViewType){
            super(itemView);

            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            title=itemView.findViewById(R.id.title);

            Holderid = 0;
        }
    }

    public MainMenuAdapter(AppCompatActivity activity, HashMap<String,HashMap<String,String>> items){this.activity=activity; this.items = items;}

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_item,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);

        return vhItem;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position){
        final ViewHolder hold = holder; final int pos = position;

        holder.name.setText(items.get(pos+"").get("name"));
        holder.description.setText(items.get(pos+"").get("description"));
        holder.title.setText(items.get(pos+"").get("title"));

        holder.itemView.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            APP.getAnimViews().click(hold.itemView,400);
            APP.getAnimViews().click(hold.name,100);
            APP.getAnimViews().click(hold.description,200);
            APP.getAnimViews().click(hold.title,300);
            hold.itemView.animate().setDuration(400).setInterpolator(new BounceInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    menuItemClicked(pos);
                }
            }).start();
        }});
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static void menuItemClicked(int position){
        switch(position){
            case 0:
                Intent intentNew = new Intent(activity, NewSectionActivity.class);
                activity.startActivity(intentNew);
                activity.finish();
                break;

            case 1:
                Intent intentView = new Intent(activity, ViewSectionsActivity.class);
                activity.startActivity(intentView);
                activity.finish();
                break;

            case 2:
                Intent intentFilter = new Intent(activity, FilterViewActivity.class);
                activity.startActivity(intentFilter);
                activity.finish();
                break;
        }
    }
}

package com.artbycode.spacelet.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Activity.SectionReportActivity;
import com.artbycode.spacelet.Activity.SingleSectionActivity;
import com.artbycode.spacelet.Activity.ViewSectionsActivity;
import com.artbycode.spacelet.Data.Section;
import com.artbycode.spacelet.R;
import com.artbycode.spacelet.SingletonSession;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class FilterSectionAdapter extends RecyclerView.Adapter<FilterSectionAdapter.ViewHolder>{
    private static AppCompatActivity activity;
    private int lastPosition = -1;
    private static ArrayList<HashMap<String,String>> items = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid; TextView name,description,capacity,address;
        ViewHolder(View itemView, int ViewType){
            super(itemView);

            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            capacity=itemView.findViewById(R.id.capacity);
            address=itemView.findViewById(R.id.address);

            Holderid = 0;
        }
    }

    public FilterSectionAdapter(AppCompatActivity activity, ArrayList<HashMap<String,String>> items){this.activity=activity; this.items = items;}

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_section_item,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);

        return vhItem;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position){
        final ViewHolder hold = holder; final int pos = position;

        holder.name.setText(items.get(pos).get("name"));
        holder.description.setText(items.get(pos).get("description"));
        holder.capacity.setText(items.get(pos).get("capacity"));
        holder.address.setText(items.get(pos).get("address"));

        holder.itemView.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            APP.getAnimViews().click(hold.itemView,400);
            APP.getAnimViews().click(hold.name,100);
            APP.getAnimViews().click(hold.description,200);
            APP.getAnimViews().click(hold.address,300);
            APP.getAnimViews().click(hold.capacity,400);
            hold.itemView.animate().setDuration(400).setInterpolator(new BounceInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                }
            }).start();
        }});
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

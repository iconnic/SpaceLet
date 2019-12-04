package com.artbycode.spacelet.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Activity.SingleSectionActivity;
import com.artbycode.spacelet.Data.Section;
import com.artbycode.spacelet.Data.Space;
import com.artbycode.spacelet.R;
import com.artbycode.spacelet.SingletonSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SingleSpaceAdapter extends RecyclerView.Adapter<SingleSpaceAdapter.ViewHolder>{
    private static AppCompatActivity activity;
    private int lastPosition = -1;
    private static ArrayList<Space> items = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid; TextView name,description,price,time; RelativeLayout main;
        ViewHolder(View itemView, int ViewType){
            super(itemView);

            main=itemView.findViewById(R.id.single_space_item);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            price=itemView.findViewById(R.id.price);
            time=itemView.findViewById(R.id.time);

            Holderid = 0;
        }
    }

    public SingleSpaceAdapter(AppCompatActivity activity, ArrayList<Space> items){this.activity=activity; this.items = items;}

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_space_item,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);

        return vhItem;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position){
        final ViewHolder hold = holder; final int pos = position;

        if(items.get(pos).getStatus().equalsIgnoreCase(APP.getStatusAvailable())){
            holder.main.setBackground(activity.getResources().getDrawable(R.drawable.curve_accent));
        }

        if(items.get(pos).getStatus().equalsIgnoreCase(APP.getStatusOccupied())){
            holder.main.setBackground(activity.getResources().getDrawable(R.drawable.curve_outline));
        }

        holder.name.setText(items.get(pos).getName());
        holder.description.setText(items.get(pos).getDescription());
        holder.price.setText(items.get(pos).getPrice());
        holder.time.setText(items.get(pos).getTimeOccupied());

        holder.itemView.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            APP.getAnimViews().click(hold.itemView,400);
            APP.getAnimViews().click(hold.name,100);
            APP.getAnimViews().click(hold.description,200);
            APP.getAnimViews().click(hold.price,300);
            APP.getAnimViews().click(hold.time,400);
            hold.itemView.animate().setDuration(400).setInterpolator(new BounceInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    String newStatus; String time = activity.getString(R.string.label_not_available);
                    if(items.get(pos).getStatus().equalsIgnoreCase(APP.getStatusAvailable())){
                        newStatus = APP.getStatusOccupied();
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm");
                        time = format.format(Calendar.getInstance().getTime());
                    }else{
                        newStatus = APP.getStatusAvailable();
                    }
                    SingleSectionActivity.changeStatus(items.get(pos).getSpaceId(),newStatus,time);
                }
            }).start();
        }});

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                APP.getAnimViews().click(hold.itemView,400);
                //open the popup to edit the specific space details
                SingletonSession.Instance().setSpace(items.get(pos));
                SingleSectionActivity.setSlideSpaceDetails();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

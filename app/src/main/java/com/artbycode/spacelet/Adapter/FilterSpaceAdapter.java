package com.artbycode.spacelet.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.Data.Space;
import com.artbycode.spacelet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FilterSpaceAdapter extends RecyclerView.Adapter<FilterSpaceAdapter.ViewHolder>{
    private static AppCompatActivity activity;
    private int lastPosition = -1;
    private static ArrayList<Space> items = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid; TextView name,description,price,time,status; RelativeLayout main;
        ViewHolder(View itemView, int ViewType){
            super(itemView);

            main=itemView.findViewById(R.id.filter_space_item);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            price=itemView.findViewById(R.id.price);
            time=itemView.findViewById(R.id.time);
            status=itemView.findViewById(R.id.status);

            Holderid = 0;
        }
    }

    public FilterSpaceAdapter(AppCompatActivity activity, ArrayList<Space> items){this.activity=activity; this.items = items;}

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_space_item,parent,false);
        ViewHolder vhItem = new ViewHolder(v,viewType);

        return vhItem;
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position){
        final ViewHolder hold = holder; final int pos = position;

        holder.name.setText(items.get(pos).getName());
        holder.description.setText(items.get(pos).getDescription());
        holder.price.setText(items.get(pos).getPrice());
        //holder.time.setText(items.get(pos).getTimeOccupied());
        holder.time.setText(elapsedTime(pos));

        if(items.get(pos).getStatus().equalsIgnoreCase(APP.getStatusOccupied())){
            holder.status.setText(activity.getResources().getString(R.string.label_occupied));
        }else{
            holder.status.setText(activity.getResources().getString(R.string.label_available));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){
            APP.getAnimViews().click(hold.itemView,400);
            APP.getAnimViews().click(hold.name,100);
            APP.getAnimViews().click(hold.description,200);
            APP.getAnimViews().click(hold.price,300);
            APP.getAnimViews().click(hold.time,400);
            hold.itemView.animate().setDuration(400).setInterpolator(new BounceInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    hold.time.setText(elapsedTime(pos));
                }
            }).start();
        }});

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static String elapsedTime(int pos){
        String timeSince = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm");
        String currentTime = format.format(Calendar.getInstance().getTime());
        String compareTime = items.get(pos).getTimeOccupied();

        try {
            Date firstDate = format.parse(currentTime);
            Date secondDate = format.parse(compareTime);

            //firstDate.compareTo(secondDate);
            long milliSeconds = firstDate.getTime() - secondDate.getTime();
            long seconds = milliSeconds / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if(days>0){timeSince = timeSince + " " + days + activity.getResources().getString(R.string.label_days);}
            if(hours>0){timeSince = timeSince + " " + hours % 24 + activity.getResources().getString(R.string.label_hours);}
            if(minutes>0){timeSince = timeSince + " " + minutes % 60 +activity.getResources().getString(R.string.label_minutes);}
            //if(seconds>0){timeSince = timeSince + " "+}

            //timeSince = days + activity.getResources().getString(R.string.label_days)+" "
            //        + hours % 24 + activity.getResources().getString(R.string.label_hours) + " "
            //        + minutes % 60 +activity.getResources().getString(R.string.label_minutes)+ " ";
        } catch (ParseException e) {
            e.printStackTrace();
            timeSince = activity.getResources().getString(R.string.label_not_available);
        }

        return timeSince;
    }

}

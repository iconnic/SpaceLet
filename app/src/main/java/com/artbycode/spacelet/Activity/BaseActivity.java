package com.artbycode.spacelet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.artbycode.spacelet.APP;
import com.artbycode.spacelet.R;

public class BaseActivity extends AppCompatActivity {
    private static AppCompatActivity activity; private static ProgressDialog loading; private static TextView dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        activity = this;
    }

    public static void showLoading(){
        loading = new ProgressDialog(activity); loading.setCancelable(false);
        loading.setMessage(activity.getResources().getString(R.string.label_loading));
        loading.getWindow().setBackgroundDrawableResource(R.color.colorWhite);
        loading.show();
    }

    public static void endLoading(){if(loading!=null){if(loading.isShowing()){loading.dismiss();}}}

    public static void bottomDialog(boolean showNow,String message){
        dialog = activity.findViewById(R.id.dialog);
        dialog.setText(message);
        if(showNow){
            dialog.setVisibility(View.VISIBLE);
            //APP.getAnimViews().click(dialog,800);
            TranslateAnimation animate = new TranslateAnimation(0, 0, dialog.getHeight(), 0);
            animate.setDuration(800);
            animate.setFillAfter(true);
            dialog.startAnimation(animate);
        }else {
            //APP.getAnimViews().out(dialog,800);
            TranslateAnimation animate = new TranslateAnimation(0, 0,0,dialog.getHeight());
            animate.setDuration(800);
            animate.setFillAfter(true);
            dialog.startAnimation(animate);

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    dialog.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 800);
        }
    }


    private long backbtn;
    @Override
    public void onBackPressed(){
        if(backbtn+2000>System.currentTimeMillis())super.onBackPressed();
        else{
            APP.getToast().showToast(this,getResources().getString(R.string.app_exit),true);
        }
        backbtn=System.currentTimeMillis();
    }
}

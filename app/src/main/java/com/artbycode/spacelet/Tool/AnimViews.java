package com.artbycode.spacelet.Tool;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Iconnic on 6/28/2018.
 */

public class AnimViews {
    private Random r = new Random(); private float starter=0.8f; private Boolean thisone=true;

    public AnimViews(){}

    public void runAnim(ArrayList<View> views,int duration,int type){
        int speed;
        for(int i=0;i<views.size();i++){
            speed=(r.nextInt(duration - (duration/10)) + (duration/10));
            switch (type){
                case 0: starter=0.0f; in(views.get(i),speed); break; //in
                case 1: starter=0.0f; out(views.get(i),speed); break; //out
                case 2: views.get(i).setVisibility(View.GONE); break; //invisible
                case 3: views.get(i).setVisibility(View.VISIBLE); break; //visible
                case 4: fancyOut(views.get(i),speed); break; //fancy out
                case 5: click(views.get(i),speed); break; //bounce
            }
        }
    }

    public void in(View v,int anim_speed){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0.0f, 1.0f).setDuration(anim_speed);
        ObjectAnimator xx = ObjectAnimator.ofFloat(v, "scaleX", starter, 1.0f).setDuration(anim_speed);
        ObjectAnimator yy = ObjectAnimator.ofFloat(v, "scaleY", starter, 1.0f).setDuration(anim_speed);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.play(alpha).with(xx).with(yy);
        animatorSet.start();
    }

    public void out(View v,int anim_speed){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.0f).setDuration(anim_speed);
        ObjectAnimator xx = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, starter).setDuration(anim_speed);
        ObjectAnimator yy = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, starter).setDuration(anim_speed);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.play(alpha).with(xx).with(yy);
        animatorSet.start();
    }

    public void fancyOut(View v, int anim_speed){
        ObjectAnimator one = ObjectAnimator.ofFloat(v, "translationX", 0.0f, -1200f).setDuration(anim_speed);
        ObjectAnimator two = ObjectAnimator.ofFloat(v, "translationX", 0.0f, 1200f).setDuration(anim_speed);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new BounceInterpolator());
        if(thisone){animatorSet.play(one); thisone=false;}else{animatorSet.play(two); thisone=true;}
        animatorSet.start();
    }

    public void click(View v,int anim_speed){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0.8f, 1.0f).setDuration(anim_speed);
        ObjectAnimator xx = ObjectAnimator.ofFloat(v, "scaleX", 0.9f, 1.0f).setDuration(anim_speed);
        ObjectAnimator yy = ObjectAnimator.ofFloat(v, "scaleY", 0.9f, 1.0f).setDuration(anim_speed);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.play(alpha).with(xx).with(yy);
        animatorSet.start();
    }

    public void highlight(View v,int anim_speed,int repeat){
        ObjectAnimator alpha = ObjectAnimator.ofFloat(v, "alpha", 0.8f, 1.0f).setDuration(anim_speed);
        ObjectAnimator xx = ObjectAnimator.ofFloat(v, "scaleX", 0.95f, 1.0f).setDuration(anim_speed);
        ObjectAnimator yy = ObjectAnimator.ofFloat(v, "scaleY", 0.95f, 1.0f).setDuration(anim_speed);
        alpha.setRepeatCount(repeat); alpha.setRepeatMode(ObjectAnimator.REVERSE);
        xx.setRepeatCount(repeat); xx.setRepeatMode(ObjectAnimator.REVERSE);
        yy.setRepeatCount(repeat); yy.setRepeatMode(ObjectAnimator.REVERSE);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.play(alpha).with(xx).with(yy);
        animatorSet.start();
    }
}

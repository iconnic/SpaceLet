package com.artbycode.spacelet.View;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.artbycode.spacelet.R;

/**
 * Created by Iconnic on 3/27/2018.
 */

public class IconnicToast {
    private static IconnicToast iconnicToast;
    private static Toast toast;

    public static IconnicToast getInstance(){if(iconnicToast == null){return new IconnicToast();} return iconnicToast;}

    public IconnicToast(){}

    public void showToast(Context context , String text, boolean isShort){
        LayoutInflater inflater = LayoutInflater.from(context);
        CardView layout = (CardView)inflater.inflate(R.layout.iconnictoast, null);
        TextView tv = (TextView)layout.findViewById(R.id.msg); tv.setText(text);

        toast = new Toast(context); toast.setView(layout); toast.setGravity(Gravity.CENTER, 0, 0);
        if(isShort){toast.setDuration(Toast.LENGTH_SHORT);}else{toast.setDuration(Toast.LENGTH_LONG);}

        toast.show();
    }

    public void cancelToast(){toast.cancel();}
}

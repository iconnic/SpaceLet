package com.artbycode.spacelet;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import com.artbycode.spacelet.Tool.AnimViews;
import com.artbycode.spacelet.View.IconnicToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class APP extends Application {
    private static final String FIREBASE_EMAIL = "please replace this with your own";
    private static final String FIREBASE_UID = "please replace this with your own";
    private static final String FIREBASE_PWD = "please replace this with your own";

    private static final String FIREBASE_PATH = "/spaceLet/";
    private static final String FIREBASE_PATH_SECTION = "section/";
    private static final String FIREBASE_PATH_SPACE = "space/";
    private static final String FIREBASE_PATH_USER = "user/";

    private static final String STATUS_AVAILABLE = "0";
    private static final String STATUS_OCCUPIED = "1";
    private static final String STATUS_OFFLINE = "404";

    private static final String[] sectionFields = {"sectionId","status","name","description","capacity","address","imgURL"};
    private static final String[] spaceFields = {"spaceId","status","name","description","price","timeOccupied","imgURL"};
    private static final String[] userFields = {"userId","number","password","username","status","imgURL"};

    private static AnimViews anim = new AnimViews();

    private static IconnicToast toast = new IconnicToast();

    private static Boolean firebaseOnline = false;

    @Override public void onCreate(){
        super.onCreate();

        loginToFirebase();
    }

    public static AnimViews getAnimViews(){return anim;}

    public static IconnicToast getToast(){return toast;}

    public static String getFirebaseEmail() {
        return FIREBASE_EMAIL;
    }

    public static String getFirebaseUid() {
        return FIREBASE_UID;
    }

    public static String getFirebasePwd() {return FIREBASE_PWD;}

    public static String getFirebasePath() {return FIREBASE_PATH;}

    public static String getFirebasePathSection() {return getFirebasePath()+FIREBASE_PATH_SECTION;}

    public static String getFirebasePathSpace() {return getFirebasePath()+FIREBASE_PATH_SPACE;}

    public static String getFirebasePathUser() {return getFirebasePath()+FIREBASE_PATH_USER;}

    public static String[] getSectionFields() {return sectionFields;}

    public static String[] getSpaceFields() {return spaceFields;}

    public static String[] getUserFields() {return userFields;}

    public static String getStatusAvailable() {return STATUS_AVAILABLE;}

    public static String getStatusOccupied() {return STATUS_OCCUPIED;}

    public static String getStatusOffline() {return STATUS_OFFLINE;}

    public static Boolean getFirebaseOnline(){return firebaseOnline;}

    public static void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = getFirebaseEmail();
        String password = getFirebasePwd();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("FIREBASE_LOGIN", "firebase authentication success");
                    firebaseOnline = true;
                } else {
                    //toast.showToast(context,context.getResources().getString(R.string.firebase_connect_error),true);
                    Log.d("FIREBASE_LOGIN", "firebase authentication failed");
                    firebaseOnline = false;
                }
            }
        });
    }

}

package com.softwaremobility.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by darkgeat on 5/24/16.
 */
public class Utils {

    private static final int REQUEST_CODE_NETWORK = 1787;

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public static boolean permissionNetworkGranted(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                // Should we show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_NETWORK_STATE)){
                    return false;
                }else {
                    //No explanation needed, we can request the permissions.
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},REQUEST_CODE_NETWORK);
                    return false;
                }
            }else {
                return true;
            }
        }else {
            return true;
        }
    }
}

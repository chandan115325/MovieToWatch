package com.chandan.android.movietowatch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by CHANDAN on 8/4/2017.
 */

public class NetworkHelper {

    public static boolean hasNetworkAccess(Context context){

        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return  activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

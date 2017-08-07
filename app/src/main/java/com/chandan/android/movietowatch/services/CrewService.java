package com.chandan.android.movietowatch.services;

/**
 * Created by CHANDAN on 8/6/2017.
 */

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.chandan.android.movietowatch.model.Crew;
import com.chandan.android.movietowatch.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by CHANDAN on 8/3/2017.
 */

public class CrewService extends IntentService {

    private String profilePath;
    private String name;
    private String job;
    private int size;
    private String type;

    private ArrayList<Crew> crewArrayList = new ArrayList<>();

    public static final String TAG = "MyServiceCrew";
    public static final String MY_SERVICE_CREW = "myServiceCrew";
    public static final String MY_SERVICE_PAYLOAD_CREW = "myServicePayloadCrew";


    public CrewService() {
        super("MyServicesCrew");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: "+uri.toString());

        String response;
        try{
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.i(TAG,"response:" +response);
            JSONArray itemsArray = jsonObject.getJSONArray("crew");

            HashMap<String, String> hashMap = new HashMap<>();
            //processing JSON data
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject cast = itemsArray.getJSONObject(i);

                profilePath = cast.getString("profile_path");
                name = cast.getString("name");
                job = cast.getString("job");

                crewArrayList.add(new Crew( name, job, profilePath));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent messageIntent = new Intent(MY_SERVICE_CREW);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD_CREW, crewArrayList);


        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}

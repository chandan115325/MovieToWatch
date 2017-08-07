package com.chandan.android.movietowatch.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.chandan.android.movietowatch.model.Movie;
import com.chandan.android.movietowatch.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CHANDAN on 8/3/2017.
 */

public class MovieService extends IntentService {
    private int voteCount;
    private int id;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private List<Integer> genreIds = null;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;


    private ArrayList<Movie> arrayMovieList = new ArrayList<>();

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";


    public MovieService() {
        super("MyServices");
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
            JSONArray itemsArray = jsonObject.getJSONArray("results");

            HashMap<String, String> hashMap = new HashMap<>();
            //processing JSON data
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject movie = itemsArray.getJSONObject(i);

                 id = movie.getInt("id");
                 title = movie.getString("title");
                 originalTitle = movie.getString("original_title");
                 releaseDate = movie.getString("release_date");
                 popularity = movie.getDouble("popularity");
                 voteAverage = movie.getDouble("vote_average");
                 voteCount = movie.getInt("vote_count");
                 posterPath = movie.getString("poster_path");
                 video = movie.getBoolean("video");
                 overview = movie.getString("overview");
                 backdropPath = movie.getString("backdrop_path");

                arrayMovieList.add(new Movie(title, voteCount,  id,  video, voteAverage,  popularity, posterPath,
                         originalTitle,  backdropPath,  overview,  releaseDate));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, arrayMovieList);


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

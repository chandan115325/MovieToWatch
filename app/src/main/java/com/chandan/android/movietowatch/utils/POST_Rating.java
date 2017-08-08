package com.chandan.android.movietowatch.utils;

import android.os.AsyncTask;
import android.util.Log;

import static android.R.attr.id;

public class POST_Rating extends AsyncTask<Object, Object, String> {
    int movie_id;
    Float mrating;
    String Guest_id;
    int user_Count;

    public POST_Rating(int movie_id, float rating, String guest_id, int user_count) {
        this.movie_id = movie_id;
        this.mrating = rating;
        this.Guest_id = guest_id;
        this.user_Count = user_count;
    }

    @Override
    protected String doInBackground(Object... strings) {
        String url_post = "http://api.themoviedb.org/3/movie/"+id+"/rating?api_key=8496be0b2149805afa458ab8ec27560c&guest_session_id="+Guest_id;
        JSONFunctions_post JSON_post = new JSONFunctions_post(mrating, user_Count);
        Log.e("Post rating", mrating+", "+user_Count);
        JSON_post.postJSON_toURL(url_post);
    return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

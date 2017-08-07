package com.chandan.android.movietowatch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.chandan.android.movietowatch.adapter.MoviesAdapter;
import com.chandan.android.movietowatch.model.Movie;
import com.chandan.android.movietowatch.services.MovieService;
import com.chandan.android.movietowatch.utils.NetworkHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
   // private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    //private FavoriteDbHelper favoriteDbHelper;
    private AppCompatActivity activity = MainActivity.this;
    public static final String LOG_TAG = MoviesAdapter.class.getName();

    private ArrayList<Movie> movieList = new ArrayList<>();
    private static final String JSON_URL =
            " https://api.themoviedb.org/3/movie/popular?api_key=047d097b7479b4e84c76129f2d4566ec";
            //" https://api.themoviedb.org/3/movie/top_rated?api_key=047d097b7479b4e84c76129f2d4566ec"
            //" https://api.themoviedb.org/3/movie/popular?api_key=047d097b7479b4e84c76129f2d4566ec"
            //" https://api.themoviedb.org/3/movie/upcoming?api_key=047d097b7479b4e84c76129f2d4566ec"
            //" https://api.themoviedb.org/3/movie/now_playing?api_key=047d097b7479b4e84c76129f2d4566ec";

    private TextView networkStatus,output;

    private boolean networkOk;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

           /* MovieResponse[] dataItems = (MovieResponse[]) intent
                    .getParcelableArrayExtra(MovieService.MY_SERVICE_PAYLOAD);
            for (MovieResponse item : dataItems) {
                output.append(item.getPage() + "\n");
            }
            */

           movieList = intent.getParcelableArrayListExtra(MovieService.MY_SERVICE_PAYLOAD);
            initViews(movieList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*networkStatus = (TextView)findViewById(R.id.network_status);
        output = (TextView)findViewById(R.id.output);
*/
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MovieService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(MainActivity.this);
       // networkStatus.setText("Network ok: " + networkOk);




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadJSON(JSON_URL);


    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }


    private void initViews(final ArrayList<Movie> movieList){

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MoviesAdapter(this, movieList);



        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //for LinearLayout like a list use this code
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
            //for LinearLayout like a list use this code
            //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       // favoriteDbHelper = new FavoriteDbHelper(activity);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                initViews(movieList);
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        //checkSortOrder();

    }


    // LoadJson
    public void loadJSON(String jsonURL){
        if(networkOk) {
            Intent intent = new Intent(MainActivity.this, MovieService.class);
            intent.setData(Uri.parse(jsonURL));
            startService(intent);
        }
        else{
            Toast.makeText(MainActivity.this,"Network not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.menu_top_rated):
                {
                    String URL_JSON = " https://api.themoviedb.org/3/movie/top_rated?api_key=047d097b7479b4e84c76129f2d4566ec";
                    loadJSON(URL_JSON);
                }
            case (R.id.menu_upcoming):
            {
                String URL_JSON = " https://api.themoviedb.org/3/movie/upcoming?api_key=047d097b7479b4e84c76129f2d4566ec";
                loadJSON(URL_JSON);
            }

            case (R.id.menu_now_playing):
            {
                String URL_JSON = " https://api.themoviedb.org/3/movie/now_playing?api_key=047d097b7479b4e84c76129f2d4566ec";
                loadJSON(URL_JSON);
            }

            case (R.id.menu_most_popular):
            {
                String URL_JSON = "https://api.themoviedb.org/3/movie/popular?api_key=047d097b7479b4e84c76129f2d4566ec";
                loadJSON(URL_JSON);
            }

            case (R.id.menu_latest):
            {
                String URL_JSON = " https://api.themoviedb.org/3/movie/now_playing?api_key=047d097b7479b4e84c76129f2d4566ec";
                loadJSON(URL_JSON);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

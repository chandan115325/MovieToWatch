package com.chandan.android.movietowatch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chandan.android.movietowatch.R;
import com.chandan.android.movietowatch.model.Crew;

import java.util.ArrayList;

/**
 * Created by CHANDAN on 8/7/2017.
 */

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Crew> crewList;

    public CrewAdapter(Context mContext, ArrayList<Crew> crewList){
        this.mContext = mContext;
        this.crewList = crewList;

    }

    @Override
    public CrewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cast_crew_custom, viewGroup, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final CrewAdapter.MyViewHolder viewHolder, int i){
        viewHolder.name.setText(crewList.get(i).getName());
        viewHolder.job.setText(crewList.get(i).getJob());
        //viewHolder.castImage.setImageResource(castList.get(i).);
        String poster = "https://image.tmdb.org/t/p/w500" + crewList.get(i).getProfilePath();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(viewHolder.crewImage);


    }

    @Override
    public int getItemCount(){

        return crewList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,job;
        public ImageView crewImage;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.text_name);
            job = (TextView) view.findViewById(R.id.textView_job);
            crewImage = (ImageView) view.findViewById(R.id.imageView_crew);




        }
    }
}

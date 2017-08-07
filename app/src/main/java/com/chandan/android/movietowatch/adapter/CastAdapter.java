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
import com.chandan.android.movietowatch.model.Cast;

import java.util.ArrayList;

/**
 * Created by CHANDAN on 8/7/2017.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Cast> castList;

    public CastAdapter(Context mContext, ArrayList<Cast> castList){
        this.mContext = mContext;
        this.castList = castList;

    }

    @Override
    public CastAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cast_crew_list, viewGroup, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final CastAdapter.MyViewHolder viewHolder, int i){
        viewHolder.name.setText(castList.get(i).getName());
        viewHolder.role.setText(castList.get(i).getCharacter());
        //viewHolder.castImage.setImageResource(castList.get(i).);
        String poster = "https://image.tmdb.org/t/p/w500" + castList.get(i).getProfilePath();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(viewHolder.castImage);


    }

    @Override
    public int getItemCount(){

        return castList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,role;
        public ImageView castImage;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.textView_name);
            role = (TextView) view.findViewById(R.id.textView_role);
            castImage = (ImageView) view.findViewById(R.id.imageView_cast_crew);




        }
    }
}

package com.chandan.android.movietowatch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chandan.android.movietowatch.R;
import com.chandan.android.movietowatch.model.Cast;

import java.util.ArrayList;
import java.util.List;

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
        viewHolder.title.setText(castList.get(i).getName());

    }

    @Override
    public int getItemCount(){

        return castList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

           /* view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Trailer clickedDataItem = trailerList.get(pos);
                        String videoId = trailerList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(intent);

                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

        }
    }
}

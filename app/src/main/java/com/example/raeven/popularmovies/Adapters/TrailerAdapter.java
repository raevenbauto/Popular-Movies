package com.example.raeven.popularmovies.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.raeven.popularmovies.Model.TrailerModel;
import com.example.raeven.popularmovies.R;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context mContext;
    private TrailerOnClickListener mTrailerClickListener;
    private ArrayList<TrailerModel> mTrailerObject;


    public TrailerAdapter(TrailerOnClickListener mTrailerClickListener, ArrayList<TrailerModel> mTrailerObject){
        this.mTrailerClickListener = mTrailerClickListener;
        this.mTrailerObject = mTrailerObject;
    }

    public interface TrailerOnClickListener{
        void trailerOnClick(TrailerModel trailersObject);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public VideoView vv_trailerVideo;
        public TextView tv_trailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            vv_trailerVideo = (VideoView) itemView.findViewById(R.id.vv_trailerVideo);
            tv_trailerName = (TextView) itemView.findViewById(R.id.tv_trailerName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            TrailerModel trailerDetailsObject = mTrailerObject.get(position);
            mTrailerClickListener.trailerOnClick(trailerDetailsObject);

        }
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.movie_trailer_list;
        boolean shouldAttach = false;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, null, shouldAttach);


        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        //Bind data information here.
        String trailerName = mTrailerObject.get(position).getTrailerName();
        holder.tv_trailerName.setText(trailerName);
    }

    @Override
    public int getItemCount() {

        if (mTrailerObject != null){
            if (mTrailerObject.size() == 0){
                return 0;
            }

            else {
                return mTrailerObject.size();
            }
        }

        else
            return 0;

    }

    public void loadData(){
        notifyDataSetChanged();
    }

}

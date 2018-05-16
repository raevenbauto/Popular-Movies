package com.example.raeven.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raeven.popularmovies.Model.MovieModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private ArrayList<MovieModel> mMovieDataList = new ArrayList<MovieModel>();
    private Context mContext;

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            iv_moviePoster = (ImageView)itemView.findViewById(R.id.iv_moviePoster);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.movie_layout_list;
        boolean shouldAttach = false;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, null, shouldAttach);


        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String movieImageLink = mMovieDataList.get(position).getMainPosterLink();
        Picasso.with(mContext).load(movieImageLink).into(holder.iv_moviePoster);
    }

    @Override
    public int getItemCount() {
        if (mMovieDataList.size() == 0){
            return 0;
        }

        else {
            return mMovieDataList.size();
        }
    }

    public void loadData(ArrayList<MovieModel> movieDataList){
        mMovieDataList = movieDataList;
        notifyDataSetChanged();
    }


}
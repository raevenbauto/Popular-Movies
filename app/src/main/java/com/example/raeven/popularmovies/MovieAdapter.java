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

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    String [] movieTitles = {"Avengers", "MIB2", "Beauty and the Beast", "Narcos",
                                "Avengers", "MIB2", "Beauty and the Beast", "Narcos",
                                "Avengers", "MIB2", "Beauty and the Beast", "Narcos"};
    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_moviePoster;
        public TextView tv_movieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            iv_moviePoster = (ImageView)itemView.findViewById(R.id.iv_moviePoster);
            tv_movieTitle = (TextView)itemView.findViewById(R.id.tv_movieTitle);
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
        String movieTitle = movieTitles[position];
        holder.tv_movieTitle.setText(movieTitle);
    }

    @Override
    public int getItemCount() {
        if (movieTitles.length == 0){
            return 0;
        }

        else {
            return movieTitles.length;
        }
    }

    public void loadData(){
        notifyDataSetChanged();
    }


}

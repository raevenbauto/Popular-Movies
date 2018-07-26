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

import com.example.raeven.popularmovies.Model.ReviewModel;
import com.example.raeven.popularmovies.Model.TrailerModel;
import com.example.raeven.popularmovies.R;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context mContext;
    private TrailerOnClickListener mReviewsClickListener;
    private ArrayList<ReviewModel> mReviewObject;


    public ReviewsAdapter(TrailerOnClickListener mReviewsClickListener, ArrayList<ReviewModel> mReviewObject){
        this.mReviewsClickListener = mReviewsClickListener;
        this.mReviewObject = mReviewObject;
    }

    public interface TrailerOnClickListener{
        void reviewOnClick(ReviewModel reviewsObject);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_reviewerName;
        public TextView tv_review;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            tv_reviewerName = (TextView) itemView.findViewById(R.id.tv_reviewerName);
            tv_review = (TextView) itemView.findViewById(R.id.tv_review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ReviewModel trailerDetailsObject = mReviewObject.get(position);
            mReviewsClickListener.reviewOnClick(trailerDetailsObject);
        }
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.movie_reviews_list;
        boolean shouldAttach = false;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layout, null, shouldAttach);


        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        //Bind data information here.
        String reviewerName = mReviewObject.get(position).getReviewerName();
        String review = mReviewObject.get(position).getReview();
        System.out.println(reviewerName);
        System.out.println(review);
        holder.tv_reviewerName.setText(reviewerName);
        holder.tv_review.setText(review);
    }

    @Override
    public int getItemCount() {
        if (mReviewObject != null){
            if (mReviewObject.size() == 0){
                return 0;
            }

            else {
                return mReviewObject.size();
            }
        }

        else
            return 0;

    }

    public void loadData(){ notifyDataSetChanged(); }

}

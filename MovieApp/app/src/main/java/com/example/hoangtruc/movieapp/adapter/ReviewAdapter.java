package com.example.hoangtruc.movieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hoangtruc.movieapp.R;
import com.example.hoangtruc.movieapp.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(ArrayList<Review> mReview, Context mContext) {
        this.mReviews = mReview;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.review_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
           Review review=mReviews.get(position);
           holder.mReview=review;
           holder.mReview_Content.setText(review.getmContent());
           holder.mReview_Author.setText(review.getmAuthor());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Review mReview;
        public View mView;
        private TextView mReview_Author;
        private TextView mReview_Content;
        public ViewHolder(View itemView) {
            super(itemView);
            mReview_Author=itemView.findViewById(R.id.textview_review_author);
            mReview_Content=itemView.findViewById(R.id.textview_review_content);

        }
    }
    public void add(List<Review> reviewList){
        mReviews.clear();
        mReviews.addAll(reviewList);
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }
}

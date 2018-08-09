package com.example.hoangtruc.movieapp.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hoangtruc.movieapp.DetailMovie;
import com.example.hoangtruc.movieapp.R;
import com.example.hoangtruc.movieapp.adapter.ReviewAdapter;
import com.example.hoangtruc.movieapp.adapter.TrailerAdapter;
import com.example.hoangtruc.movieapp.model.Movie;
import com.example.hoangtruc.movieapp.model.Review;
import com.example.hoangtruc.movieapp.model.Trailer;
import com.example.hoangtruc.movieapp.utils.ReviewsNetworkUtils;
import com.example.hoangtruc.movieapp.utils.TrailerNetworkUtils;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailFragment extends Fragment implements TrailerNetworkUtils.Listener, ReviewsNetworkUtils.Listener {
    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";
    public static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie mMovie;
    private RecyclerView mRecyclerViewTrailer;
    private RecyclerView mRecyclerViewReview;
    private TrailerAdapter mTrailerApdapter;
    private ReviewAdapter mReviewAdapter;
    private ImageView mImagePosterView;
    private TextView mTextRatingView;
    private TextView mTextTitleView;
    private TextView mTextOverView;
    private TextView mReleaseDateView;

    private Button mButtonWatchTrailer;
    private Button mButtonAddtoFavorite;
    private Button mButtonRemoveFavorite;
    private List<ImageView> mRatingstar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_MOVIE)){
            mMovie=getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity=getActivity();
        CollapsingToolbarLayout collapsingToolbarLayout=activity.findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbarLayout!=null && activity instanceof DetailMovie){
            collapsingToolbarLayout.setTitle(mMovie.getOriginalTitle());
        }
        ImageView image_header=activity.findViewById(R.id.image_view_header);
        if (image_header!=null){
            Glide.with(activity.getApplicationContext())
                    .load(mMovie.getBackdropPath())
                    .into(image_header);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_detail, container, false);
        initViews(view);
        mTextTitleView.setText(mMovie.getOriginalTitle());
        mTextOverView.setText(mMovie.getOverview());
        mReleaseDateView.setText(mMovie.getReleaseDate());
        Glide.with(view.getContext())
                .load(mMovie.getPosterPath())
                .into(mImagePosterView);
        loadTrailer();
        loadReview();
        getTrailer();
        getReview();
        return view;
    }

    private void initViews(View view) {
        mRecyclerViewTrailer=view.findViewById(R.id.recycler_view_trailer_list);
        mRecyclerViewReview=view.findViewById(R.id.recycler_view_review_list);
        mImagePosterView=view.findViewById(R.id.image_movie_poster);
        mTextRatingView=view.findViewById(R.id.textview_movie_user_rating);
        mTextTitleView=view.findViewById(R.id.textview_movie_title);
        mTextOverView=view.findViewById(R.id.textview_movie_overview);
        mReleaseDateView=view.findViewById(R.id.textview_movie_release_date);
        mButtonWatchTrailer=view.findViewById(R.id.button_watch_trailer);
        mButtonAddtoFavorite=view.findViewById(R.id.button_favorite);
        mButtonRemoveFavorite=view.findViewById(R.id.button_remove_favorites);
    }

    private void loadTrailer() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewTrailer.setLayoutManager(manager);
        mTrailerApdapter = new TrailerAdapter(new ArrayList<Trailer>(), getActivity().getApplicationContext());
        mRecyclerViewTrailer.setAdapter(mTrailerApdapter);
        mRecyclerViewTrailer.setNestedScrollingEnabled(false);
        getTrailer();
    }

    private void getTrailer() {
        TrailerNetworkUtils task = new TrailerNetworkUtils(MovieDetailFragment.this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    private void loadReview() {
        mReviewAdapter = new ReviewAdapter(new ArrayList<Review>(), getActivity().getApplicationContext());
        mRecyclerViewReview.setAdapter(mReviewAdapter);
        getReview();
    }

    private void getReview() {
        ReviewsNetworkUtils task = new ReviewsNetworkUtils(MovieDetailFragment.this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    @Override
    public void onReviewsFinished(List<Review> reviews) {
        mReviewAdapter.add(reviews);
    }

    @Override
    public void onLoadFinished(List<Trailer> trailers) {
        mTrailerApdapter.add(trailers);
        mButtonWatchTrailer.setEnabled(!trailers.isEmpty());
    }
}

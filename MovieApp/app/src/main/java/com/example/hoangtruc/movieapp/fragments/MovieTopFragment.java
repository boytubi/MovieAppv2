package com.example.hoangtruc.movieapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoangtruc.movieapp.BuildConfig;
import com.example.hoangtruc.movieapp.R;
import com.example.hoangtruc.movieapp.adapter.MovieDataAdapter;
import com.example.hoangtruc.movieapp.model.Movie;
import com.example.hoangtruc.movieapp.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MovieTopFragment extends Fragment {
    public static final String TOP_RATED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";
    private ArrayList<Movie> mTopRatedList;
    private String topRatedMovies;
    private RecyclerView mRecyclerView;
    private MovieDataAdapter mMovieDataAdapter;

    public MovieTopFragment() {
    }
    public static MovieTopFragment newInstance(){
        MovieTopFragment movieTopFragment=new MovieTopFragment();
        Bundle bundle=new Bundle();
        movieTopFragment.setArguments(bundle);
        return movieTopFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_top_rate,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
       mRecyclerView =view.findViewById(R.id.recycler_view);
       mMovieDataAdapter=new MovieDataAdapter(getActivity().getApplicationContext(),new ArrayList<Movie>());
       mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));
       mRecyclerView.setAdapter(mMovieDataAdapter);
       new FetchTopMovie().execute();
       mMovieDataAdapter.notifyDataSetChanged();
    }
    public class FetchTopMovie extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mTopRatedList=new ArrayList<>();
            topRatedMovies=TOP_RATED_URL+ BuildConfig.THE_MOVIE_DB_API_TOKEN;
            try {
                mTopRatedList= NetworkUtils.fetchData(topRatedMovies);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMovieDataAdapter=new MovieDataAdapter(getActivity().getApplicationContext(),new ArrayList<Movie>());
            mMovieDataAdapter.add(mTopRatedList);
            mRecyclerView.setAdapter(mMovieDataAdapter);
        }
    }
}

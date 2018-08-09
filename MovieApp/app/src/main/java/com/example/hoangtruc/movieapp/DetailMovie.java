package com.example.hoangtruc.movieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.example.hoangtruc.movieapp.fragments.MovieDetailFragment;


public class DetailMovie extends AppCompatActivity {
    private Toolbar mToolbar_detail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_movie);
        mToolbar_detail=findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar_detail);
         if (savedInstanceState==null){
             Bundle arg=new Bundle();
             arg.putParcelable(MovieDetailFragment.ARG_MOVIE,getIntent().getParcelableExtra(MovieDetailFragment.ARG_MOVIE));
             MovieDetailFragment fragment=new MovieDetailFragment();
             fragment.setArguments(arg);
             getSupportFragmentManager().beginTransaction()
                     .add(R.id.nested_scroll_movie_detail,fragment).commit();
         }
    }
}

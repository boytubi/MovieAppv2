package com.example.hoangtruc.movieapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.hoangtruc.movieapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils  {
    private static final String  LOG_TAG=NetworkUtils.class.getSimpleName();
    public static ArrayList<Movie> fetchData(String url) throws IOException{
        ArrayList<Movie> movies=new ArrayList<>();
        try {
            URL url_movie= new URL(url);
            HttpURLConnection connection= (HttpURLConnection) url_movie.openConnection();
            connection.connect();

            InputStream ip=connection.getInputStream();
            String result= readStream(ip);
            parseJSON(result,movies);
            ip.close();
        }catch (IOException e){
            e.printStackTrace();
        }
          return movies;
    }
    public static void parseJSON(String data,ArrayList<Movie> movies){
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray resArray=jsonObject.getJSONArray("results");
            for (int i=0;i<resArray.length();i++){
                JSONObject object=resArray.getJSONObject(i);
                Movie movie=new Movie();
                movie.setId(object.getLong("id"));
                movie.setVoteAverage(object.getString("vote_average"));
                movie.setOriginalTitle(object.getString("original_title"));
                movie.setBackdropPath(object.getString("backdrop_path"));
                movie.setOverview(object.getString("overview"));
                movie.setReleaseDate(object.getString("release_date"));
                movie.setPosterPath(object.getString("poster_path"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }
    public static Boolean networkStatus(Context context){
        ConnectivityManager manager= (ConnectivityManager)
                context.getSystemService((Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if (networkInfo!=null &&networkInfo.isConnected()){
            return true;
        }
        return false;
    }

   private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

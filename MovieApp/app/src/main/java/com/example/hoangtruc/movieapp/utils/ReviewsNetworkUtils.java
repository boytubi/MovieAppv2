package com.example.hoangtruc.movieapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.hoangtruc.movieapp.BuildConfig;
import com.example.hoangtruc.movieapp.common.Constant;
import com.example.hoangtruc.movieapp.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReviewsNetworkUtils extends AsyncTask<Long, Void, List<Review>> {

    private final Listener mListener;

    public ReviewsNetworkUtils(Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected List<Review> doInBackground(Long... longs) {
        List<Review> reviews = new ArrayList<>();
        long movieId = longs[0];
        try {
            URL url = new URL(Constant.END_POINT + movieId + "/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream=connection.getInputStream();
            String result =readStream(inputStream);
            parseJSON(result,reviews);
            inputStream.close();
            return reviews;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviewList) {
        super.onPostExecute(reviewList);
        if (reviewList !=null){
            mListener.onReviewsFinished(reviewList);
        }else {
            mListener.onReviewsFinished(new ArrayList<Review>());
        }
    }

   public interface Listener {
        void onReviewsFinished(List<Review> reviews);
    }

    public static void parseJSON(String data, List<Review> reviewList) {
        Review review = new Review();

        try {
            JSONObject object = new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                review.setmId(jsonObject.getString("id"));
                review.setmAuthor(jsonObject.getString("author"));
                review.setmUrl(jsonObject.getString("url"));
                review.setmContent(jsonObject.getString("content"));
                reviewList.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private String readStream(InputStream in) {
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

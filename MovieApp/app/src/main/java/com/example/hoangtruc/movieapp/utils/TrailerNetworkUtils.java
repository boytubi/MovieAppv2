package com.example.hoangtruc.movieapp.utils;

import android.os.AsyncTask;

import com.example.hoangtruc.movieapp.BuildConfig;
import com.example.hoangtruc.movieapp.common.Constant;
import com.example.hoangtruc.movieapp.model.Trailer;
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

public class TrailerNetworkUtils extends AsyncTask<Long,Void,List<Trailer>> {
    private Listener mListener;

    public TrailerNetworkUtils(Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected List<Trailer> doInBackground(Long... longs) {
        List <Trailer> trailers=new ArrayList<>();
        long movieId=longs[0];
        try {
            URL url;
            url=new URL(Constant.END_POINT+movieId+"/videos?api_key="+ BuildConfig.THE_MOVIE_DB_API_TOKEN);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream=connection.getInputStream();
            String result= readStream(inputStream);
            parseJSON(result,trailers);
            inputStream.close();
            return trailers;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        if (trailers!=null){
            mListener.onLoadFinished(trailers);
        }else {
            mListener.onLoadFinished(new ArrayList<Trailer>());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public interface  Listener{
        void onLoadFinished(List<Trailer> trailers);
    }
    public static void parseJSON(String data,List<Trailer> trailerList){
        Trailer trailer=new Trailer();
        try {
            JSONObject object=new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                trailer.setmId(jsonObject.getString("id"));
                trailer.setmKey(jsonObject.getString("key"));
                trailer.setmName(jsonObject.getString("name"));
                trailer.setmSite(jsonObject.getString("site"));
                trailer.setmSize(jsonObject.getString("size"));
                trailerList.add(trailer);
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
            String line ="";
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

package com.codepath.apps.twitterclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.TweetListViewAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    private TweetListViewAdapter tweetAdapter;
    private ListView timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweetAdapter = new TweetListViewAdapter(this,tweets);

        timeline = (ListView)findViewById(R.id.lvTimeline);
        setOnScrollListener();
        timeline.setAdapter(tweetAdapter);

        popuateMoreDataFromServer(0);//initial size as 0 to indicate first time loading

    }

    private void setOnScrollListener() {
        timeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
               return popuateMoreDataFromServer(totalItemsCount); //use total count as offset
            }
        });
    }

    private boolean popuateMoreDataFromServer(final int offset) {
        if(!isNetworkAvailable()){
            toastErrorMessage("No Internet connection");
            return false;
        }

        //if non-zero offset, get max id of tweet encountered so far to request more only below that to avoid duplicaiton
        // in endless scrolling feature
        long maxId=0;
        if(offset>0){
            //subtract 1 to avoid repeat of last processed id. Ref : https://dev.twitter.com/rest/public/timelines
            maxId= tweets.get(offset-1).getId()-1;
        }
        client = TwitterApplication.getRestClient(); //singleton client
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.d("DEBUG", response.toString());
                    if (0 == offset) {
                        tweets.clear();//first time loading on this activity. start from scratch
                    }
                    tweets.addAll(Tweet.fromJson(response)); // append new items
                    tweetAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    toastErrorMessage("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                toastErrorMessage("Error connecting to Twitter. Please try again");
                Log.d("DEBUG", response.toString());

            }
        });
        return true; // need to check what to do if API call failed half way
    }

    // check network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void toastErrorMessage(String errorMsg){
        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
    }


}

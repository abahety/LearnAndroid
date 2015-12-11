package com.codepath.apps.twitterclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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
        timeline.setAdapter(tweetAdapter);

        populateTimeline();

    }

    private void populateTimeline() {
        client = TwitterApplication.getRestClient(); //singleton client
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.d("DEBUG", response.toString());
                    tweets.clear();// clear existing items if needed
                    tweets.addAll(Tweet.fromJson(response)); // add new items
                    tweetAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    toastErrorMessage("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("DEBUG", response.toString());
                toastErrorMessage("Error connecting to Twitter. Please try again");
            }
        });
    }

    private void toastErrorMessage(String errorMsg){
        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
    }


}

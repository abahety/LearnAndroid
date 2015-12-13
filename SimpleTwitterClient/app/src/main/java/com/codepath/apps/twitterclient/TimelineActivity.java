package com.codepath.apps.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    private TweetListViewAdapter tweetAdapter;
    private ListView timeline;
    // swipe container
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //set icon for action bar
        setActionBarIcon();

        // get listview
        timeline = (ListView)findViewById(R.id.lvTimeline);
        //attach list view adapter to listiew
        tweetAdapter = new TweetListViewAdapter(this,tweets);
        //set endless scroll listener
        setOnScrollListener();
        //configure swipe refersh container
        configureSwipeContainer();

        timeline.setAdapter(tweetAdapter);

        // this will fetch tweets for the first time also
        updateTimelineSinceToLatestTweets();

    }

    //configure pull to refresh
    private void configureSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTimelineSinceToLatestTweets();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.twitterBlue,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    private void setActionBarIcon() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                //launch compose activity
                Intent compose = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(compose, TwitterApplication.REQ_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setOnScrollListener() {
        timeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
               return getTweetsOlderThanOffset(totalItemsCount); //use total count as offset
            }
        });
    }

    // get tweets older than the latest seen so far determined by offset
    private boolean getTweetsOlderThanOffset(final int offset) {
        if(!isNetworkAvailable()){
            toastErrorMessage(getString(R.string.no_network));
            return false;
        }

        //if non-zero offset, get max id of tweet encountered so far to request more only below that to avoid duplicaiton
        // in endless scrolling feature
        long maxId=0;
        if(offset>0){
            //subtract 1 to avoid repeat of last processed id. Ref : https://dev.twitter.com/rest/public/timelines
            maxId= tweets.get(offset-1).getId()-1;
        }
        client = getRestClient();
        client.getTweetsOlderThanMaxId(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    //Log.d("DEBUG", response.toString());
                    if (0 == offset) {
                        tweets.clear();//first time loading on this activity. start from scratch
                    }
                    tweets.addAll(Tweet.fromJson(response)); // append new items
                    tweetAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    toastErrorMessage(getString(R.string.errorOccured));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                toastErrorMessage(getString(R.string.errorfromTwitter));
                Log.d("DEBUG", response.toString());

            }
        });
        return true; // need to check what to do if API call failed half way
    }

    private TwitterClient getRestClient() {
        return TwitterApplication.getRestClient(); //singleton client
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==TwitterApplication.RESP_OK&& requestCode==TwitterApplication.REQ_CODE){
            //tweet was posted successfully from compose activity. update the list view data
            updateTimelineSinceToLatestTweets();
        }
    }

    //get latest tweets from last refreshed set of tweets
    private void updateTimelineSinceToLatestTweets() {
        //check if network is available
        if(!isNetworkAvailable()){
            toastErrorMessage(getString(R.string.no_network));
            return;
        }

        //get id of tweet since most recent seen so far
        long since_id=0;
        //if tweets data is empty, just call populateMoreData.
        if(!tweets.isEmpty()){ // if activity not loaded first time
            //get topmost tweet data id. we need data newer than that to refresh home timeline
            since_id = tweets.get(0).getId();
        }
        //make API call
        client = getRestClient();
        client.getFreshTweetsSinceLatestId(since_id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.d("DEBUG", response.toString());
                    //form new array list with latest data
                    List<Tweet> latestTweets = new ArrayList<Tweet>();
                    latestTweets.addAll(Tweet.fromJson(response));
                    //append already obtained data
                    latestTweets.addAll(tweets);
                    //clear current data
                    tweets.clear();
                    //add entire data into old list from new one.
                    tweets.addAll(latestTweets);
                    //notify adapter
                    tweetAdapter.notifyDataSetChanged();
                    //nullify new list.
                    latestTweets.clear();
                    //go to the top of listview
                    timeline.setSelectionAfterHeaderView();

                } catch (Exception e) {
                    toastErrorMessage(getString(R.string.errorOccured));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                toastErrorMessage(getString(R.string.errorfromTwitter));
                Log.d("DEBUG", response.toString());

            }
        });
    }


//    private void resizeArrayToLimitMemoryUsage(boolean truncateFromFront){
//        if(tweets.size() < TwitterApplication.MAX_TWEET_HELD_IN_MEMORY){
//            return;// nothing needed
//        }
//
//        if(truncateFromFront){
//
//        }
//    }
}

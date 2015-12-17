package com.abahety.app.easytwitter.timeline;

import android.os.Bundle;
import android.util.Log;

import com.abahety.app.easytwitter.datamodel.Tweet;
import com.abahety.app.easytwitter.fragment.TweetFragment;
import com.abahety.app.easytwitter.network.TwitterClient;
import com.codepath.apps.twitterclient.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by abahety on 12/17/15.
 */
public class MentionsTimelineFragment extends TweetFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = getRestClient();
        populateTimeline(0);
    }

    public void populateTimeline(long maxId){
        if(!isNetworkAvailable()){
            toastErrorMessage(getString(R.string.no_network));
            return;
        }

        client = getRestClient();
        client.getMentionsTimeLineMethod(maxId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    addTweets(Tweet.fromJson(response)); // append new items
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
}

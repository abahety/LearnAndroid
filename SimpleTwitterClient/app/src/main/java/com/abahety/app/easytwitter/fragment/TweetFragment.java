package com.abahety.app.easytwitter.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.datamodel.Tweet;
import com.abahety.app.easytwitter.network.TwitterClient;
import com.codepath.apps.twitterclient.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abahety on 12/16/15.
 * Fragment to show listview of timeline/tweet/mentions etc data.
 */
public abstract class TweetFragment extends Fragment {
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    private TweetListViewAdapter tweetAdapter;
    private ListView timeline;
    private SwipeRefreshLayout swipeContainer;
    protected int DEFAULT_MAX_ID=0;

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetAdapter = new TweetListViewAdapter(getActivity(), tweets);
        refresh();
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_tweet, parent, false);
        // get listview
        timeline = (ListView)v.findViewById(R.id.lvTimeline);
        //get swipe container
        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set adapter
        timeline.setAdapter(tweetAdapter);
        //set swipe container for pull to refresh
        configureSwipeContainer();
        //configure infinite pagination/endless scroller on listview
        setOnScrollListenerForTimeline();

    }

    //configure pull to refresh
    private void configureSwipeContainer() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refresh entire timeline
                loadMoreData(DEFAULT_MAX_ID);
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.twitterBlue,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    //set infinte pagination scroller
    private void setOnScrollListenerForTimeline() {
        timeline.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                //if non-zero offset, get max id of tweet encountered so far to request more only below that to avoid duplicaiton
                // in endless scrolling feature
                long maxId=0;
                if(totalItemsCount>0){
                    //subtract 1 to avoid repeat of last processed id. Ref : https://dev.twitter.com/rest/public/timelines
                    maxId= tweets.get(totalItemsCount-1).getId()-1;
                }
                return loadMoreData(maxId);// need to check what to do if API call failed half way
//                return loadMoreData(totalItemsCount); //use total count as offset
            }
        });
    }

    private void clearData(){
        tweets.clear();
    }

    private void addDataToListView(List<Tweet> tweets){
        tweetAdapter.addAll(tweets);
    }

    protected TwitterClient getRestClient() {
        return TwitterApplication.getRestClient(); //singleton client
    }

    // check network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void toastErrorMessage(String errorMsg){
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    private JsonHttpResponseHandler getJsonHttpRespHandlerNewInstance(final long maxId){
        return new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if (0 == maxId) {
                        clearData();//first time loading on this activity. start from scratch
                    }
                    addDataToListView(Tweet.fromJson(response)); // append new items
                } catch (Exception e) {
                    toastErrorMessage(getString(R.string.errorOccured));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                toastErrorMessage(getString(R.string.errorfromTwitter));
                Log.d("DEBUG", response.toString());
            }
        };
    }

    private boolean loadMoreData(long maxId){
        if(!isNetworkAvailable()){
            toastErrorMessage(getString(R.string.no_network));
            return false;
        }
        populateTimeline(maxId, getJsonHttpRespHandlerNewInstance(maxId));
        return true;
    }

    protected abstract void populateTimeline(long maxId, JsonHttpResponseHandler responseHandler);

    /**
     * Refresh the data in list view. Wipes out all old data and gets fresh one
     */
    public void refresh() {
        loadMoreData(DEFAULT_MAX_ID);
    }
}

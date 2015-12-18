package com.abahety.app.easytwitter.activities.timeline;


import com.abahety.app.easytwitter.fragment.TweetFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment  extends TweetFragment {
    public void populateTimeline(final long maxId, JsonHttpResponseHandler jsonRespHander){
        getRestClient().getHomeTimeline(maxId,jsonRespHander);
    }
}

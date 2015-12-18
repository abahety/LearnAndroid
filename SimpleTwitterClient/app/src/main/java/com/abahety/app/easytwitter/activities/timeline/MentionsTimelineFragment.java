package com.abahety.app.easytwitter.activities.timeline;

import com.abahety.app.easytwitter.fragment.TweetFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TweetFragment {

    @Override
    protected void populateTimeline(long maxId, JsonHttpResponseHandler responseHandler) {
        getRestClient().getMentionsTimeLine(maxId,responseHandler);
    }
}

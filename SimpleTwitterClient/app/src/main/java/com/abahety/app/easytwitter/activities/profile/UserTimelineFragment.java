package com.abahety.app.easytwitter.activities.profile;

import android.os.Bundle;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.fragment.TweetFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetFragment {

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment profileFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString(TwitterApplication.SCREEN_NAME, screenName);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    protected void populateTimeline(long maxId, JsonHttpResponseHandler responseHandler) {
        String screenName = getArguments().getString(TwitterApplication.SCREEN_NAME);
        getRestClient().getUserTimeline(screenName,maxId,responseHandler);
    }
}

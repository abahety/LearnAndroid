package com.abahety.app.easytwitter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.datamodel.TwitterUser;
import com.abahety.app.easytwitter.network.TwitterClient;
import com.codepath.apps.twitterclient.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;


public class UserHeaderFragment extends Fragment{
    private ImageView ivProfile;
    private TextView tvUserName, tvScreenName, tvTagLine;
    private TextView tvTweetCount, tvFollowersCount, tvFollowingCount;
    private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_user_header, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get Imageview
        ivProfile = (ImageView)view.findViewById(R.id.ivProfile);
        //get textviews
        tvUserName=(TextView)view.findViewById(R.id.tvUserName);
        tvScreenName = (TextView)view.findViewById(R.id.tvScreenName);
        tvTagLine = (TextView)view.findViewById(R.id.tvTagline);
        tvTweetCount = (TextView)view.findViewById(R.id.tvTweetCount);
        tvFollowersCount = (TextView)view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = (TextView)view.findViewById(R.id.tvFollowingCount);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateUserDetails();
    }

    private void populateUserDetails() {
        String screenName=getArguments().getString(TwitterApplication.SCREEN_NAME);
        //if screen name is null, get current logged in user details
        if(screenName==null) {
            client.getMe(getJsonHttpResponseHandler());
        }else{
            //else get details for the user who is passed in this fragment to be displayed
            client.getUSerDetails(screenName,getJsonHttpResponseHandler());
        }
    }

    @NonNull
    private JsonHttpResponseHandler getJsonHttpResponseHandler() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TwitterUser user = TwitterUser.fromJson(response);
                if (user != null) {
                    Picasso.with(getActivity()).load(user.getProfileImageUrl()).fit().into(ivProfile);
                    tvUserName.setText(user.getUsername());
                    tvScreenName.setText(user.getScreenName());
                    tvTagLine.setText(user.getTagLine());
                    tvTweetCount.setText(String.valueOf(user.getTweetCount()));
                    tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
                    tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), getString(R.string.errorfromTwitter), Toast.LENGTH_LONG).show();
                if (errorResponse != null) {
                    Log.d("DEBUG", "Error getting logged in user details. StatusCode=" + statusCode + ",error=" + errorResponse.toString());
                }
            }
        };
    }

    public static UserHeaderFragment newInstance(String screenName){
        UserHeaderFragment fm = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString(TwitterApplication.SCREEN_NAME, screenName);
        fm.setArguments(args);
        return fm;
    }
}

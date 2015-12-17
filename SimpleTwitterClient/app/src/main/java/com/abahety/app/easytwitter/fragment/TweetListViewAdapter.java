package com.abahety.app.easytwitter.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.abahety.app.easytwitter.datamodel.Tweet;
import com.abahety.app.easytwitter.datamodel.TwitterUser;
import com.codepath.apps.twitterclient.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abahety on 12/10/15.
 */
public class TweetListViewAdapter extends ArrayAdapter<Tweet> {
    private static class ViewHolder{
        TextView tvUsername;
        ImageView ivProfile, ivMedia;
        TextView tvScreenName;
        TextView tvTimeStamp;
        TextView tvBody;

    }

    public TweetListViewAdapter(Context context, ArrayList<Tweet> tweetLists) {
        super(context, 0, tweetLists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Tweet tweet = getItem(position);
        final ViewHolder viewHolder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet, parent, false);
            viewHolder.tvUsername =(TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvScreenName=(TextView)convertView.findViewById(R.id.tvScreenName);
            viewHolder.ivProfile = (ImageView)convertView.findViewById(R.id.ivProfile);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimeStamp = (TextView)convertView.findViewById(R.id.tvTimestamp);
            viewHolder.ivMedia=(ImageView)convertView.findViewById(R.id.ivMedia);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Populate the data into the template view using the data object
        TwitterUser user = tweet.getUser();
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(viewHolder.ivProfile);
        viewHolder.tvUsername.setText(user.getUsername());
        viewHolder.tvScreenName.setText(user.getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimeStamp.setText(Tweet.getRelativeTimeAgo(tweet.getCreateAt()));
        viewHolder.ivMedia.setImageResource(0);//clear the image
        if(Tweet.MediaType.Photo==tweet.getMediaType()){
            Picasso.with(getContext()).load(tweet.getMediaUrl()).into(viewHolder.ivMedia);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

package com.codepath.apps.twitterclient.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by abahety on 12/10/15.
 */

/*
{
    "coordinates": null,
    "truncated": false,
    "created_at": "Tue Aug 28 21:16:23 +0000 2012",
    "favorited": false,
    "id_str": "240558470661799936",
    "in_reply_to_user_id_str": null,
    "entities": {
      "urls": [

      ],
      "hashtags": [

      ],
      "user_mentions": [

      ]
    },
    "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "created_at": "Wed Mar 03 19:37:35 +0000 2010",
      "location": "San Francisco, CA",
      "follow_request_sent": false,
      "id_str": "119476949",
      "is_translator": false,
      "profile_link_color": "0084B4",
      "entities": {
        "url": {
          "urls": [
            {
              "expanded_url": null,
              "url": "http://bit.ly/oauth-dancer",
              "indices": [
                0,
                26
              ],
              "display_url": null
            }
          ]
        },
        "description": null
      },
      "default_profile": false,
      "url": "http://bit.ly/oauth-dancer",
      "contributors_enabled": false,
      "favourites_count": 7,
      "utc_offset": null,
      "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
      "id": 119476949,
      "listed_count": 1,
      "profile_use_background_image": true,
      "profile_text_color": "333333",
      "followers_count": 28,
      "lang": "en",
      "protected": false,
      "geo_enabled": true,
      "notifications": false,
      "description": "",
      "profile_background_color": "C0DEED",
      "verified": false,
      "time_zone": null,
      "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "statuses_count": 166,
      "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
      "default_profile_image": false,
      "friends_count": 14,
      "following": false,
      "show_all_inline_media": false,
      "screen_name": "oauth_dancer"
    },
    "in_reply_to_screen_name": null,
    "in_reply_to_status_id": null
  },
 */
public class Tweet {

    private String body;
    private long id; //unique id of tweet
    private String createAt;
    private TwitterUser user;

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public TwitterUser getUser() {
        return user;
    }

    public static Tweet fromJson(JSONObject json){
        Tweet tweet = new Tweet();
        try {
            //get text
            tweet.body = json.optString("text");
            tweet.createAt = json.optString("created_at");
            tweet.id = json.optLong("id");
            tweet.user=TwitterUser.fromJson(json.getJSONObject("user"));
        }catch(Exception e){
            //log error
            return null;
        }
        return tweet;
    }

    //decode Tweets from array of tweets in JSON format
    public static List<Tweet> fromJson(JSONArray jsonArray){
        List<Tweet> listOfTweets = new ArrayList<Tweet>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace(); //do proper logging
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                listOfTweets.add(tweet);
            }
        }
        return listOfTweets;
    }

    public static String getRelativeTimeAgo(String rawDate){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return relativeDate;
        }
        String abbreviatedTime="";
        if("Yesterday".equalsIgnoreCase(relativeDate)){
            abbreviatedTime="1d"; //found this in test mode. Somehow 1d does not come out of this library
        }else {
            abbreviatedTime = relativeDate.substring(0, relativeDate.indexOf(' ') + 2).replace(" ", "");
        }
        //Log.d("DEBUG", rawDate + "==>" +relativeDate+"==>"+ abbreviatedTime);
        return abbreviatedTime;
    }
}

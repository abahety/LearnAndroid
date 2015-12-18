package com.abahety.app.easytwitter.network;

import android.content.Context;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "4qxK0OMSbqmR1yNhk3UQ";       // Change this
	public static final String REST_CONSUMER_SECRET = "bygwyPtMIciI7Et3rTgpIbs6rMHMVZlueFgYtGMw"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://simpletweet"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", TwitterApplication.TWEET_FETCH_COUNT);
		getClient().get(apiUrl, params, handler);
		if(max_id!=0) {
			params.put("max_id", max_id);
		}
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	public void getMe(AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("account/verify_credentials.json");
		getClient().get(apiURL, handler);
	}

	public void getUserTimeline(String screen_name, long max_id, AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		if(max_id!=0) {
			params.put("max_id", max_id);
		}
		params.put(TwitterApplication.SCREEN_NAME,screen_name);
		getClient().get(apiURL, params, handler);
	}

	public void getMentionsTimeLine(long max_id, AsyncHttpResponseHandler handler) {
		String apiURL = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		if(max_id!=0) {
			params.put("max_id", max_id);
		}
		getClient().get(apiURL, params, handler);
	}

	public void getUSerDetails(String screenName, AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put(TwitterApplication.SCREEN_NAME, screenName);
		getClient().get(apiURL, params, handler);
	}
}
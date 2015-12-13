package com.codepath.apps.twitterclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.TwitterUser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends AppCompatActivity {
    private TwitterClient client;
    private ImageView ivProfile;
    private EditText etTweet;
    private TextView tvName, tvScreenName,tvCharCount;
    private Button bPostTweet;
    private int maxCharCount=140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        //set icon for action bar
        setActionBarIcon();
        //get view pieces to put data into
        ivProfile = (ImageView)findViewById(R.id.ivProfile);
        etTweet = (EditText)findViewById(R.id.etPost);
        setOnTextChangeListenerForTweetBody(etTweet);
        tvName = (TextView)findViewById(R.id.tvUserName);
        tvScreenName = (TextView)findViewById(R.id.tvScreenName);
        bPostTweet = (Button)findViewById(R.id.bPostTweet);
        tvCharCount= (TextView)findViewById(R.id.tvCharCount);
        initializeTwitterRestClient();
        //get logged in user details
        populateUserDetail();

    }

    private void setActionBarIcon() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    // to keep changing character count
    private void setOnTextChangeListenerForTweetBody(final EditText etTweet) {

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int charCount = maxCharCount - s.length();
                    tvCharCount.setText(Integer.toString(charCount));
                    bPostTweet.setEnabled(true);
                } else {
                    tvCharCount.setText(String.valueOf(maxCharCount));
                    bPostTweet.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //mark these items visible after user data is loaded
    private void makeItemsOnViewVisible(){
        etTweet.setVisibility(View.VISIBLE);
        tvCharCount.setVisibility(View.VISIBLE);
        tvCharCount.setText(String.valueOf(maxCharCount));
        bPostTweet.setVisibility(View.VISIBLE);
        focusTweetBody();
    }

    private void populateUserDetail() {
        client.getMe(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                TwitterUser user = TwitterUser.fromJson(response);
                if (user != null) {
                    Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).fit().into(ivProfile);
                    tvName.setText(user.getUsername());
                    tvScreenName.setText(user.getScreenName());
                    makeItemsOnViewVisible();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                makeItemsOnViewVisible();
                Toast.makeText(getApplicationContext(), getString(R.string.errorfromTwitter), Toast.LENGTH_LONG).show();
                if (errorResponse != null) {
                    Log.d("DEBUG", "Error getting logged in user details. StatusCode=" + statusCode + ",error=" + errorResponse.toString());
                }
            }
        });
    }

    private void initializeTwitterRestClient() {
        client = TwitterApplication.getRestClient();
    }

    public void postTweet(final View view) {
        //get text from editText
        String tweetBody = etTweet.getText().toString();
        client.postTweet(tweetBody, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                hideKeyboard(etTweet);
                toastMessage(getString(R.string.tweet_success));
                goBackToTimeLine(TwitterApplication.RESP_OK);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,JSONObject response) {
                toastMessage(getString(R.string.tweet_failure));
                focusTweetBody();

            }

            private void goBackToTimeLine(int code){
                setResult(code);
                finish();
            }

            private void toastMessage(String message){
                Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                toast.show();
            }
        });

    }

    private void showKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etTweet, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void focusTweetBody() {
        if(etTweet.requestFocus()){
            showKeyboard(etTweet);
        }
    }
}

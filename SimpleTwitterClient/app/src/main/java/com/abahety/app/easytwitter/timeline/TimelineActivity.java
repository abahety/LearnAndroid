package com.abahety.app.easytwitter.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.compose.ComposeTweetActivity;
import com.abahety.app.easytwitter.network.TwitterClient;
import com.codepath.apps.twitterclient.R;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //set icon for action bar
        setActionBarIcon();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetFragmentPagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));
        viewPager.setCurrentItem(0);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setActionBarIcon() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                //launch compose activity
                Intent compose = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(compose, TwitterApplication.REQ_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode==TwitterApplication.RESP_OK&& requestCode==TwitterApplication.REQ_CODE){
//            //tweet was posted successfully from compose activity. update the list view data
//            updateTimelineSinceToLatestTweets();
//        }
//    }
}

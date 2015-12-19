package com.abahety.app.easytwitter.activities.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.abahety.app.easytwitter.activities.profile.ProfileActivity;
import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.compose.ComposeTweetActivity;
import com.abahety.app.easytwitter.fragment.SmartFragmentStatePagerAdapter;
import com.abahety.app.easytwitter.fragment.TweetFragment;
import com.abahety.app.easytwitter.network.TwitterClient;
import com.codepath.apps.twitterclient.R;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //set icon for action bar
        setActionBarIcon();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TweetFragmentPagerAdapter(getSupportFragmentManager(),
                TimelineActivity.this));

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
            case R.id.miProfile:
                //launch profile activity
                Intent profile = new Intent(TimelineActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==TwitterApplication.RESP_OK&& requestCode==TwitterApplication.REQ_CODE){
            viewPager.setCurrentItem(0);
           SmartFragmentStatePagerAdapter smartFragmentAdapter = (SmartFragmentStatePagerAdapter)viewPager.getAdapter();
           TweetFragment fm = (TweetFragment)smartFragmentAdapter.getRegisteredFragment(0);
            fm.refresh();

        }
    }
}

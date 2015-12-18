package com.abahety.app.easytwitter.activities.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.abahety.app.easytwitter.common.TwitterApplication;
import com.abahety.app.easytwitter.fragment.UserHeaderFragment;
import com.codepath.apps.twitterclient.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the screen name
        String screenName = getIntent().getStringExtra(TwitterApplication.SCREEN_NAME);

        if(savedInstanceState==null) {
            //create usertimeline fragment
            UserHeaderFragment userHeaderFragment = UserHeaderFragment.newInstance(screenName);
            //create usertimeline fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            //Display usertimeline fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fmUserHeaderContainer,userHeaderFragment);
            ft.replace(R.id.ftContainer, userTimelineFragment);
            ft.commit();//changes the fragment

//            ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.ftContainer, userTimelineFragment);
//            ft.commit();//changes the fragment
        }
    }

}

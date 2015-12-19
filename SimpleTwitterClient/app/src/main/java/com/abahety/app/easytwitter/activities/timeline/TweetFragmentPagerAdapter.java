package com.abahety.app.easytwitter.activities.timeline;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.abahety.app.easytwitter.fragment.SmartFragmentStatePagerAdapter;

public class TweetFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
    final int TAB_COUNT = 2;
    private String tabTitles[] = new String[] { "HOME", "@ MENTIONS"};
    private Context context;

    public TweetFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new HomeTimelineFragment();
            case 1:
                return new MentionsTimelineFragment();
            default:
                return new HomeTimelineFragment();
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

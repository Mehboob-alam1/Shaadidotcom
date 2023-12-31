package com.mehboob.myshadi.adapters.homeAdapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mehboob.myshadi.databinding.FragmentChangePassBinding;
import com.mehboob.myshadi.views.dashboard.frags.matches.DailyFragment;
import com.mehboob.myshadi.views.dashboard.frags.matches.MyMatchesFragment;
import com.mehboob.myshadi.views.dashboard.frags.matches.NearMeFragment;
import com.mehboob.myshadi.views.dashboard.frags.matches.NewFragment;
import com.mehboob.myshadi.views.dashboard.frags.matches.SearchFragment;
import com.mehboob.myshadi.views.dashboard.frags.matches.ShortListedFragment;

public class MatchesTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    public MatchesTabAdapter(Context context, FragmentManager fm,int totalTabs) {
        super(fm);

        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyMatchesFragment myMatchesFragment = new MyMatchesFragment();
                return myMatchesFragment;
            case 1:
                NearMeFragment nearMeFragment = new NearMeFragment();
                return nearMeFragment;
            case 2:
                ShortListedFragment shortListedFragment = new ShortListedFragment();
                return shortListedFragment;



            case 3:
                NewFragment newFragment = new NewFragment();
                return newFragment;
            case 4:
                DailyFragment dailyFragment = new DailyFragment();
                return dailyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

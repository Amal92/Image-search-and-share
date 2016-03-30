package com.amal.imageshare.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.amal.imageshare.Fragments.ViewPagerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by amal on 28/03/16.
 */
public class viewPagerAdapter extends FragmentStatePagerAdapter {

    //thug life, slap, funny expressions
    String ITEMS[] ={"facepalm","bitch please","laughing","batman slap"};

    List<String> items = new ArrayList<String>(Arrays.asList(ITEMS));

    public viewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ViewPagerFragment.ARG_OBJECT, items.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "# " + (items.get(position));
    }

    public void add(String query){
        items.add(query);
    }

}

package com.amal.imageshare;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.amal.imageshare.Adapters.viewPagerAdapter;
import com.amal.imageshare.Utils.PreferenceHelper;
import com.astuetz.PagerSlidingTabStrip;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.PointTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.splunk.mint.Mint;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private SearchBox search;
    private ViewPager viewPager;
    private viewPagerAdapter viewAdapter;
    private PagerSlidingTabStrip tabs;

    private AdView mAdView;
    private ShowcaseView showcaseView;

    public static void cleanCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mint.initAndStartSession(MainActivity.this, "3c750e3e");


        mAdView = (AdView) findViewById(R.id.adView);

        viewPager = (ViewPager) findViewById(R.id.my_viewpager);
        viewAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTextColor(getResources().getColor(android.R.color.white));
        tabs.setIndicatorColor(getResources().getColor(android.R.color.white));
        tabs.setViewPager(viewPager);

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        search.setHint(getString(R.string.searchormic));
        search.setLogoText(getString(R.string.searchormic));
        search.setLogoTextColor(getResources().getColor(R.color.gray));
        search.setDrawerLogo(R.drawable.ic_action_logo_launcher);
        search.setDrawerLogo(R.mipmap.ic_launcher);

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
                if (showcaseView != null && showcaseView.isShowing()) {
                    showcaseView.hide();
                }
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                //   encodeSearchString(searchTerm);
                viewAdapter.add(searchTerm);
                viewAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(viewAdapter.getCount() - 1);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });


     /*   if (new PreferenceHelper(this).getFirstuse()) {
            showIntro();
        } else {
            load_ad();
        }*/

        load_ad();

        //   encodeSearchString("facepalm");
    }

    private void showIntro() {
        final int width = getWindowManager().getDefaultDisplay().getWidth();
        final int height = getWindowManager().getDefaultDisplay().getHeight();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final View mView;
                mView = findViewById(R.id.searchbox);
                int[] location = new int[2];
                mView.getLocationInWindow(location);
                int x = location[0] + mView.getWidth() / 3;
                int y = location[1] + mView.getHeight() / 2;
                String content = getResources().getString(R.string.showcase_content_3);
                showcaseView = new ShowcaseView.Builder(MainActivity.this)
                        .withMaterialShowcase()
                        .setTarget(new PointTarget(width + 100, height + 100))
                        .setContentTitle(getResources().getString(R.string.showcase_title))
                        .setContentText(content)
                        .setStyle(R.style.CustomShowcaseTheme2)
                        .replaceEndButton(R.layout.view_custom_next_button)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new PreferenceHelper(MainActivity.this).putFirstuse(false);
                            }
                        })
                        .build();
                //  showcaseView.hideButton();
            }
        }, 1000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
            // encodeSearchString(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            // cleanCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void load_ad() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}

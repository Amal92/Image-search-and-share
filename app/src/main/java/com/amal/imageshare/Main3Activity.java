package com.amal.imageshare;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.amal.imageshare.Adapters.StaggeredGridAdapter;
import com.amal.imageshare.Interfaces.AsyncTaskCompleteListener;
import com.amal.imageshare.Models.SearchEngineResults;
import com.amal.imageshare.Networking.HttpRequester;
import com.amal.imageshare.Utils.Const;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.splunk.mint.Mint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class Main3Activity extends AppCompatActivity implements AsyncTaskCompleteListener {

    StaggeredGridAdapter staggeredGridAdapter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ArrayList<SearchEngineResults> searchEngineResultsArrayList;
    private SearchBox search;
    private SmoothProgressBar progressBar;
    private String next;
    private boolean loading = true;

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
        Mint.initAndStartSession(Main3Activity.this, "3c750e3e");
        progressBar = (SmoothProgressBar) findViewById(R.id.progress_bar);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        search.setHint(getString(R.string.searchormic));
        search.setDrawerLogo(R.drawable.ic_action_logo_launcher);
        search.setDrawerLogo(R.mipmap.ic_launcher);

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
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
                encodeSearchString(searchTerm);

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


        searchEngineResultsArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        staggeredGridAdapter = new StaggeredGridAdapter(this, searchEngineResultsArrayList);
        recyclerView.setAdapter(staggeredGridAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    int totalItemCount = staggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItemPositions = new int[2];
                    int pastVisiblesItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItemPositions)[0];
                    if (!next.isEmpty() && loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loadMoreData(next);
                            loading = false;
                        }
                    }
                }
            }
        });

    }

    private void encodeSearchString(String searchTerm) {
        if (!searchTerm.isEmpty()) {
            try {
                String query = URLEncoder.encode(searchTerm, "utf-8");
                fetchJsonData(query);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
            encodeSearchString(matches.get(0));
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

    private void fetchJsonData(String searchTerm) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.URL, Const.Endpoints.getFullSearchQuery(searchTerm));

        new HttpRequester(this, Const.GET, map, Const.ServiceCode.GET_QUERY, this);
    }

    private void loadMoreData(String queryUrl) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.URL, Const.Endpoints.getNextSearchUrl(queryUrl));

        new HttpRequester(this, Const.GET, map, Const.ServiceCode.NEXT_QUERY, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        progressBar.setVisibility(View.GONE);
        switch (serviceCode) {
            case Const.ServiceCode.GET_QUERY:
                searchEngineResultsArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    next = jsonObject.optString(Const.Params.NEXT);
                    JSONArray resultsArray = jsonObject.getJSONArray(Const.Params.RESULTS);
                    for (int i = 0; i < resultsArray.length(); i++) {
                        SearchEngineResults searchEngineResult = new SearchEngineResults();
                        JSONObject resultObj = resultsArray.getJSONObject(i);
                        searchEngineResult.setHeight(resultObj.optString(Const.Params.HEIGHT));
                        searchEngineResult.setWidth(resultObj.optString(Const.Params.WIDTH));
                        searchEngineResult.setImage(resultObj.optString(Const.Params.IMAGE));
                        searchEngineResult.setSource(resultObj.optString(Const.Params.SOURCE));
                        searchEngineResult.setThumbnail(resultObj.optString(Const.Params.THUMBNAIL));
                        searchEngineResult.setTitle(resultObj.optString(Const.Params.TITLE));
                        searchEngineResult.setUrl(resultObj.optString(Const.Params.URL));
                        searchEngineResultsArrayList.add(searchEngineResult);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                staggeredGridAdapter.notifyDataSetChanged();
                break;
            case Const.ServiceCode.NEXT_QUERY:
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    next = jsonObject.optString(Const.Params.NEXT);
                    loading = true;
                    JSONArray resultsArray = jsonObject.getJSONArray(Const.Params.RESULTS);
                    for (int i = 0; i < resultsArray.length(); i++) {
                        SearchEngineResults searchEngineResult = new SearchEngineResults();
                        JSONObject resultObj = resultsArray.getJSONObject(i);
                        searchEngineResult.setHeight(resultObj.optString(Const.Params.HEIGHT));
                        searchEngineResult.setWidth(resultObj.optString(Const.Params.WIDTH));
                        searchEngineResult.setImage(resultObj.optString(Const.Params.IMAGE));
                        searchEngineResult.setSource(resultObj.optString(Const.Params.SOURCE));
                        searchEngineResult.setThumbnail(resultObj.optString(Const.Params.THUMBNAIL));
                        searchEngineResult.setTitle(resultObj.optString(Const.Params.TITLE));
                        searchEngineResult.setUrl(resultObj.optString(Const.Params.URL));
                        searchEngineResultsArrayList.add(searchEngineResult);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                staggeredGridAdapter.notifyDataSetChanged();
                break;
        }
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


}

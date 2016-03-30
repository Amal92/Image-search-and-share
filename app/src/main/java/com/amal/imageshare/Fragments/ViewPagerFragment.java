package com.amal.imageshare.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.amal.imageshare.Adapters.StaggeredGridAdapter;
import com.amal.imageshare.Interfaces.AsyncTaskCompleteListener;
import com.amal.imageshare.Models.SearchEngineResults;
import com.amal.imageshare.Networking.HttpRequester;
import com.amal.imageshare.R;
import com.amal.imageshare.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment implements AsyncTaskCompleteListener {

    public static final String ARG_OBJECT = "string-to-search";
    private String next;
    private boolean loading = true;

    private ProgressBar progressBar;
    StaggeredGridAdapter staggeredGridAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<SearchEngineResults> searchEngineResultsArrayList;

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_view_pager, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        searchEngineResultsArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        //  staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        staggeredGridAdapter = new StaggeredGridAdapter(getActivity(), searchEngineResultsArrayList);
        recyclerView.setAdapter(staggeredGridAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int[] firstVisibleItemPositions = new int[2];
                    int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                    if (!next.isEmpty() && loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loadMoreData(next);
                            loading = false;
                        }
                    }
                }
            }
        });

        Bundle args = getArguments();
        encodeSearchString(args.getString(ARG_OBJECT));
        return rootView;
    }

    private void fetchJsonData(String searchTerm) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.URL, Const.Endpoints.getFullSearchQuery(searchTerm));

        new HttpRequester(getActivity(), Const.GET, map, Const.ServiceCode.GET_QUERY, this);
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

    private void loadMoreData(String queryUrl) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.URL, Const.Endpoints.getNextSearchUrl(queryUrl));

        new HttpRequester(getActivity(), Const.GET, map, Const.ServiceCode.NEXT_QUERY, this);
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


}

package com.amal.imageshare.Utils;

import com.amal.imageshare.Models.SearchEngineResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by amal on 12/12/15.
 */
public class ParseContent {

    public static ArrayList<SearchEngineResults> parseJson(String response) {
        ArrayList<SearchEngineResults> searchEngineResultsArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
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
        return searchEngineResultsArrayList;
    }

}

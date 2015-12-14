package com.amal.imageshare.Networking;

import android.content.Context;
import android.util.Log;

import com.amal.imageshare.Interfaces.AsyncTaskCompleteListener;
import com.amal.imageshare.Utils.Commonutils;
import com.amal.imageshare.Utils.Const;
import com.amal.imageshare.app.AppController;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Amal on 28-06-2015.
 */
public class HttpRequester {

    int servicecode;
    private Context activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;
    private Map<String, String> map;

    public HttpRequester(Context activity, int method_type, Map<String, String> map, int servicecode, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        int method;
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
        this.map = map;
        this.servicecode = servicecode;
        if (method_type == 0)
            method = Request.Method.GET;
        else
            method = Request.Method.POST;
        String URL = map.get(Const.URL);
        map.remove(Const.URL);

        if (method == Request.Method.POST)
            volley_requester(method, URL, (map == null) ? null : map);
        else
            volley_requester(URL);

    }


    public void volley_requester(int method, String url, final Map<String, String> requestbody) {

        StringRequest jsonObjRequest = new StringRequest(method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        asyncTaskCompleteListener.onTaskCompleted(response.toString(), servicecode);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Log.d("amal", "volley requester 1" + error.toString());
                    String msg = "No network connection.Please check your internet";
                    Commonutils.showtoast(msg, activity);
                    Commonutils.progressdialog_hide();
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params = requestbody;
                return params;
            }

        };

        // To keep Timeout 30 seconds. Time is entered in milliseconds.
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjRequest);

    }

    public void volley_requester(String url) {

        JsonObjectRequest jsongetrequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                asyncTaskCompleteListener.onTaskCompleted(response.toString(), servicecode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Log.d("amal", "volley requester 2" + error.toString());
                    String msg = "No network connection.Please check your internet";
                    Commonutils.showtoast(msg, activity);
                    Commonutils.progressdialog_hide();
                }
            }
        });

        // To keep Timeout 30 seconds. Time is entered in milliseconds.
        jsongetrequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsongetrequest);
    }


}

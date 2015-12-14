package com.amal.imageshare.Utils;


/**
 * Created by user on 6/29/2015.
 */
public class Const {

    //General
    public static final String URL = "url";
    public static final String CACHED_DIRECTORY_NAME = "shared_images_from_this_app";
    //for GET/POST method
    public static int GET = 0;
    public static int POST = 1;

    public static class Endpoints {
        // public static final String HOST_URL = "https://duckduckgo.com/i.js?q=Temple%20of%20the%20Golden%20Pavilion&s=50";
        //  public static final String HOST_URL = "https://duckduckgo.com/i.js?q=minion&s=50";
        private static final String HOST_URL = "https://duckduckgo.com/";

        public static String getFullSearchQuery(String query) {
            String complete_url = HOST_URL + "i.js?q=" + query + "&s=50";
            return complete_url;
        }
        public static String getNextSearchUrl(String query) {
            String complete_url = HOST_URL + query;
            return complete_url;
        }
    }

    public class Params {
        public static final String RESULTS = "results";
        public static final String HEIGHT = "height";
        public static final String IMAGE = "image";
        public static final String SOURCE = "source";
        public static final String THUMBNAIL = "thumbnail";
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String WIDTH = "width";
        public static final String NEXT = "next";
    }

    public class ServiceCode {
        public static final int GET_QUERY = 1;
        public static final int NEXT_QUERY = 2;
    }

}
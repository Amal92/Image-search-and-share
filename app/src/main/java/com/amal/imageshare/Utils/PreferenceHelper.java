package com.amal.imageshare.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amal on 19/12/15.
 */
public class PreferenceHelper {
    private SharedPreferences app_prefs;
    private Context context;
    private String IS_FIRST_USE = "isFirstUse";

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences(Const.PREF_NAME,
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putFirstuse(boolean isFirstUse) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_FIRST_USE, isFirstUse);
        edit.commit();
    }

    public boolean getFirstuse() {
        return app_prefs.getBoolean(IS_FIRST_USE, true);
    }

}

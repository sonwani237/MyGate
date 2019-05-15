package com.troology.mygate.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public enum UtilsMethods {

    INSTANCE;

    public SharedPreferences getPerf(final Context context){
        return context.getSharedPreferences(ApplicationConstant.INSTANCE.appPref, context.MODE_PRIVATE);
    }
    
    public void save(final Context context, String key, Object o) {
        getPerf(context).edit()
                .putString(key, new Gson().toJson(o))
                .apply();
    }

    public <T> T get(final Context context, String key, Class<T> returnType) {
        String json = getPerf(context).getString(key, null);
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, returnType);
    }

}

package com.forbitbd.myplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

    private static final String SP_NAME ="AppPreference";
    private static final String COUNTER ="COUNTER";
    private static final String BACK_COUNTER ="BACK_COUNTER";

    private SharedPreferences userLocalDatabase;

    private static AppPreference instance;


    private AppPreference(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public static AppPreference getInstance(Context context) {
        if (instance == null) {
            instance = new AppPreference(context);
        }
        return instance;
    }


    public void increaseCounter(){
        int prev = userLocalDatabase.getInt(COUNTER,0);
        prev++;
        userLocalDatabase.edit().putInt(COUNTER,prev).apply();
    }

    public int getCounter(){
        return userLocalDatabase.getInt(COUNTER,0);
    }

    public void resetCounter(){
        userLocalDatabase.edit().putInt(COUNTER,0).apply();
    }


    public void increaseBackCounter(){
        int prev = userLocalDatabase.getInt(BACK_COUNTER,0);
        prev++;
        userLocalDatabase.edit().putInt(BACK_COUNTER,prev).apply();
    }

    public int getBackCounter(){
        return userLocalDatabase.getInt(BACK_COUNTER,0);
    }

    public void resetBackCounter(){
        userLocalDatabase.edit().putInt(BACK_COUNTER,0).apply();
    }
}

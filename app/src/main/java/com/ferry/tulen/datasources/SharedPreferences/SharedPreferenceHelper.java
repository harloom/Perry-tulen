package com.ferry.tulen.datasources.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public  static String KEY_TYPE_LOGIN = "TYPE_LOGIN";
    public  static  String KEY_ID_USER =  "KEY_ID_USER";


    public SharedPreferenceHelper(Context context) {
        sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}

// use
//SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(this);
//sharedPreferenceHelper.saveString("key_name", "string value");
//String myString = sharedPreferenceHelper.getString("key_name", null);
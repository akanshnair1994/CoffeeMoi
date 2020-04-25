package com.hexamind.coffeemoi.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.hexamind.coffeemoi.Model.Orders;

public class SharedPreferenceManager {
    private static SharedPreferences sharedPreferences;

    public static void saveStringToSharedPreference(Context context, String data) {
        SharedPreferences.Editor editor = checkIfSharedPrefsIsNull(context).edit();
        editor.putString(Constants.STRING_DATA, data);
        editor.apply();
    }

    public static void saveCurrentUser(Context context, FirebaseUser currentUser) {
        Gson gson = new Gson();
        String data = gson.toJson(currentUser);

        SharedPreferences.Editor editor = checkIfSharedPrefsIsNull(context).edit();
        editor.putString(Constants.CURRENT_USER, data);
        editor.apply();
    }

    public static void saveObjectToSharedPreference(Context context, Object data) {
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);

        saveStringToSharedPreference(context, jsonData);
    }

    public static void deleteSharedPreference(Context context) {
        checkIfSharedPrefsIsNull(context).edit().clear().apply();
    }

    public static String getStringFromSharedPreference(String stringToRetrieve, Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getString(stringToRetrieve, "");
    }

    public static Orders getObjectFromSharedPreference(String objectToRetrieve, Class<Orders> object, Context context) {
        String json = checkIfSharedPrefsIsNull(context).getString(objectToRetrieve, "");
        return new Gson().fromJson(json, object);
    }

    private static SharedPreferences checkIfSharedPrefsIsNull(Context context) {
        if  (sharedPreferences == null)
            return context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}

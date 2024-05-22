package com.example.pruebafinalis;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String TOKEN_KEY = "token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clearSession() {
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}

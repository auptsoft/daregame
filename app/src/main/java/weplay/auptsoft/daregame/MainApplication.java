package weplay.auptsoft.daregame;

import android.app.Application;

import com.google.gson.Gson;

import weplay.auptsoft.daregame.models.User;
import weplay.auptsoft.daregame.services.RESTUtil;

/**
 * Created by Andrew on 19.3.19.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppState.preferences = getSharedPreferences(AppState.PREFERENCE_NAME, MODE_PRIVATE);
        AppState.isFirstTime = AppState.preferences.getBoolean(AppState.IS_FIRST_TIME_PREF_KEY, true);
        AppState.api_key = AppState.preferences.getString(AppState.API_KEY_PREF, "");

        getApiKey();
        getUser();
    }

    public boolean loginUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        AppState.preferences.edit().putString(AppState.USER_PREF_KEY, userJson).apply();

        return getUser() != null;
    }

    private User getUser() {
        String userJson = AppState.preferences.getString(AppState.USER_PREF_KEY, null);
        if(userJson == null) return null;
        else {
            return new Gson().fromJson(userJson, User.class);
        }
    }

    public void logoutUser() {
        AppState.preferences.edit().putString(AppState.USER_PREF_KEY, null).apply();
        AppState.user = null;
        AppState.api_key = "";
    }

    public void saveApiKey(String token) {
        AppState.preferences.edit().putString(AppState.API_KEY_PREF, "Bearer "+token).apply();
        AppState.api_key = "Bearer "+token;
        RESTUtil.setAccessKey(AppState.api_key);
    }

    private void getApiKey() {
        String api_key = AppState.preferences.getString(AppState.API_KEY_PREF, "");
        AppState.api_key = api_key;
        RESTUtil.setAccessKey(AppState.api_key);
    }

    public void disableFirstTimeEvent() {
        AppState.preferences.edit().putBoolean(AppState.IS_FIRST_TIME_PREF_KEY, false).apply();
        AppState.isFirstTime = false;
    }
}

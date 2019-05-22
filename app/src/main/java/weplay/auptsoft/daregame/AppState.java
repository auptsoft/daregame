package weplay.auptsoft.daregame;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import weplay.auptsoft.daregame.models.Category;
import weplay.auptsoft.daregame.models.Challenge;
import weplay.auptsoft.daregame.models.User;

/**
 * Created by Andrew on 15.3.19.
 */

public class AppState {
    public static final String BASE_URL = "http://192.168.43.32/";
    public static final String INITIAL_PATH = "daregame/public/api";

    //public static final String MEDIA_PATH

    public static boolean isFirstTime = true;
    public static final String IS_FIRST_TIME_PREF_KEY = "is_first_time";

    public static SharedPreferences preferences;
    public static final String PREFERENCE_NAME = "daregame_preference_name";

    public static User user;
    public static final String USER_PREF_KEY = "authenticated_user";

    public static String api_key = "";
    public static final String API_KEY_PREF = "api_key";

    public static Challenge currentChallenge;

    public static ArrayList<Category> categories;
    public static Challenge currentEditChallenge;

    //Methods below are used to handle page refresh
    private static int dirtyIndex = 0;
    public static void setDirty(int level) {
        dirtyIndex = level;
    }

    public static boolean isDirty() {
        return dirtyIndex!=0;
    }


    public static void handleDirty() {
        if(dirtyIndex>0) {
            dirtyIndex--;
        }
    }
    //end

}

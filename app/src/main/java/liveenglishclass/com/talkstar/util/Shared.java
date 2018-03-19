package liveenglishclass.com.talkstar.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kwangheejung on 2018. 3. 19..
 */

public final class Shared extends Activity {
    private static final String keyValue = "talkstarKeyValue9991@";
    private static SharedPreferences pref;


    public static void savePreferences(Activity activity, String key, String value)
    {
        SharedPreferences pref = activity.getSharedPreferences(keyValue, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPerferences(Activity activity, String key) {
        SharedPreferences pref = activity.getSharedPreferences(keyValue, MODE_PRIVATE);
        String value = pref.getString(key, "");
        return value;
    }

    public static void removePrefercences(Activity activity,String key)
    {
        SharedPreferences pref = activity.getSharedPreferences(keyValue, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }


    public static void removeAllPrefercences(Activity activity)
    {
        SharedPreferences pref = activity.getSharedPreferences(keyValue, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }
}

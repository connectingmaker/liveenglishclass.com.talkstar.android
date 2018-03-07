package liveenglishclass.com.talkstar.core;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by kwangheejung on 2018. 3. 7..
 */

public class ActivityManager
{
    //Singleton
    public static final ActivityManager activityManager = new ActivityManager();
    private ArrayList<Activity> listActivity = null;

    private ActivityManager()
    {
        listActivity = new ArrayList<Activity>();
    }

    public static ActivityManager getInstance()
    {
        return activityManager;
    }

    //custom method
    //add activity
    public void addActivity(Activity activity)
    {
        listActivity.add(activity);
    }

    //remove activity
    public boolean removeActivity(Activity activity)
    {
        return listActivity.remove(activity);
    }

    //all activity finish
    public void finishAllActivity()
    {
        for(Activity activity : listActivity)
        {
            activity.finish();
        }
    }

    //Getter, Setter
    public ArrayList<Activity> getListActivity()
    {
        return listActivity ;
    }

    public void setListActivity(ArrayList<Activity> listActivity)
    {
        this.listActivity = listActivity ;
    }
}

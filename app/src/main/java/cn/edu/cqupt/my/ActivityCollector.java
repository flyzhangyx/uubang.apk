package cn.edu.cqupt.my;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector extends AppCompatActivity {
    public static Context acontext=null;
    public static List<Activity> a= new ArrayList<>();
    public static void addActivity(Activity b)
    {
        a.add(b);
    }
    public static void setcontext(Context con)
    {
       acontext=con;
    }
    public static void deleteActivity(Activity b)
    {
        a.remove(a);
    }
    public static void finishallActivity()
    {
        for(Activity activity:a)
        {
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}

package com.estar.hh.survey.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 * 类名：activity的管理类
 */

public class ActivityManagerUtils {

    private static ActivityManagerUtils mActivityManagerUtils;
    private static Activity mCurrentActivity;


    static {
        mActivityManagerUtils = new ActivityManagerUtils();
    }

    private ActivityManagerUtils() {
        /**
         * 这里面写一些需要执行初始化的工作
         */
    }

    public static void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

    public static ActivityManagerUtils getInstance() {
        return mActivityManagerUtils;

    }

    /**
     * 打开的activity
     **/

    private List<Activity> activities = new ArrayList<Activity>();


    /**
     * 新建了一个activity
     *
     * @param activity
     */

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public void finishActivity(Activity activity) {

        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 应用退出，结束所有的activity
     */

    public void exit() {

        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);

    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityclass(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    this.activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity getActivityclass(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }

        return null;
    }

}

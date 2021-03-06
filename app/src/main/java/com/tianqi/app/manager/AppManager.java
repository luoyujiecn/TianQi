package com.tianqi.app.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;


/**
 * @类名称：AppManager
 * @类描述：应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static final String TAG = AppManager.class.getSimpleName();
    public static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        // ...
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
            activityStack = new Stack<Activity>();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 返回到指定的Activity并且清除Stack中其位置上的所有Activity
     *
     * @param clz 指定的Activity
     * @return 该Activity 目前不返回，有需要再加
     */
    public Activity getTopWith(Class<?> clz) {
        Activity resAc = null;
        int postion = 0;
        if (null != clz) {
            if (null != activityStack && activityStack.size() > 0) {
                for (int i = 0; i < activityStack.size(); i++) {
                    if (activityStack.get(i).getClass().getName().contains(clz.getName())) {
                        postion = i;
                    }
                }
                int num = activityStack.size() - postion - 1;
                if (num > 0) {
                    for (int i = 0; i < num; i++) {
                        if (!activityStack.empty()) {
                            finishActivity(activityStack.pop());
                        }
                    }
                }
            }
        }
        // TODO 目前不返回，有需要再加
        return resAc;
    }

    /**
     * 判断是否有该Activity
     *
     * @param clz Activiy
     * @return true 则存在
     */
    public static boolean hasActivity(Class<?> clz) {
        if (null != clz) {
            if (null != activityStack && activityStack.size() > 0) {
                for (int i = 0; i < activityStack.size(); i++) {
                    if (activityStack.get(i).getClass().getName().contains(clz.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null || activityStack.size() == 0) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            //OftenUtils.killAppProcess(context);
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
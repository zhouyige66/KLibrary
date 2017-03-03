package cn.kk20.lib;

import android.app.Activity;

import java.util.Stack;

/**
 * Description: 应用程序Activity管理类
 * Author: kk20
 * Email: 751664206@qq.com
 * Date: 2017/8/10
 * Modify: 2017/1/16 上午11:12
 * Version: V1.0.0
 */
public class ActivityManager {
    private static ActivityManager instance = null;
    private Stack<Activity> activityStack = new Stack<>();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取栈顶Activity
     */
    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() > 1) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
                removeActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Stack<Activity> middle = new Stack<Activity>();
        middle.addAll(activityStack);
        for (int i = 0; i < middle.size(); i++) {
            if (null != middle.get(i)) {
                middle.get(i).finish();
            }
        }

        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
    }
}

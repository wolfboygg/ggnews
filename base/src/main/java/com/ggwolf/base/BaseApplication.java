package com.ggwolf.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

public class BaseApplication extends Application {

    public static BaseApplication sApplication;

    public static boolean sIsDebug;

    public static void setsIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
            if (runningApp.pid == android.os.Process.myPid()) {
                if (runningApp.processName != null) {
                    return runningApp.processName;
                }
            }
        }
        return null;
    }

}

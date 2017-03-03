package cn.kk20.lib.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xutils.x;

import java.io.File;

import cn.kk20.lib.util.IFileUtils;
import cn.kk20.lib.util.permission.PermissionsChecker;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Description: Application基类
 * Author: kk20
 * Email: 751664206@qq.com
 * Date: 2017/8/10
 * Modify: 2017/1/16 上午11:14
 * Version: V1.0.0
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    public static BaseApplication application;
    protected Logger log;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        PermissionsChecker permissionsChecker = new PermissionsChecker(this);
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!permissionsChecker.lacksPermissions(permissions)) {
            init();
        }

        // 注册Activity生命周期回调
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private void initLog4j() {
        String path = IFileUtils.mkdir2SDCard(this.getPackageName() + File.separator + "log");

        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(path + "log.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.DEBUG);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 2);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();

        log = Logger.getLogger(getClass());
        log.info("log4j init ok!");
    }

    private void initXUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    protected void init() {
        // 初始化日志收集工具
        initLog4j();
        // 初始化xUtils
        initXUtils();
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

}

package cn.kk20.lib.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import org.apache.log4j.Level;
import org.xutils.x;

import java.io.File;

import cn.kk20.lib.exception.CrashHandler;
import cn.kk20.lib.exception.CrashTipActivity;
import cn.kk20.lib.util.UtilFile;
import cn.kk20.lib.permission.PermissionsChecker;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * @Description Application基类
 * @Author kk20
 * @Date 2017/5/18
 * @Version V1.0.0
 */
public abstract class BaseApplication extends Application implements
        Application.ActivityLifecycleCallbacks {
    public static BaseApplication mBaseApplication;
    protected CrashHandler mCrashHandler;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        mBaseApplication = this;

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
        String path = UtilFile.mkdir2SDCard(this.getPackageName() + File.separator + "log");

        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(path + "log.txt");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.DEBUG);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 2);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
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
        // 程序异常处理
        mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(this);
        mCrashHandler.setHandleException(new CrashHandler.HandleException() {
            @Override
            public void handleException(Throwable ex) {
                if (autoRestart()) {
                    // 此处示例发生异常后，重新启动应用
                    Intent intent = new Intent(BaseApplication.this, CrashTipActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * 是否自动重启
     *
     * @return true，自动重启；false，不自动重启
     */
    public abstract boolean autoRestart();

    /**
     * 重启进入页面
     *
     * @return
     */
    public abstract Class<? extends Activity> getTargetActivity();

}

package cn.kk20.testdemo;

import android.app.Activity;

import cn.kk20.lib.base.BaseApplication;

/**
 * @Description
 * @Author kk20
 * @Date 2017/5/18
 * @Version V1.0.0
 */
public class IBaseApplication extends BaseApplication {

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean autoRestart() {
        return true;
    }

    @Override
    public Class<MainActivity> getTargetActivity() {
        return MainActivity.class;
    }
}

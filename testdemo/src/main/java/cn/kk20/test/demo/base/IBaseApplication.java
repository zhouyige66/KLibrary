package cn.kk20.test.demo.base;

import android.app.Activity;

import cn.kk20.lib.base.BaseApplication;
import cn.kk20.test.demo.AppStartActivity;

/**
 * @Description
 * @Author kk20
 * @Date 2017/6/21
 * @Version V1.0.0
 */
public class IBaseApplication extends BaseApplication {

    @Override
    public boolean autoRestart() {
        return true;
    }

    @Override
    public Class<? extends Activity> getTargetActivity() {
        return AppStartActivity.class;
    }

    @Override
    public void init() {
        super.init();
    }
}

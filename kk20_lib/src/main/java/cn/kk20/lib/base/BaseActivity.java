package cn.kk20.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;

import cn.kk20.lib.ActivityManager;

/**
 * @Description Activity基类
 * @Author kk20
 * @Date 2017/5/18
 * @Version V1.0.0
 */
public class BaseActivity extends AutoLayoutActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getInstance().removeActivity(this);
    }

}

package cn.kk20.test.demo;

import android.os.Bundle;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.kk20.lib.util.ILogUtils;
import cn.kk20.test.demo.base.IBaseActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends IBaseActivity {

    @Event(R.id.button)
    private void testCrash(View view) {
        ILogUtils.i("点击按钮");
        String str = "test";
        System.out.print(str.substring(0, 6));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

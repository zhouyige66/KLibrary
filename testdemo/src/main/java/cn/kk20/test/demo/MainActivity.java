package cn.kk20.test.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import cn.kk20.lib.util.ILogUtils;
import cn.kk20.test.demo.base.IBaseActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends IBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ILogUtils.i("调用了onCreate()");

        if(savedInstanceState!=null){
            Bundle mBundle = savedInstanceState.getBundle("custom");
            if (mBundle != null) {
                ILogUtils.i("保存数据为：" + mBundle.getString("data"));
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ILogUtils.i("调用了onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        ILogUtils.i("调用了onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILogUtils.i("调用了onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILogUtils.i("调用了onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ILogUtils.i("调用了onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ILogUtils.i("调用了onDestroy()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle mBundle = new Bundle();
        mBundle.putString("data", "kk20");
        outState.putBundle("custom", mBundle);
        super.onSaveInstanceState(outState);
        ILogUtils.i("调用了onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ILogUtils.i("调用了onRestoreInstanceState()");

        Bundle mBundle = savedInstanceState.getBundle("custom");
        ILogUtils.i("保存数据为：" + mBundle.getString("data"));
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        ILogUtils.i("调用了onCreateView()");
        return super.onCreateView(name, context, attrs);
    }

    @Event(R.id.button)
    private void testCrash(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }

}


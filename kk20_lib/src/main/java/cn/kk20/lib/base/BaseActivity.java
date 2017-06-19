package cn.kk20.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;

import java.util.logging.Logger;

import cn.kk20.lib.ActivityManager;
import cn.kk20.lib.mvp.IBaseView;
import cn.kk20.lib.widget.CommonLoadingDialog;

/**
 * @Description Activity基类
 * @Author kk20
 * @Date 2017/5/18
 * @Version V1.0.0
 */
public class BaseActivity extends AutoLayoutActivity implements IBaseView {
    protected Logger mLogger = Logger.getLogger(getClass().getSimpleName());
    protected CommonLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideLoading();
        if (mLoadingDialog != null) {
            mLoadingDialog = null;
        }
        ActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new CommonLoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        imm.hideSoftInputFromInputMethod(rootView.getWindowToken(), 0);
    }

    @Override
    public void showMessage(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void jump(Bundle data, Class<?> targetClass, boolean close) {
        Intent intent = new Intent(this, targetClass);
        if (data == null) {
            intent.putExtra("data", data);
        }
        startActivity(intent);
        if (close) {
            finish();
        }
    }
}

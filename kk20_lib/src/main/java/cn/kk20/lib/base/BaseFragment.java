package cn.kk20.lib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * Description: Fragment基类
 * Author: kk20
 * Email: 751664206@qq.com
 * Date: 2016/8/10
 * Modify: 2017/1/16 上午11:16
 * Version: V1.0.0
 */
public class BaseFragment extends Fragment {
    private boolean injected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, view);
        }
    }

}

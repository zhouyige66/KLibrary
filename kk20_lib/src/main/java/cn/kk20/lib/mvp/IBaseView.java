package cn.kk20.lib.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @Description
 * @Author kk20
 * @Date 2017/6/9
 * @Version V1.0.0
 */
public interface IBaseView {

    void showLoading();

    void hideLoading();

    void hideSoftInput();

    void showMessage(@NonNull String msg);

    void jump(Bundle data, Class<?> targetClass, boolean close);

}

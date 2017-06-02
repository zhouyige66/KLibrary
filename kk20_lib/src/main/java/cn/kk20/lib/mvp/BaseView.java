package cn.kk20.lib.mvp;

/**
 * @Description
 * @Author kk20
 * @Date 2017/5/10
 * @Version V1.0.0
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}

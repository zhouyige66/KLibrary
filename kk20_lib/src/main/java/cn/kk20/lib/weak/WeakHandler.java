package cn.kk20.lib.weak;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @Description
 * @Author kk20
 * @Date 2017/8/9
 * @Version V1.0.0
 */
public abstract class WeakHandler extends Handler {
    private WeakReference<Context> wr;

    private WeakHandler() {

    }

    public WeakHandler(Context context) {
        wr = new WeakReference<Context>(context);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        handle(msg);
    }

    public abstract void handle(Message msg);
}

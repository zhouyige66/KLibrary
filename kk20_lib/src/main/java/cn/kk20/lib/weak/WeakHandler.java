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
public final class WeakHandler extends Handler {
    private WeakReference<MessageHandler> wr;

    private WeakHandler() {

    }

    public WeakHandler(MessageHandler handler) {
        wr = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(Message msg) {
        if (wr.get() != null) {
            wr.get().handle(msg);
        }
    }

    public interface MessageHandler {
        void handle(Message msg);
    }

}

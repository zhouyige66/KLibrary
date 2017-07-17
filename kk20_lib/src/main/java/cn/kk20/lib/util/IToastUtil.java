package cn.kk20.lib.util;

import android.widget.Toast;

import org.xutils.x;

/**
 * Toast统一管理类
 */
public class IToastUtil {
    private static Toast toast;

    private IToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void toast(String msg) {
        if (msg.equals("")) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
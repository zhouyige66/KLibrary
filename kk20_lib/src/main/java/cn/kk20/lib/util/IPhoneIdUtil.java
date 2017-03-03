package cn.kk20.lib.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @author kk20
 * @version V1.0
 * @Description 获取android设备唯一标识
 * @date 2016/12/29 10:46
 */

public class IPhoneIdUtil {

    public static String getPhoneId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return getAndroidId(context);
        } else {
            return deviceId;
        }
    }

    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        if (androidId == null) {
            return getSerialNumber();
        } else {
            return androidId;
        }
    }

    public static String getSerialNumber() {
        if (Build.VERSION.SDK_INT > 8) {
            return android.os.Build.SERIAL;
        } else {
            return null;
        }
    }
}

package cn.kk20.lib.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 获取android设备唯一标识
 */
public class IPhoneIdUtil {

    public static String getPhoneId(Context context) {
        String deviceId = getIMEI(context);
        if (deviceId == null) {
            return getAndroidId(context);
        } else {
            return deviceId;
        }
    }

    /**
     * 获取手机IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 获取手机IMSI号
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();//MCC(移动国家号码,3位)、MNC(网络id，2位)、MSIN(等长11位数字)
        return imsi;
    }

    /**
     * 获取AndroidId（设备被wipe后该值会被重置）
     *
     * @param context
     * @return 16进制字符串
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        if (androidId == null) {
            return getSerialNumber();
        } else {
            return androidId;
        }
    }

    /**
     * 获取SerialNumber（Android2.3以上可用）
     *
     * @return
     */
    public static String getSerialNumber() {
        if (Build.VERSION.SDK_INT > 8) {
            return android.os.Build.SERIAL;
        } else {
            return null;
        }
    }
}

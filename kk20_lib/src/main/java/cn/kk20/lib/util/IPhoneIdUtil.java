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

    /**
     * 获取手机IMEI号（国际移动设备身份码）
     * 唯一地识别一台移动设备的编码
     *
     * @param context
     * @return 15位数字编码
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(context.TELEPHONY_SERVICE);
        //TAC(6位数字)+FAC(2位数字)+SNR(6位数字)+SP(l位数字)
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 获取手机IMSI号（国际移动用户识别码）
     * 唯一地识别国内GSM移动通信网中移动客户
     *
     * @param context
     * @return 16位数字编码
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        //MCC(移动国家号码,3位,中国是460)、MNC(网络id，2位，移动00、联调01)、MSIN(等长11位数字)
        String imsi = mTelephonyMgr.getSubscriberId();
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
        return androidId;
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

    public static String getPhoneId(Context context) {
        String deviceId = getIMEI(context);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getAndroidId(context);
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = getSerialNumber();
                if (TextUtils.isEmpty(deviceId)) {
                    deviceId = getIMSI(context);
                }
            }
        }

        return deviceId;
    }
}

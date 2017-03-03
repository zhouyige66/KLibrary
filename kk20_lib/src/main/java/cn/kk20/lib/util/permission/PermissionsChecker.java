package cn.kk20.lib.util.permission;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/**
 * 检查权限的工具类
 * Created by kk20 on 2016/11/15.
 */
public class PermissionsChecker {
    private Context mContext;
    private int targetSdkVersion = 0;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = mContext.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_DENIED;
        } else {
            result = ContextCompat.checkSelfPermission(mContext, permission)
                    == PermissionChecker.PERMISSION_DENIED;
        }

        return result;
    }
}

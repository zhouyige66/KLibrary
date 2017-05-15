package cn.kk20.lib.util.update;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;

import cn.kk20.lib.R;
import cn.kk20.lib.util.IToastUtil;
import cn.kk20.lib.util.IHttpUtils;
import cn.kk20.lib.widget.CommonLoadingDialog;
import cn.kk20.lib.widget.CommonTipDialog;

public class CheckUpdateUtil {
    private Context mContext;

    // 当前版本号
    private int versionCode;
    // 当前版本名称
    private String versionName;
    // 服务器版本号
    private int serverVersionCode;
    // 服务器版本名称
    private String serverVersionName;
    // 更新内容
    private String serverVersionDescription;

    // 是否显示获取版本信息dialog
    private boolean isShowLoadingDialog = false;
    // 加载数据对话框
    private CommonLoadingDialog loadingDialog;

    // 检查更新地址
    private String checkUpdateUrl = null;
    // 新版本下载地址
    private String apkDownloadUrl = null;
    // 应用下载保存地址
    private String apkSavePath = null;

    public CheckUpdateUtil(Context context, String checkUpdateUrl, String apkSavePath,boolean showLoadingDialog) {
        super();
        this.mContext = context;

        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        this.checkUpdateUrl = checkUpdateUrl;
        this.apkSavePath = ((Environment
                .getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) ? Environment
                .getExternalStorageDirectory().getAbsolutePath() : Environment
                .getDataDirectory().getAbsolutePath()) + apkSavePath;
        this.isShowLoadingDialog = showLoadingDialog;
    }

    public void checkUpdate() {
        if (isShowLoadingDialog) {
            loadingDialog = new CommonLoadingDialog(mContext);
            loadingDialog.setMessage("正在获取最新版本信息...");
            loadingDialog.show();
        }

        if (checkUpdateUrl == null || checkUpdateUrl.equals("")) {
            IToastUtil.toast("检查更新地址不能为空");
            return;
        }

        IHttpUtils httpUtils = new IHttpUtils();
        RequestParams params = httpUtils.getParams(checkUpdateUrl);
        params.addParameter("code", versionCode);
        params.addParameter("packageName", mContext.getPackageName());
        params.setConnectTimeout(60 * 1000);
        httpUtils.doPost(params, "检查更新", new IHttpUtils.IHttpRequestCallback() {
            @Override
            public void onHttpRequestSuccess(String result) {
                if (isShowLoadingDialog) {
                    loadingDialog.dismiss();
                }
                try {
                    JSONObject resultJsonObject = new JSONObject(result);
                    if (resultJsonObject.getString("code").equals("0")) {
                        JSONObject data = resultJsonObject.getJSONObject("data");
                        if (data.optString("url") != null) {
                            apkDownloadUrl = data.getString("url");
                        }
                        serverVersionName = data.getString("versionName");
                        serverVersionCode = data.getInt("versionCode");
                        serverVersionDescription = data.getString("description");
                        showUpdateDialog();
                    } else {
                        if (isShowLoadingDialog) {
                            IToastUtil.toast("您当前版本已经是最新的！");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onHttpRequestError(String msg) {
                if (isShowLoadingDialog) {
                    loadingDialog.dismiss();
                    IToastUtil.toast("获取最新版本信息出错：" + msg);
                }

            }
        });
    }

    public void showUpdateDialog() {
        CommonTipDialog tipDialog = new CommonTipDialog(mContext);
        tipDialog.setTitle("检测到新版本：" + serverVersionName);
        tipDialog.setTitleVisiable();
        tipDialog.setMessage(serverVersionDescription);
        tipDialog.setLeftBtnText("立即更新");
        tipDialog.setRightBtnText("暂不更新");
        tipDialog.setListener(new CommonTipDialog.OnBtnClickListener() {

            @Override
            public void onLeftBtnClickListener() {
                downloadApk();
            }

            @Override
            public void onRightBtnClickListener() {

            }
        });

        tipDialog.show();
    }

    public void downloadApk() {
        if (apkDownloadUrl == null || apkDownloadUrl.equals("")) {
            IToastUtil.toast("下载地址为空，无法下载应用");
            return;
        }

        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        View dialog_view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_dialog_download_apk, null);
        Button btn_cancle_download = (Button) dialog_view
                .findViewById(R.id.btn_cancle);
        Button btn_download_backup = (Button) dialog_view
                .findViewById(R.id.btn_background);
        dialog.setContentView(dialog_view);
        dialog.setCanceledOnTouchOutside(false);

        String savePath = apkSavePath + serverVersionName + ".apk";
        RequestParams params = new RequestParams(apkDownloadUrl);
        params.setAutoResume(true);
        params.setAutoRename(true);
        params.setSaveFilePath(savePath);
        params.setCancelFast(true);
        DownloadAPKCallback downloadAPKCallback = new DownloadAPKCallback(dialog, dialog_view);
        final Callback.Cancelable cancelable = x.http().get(params, downloadAPKCallback);

        btn_cancle_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelable.cancel();
            }
        });
        btn_download_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class DownloadAPKCallback implements
            Callback.CommonCallback<File>,
            Callback.ProgressCallback<File>,
            Callback.Cancelable {

        private Dialog dialog;
        private ProgressBar progressBar;
        private TextView downloadProgress, downloadTotal;

        public DownloadAPKCallback(Dialog dialog, View downloadView) {
            this.dialog = dialog;
            progressBar = (ProgressBar) downloadView.findViewById(R.id.pb);
            downloadProgress = (TextView) downloadView.findViewById(R.id.tv_progress);
            downloadTotal = (TextView) downloadView.findViewById(R.id.tv_total);
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            int progress = (int) (current * 100 / total);
            progressBar.setProgress(progress);
            String max = new BigDecimal(total / 1024.00 / 1024.00).setScale(2,
                    BigDecimal.ROUND_HALF_UP) + "M";
            String now = new BigDecimal(current / 1024.00 / 1024.00).setScale(2,
                    BigDecimal.ROUND_HALF_UP) + "M";
            downloadTotal.setText("总大小：" + (max.equals("0.00M") ? "未知" : max));
            downloadProgress.setText("已下载：" + now);
        }

        @Override
        public void onSuccess(File result) {
            dialog.dismiss();
            new FileHelper(x.app()).openFile(result);//更新
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {

        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }

}

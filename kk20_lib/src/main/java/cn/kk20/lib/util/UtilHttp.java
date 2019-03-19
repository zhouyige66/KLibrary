package cn.kk20.lib.util;

import android.util.Log;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import cn.kk20.lib.base.BaseApplication;

/**
 * 网络请求工具类
 */
public class UtilHttp {

    public RequestParams getParams(String url) {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(30 * 1000);// 30s请求时间
        return params;
    }

    public RequestParams getParams(String url, String jsonStr) {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(30 * 1000);// 30s请求时间
        params.setAsJsonContent(true);
        params.setBodyContent(jsonStr);
        return params;
    }

    /**
     * post方式请求
     *
     * @param params 参数
     * @param tipMsg 打印信息头部
     * @param callback 请求回调
     */
    public void doPost(RequestParams params, final String tipMsg, final IHttpRequestCallback callback) {
        if (!UtilNetwork.isNetworkEnable(BaseApplication.mBaseApplication)) {
            Toast.makeText(BaseApplication.mBaseApplication, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        if (params.isAsJsonContent()) {
            UtilLog.i(tipMsg + "--->请求地址：" + params.getUri());
            UtilLog.i(tipMsg + "--->请求参数：" + params.getBodyContent());
        } else {
            UtilLog.i(tipMsg + "--->请求地址：" + params);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // 这里对重复登录等情况进行处理
                UtilLog.i(tipMsg + "--->返回结果：" + result);
                if (callback != null) {
                    callback.onHttpRequestSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 统一提示错我信息：tipMsg:ex.getMessage
                UtilLog.i(tipMsg + "--->出错:" + ex.toString());
                if (callback != null) {
                    callback.onHttpRequestError(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * get方式请求
     *
     * @param params 参数
     * @param tipMsg 打印信息头部
     * @param callback 请求回调
     */
    public void doGet(RequestParams params, final String tipMsg, final IHttpRequestCallback callback) {
        if (params.isAsJsonContent()) {
            Log.i("kk20", tipMsg + "--->请求地址：" + params.getUri());
            Log.i("kk20", tipMsg + "--->请求参数：" + params.getBodyContent());
        } else {
            Log.i("kk20", tipMsg + "--->请求地址：" + params);
        }
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // 这里对重复登录等情况进行处理
                Log.i("kk20", tipMsg + "--->返回结果：" + result);
                if (callback != null) {
                    callback.onHttpRequestSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 统一提示错我信息：tipMsg:ex.getMessage
                Log.i("kk20", tipMsg + "--->出错:" + ex.toString());
                if (callback != null) {
                    callback.onHttpRequestError(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 上传文件
     *
     * @param uploadUrl 上传地址
     * @param filePath 上传文件路径
     * @param callback 上传回调
     */
    public void uploadFile(String uploadUrl, String filePath, final IUploadHttpRequestCallback callback) {
        if (filePath == null || filePath.equals("")) {
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        RequestParams params = new RequestParams(uploadUrl);
        params.addBodyParameter("client_file", new File(filePath));
        params.addBodyParameter("enctype", "multipart/form-data");
        x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if(callback!=null){
                    callback.onHttpRequestLoading(total,current);
                }
            }

            @Override
            public void onSuccess(String result) {
                if(callback!=null){
                    callback.onHttpRequestSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(callback!=null){
                    callback.onHttpRequestError(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface IHttpRequestCallback {
        void onHttpRequestSuccess(String result);

        void onHttpRequestError(String msg);
    }

    public interface IUploadHttpRequestCallback extends IHttpRequestCallback{
        void onHttpRequestLoading(long total, long current);
    }


}

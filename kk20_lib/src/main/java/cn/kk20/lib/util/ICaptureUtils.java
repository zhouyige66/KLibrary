package cn.kk20.lib.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.kk20.lib.R;
import cn.kk20.lib.view.ScreenShotView;

/**
 * 截图演示工具类
 */
public class ICaptureUtils {
    private Activity mActivity;
    private View captureView = LayoutInflater.from(mActivity).inflate(R.layout.layout_capture, null);
    private ScreenShotView mScreenShotView;
    private LinearLayout mLinearLayout;
    private Button btn_left, btn_right;

    private ScreenShotView.OnCaptureListener onCaptureListener;
    private CaptureHideListener captureHideListener;

    private Bitmap screenBitmap;
    private String bitmapSavePath;

    private boolean captureViewHasAdd = false;

    public ICaptureUtils(Activity activity) {
        super();

        this.mActivity = activity;
        init();
    }

    private void init() {
        bitmapSavePath = Environment.getExternalStorageDirectory() + "/test.png";

        mScreenShotView = (ScreenShotView) captureView.findViewById(R.id.v_capture);
        mLinearLayout = (LinearLayout) captureView.findViewById(R.id.ll_capture_operation);
        btn_left = (Button) captureView.findViewById(R.id.btn_left);
        btn_right = (Button) captureView.findViewById(R.id.btn_right);
        btn_left.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mScreenShotView.reset();
                btn_right.setText("截取全屏");
                captureView.setVisibility(View.GONE);
                if (captureHideListener != null) {
                    captureHideListener.captureHideCallbak();
                }
            }
        });
        btn_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                saveBitmap();
            }
        });

        onCaptureListener = new ScreenShotView.OnCaptureListener() {

            @Override
            public void onCaptureEnd() {
                // TODO Auto-generated method stub
                mLinearLayout.setVisibility(View.VISIBLE);
                if (mScreenShotView.isCaptured()) {
                    btn_right.setText("截取");
                }
            }

            @Override
            public void onCaptureBegin() {
                // TODO Auto-generated method stub
                mLinearLayout.setVisibility(View.GONE);
            }
        };
        mScreenShotView.setOnCaptureListener(onCaptureListener);
    }

    /**
     * 保存截图
     */
    private void saveBitmap() {
        mLinearLayout.setVisibility(View.GONE);
        mScreenShotView.setVisibility(View.GONE);
        View view = mActivity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        screenBitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        if (mScreenShotView.isCaptured()) {
            // 保存截取区域截图
            screenBitmap = Bitmap.createBitmap(screenBitmap, mScreenShotView.getShotX(), mScreenShotView.getShotY() + statusBarHeight,
                    mScreenShotView.getBitmapWidth(), mScreenShotView.getBitmapHeigth());
        } else {
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();  // 已经过时
            // 保存全屏截图
            screenBitmap = Bitmap.createBitmap(screenBitmap, 0, statusBarHeight,
                    screenBitmap.getWidth(), height - statusBarHeight);
        }
        try {
            FileOutputStream fout = new FileOutputStream(bitmapSavePath);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            if (fout != null) {
                fout.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setDrawingCacheEnabled(false);

        mScreenShotView.reset();
        btn_right.setText("截取全屏");
        mLinearLayout.setVisibility(View.VISIBLE);
        mScreenShotView.setVisibility(View.VISIBLE);
        captureView.setVisibility(View.GONE);
        if (captureHideListener != null) {
            captureHideListener.captureHideCallbak();
        }
    }

    public void showOrHideCaptureView() {
        if (captureViewHasAdd) {
            captureView.setVisibility(View.VISIBLE);
        } else {
            mActivity.addContentView(captureView,
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            captureViewHasAdd = true;
        }
    }

    public void setCaptureHideListener(CaptureHideListener captureHideListener) {
        this.captureHideListener = captureHideListener;
    }

    /**
     * 截图图层隐藏
     */
    public interface CaptureHideListener {
        void captureHideCallbak();
    }

}

package cn.kk20.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.kk20.lib.R;

/**
 * @Description 数字时钟TextView
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
public class DigitalClockTextView extends TextView {
    private String mDefaultFormat = "HH:mm:ss";

    private Runnable mTicker;
    private Handler mHandler;

    /**
     * 时钟显示格式
     */
    private String mFormat = "";
    /**
     * 停止计时
     */
    private boolean mTickerStopped = false;

    public DigitalClockTextView(Context context) {
        this(context, null);
    }

    public DigitalClockTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalClockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DigitalClockTextView);
        mFormat = typedArray.getString(R.styleable.DigitalClockTextView_date_format);
        if (TextUtils.isEmpty(mFormat)) {
            mFormat = mDefaultFormat;
        }

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();

        mHandler = new Handler();
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mFormat);
                setText(simpleDateFormat.format(new Date()));
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + 1000;
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * 设置时钟格式
     *
     * @param format
     */
    public void setFormat(String format) {
        mFormat = format;
    }
}

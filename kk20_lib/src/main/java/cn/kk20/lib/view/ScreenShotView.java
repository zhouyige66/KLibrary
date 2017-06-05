package cn.kk20.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.kk20.lib.R;

/**
 * @Description 简单区域截图视图
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
@SuppressLint("NewApi")
public class ScreenShotView extends View {
    private int mWidth, mHeight;
    private int x = 0, y = 0, m = 0, n = 0;
    private Rect desRect, srcRect = null;

    private int bitmapWidth, bitmapHeigth;
    private boolean isCaptured = false;

    private OnCaptureListener onCaptureListener = null;

    public ScreenShotView(Context context) {
        this(context, null);
    }

    public ScreenShotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

        // 关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(175);
        canvas.drawRect(desRect, paint);
        Paint newPaint = new Paint();
        newPaint.setColor(Color.RED);
        newPaint.setAlpha(125);
        newPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        if (srcRect != null) {
            canvas.drawRect(srcRect, newPaint);
            // 画截图指示点
            Bitmap point = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_capture_paint_block);
            canvas.drawBitmap(point, srcRect.left - point.getWidth(), srcRect.top - point.getHeight(),
                    new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, srcRect.right, srcRect.top - point.getHeight(), new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, srcRect.left - point.getWidth(),
                    (srcRect.bottom + srcRect.top - point.getHeight()) / 2, new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, (srcRect.left + srcRect.right - point.getWidth()) / 2, srcRect.top - point.getHeight(),
                    new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, srcRect.right, (srcRect.bottom + srcRect.top - point.getHeight()) / 2,
                    new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, (srcRect.left + srcRect.right - point.getWidth()) / 2, srcRect.bottom,
                    new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, srcRect.left - point.getWidth(), srcRect.bottom, new Paint(Paint.DITHER_FLAG));
            canvas.drawBitmap(point, srcRect.right, srcRect.bottom, new Paint(Paint.DITHER_FLAG));

            // 画线
            Paint linePaint = new Paint();
            linePaint.setDither(true);
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.parseColor("#ff00ffff"));
            linePaint.setStrokeWidth(4);
            canvas.drawLine(srcRect.left, srcRect.top, srcRect.left, srcRect.bottom, linePaint);
            canvas.drawLine(srcRect.left, srcRect.top, srcRect.right, srcRect.top, linePaint);
            canvas.drawLine(srcRect.right, srcRect.top, srcRect.right, srcRect.bottom, linePaint);
            canvas.drawLine(srcRect.left, srcRect.bottom, srcRect.right, srcRect.bottom, linePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        desRect = new Rect(0, 0, mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (isCaptured) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = 0;
                y = 0;
                bitmapWidth = 0;
                bitmapHeigth = 0;
                x = (int) event.getX();
                y = (int) event.getY();

                if (onCaptureListener != null) {
                    onCaptureListener.onCaptureBegin();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                m = (int) event.getX();
                n = (int) event.getY();
                m = m > 0 ? m : 0;
                n = n > 0 ? n : 0;
                m = m > mWidth ? mWidth : m;
                n = n > mHeight ? mHeight : n;

                int width = 0, height = 0;
                if (m >= x) {
                    width = m - x;
                    if (n >= y) {
                        height = n - y;
                        srcRect = new Rect(x, y, m, n);
                    } else {
                        height = y - n;
                        srcRect = new Rect(x, n, m, y);
                    }
                } else {
                    width = x - m;
                    if (n >= y) {
                        height = n - y;
                        srcRect = new Rect(m, y, x, n);
                    } else {
                        height = y - n;
                        srcRect = new Rect(m, n, x, y);
                    }
                }

                refreshView();
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();

                // 判断移动点是否超出屏幕
                upX = upX > 0 ? upX : 0;
                upY = upY > 0 ? upY : 0;
                upX = upX > mWidth ? mWidth : upX;
                upY = upY > mHeight ? mHeight : upY;
                if (upX > x) {
                    bitmapWidth = upX - x;
                } else {
                    bitmapWidth = x - upX;
                    x = upX;
                }

                if (upY > y) {
                    bitmapHeigth = upY - y;
                } else {
                    bitmapHeigth = y - upY;
                    y = upY;
                }

                if (bitmapHeigth > 0 && bitmapHeigth > 0) {
                    isCaptured = true;
                } else {
                    isCaptured = false;
                }

                if (onCaptureListener != null) {
                    onCaptureListener.onCaptureEnd();
                }
        }

        return true;
    }

    public void refreshView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void reset() {
        x = y = m = n = bitmapWidth = bitmapHeigth = 0;
        desRect = new Rect(0, 0, mWidth, mHeight);
        srcRect = null;
        isCaptured = false;
        refreshView();
    }

    public int getShotX() {
        return x;
    }

    public int getShotY() {
        return y;
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeigth() {
        return bitmapHeigth;
    }

    public void setBitmapHeigth(int bitmapHeigth) {
        this.bitmapHeigth = bitmapHeigth;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setOnCaptureListener(OnCaptureListener onCaptureListener) {
        this.onCaptureListener = onCaptureListener;
    }

    public interface OnCaptureListener {
        public void onCaptureBegin();

        public void onCaptureEnd();
    }
}

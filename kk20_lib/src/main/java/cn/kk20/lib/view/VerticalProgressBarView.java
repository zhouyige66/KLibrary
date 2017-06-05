package cn.kk20.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description 简单垂直柱形图
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
@SuppressLint("DrawAllocation")
public class VerticalProgressBarView extends View {
    private int progress = 0;
    private int max = 100;
    private int width = 0;
    private int height = 0;
    private int startX = 0;
    private int startY = 0;
    private Paint paint;

    private String text = "";

    public VerticalProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);// 设置抗锯齿
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("调用onDraw()" + progress + "/" + text);

        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(10);// 设置画笔宽度
        canvas.drawRect(startX, startY, width, height, paint);

        paint.setColor(Color.parseColor("#00CCFF"));
        float top = (float) (height / 2 + (max - progress)*height*0.5 / max);
        System.out.print("起始高度："+height/2);
        System.out.print("计算后top："+top);
        canvas.drawRect(startX, top, width, height, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(25);

        // 计算文字所在矩形，可以得到宽高
        String str = progress + "%";
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        float x = (width - w) / 2;
        float y = top - 10;
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(str, x, y, paint);

        // 画人数
        Rect rect1 = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect1);
        int w1 = rect1.width();
        float x1 = (width - w1) / 2;
        float y1 = top - h - 20;
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x1, y1, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    public int getViewWidth() {
        return width;
    }

    public void setViewWidth(int width) {
        this.width = width;
    }

    public int getViewHeight() {
        return height;
    }

    public void setViewHeight(int height) {
        this.height = height;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setText(String text) {
        this.text = text;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}

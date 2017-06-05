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
 * @Description 简单水平柱形图
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
@SuppressLint("DrawAllocation")
public class HorizontalProgressBarView extends View {
    private int progress = 60;
    private String text="";
    private int max = 100;
    private int width = 0;
    private int height = 0;
    private int startX = 0;
    private int startY = 0;
    private Paint paint;

    public HorizontalProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(10);
        canvas.drawRect(startX, startY, width, height, paint);

        paint.setColor(Color.parseColor("#00CCFF"));
        canvas.drawRect(startX, startY, ((float) 4 * width * progress / max / 5),
                height, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(25);

        // 计算文字所在矩形，可以得到宽高
        String str =text;
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        float lengh = progress * 4 * width / max / 5;
        float x = lengh + 10;
//        if (lengh - w - 10  > 0) {
//            x = lengh - w -10 ;
//        }else{
//            x = lengh + 10;
//        }
        float y = (height + h) / 2;
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(str, x, y, paint);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

}

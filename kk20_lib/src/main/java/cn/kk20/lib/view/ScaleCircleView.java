package cn.kk20.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description 简单圆形比例图
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
public class ScaleCircleView extends View {
    private Paint mPaint;
    private int radius = 0;
    private int width = 0, height = 0;

    private int percent = 52;


    public ScaleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 设置是否抗锯齿
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.parseColor("#FFA2A2"));
        canvas.drawCircle(width / 2, height / 2, radius, mPaint);

        // 画扇形
        mPaint.setColor(Color.parseColor("#19E5E6"));
        int x = width / 2 - radius;
        int y = height / 2 - radius;
        canvas.drawArc(new RectF(x, y, x + radius * 2, y + radius * 2), -90, 360 * percent / 100, true, mPaint);

//        mPaint.setColor(Color.parseColor("#FFA2A2"));
//        if(percent<50){
//            float dy = (float)Math.abs(Math.tan(180-percent*360/100)*20);
//            Log.i("zzy", "计算值为：" + dy);
//            canvas.drawArc(new RectF(x-20, y+dy, x-20 + radius * 2, y+dy + radius * 2), 360*percent/100-90, (100-percent)*360/100, true, mPaint);
//        }else{
//            float dy = (float)Math.abs(Math.tan(180+360 * percent / 200)*20);
//            Log.i("zzy", "计算值为：" + dy);
//            canvas.drawArc(new RectF(x-20, y-dy, x-20 + radius * 2, y-dy + radius * 2), 360*percent/100-90, (100-percent)*360/100, true, mPaint);
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        radius = w / 2 > h / 2 ? h / 2-20 : w / 2-20;
    }

    public void setPercent(int percent) {
        this.percent = percent;

        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}

package cn.kk20.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import cn.kk20.lib.R;

/**
 * @Description 可翻页画板
 * @Author kk20
 * @Date 2017/5/15
 * @Version V1.0.0
 */
public class KPaintView extends View {
    private static final float TOUCH_TOLERANCE = 4;
    private static int[] paintColor = {Color.RED, Color.GREEN, Color.BLUE, Color.BLACK,
            Color.YELLOW, Color.CYAN};
    private static int[] paintSize = {5, 10, 15, 20, 25};

    /*定义部分*/
    @IntDef({MODE_PEN, MODE_ERASER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PaintMode {
    }

    public static final int MODE_PEN = 0;
    public static final int MODE_ERASER = 1;

    private Canvas mCanvas = null;
    private Bitmap mBitmap = null;
    private Paint mBitmapPaint, mPaint;
    private Bitmap penBitmap, eraserBitmap;
    private int mPaintColor = Color.RED;
    private int mPaintStrokeWidth = 1;
    private int mPaintMode = MODE_PEN;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private boolean hasMeasured = false;
    private float mX, mY;
    private boolean isMoving = false;
    private Path mPath = null;//当前绘画路径
    private DrawPath mDrawPath;//当前绘画属性
    private ArrayList<DrawPath> savePathList;
    private ArrayList<DrawPath> deletePathList;

    //每一页数据集合
    private ArrayList<PageDrawData>pageDrawDatas;

    public KPaintView(Context c) {
        this(c, null);
    }

    public KPaintView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
    }

    public KPaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i("kk20", "onMeasure量尺寸宽度：" + MeasureSpec.getSize(widthMeasureSpec));
        Log.i("kk20", "onMeasure量尺寸高度：" + MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("kk20", "onSizeChanged量尺寸宽度：" + w);
        Log.i("kk20", "onSizeChanged量尺寸高度：" + h);

        // 暂时这样处理尺寸变化引起的画布重绘问题
        if (!hasMeasured) {
            hasMeasured = true;
            mBitmapWidth = w;
            mBitmapHeight = h;
            reset();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint); // 显示旧的画布
        }
        if (mPath != null) {
            if (mPaintMode == MODE_PEN) {
                mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
            } else {
                mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
            }
            // 实时的显示
            canvas.drawPath(mPath, mPaint);

            // 移动时，显示画笔图标
            if (this.isMoving) {
                // 设置画笔的图标
                if (mPaintMode == MODE_PEN) {
                    canvas.drawBitmap(penBitmap, this.mX, this.mY - penBitmap.getHeight(),
                            mBitmapPaint);
                } else {
                    canvas.drawBitmap(eraserBitmap, this.mX, this.mY - eraserBitmap.getHeight(),
                            mBitmapPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x, y);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void init() {
        // 关闭硬件加速(不然当模式为擦除模式时有问题)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeWidth(mPaintStrokeWidth);

        penBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_paint_pen);
        eraserBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_paint_eraser);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        savePathList = new ArrayList<DrawPath>();
        deletePathList = new ArrayList<DrawPath>();
    }

    private void reset() {
        mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap); // 所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    private void resetPaint() {
        if (mPaintMode == MODE_PEN) {
            mPaint.setColor(mPaintColor);
            mPaint.setStrokeWidth(mPaintStrokeWidth);
        } else {
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(50);
        }
    }

    private void touch_start(float x, float y) {
        mPath = new Path();
        mDrawPath = new DrawPath();
        mDrawPath.path = mPath;
        mDrawPath.paint = mPaint;
        mPath.reset();// 清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        this.isMoving = false;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);// 源代码是这样写的，可是我没有弄明白，为什么要这样？
        }
        mX = x;
        mY = y;
        this.isMoving = true;
    }

    private void touch_up(float x, float y) {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        savePathList.add(mDrawPath);
        mPath = null;
        this.isMoving = false;
    }

    /**
     * 设置画笔模式（0：画笔模式，1：擦除模式）
     *
     * @param mode
     */
    public void setPaintMode(@PaintMode int mode) {
        mPaintMode = mode;
        resetPaint();
    }

    /**
     * 选择画笔大小
     *
     * @param index
     */
    public void selectPaintSize(int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > paintSize.length) {
            mPaintStrokeWidth = index;
        } else {
            mPaintStrokeWidth = paintSize[index];
        }
        resetPaint();
    }

    /**
     * 自定义画笔尺寸
     *
     * @param size
     */
    public void setPaintSize(int size) {
        mPaintStrokeWidth = size;
        resetPaint();
    }

    /**
     * 选择画笔颜色
     *
     * @param index
     */
    public void selectPaintColor(int index) {
        if (index < 0) {
            index = 0;
        }
        if (index > paintColor.length) {
            mPaintColor = index;
        } else {
            mPaintColor = paintColor[index];
        }
        resetPaint();
    }

    /**
     * 自定义画笔颜色
     *
     * @param color
     */
    public void setPaintColor(@ColorInt int color) {
        mPaintColor = color;
        resetPaint();
    }

    /**
     * 撤销的核心思想就是将画布清空，将保存下来的Path路径最后一个移除掉，重新将路径画在画布上面。
     */
    public void undo() {
        if (savePathList != null && savePathList.size() > 0) {
            // 清空画布
            reset();

            // 将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePathList.get(savePathList.size() - 1);
            deletePathList.add(drawPath);
            savePathList.remove(savePathList.size() - 1);

            // 将路径保存列表中的路径重绘在画布上
            Iterator<DrawPath> iter = savePathList.iterator(); // 重复保存
            while (iter.hasNext()) {
                DrawPath dp = iter.next();
                mCanvas.drawPath(dp.path, dp.paint);
            }
            invalidate();// 刷新
        }
    }

    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)，
     * 然后从redo的列表里面取出最顶端对象，画在画布上面即可。
     */
    public void redo() {
        if (deletePathList.size() > 0) {
            // 将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePathList.get(deletePathList.size() - 1);
            savePathList.add(dp);
            // 将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            // 将该路径从删除的路径列表中去除
            deletePathList.remove(deletePathList.size() - 1);
            invalidate();
        }
    }

    /**
     * 清空的主要思想就是初始化画布，将保存路径的两个List清空
     */
    public void removeAllPaint() {
        savePathList.clear();
        deletePathList.clear();
        reset();
        invalidate();
    }

    /**
     * 返回画布模式
     *
     * @return
     */
    @PaintMode
    public int getPaintMode() {
        return mPaintMode;
    }

    /**
     * 保存所绘图形，返回绘图文件的存储路径
     */
    public String saveBitmap() {
        // 获得系统当前时间，并以该时间作为文件名
        String str = UUID.randomUUID().toString() + ".jpg";
        String paintFilePath = "";
        File dir = new File(Environment.getExternalStorageDirectory() + "/Paint/");
        File file = new File(Environment.getExternalStorageDirectory() + "/Paint/", str);
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            if (file.exists()) {
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            paintFilePath = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paintFilePath;
    }

    // 路径对象
    class DrawPath {
        Path path;
        Paint paint;
    }

    // 每一页绘图数据
    class PageDrawData {
        Bitmap backgroungBitmap;
        ArrayList<DrawPath> savePathList;
        ArrayList<DrawPath> deletePathList;
    }
}

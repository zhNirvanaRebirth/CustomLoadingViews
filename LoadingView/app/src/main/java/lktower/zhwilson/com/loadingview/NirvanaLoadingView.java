package lktower.zhwilson.com.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Created by zhwilson on 2016/10/9.
 * 模仿别人的炫酷的进度条
 * <p/>
 * obtainStyledAttributes 四个参数的相关解释：http://www.cnblogs.com/angeldevil/p/3479431.html
 */
public class NirvanaLoadingView extends View {
    //进度条内外层默认间隔
    private final float defaultProgressPadding;
    //转动叶片外圆默认线宽
    private final float defaultLineWidth;
    //转动叶片和外圆默认间隔
    private final float defaultFanPadding;
    //进度条内外层间隔
    private float progressPadding;
    //转动叶片尺寸
    private int fanSize;
    //转动叶片外圆线宽
    private float lineWidth;
    //转动叶片和外圆的间隔
    private float fanPadding;
    //控件的宽度
    private int width;
    //控件的高度
    private int height;
    //进度条圆角半径
    private int roundRadius;
    //进度条宽度
    private int loadingW;
    //进度条高度
    private int loadingH;
    private float maxProgress = 100;
    //当前进度
    private float progress = 0;

    private Bitmap mLeafBitmap;
    private int mLeafWidth, mLeafHeight;
    private Paint mBitmapPaint;


    public NirvanaLoadingView(Context context) {
        this(context, null);
    }

    public NirvanaLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.ThemeNirvanaLoadingViewStyle);
    }

    public NirvanaLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fanSize = BitmapFactory.decodeResource(context.getResources(), R.drawable.fan).getWidth();
        defaultProgressPadding = dp2px(5);
        defaultLineWidth = dp2px(2);
        defaultFanPadding = dp2px(3);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NirvanaLoadingViewStyle, defStyleAttr, 0);
        progressPadding = typedArray.getDimension(R.styleable.NirvanaLoadingViewStyle_inner_progress_padding, defaultProgressPadding);
        lineWidth = typedArray.getDimension(R.styleable.NirvanaLoadingViewStyle_fan_circle_line_width, defaultLineWidth);
        fanPadding = typedArray.getDimension(R.styleable.NirvanaLoadingViewStyle_fan_circle_padding, defaultFanPadding);
        typedArray.recycle();
        mLeafInfos = new LeafFactory().generateLeafs(6);
        mLeafBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.leaf)).getBitmap();
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) (fanSize + fanPadding * 2 + lineWidth * 2 + progressPadding);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) (fanSize + fanPadding * 2 + lineWidth * 2);
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (isWidth) {
                result = Math.max(result, size);
            } else {
                result = Math.min(result, size);
            }
        }

        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        loadingW = width < getSuggestedMinimumWidth() ? getSuggestedMinimumWidth() : width;
        loadingH = height > getSuggestedMinimumHeight() ? getSuggestedMinimumHeight() : height;
        roundRadius = loadingW > loadingH ? loadingH / 2 : loadingW / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#fdd052"));
        canvas.translate(width / 2, height / 2);

        RectF rectF = new RectF(-loadingW / 2, -loadingH / 2, loadingW / 2, loadingH / 2);
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#64FFFFFF"));
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, rectPaint);

        Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.parseColor("#ffa800"));
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        float currentProgress = (loadingW - progressPadding * 2) * (progress / maxProgress);
        float arcRadius = roundRadius - progressPadding;
        RectF arcRectF = new RectF(-(loadingW / 2 - progressPadding), -(loadingH / 2 - progressPadding), -(loadingW / 2 - progressPadding - arcRadius * 2), loadingH / 2 - progressPadding);
        if (currentProgress >= arcRadius) {//超过了扇形区域
            canvas.drawArc(arcRectF, 90, 180, false, progressPaint);
            canvas.drawRect(-(loadingW / 2 - roundRadius), -(loadingH / 2 - progressPadding), -(loadingW / 2 - roundRadius) + currentProgress - arcRadius, loadingH / 2 - progressPadding, progressPaint);
            drawLeafs(canvas);
        } else {
            double angle = (180.0 / Math.PI) * Math.acos((arcRadius - currentProgress) / arcRadius);
            canvas.drawArc(arcRectF, (float) (180 - angle), (float) angle * 2, false, progressPaint);
            drawLeafs(canvas);
        }

        Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setColor(Color.WHITE);
        ringPaint.setStrokeWidth(lineWidth);
        ringPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(loadingW / 2 - roundRadius, 0, roundRadius - lineWidth, ringPaint);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#fdd052"));
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(loadingW / 2 - roundRadius, 0, roundRadius - lineWidth, circlePaint);

        Rect bitmap = new Rect(0, 0, fanSize, fanSize);
        Rect dist = new Rect((int) (loadingW / 2 - fanSize - fanPadding - lineWidth), -fanSize / 2, (int) (loadingW / 2 - fanPadding - lineWidth), fanSize / 2);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fan), bitmap, dist, new Paint());
        postInvalidate();
    }

    private float dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    private List<Leaf> mLeafInfos;
    // 叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 3000;
    // 叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;

    // 叶子飘动一个周期所花的时间
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    // 叶子旋转一周需要的时间
    private long mLeafRotateTime = LEAF_ROTATE_TIME;

    /**
     * 绘制叶子
     *
     * @param canvas
     */
    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Leaf leaf = mLeafInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime);
                // 根据时间计算旋转角度
                canvas.save();
                // 通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                if (leaf.positionX < (progress/maxProgress)*loadingW - loadingW/2)
                    continue;
                float transX = leaf.positionX;
                float transY = leaf.positionY;
                matrix.postTranslate(transX, transY);
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime)
                        / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDir == 0 ? angle + leaf.rotateAngle : -angle
                        + leaf.rotateAngle;
                matrix.postRotate(rotate, transX
                        + mLeafWidth / 2, transY + mLeafHeight / 2);
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            } else {
                continue;
            }
        }
    }

    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {//重新设置叶子开始绘制时间，以便不停有叶子飞出
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }

        float fraction = (float) intervalTime / mLeafFloatTime;//计算出已完成的百分比
        leaf.positionX = (int) (loadingW / 2 - loadingW * fraction);//因为我们的坐标点的移动，这里需要用进度条的1/2, 同时控制叶子在指定宽度范围内绘制
        leaf.positionY = getLocationY(leaf);
    }

    // 通过叶子信息获取当前叶子的Y值
    private int getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / loadingW);
        mMiddleAmplitude = (int) (roundRadius - progressPadding - mLeafHeight);
        float a = mMiddleAmplitude;
        switch (leaf.amplitude) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case LARGE:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }

        return leaf.floatType == 0 ? (int) (a * Math.sin(w * leaf.positionX)) : (int) (a * Math.cos(w * leaf.positionX));
    }
}

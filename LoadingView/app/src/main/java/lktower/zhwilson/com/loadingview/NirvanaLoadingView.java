package lktower.zhwilson.com.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

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
    private int roundRadius;

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
        roundRadius = width > height ? height / 2 : width / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);
        canvas.drawColor(Color.YELLOW);

        RectF rectF = new RectF(-width / 2, -height / 2, width / 2, height / 2);
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, rectPaint);
    }

    private float dp2px(float dp) {
        float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(float sp) {
        float scale = getResources().getDisplayMetrics().scaledDensity;
        return scale * sp;
    }
}

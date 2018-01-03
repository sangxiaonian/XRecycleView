package sang.com.freerecycleview.view.refrush;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import sang.com.freerecycleview.utils.DeviceUtils;

/**
 * 作者： ${PING} on 2017/12/27.
 * 默认情况下的刷新控件
 */

public class DefaultLoadMoreView extends BaseView {


    private Paint mPaint;
    private Path mPath;
    private String dragText = "下拉刷新";
    private String upText = "松手刷新";
    private Rect textRect;
    private float gap;
    private ValueAnimator loadAnmition,dragAnimation;
    private int startRoat,dragRoat;
    private String drawText="";


    public DefaultLoadMoreView(Context context) {
        super(context);
        initView(context, null);
    }

    public DefaultLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public DefaultLoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    protected void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        dragRoat=180;
        drawText=dragText;
        textRect = new Rect();
        gap = DeviceUtils.dip2px(context, 5);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(DeviceUtils.dip2px(context, 15));
        int color = Color.parseColor("#B8B7B8");
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

        loadAnmition = ValueAnimator.ofInt(0, 12);
        loadAnmition.setDuration(1000);
        loadAnmition.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                startRoat = value * 30;
                postInvalidate();
            }
        });
        loadAnmition.setInterpolator(new LinearInterpolator());
        loadAnmition.setRepeatCount(Integer.MAX_VALUE);

        dragAnimation = ValueAnimator.ofInt(0, 180);
        dragAnimation.setDuration(200);
        dragAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                dragRoat = value ;
                postInvalidate();
            }
        });
        dragAnimation.setInterpolator(new LinearInterpolator());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, heitht;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize;
//            width = (getPaddingLeft() + DeviceUtils.dip2px(getContext(), 40) + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            heitht = heightSize;
        } else {
            heitht = (getPaddingTop() + DeviceUtils.dip2px(getContext(), 40) + getPaddingBottom());
        }
        setMeasuredDimension(width, heitht);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        Bitmap bitmap = null;
        int size = standSize / 2;
        if (state == DRAGREFRUSH) {
            bitmap = ShapFactory.creatArrows(size, size, 12, dragRoat,mPath, mPaint);
        } else if (state == UPREFRUSH) {
            bitmap = ShapFactory.creatArrows(size, size, 12, dragRoat, mPath, mPaint);
        } else   {
            bitmap = ShapFactory.creatLoad(size, size, mPaint,-startRoat);
        }
        if (bitmap != null) {
            int left = (getMeasuredWidth() - bitmap.getWidth() - textRect.width()) / 2;
            int top = (getMeasuredHeight() - bitmap.getHeight()) / 2;
            canvas.drawBitmap(bitmap, left, top, mPaint);
            bitmap.recycle();
            mPaint.getTextBounds(drawText, 0, drawText.length(), textRect);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            canvas.drawText(drawText, left + bitmap.getWidth() + gap, (getMeasuredHeight() - (fontMetrics.top + fontMetrics.bottom)) / 2, mPaint);
        }

        canvas.restore();
    }




    @Override
    public void onCancleDrag() {
        if (state == UPREFRUSH) {//当前状态是松手刷新 则装更改为刷新
            changeStated(REFRUSH);
        }
        sprinBack.moveTo(getFinishValue());
    }



    /**
     * 松手刷新
     */
    public void showUPRefrush() {
        state = UPREFRUSH;
        drawText = upText;
        dragAnimation.setIntValues(dragRoat,0);
        dragAnimation.start();
    }

    /**
     * 下拉刷新
     */
    public void showDrag() {
        state = DRAGREFRUSH;
        drawText = dragText;
        dragAnimation.setIntValues(dragRoat,180);
        dragAnimation.start();
    }

    /**
     * 正在刷新
     */
    public void startLoad() {
        state = REFRUSH;
        loadAnmition.start();
        drawText = "";
    }
    @Override
    public void refrushfail() {
        super.refrushfail();
        drawText = "";
    }

    @Override
    public void refrushSuccess() {
        super.refrushSuccess();
        drawText = "";
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancle();
    }

    @Override
    public void cancle() {
        super.cancle();
        loadAnmition.cancel();
        dragAnimation.cancel();
    }
}

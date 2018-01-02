package sang.com.freerecycleview.view.refrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import sang.com.freerecycleview.utils.DeviceUtils;
import sang.com.freerecycleview.utils.FRLog;

import static android.R.attr.bitmap;

/**
 * 作者： ${PING} on 2017/12/27.
 * 默认情况下的刷新控件
 */

public class DefaultRefrushView extends BaseView {


    private int mWidth;
    private int mHeight;
    private ShapFactory factory;
    private Paint mPaint;
    private Path mPath;


    public DefaultRefrushView(Context context) {
        super(context);
        initView(context, null);
    }

    public DefaultRefrushView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public DefaultRefrushView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    protected void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
        int color = Color.parseColor("#B8B7B8");
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
        factory = ShapFactory.getInstance(mPath, mPaint, context);


        factory = ShapFactory.getInstance(mPath, mPaint, context);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mWidth == 0&&mHeight==0) {
            mHeight = (int) getStandSize();
            mWidth=w;
            FRLog.i(w+">>>"+h+">>>"+standSize);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = null;
        if (state == DRAGREFRUSH) {
            bitmap = factory.creatArrows(mHeight, mHeight, 12);
        } else if (state == UPREFRUSH) {
            bitmap = factory.creatArrows(mHeight, mHeight, 12);
        } else if (state == REFRUSH) {
            bitmap = factory.creatShap(mHeight, mHeight);
        }
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            bitmap.recycle();
        }
    }


    //拖动事件更改
    @Override
    public void onDrag(float dragX, float dragY) {
        float current = 0;
        float drag = 0;
        float v = 1;
        if (isTop) {
            if (isVertical) {
                current = getMeasuredHeight();
                drag = dragY;
                v = Math.abs(current / standSize);
            } else {
                current = getMeasuredWidth();
                drag = dragX;
                v = Math.abs(current / standSize);
            }
        } else {
            if (isVertical) {
                current = getMeasuredHeight();
                drag = -dragY;
                v = Math.abs(current / standSize);
            } else {
                current = getMeasuredWidth();
                drag = -dragX;
                v = Math.abs(current / standSize);
            }
        }
        v = v < 1 ? 1 : v;
        if (Math.abs(current) > Math.abs(current + drag)) {
            v = 1;
        }
        float value = current + drag / v;


        if (!canChangeStated()){
            value=value<standSize?standSize:value;
        }
        changeHeight(isVertical, (int) (value));

        if (canChangeStated()) {
            if (getMeasuredHeight() < standSize && state != DRAGREFRUSH) {
                state = DRAGREFRUSH;
            } else if (getMeasuredHeight() > standSize && state != UPREFRUSH) {
                state = UPREFRUSH;
            }
        }
    }

    @Override
    public void onCancleDrag() {
        super.onCancleDrag();
        changeStated();
        sprinBack.moveTo(getFinishValue());
    }

    /**
     * 更改当前状态
     */
    private void changeStated() {
        if (state==UPREFRUSH){//当前状态是松手刷新 则装更改为刷新
            state=REFRUSH;
        }
    }


}

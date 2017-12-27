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

/**
 * 作者： ${PING} on 2017/12/27.
 * 默认情况下的刷新控件
 */

public class DefaultRefrushView extends View {


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

    private void initView(Context context, AttributeSet attrs) {


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
            width = (getPaddingLeft() + DeviceUtils.dip2px(getContext(), 40) + getPaddingRight());
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
        if (mWidth == 0) {
            mHeight = mWidth = Math.max(getMeasuredWidth(), getMeasuredHeight());
        }
        Bitmap bitmap = factory.creatArrows(mWidth, mHeight, 12);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            bitmap.recycle();
        }
    }


}

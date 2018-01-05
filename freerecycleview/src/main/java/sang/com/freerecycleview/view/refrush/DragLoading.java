package sang.com.freerecycleview.view.refrush;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import sang.com.freerecycleview.utils.FRLog;

import static android.R.attr.bitmap;
import static android.R.attr.start;
import static android.R.attr.strokeWidth;
import static android.R.attr.value;

/**
 * 作者： ${PING} on 2018/1/5.
 */

public class DragLoading extends BaseView {

    private int startRoat;
    private Path mPath;
    private Paint mPaint;
    private ValueAnimator loadAnmition;
    private RectF drawRect;
    private Bitmap mBitmap;

    public DragLoading(Context context) {
        super(context);
    }

    public DragLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context, AttributeSet attrs) {
        super.initView(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();


        loadAnmition = ValueAnimator.ofInt(0, 9);
        loadAnmition.setDuration(1000);
        loadAnmition.setInterpolator(new LinearInterpolator());
        loadAnmition.setRepeatCount(Integer.MAX_VALUE);
        loadAnmition.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                startRoat = 0;
            }
        });
        loadAnmition.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                startRoat = value;
                postInvalidate();
            }
        });
        drawRect = new RectF();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();


        drawRect.left = (measuredWidth - getStandSize()) / 2;
        drawRect.top = (measuredHeight - getStandSize()) / 2;
        drawRect.right = (measuredWidth + getStandSize()) / 2;
        drawRect.bottom = (measuredHeight + getStandSize()) / 2;
        mPath.reset();
        float v = measuredHeight * 1.0f / getStandSize();
        mPath.addArc(drawRect, -120, (float) Math.ceil(v * 360));

        mPath.lineTo(measuredWidth / 2, measuredHeight / 2);
        mPath.close();

        canvas.save();
        if (v<=1) {
            canvas.clipPath(mPath);
        }
        drawLoading(canvas);
        canvas.restore();


        canvas.drawPoint(measuredWidth/2,measuredHeight/2,mPaint);
    }

    private void drawLoading(Canvas canvas) {
        float strokeWidth = mPaint.getStrokeWidth();
        int mWidth = (int) (getStandSize() / 2);
        int gap = mWidth/13;
        mPaint.setStrokeWidth(gap);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);
        int count = 9;
        int offsetRoat = startRoat % count;
        int cellRadio = 360 / count;
        for (int i = 0; i < count; i++) {
            canvas.save();
            canvas.rotate(cellRadio *( i + offsetRoat), getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            mPaint.setAlpha(255 * (count - i) / count);
            canvas.drawLine(getMeasuredWidth() / 2, gap+drawRect.top+drawRect.height()/4, getMeasuredWidth() / 2, 4*gap+drawRect.top+drawRect.height()/4, mPaint);
            canvas.restore();
        }
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAlpha(255);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int size = (int) (getStandSize() / 2);
         mBitmap = ShapFactory.creatLoad(size, size, mPaint, 0);

    }

    @Override
    public void onCancleDrag() {
        if (state == UPREFRUSH) {
            changeStated(REFRUSH);
        }
        sprinBack.moveTo(getFinishValue());
    }


    @Override
    public void startDrag() {
        super.startDrag();

    }


    @Override
    public void showDrag() {
        super.showDrag();
        loadAnmition.cancel();
    }

    @Override
    public void startLoad() {
        super.startLoad();
        loadAnmition.start();
    }
}

package sang.com.freerecycleview.view.refrush;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import sang.com.freerecycleview.utils.DeviceUtils;

import static android.R.attr.bitmap;

/**
 * Description：
 *
 * @Author：桑小年
 * @Data：2016/12/8 17:38
 */
public class ShapFactory {


    /**
     * 绘制一个箭头
     *
     * @param mWidth  控件高度
     * @param mHeight 控件宽
     * @param den     箭头杆空格数量
     * @param dragRoat
     *@param mPath
     * @param mPaint   @return 一个向下的箭头
     */
    public static Bitmap creatArrows(int mWidth, int mHeight, int den, int dragRoat, Path mPath, Paint mPaint) {
        if (mWidth <= 0 || mHeight <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.rotate(dragRoat,mWidth/2,mHeight/2);
        mPath.reset();
        mPaint.setStyle(Paint.Style.FILL);
        int amount = 0;
        for (int i = 1; i <= den; i++) {
            amount += i;
        }
        float start = mHeight * 2 / 3;
        float l = (float) ((mHeight - start) / (Math.sin(60 * Math.PI / 180) * 2));
        mPath.moveTo(mWidth / 2, mHeight);
        mPath.lineTo(mWidth / 2 - l, start);
        mPath.lineTo(mWidth / 2 + l, start);
        mPath.lineTo(mWidth / 2, mHeight);
        int count = 0;
        int last = 0;
        float left = mWidth / 2 - l / 2;
        float right = mWidth / 2 + l / 2;
        for (int i = den; i > 0; i--) {
            last = count;
            count += i;
            if (i % 2 == 0) {
                float top = start - start * last / (amount);
                float boom = start - start * count / (amount);
                mPath.addRect(left, top, right, boom, Path.Direction.CCW);
            }
        }
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        return bitmap;
    }

    /**
     * 绘制一个方块
     *
     * @param mWidth  宽
     * @param mHeight 高
     * @param mPath
     * @param mPaint
     * @return 一个四方形的Bitmap
     */
    public static Bitmap creatShap(int mWidth, int mHeight, Path mPath, Paint mPaint) {
        if (mWidth <= 0 || mHeight <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPath.addRect(0, 0, mWidth, mHeight, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制菊花加载
     *
     * @param mWidth    宽
     * @param mHeight   高
     * @param startRoat
     * @return 一个四方形的Bitmap
     */
    public static Bitmap creatLoad(int mWidth, int mHeight, Paint mPaint, int startRoat) {
        if (mWidth <= 0 || mHeight <= 0) {
            return null;
        }
        float strokeWidth = mPaint.getStrokeWidth();
        int gap = mWidth/13;

        mPaint.setStrokeWidth(gap);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        int count = 9;
        int offsetRoat = startRoat % count;
        int cellRadio = 360 / count;
        for (int i = 0; i < count; i++) {
            canvas.save();
            canvas.rotate(cellRadio *( i + offsetRoat), mWidth / 2, mHeight / 2);
            mPaint.setAlpha(255 * (count - i) / count);
            canvas.drawLine(mWidth / 2, gap, mWidth / 2, 4*gap, mPaint);
            canvas.restore();
        }

        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAlpha(255);
        return bitmap;
    }

    /**
     * 绘制对号
     *
     * @param mWidth
     * @param mHeight
     * @param mPaint
     * @param mPath
     * @return
     */
    public static Bitmap creatCorrect(int mWidth, int mHeight, Paint mPaint, Path mPath) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        int radius = mWidth / 2 - 2;
        mPath.moveTo(mWidth / 8, mHeight / 3);
        mPath.lineTo(mWidth / 4, mHeight / 4);
        mPath.lineTo(mWidth * 3 / 8, mHeight / 3);
        mPath.moveTo(mWidth * 5 / 8, mHeight / 3);
        mPath.lineTo(mWidth * 3 / 4, mHeight / 4);
        mPath.lineTo(mWidth * 7 / 8, mHeight / 3);

        mPath.moveTo(mWidth / 4, mHeight * 3 / 5);
        mPath.cubicTo(mWidth / 4, mHeight * 3 / 5, mWidth / 2, mHeight, mWidth * 3 / 4, mHeight * 3 / 5);
        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        mPath.addCircle(centerX, centerY, radius, Path.Direction.CCW);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }

    /**
     * 绘制错号
     *
     * @param mWidth
     * @param mHeight
     * @param mPaint
     * @param mPath
     * @return
     */
    public static Bitmap creatError(int mWidth, int mHeight, Paint mPaint, Path mPath) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        mPath.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPath.moveTo(mWidth / 3, mHeight / 3);
        mPath.lineTo(mWidth * 2 / 3, mHeight * 2 / 3);
        mPath.moveTo(mWidth * 2 / 3, mHeight / 3);
        mPath.lineTo(mWidth / 3, mHeight * 2 / 3);
        canvas.drawPath(mPath, mPaint);
        return bitmap;
    }


    public static Bitmap creatLoadNoMore(int measuredWidth, int size, Paint mPaint, String drawText) {
        if (measuredWidth <= 0 || size <= 0|| TextUtils.isEmpty(drawText)) {
            return null;
        }
        float strokeWidth = mPaint.getStrokeWidth();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, size, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Rect textRect=new Rect();
        mPaint.getTextBounds(drawText, 0, drawText.length(), textRect);
        int left = (measuredWidth  - textRect.width()) / 2;
        int top = (size - bitmap.getHeight()) / 2;
        canvas.drawBitmap(bitmap, left, top, mPaint);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        canvas.drawText(drawText, left , (size - (fontMetrics.top + fontMetrics.bottom)) / 2, mPaint);
        canvas.drawLine(0,size/2,left-textRect.width()/drawText.length(),size/2,mPaint);
        canvas.drawLine(left+textRect.width()/drawText.length()+textRect.width(),size/2,measuredWidth,size/2,mPaint);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAlpha(255);

        return bitmap;
    }
}
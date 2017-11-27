package sang.com.xrecycleview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class CalendarHeadView extends View {

    private String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    private int weekColor;
    private TextPaint mPaint;
    private float weekSize;
    private int mWidth;
    private final int COL = 7;
    private PointF cellPoint;
    private int mHeight;


    public CalendarHeadView(Context context) {
        this(context, null, 0);
    }

    public CalendarHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        mPaint = new TextPaint();
        cellPoint = new PointF();
        weekColor = Color.parseColor("#29AEFF");
        weekSize = 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWeeks(canvas);
    }

    //绘制星期天
    private void drawWeeks(Canvas canvas) {
        mPaint.setColor(weekColor);
        float startX = 0;
        float startY = 0;
        canvas.save();
        mPaint.setTextSize(weekSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        for (int i = 0; i < weeks.length; i++) {
            drawText(canvas, startX, startY, weeks[i]);
            startX += cellPoint.x;
        }
        canvas.restore();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        cellPoint.x = mWidth / COL;//单位宽度
        cellPoint.y = h;//单位高度
        if (weekSize==0) {
            weekSize = Math.min(cellPoint.x, cellPoint.y) / 3;
        }
    }

    private void drawText(Canvas canvas, float startX, float startY, String currentDay) {
        canvas.save();
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float y = startY + (cellPoint.y - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        float x = (cellPoint.x) / 2 + startX;
        canvas.translate(x, y);
        mPaint.setTextSize(weekSize);
        mPaint.setColor(weekColor);
        canvas.drawText(currentDay, 0, 0, mPaint);
        canvas.restore();
    }

    public void setTextSize(int weekSize) {
        this.weekSize = weekSize;
    }

    public void setTextColor(int textColor) {
        this.weekColor = textColor;
    }
}

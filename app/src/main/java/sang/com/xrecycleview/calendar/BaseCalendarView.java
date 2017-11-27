package sang.com.xrecycleview.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Map;

import static sang.com.xrecycleview.calendar.CalendarMarkBean.DONE;
import static sang.com.xrecycleview.calendar.CalendarMarkBean.UNDO;


/**
 * 作者： ${桑小年} on 2017/8/18.
 * 努力，为梦长留
 */

public class BaseCalendarView extends View {

    private Paint mPaint;
    private int currentColor, otherColor, selectBgColor, selectTextColor;//当月颜色,其他月份的颜色,星期天颜色，被选中背景颜色，被选中文字颜色
    private int mWidth, mHeight;//控件宽高
    private PointF cellPoint;
    private final int ROW = 7;//行
    private final int COL = 7;//列
    private float textSize;
    private int year, month, day;//所选的年月日
    private Rect bounds;
    private int position;
    private float mRadio;//背景半径
    private int firstWeek;//当月一号周几
    private int lastDays;//上月总天数
    private int currentDays;//当月总天数
    private boolean isToday;
    private int today;
    private OnCalendarSelectDayListener listener;
    private int unDoColor;//未完成事件颜色
    private int doneColor;//完成事件的颜色

    private final String TODAY = "今天";

    private Map<String, Integer> marks;


    private BaseCalendarView(Context context) {
        this(context, null, 0);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bounds = new Rect();
        currentColor = Color.BLACK;
        otherColor = Color.GRAY;
        selectBgColor = Color.parseColor("#29AEFF");
        selectTextColor = Color.WHITE;
        unDoColor = selectBgColor;
        doneColor = otherColor;
        cellPoint = new PointF();
        int[] currentYMD = CalendarUtils.getCurrentYMD();
        today = currentYMD[2];
        marks = new ArrayMap<>();
        initDatas();

    }

    public void setCurrentSelectData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        int[] currentYMD = CalendarUtils.getCurrentYMD();
        isToday = (month == currentYMD[1] && year == currentYMD[0]);
        initDatas();
        postInvalidate();

    }


    private void initDatas() {
        firstWeek = CalendarUtils.getDataOfWeek(year, month, 1);
        lastDays = CalendarUtils.getLastMonthDays(year, month);
        currentDays = CalendarUtils.getDaysByYearMonth(year, month);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        cellPoint.x = mWidth / COL;//单位宽度
        cellPoint.y = mHeight / ROW;//单位高度

        if (textSize == 0) {
            textSize = Math.min(cellPoint.x, cellPoint.y) / 3;
        }
        mRadio = Math.min(cellPoint.x, cellPoint.y) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(textSize);
        //绘制当月时间

        drawCurrentMonth(canvas);
    }


    private void drawText(Canvas canvas, float startX, float startY, String currentDay, int mark) {
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(currentDay, 0, currentDay.length(), bounds);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float y = startY + (cellPoint.y - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        float x = (cellPoint.x) / 2 + startX;
        canvas.translate(x, y);
        canvas.drawText(currentDay, 0, 0, mPaint);
        if (mark == DONE) {//已经完成
            mPaint.setColor(doneColor);
            canvas.drawCircle(0, (fontMetrics.bottom - fontMetrics.top) / 4, mRadio / 10, mPaint);
        } else if (mark == UNDO) {
            mPaint.setColor(unDoColor);
            canvas.drawCircle(0, (fontMetrics.bottom - fontMetrics.top) / 4, mRadio / 10, mPaint);
        }
        canvas.restore();
    }

    private void drawCurrentMonth(Canvas canvas) {
        float startX = 0;
        float startY = 0;
        int currentDay;
        int currentMonth;
        int currentYear;
        String drawText;
        for (int i = 0; i < 42; i++) {
            if (i != 0 && i % 7 == 0) {
                startY += cellPoint.y;
                startX = 0;
            }
            if (i < firstWeek) {//绘制上月数据
                mPaint.setColor(otherColor);
                currentDay = lastDays - (firstWeek - i - 1);
                currentMonth = month == 1 ? 12 : month - 1;
                currentYear = month == 1 ? year - 1 : year;
                drawText = String.valueOf(currentDay);
            } else if (i > currentDays + firstWeek - 1) {//绘制下月数据
                mPaint.setColor(otherColor);
                currentDay = i - firstWeek - currentDays + 1;
                currentMonth = month == 12 ? 1 : month + 1;
                currentYear = month == 12 ? year + 1 : year;
                drawText = String.valueOf(currentDay);
            } else {//绘制本月数据
                mPaint.setColor(currentColor);
                currentDay = i - firstWeek + 1;
                currentMonth = month;
                currentYear = year;
                mPaint.setStyle(Paint.Style.FILL);
                if (currentDay == day) {//被选中的日期
                    //绘制背景
                    mPaint.setColor(selectBgColor);
                    canvas.save();
                    canvas.translate(startX, startY);
                    canvas.drawCircle(cellPoint.x / 2, cellPoint.y / 2, mRadio, mPaint);
                    mPaint.setColor(selectTextColor);
                    canvas.restore();
                } else {//其他时候的绘制
                    //绘制当天的标记
                    if (isToday && (currentDay == today)) {
                        mPaint.setColor(selectBgColor);
                        mPaint.setStyle(Paint.Style.STROKE);
                        mPaint.setStrokeWidth(4);
                        canvas.save();
                        canvas.translate(startX, startY);
                        canvas.drawCircle(cellPoint.x / 2, cellPoint.y / 2, mRadio - 2, mPaint);
                        mPaint.setColor(currentColor);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setStrokeWidth(1);
                        canvas.restore();
                    } else {
                        mPaint.setColor(currentColor);
                    }

                }
                if (!isToday || !(currentDay == today)) {
                    drawText = String.valueOf(currentDay);
                } else {
                    drawText = TODAY;
                }
            }
            drawText(canvas, startX, startY, drawText, getMark(currentYear, currentMonth, currentDay));
            startX += cellPoint.x;

        }
    }

    StringBuffer sb = new StringBuffer();

    private int getMark(int year, int month, int day) {
        sb.setLength(0);
        sb.append(year)
                .append(month)
                .append(day);
        String s = sb.toString();
        if (marks.containsKey(s)) {
            return marks.get(s);
        } else {
            return -1;
        }

    }

    public void setMarks(List< CalendarMarkBean> lists) {
        marks.clear();
        StringBuffer sb = new StringBuffer();

        if (lists != null && lists.size() > 0) {
            for (CalendarMarkBean bean : lists) {
                sb.setLength(0);
                sb.append(bean.year)
                        .append(bean.month)
                        .append(bean.day)
                ;
                marks.put(sb.toString(), bean.marck);
            }
        }
    }


    private PointF downPoint = new PointF();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint.x = event.getX();
                downPoint.y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                if (Math.pow(moveX - downPoint.x, 2) + Math.pow(moveY - downPoint.y, 2) > 10) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                position = getSelectPoint(x, y);
                if (position < firstWeek) {
                    if (month == 1) {
                        month = 12;
                        year--;
                    } else {
                        month--;
                    }
                    day = lastDays - (firstWeek - position - 1);
                    if (listener != null) {
                        listener.onSelectLastMonthDay(year, month, day);
                    }
                } else if (position >= currentDays + firstWeek) {
                    day = (position - firstWeek) - currentDays + 1;
                    if (month == 12) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }
                    if (listener != null) {
                        listener.onSelectNextMonthDay(year, month, day);
                    }

                } else {
                    day = position - firstWeek + 1;
                    if (listener != null) {
                        listener.onSelectCurrentMonthDay(year, month, day);
                    }
                    postInvalidate();
                }
                break;

        }
        return true;
    }

    //获取被点击的位置
    private int getSelectPoint(float x, float y) {
        int currentROW = (int) Math.ceil(y / cellPoint.y) - 1;
        int currentCOL = (int) Math.ceil(x / cellPoint.x);
        return (currentCOL) - 1 + (currentROW * 7);
    }


    /**
     * 设置接口监听
     *
     * @param listener 日历被点击事件的监听
     */
    public void setOnCalendarSelectDayListener(OnCalendarSelectDayListener listener) {
        this.listener = listener;
    }

    /**
     * 设置其他月份颜色
     *
     * @param otherMonthColor 颜色
     */
    public void setOtherMonthColor(int otherMonthColor) {
        this.otherColor = otherMonthColor;
    }

    /**
     * 设置被选中背景颜色
     *
     * @param selectBgColor 颜色
     */
    public void setSelectBgColor(int selectBgColor) {
        this.selectBgColor = selectBgColor;
    }

    /**
     * 设置文字大小
     *
     * @param calanderTextSize 文字大小
     */
    public void setCalanderTextSize(int calanderTextSize) {
        this.textSize = calanderTextSize;
    }

    /**
     * 设置当前月份颜色
     *
     * @param currentMonthColor 颜色
     */
    public void currentMonthColor(int currentMonthColor) {
        this.currentColor = currentMonthColor;
    }

    /**
     * 设置完成事件的颜色
     *
     * @param doneColor 颜色
     */
    public void setDonePoint(int doneColor) {
        this.doneColor = doneColor;
    }

    /**
     * 设置尚未完成事件的颜色
     *
     * @param unDoColor 颜色
     */
    public void setUndoPoint(int unDoColor) {
        this.unDoColor = unDoColor;
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    /**
     * 日历选择接口
     */
    public interface OnCalendarSelectDayListener {
        /**
         * 选择当前月份的日期
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        void onSelectCurrentMonthDay(int year, int month, int day);

        /**
         * 选择下月月份的日期
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        void onSelectNextMonthDay(int year, int month, int day);

        /**
         * 选择上月月份的日期
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        void onSelectLastMonthDay(int year, int month, int day);

    }

    public static class Builer {
        private BaseCalendarView calendar;
        private OnCalendarSelectDayListener listener;
        private int otherColor;
        private int selectBgColor;
        private int textSize;
        private int currentColor;
        private int doneColor;
        private int unDoColor;
        private int selectTextColor;
        private List<CalendarMarkBean> marks;

        public Builer(Context context) {
            this.calendar = new BaseCalendarView(context);
        }

        /**
         * 设置接口监听
         *
         * @param listener 日历被点击事件的监听
         */
        public Builer setOnCalendarSelectDayListener(OnCalendarSelectDayListener listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置其他月份颜色
         *
         * @param otherMonthColor 颜色
         */
        public Builer setOtherMonthColor(int otherMonthColor) {
            this.otherColor = otherMonthColor;
            return this;
        }

        /**
         * 设置被选中背景颜色
         *
         * @param selectBgColor 颜色
         */
        public Builer setSelectBgColor(int selectBgColor) {
            this.selectBgColor = selectBgColor;
            return this;
        }

        /**
         * 设置文字大小
         *
         * @param calanderTextSize 文字大小
         */
        public Builer setCalanderTextSize(int calanderTextSize) {
            this.textSize = calanderTextSize;
            return this;
        }

        /**
         * 设置当前月份颜色
         *
         * @param currentMonthColor 颜色
         */
        public Builer currentMonthColor(int currentMonthColor) {
            this.currentColor = currentMonthColor;
            return this;
        }

        /**
         * 设置完成事件的颜色
         *
         * @param doneColor 颜色
         */
        public Builer setDonePoint(int doneColor) {
            this.doneColor = doneColor;
            return this;
        }

        /**
         * 设置尚未完成事件的颜色
         *
         * @param unDoColor 颜色
         */
        public Builer setUndoPoint(int unDoColor) {
            this.unDoColor = unDoColor;
            return this;
        }

        /**
         * 设置选中文字的颜色
         *
         * @param selectTextColor
         * @return
         */
        public Builer setSelectTextColor(int selectTextColor) {
            this.selectTextColor = selectTextColor;
            return this;
        }

        public Builer setMarks(List<CalendarMarkBean> marks) {
            this.marks = marks;
            return this;
        }


        public BaseCalendarView builder() {

            if (otherColor != 0) {
                calendar.setOtherMonthColor(otherColor);
            }
            if (selectBgColor != 0) {
                calendar.setSelectBgColor(selectBgColor);
            }
            if (textSize != 0) {
                calendar.setCalanderTextSize(textSize);
            }
            if (currentColor != 0) {
                calendar.currentMonthColor(currentColor);
            }
            if (selectTextColor != 0) {
                calendar.setSelectTextColor(selectTextColor);
            }
            if (unDoColor != 0) {
                calendar.setUndoPoint(unDoColor);
            }

            if (doneColor != 0) {
                calendar.setDonePoint(doneColor);
            }

            if (marks != null) {
                calendar.setMarks(marks);
            }

            if (listener != null) {
                calendar.setOnCalendarSelectDayListener(listener);
            }
            calendar.postInvalidate();
            return calendar;
        }

    }

}

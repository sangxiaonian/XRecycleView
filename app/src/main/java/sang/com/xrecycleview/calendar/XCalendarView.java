package sang.com.xrecycleview.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;


public class XCalendarView extends LinearLayout {

    private CalendarPagerView pagerView;
    private CalendarHeadView headView;
    private final int ROL = 7;
    private float cellWith, cellHeight;
    private float headHeight;

    public XCalendarView(Context context) {
        this(context, null, 0);
    }

    public XCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        headView = new CalendarHeadView(context);
        ViewGroup.LayoutParams headParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headView.setLayoutParams(headParams);
        pagerView = new CalendarPagerView(context);
        ViewGroup.LayoutParams calendarParmas = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pagerView.setLayoutParams(calendarParmas);
        addView(pagerView);
        addView(headView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWith = (w - getPaddingLeft() - getPaddingRight()) / ROL;
        if (headHeight == 0) {
            cellHeight = (h - getPaddingTop() - getPaddingBottom()) / ROL;
            headHeight = cellHeight;
        } else {
            cellHeight = (h - headHeight - getPaddingTop() - getPaddingBottom()) / (ROL - 1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof CalendarPagerView) {
                childAt.layout(getPaddingLeft(), (int) headHeight, r - getPaddingRight(), b);
            } else if (childAt instanceof CalendarHeadView) {
                childAt.layout(getPaddingLeft(), getPaddingTop(), r - getPaddingRight(), (int) headHeight);
            }

        }

    }

    /**
     * 日历操作监听方法
     */
    public interface onSelectCalendarListener {

        /**
         * 点击选择某个日期
         *
         * @param year  年月日
         * @param month
         * @param day
         */
        void onSelectDay(int year, int month, int day);

        /**
         * 滑动或者切换年月 此时由于没有选择,因此是没有日期的,只有年月
         *
         * @param year  年
         * @param month 月
         */
        void onScrollCalendar(int year, int month);
    }

    /**
     * 设置点击监听
     *
     * @param onSelectCalendarListener 监听方法
     */
    public void setOnSelectCalendarListener(onSelectCalendarListener onSelectCalendarListener) {
        pagerView.setListener(onSelectCalendarListener);
    }


    /**
     * 跳跃到指定日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    public void jumpToData(int year, int month, int day) {
        pagerView.jumpToData(year, month, day);
    }

    /**
     * 调到当前日期
     */
    public void jumpToToday() {
        int[] currentYMD = CalendarUtils.getCurrentYMD();
        jumpToData(currentYMD[0], currentYMD[1], currentYMD[2]);
    }

    /**
     * 跳转到下个月
     */
    public void nextMonth() {
        pagerView.setCurrentItem(pagerView.getCurrentItem() + 1);
    }

    /**
     * 跳转到上个月
     */
    public void lastMonth() {
        pagerView.setCurrentItem(pagerView.getCurrentItem() - 1);
    }

    public static class Builder {

        private XCalendarView calendarView;
        private int CurrentMonthColor;
        private int OtherMonthColor;
        private int SelectTextColor;
        private int SelectBgColor;
        private int calanderTextSize;
        private onSelectCalendarListener onSelectCalendarListener;
        private int unDoColor;
        private int doneColor;
        private int weekSize;
        private int weekColor;
        private List<CalendarMarkBean> marks;

        public Builder(XCalendarView calendarView) {
            this.calendarView = calendarView;
        }

        /**
         * 设置当月字体颜色
         *
         * @param color
         * @return
         */
        public Builder setCurrentMonthColor(int color) {
            this.CurrentMonthColor = color;
            return this;
        }

        /**
         * 设置其他月份字体颜色
         *
         * @param color
         * @return
         */
        public Builder setOtherMonthColor(int color) {
            OtherMonthColor = color;
            return this;
        }

        /**
         * 设置选中后字体颜色
         *
         * @param color
         * @return
         */
        public Builder setSelectTextColor(int color) {
            SelectTextColor = color;
            return this;
        }

        /**
         * 设置选中背景颜色
         *
         * @param color
         * @return
         */
        public Builder setSelectBgColor(int color) {
            SelectBgColor = color;
            return this;
        }

        /**
         * 设置日历字体大小
         *
         * @param textSize 字体大小
         * @return
         */
        public Builder setCalanderTextSize(int textSize) {
            calanderTextSize = textSize;
            return this;
        }

        /**
         * 设置监听
         *
         * @param listener
         * @return
         */
        public Builder setOnSelectCalendarListener( onSelectCalendarListener listener) {
            this.onSelectCalendarListener = listener;
            return this;
        }


        /**
         * 设置未完成标记
         *
         * @param unDoColor
         * @return
         */
        public Builder setUnDoColor(int unDoColor) {
            this.unDoColor = unDoColor;
            return this;
        }

        /**
         * 设置完成标记
         */

        public Builder setDoneColor(int doneColor) {
            this.doneColor = doneColor;
            return this;
        }

        /**
         * 添加标记
         *
         * @param list 集合
         * @return
         */
        public Builder addMarks(List<CalendarMarkBean> list) {
            this.marks = list;

            return this;
        }

        /**
         * 设置星期天文字大小
         *
         * @param textSize
         */
        public Builder setWeeksSize(int textSize) {
            this.weekSize = textSize;
            return this;
        }

        /**
         * 设置星期天文字颜色
         *
         * @param weekColor 星期天文字颜色
         * @return
         */
        public Builder setWeekColor(int weekColor) {
            this.weekColor = weekColor;
            return this;
        }

        public XCalendarView builder(Context context) {
            CalendarPagerView pagerView = new CalendarPagerView.Builder(calendarView.pagerView)
                    .setOtherMonthColor(OtherMonthColor)
                    .setCurrentMonthColor(CurrentMonthColor)
                    .setSelectTextColor(SelectTextColor)
                    .setSelectBgColor(SelectBgColor)
                    .setCalanderTextSize(calanderTextSize)
                    .setUnDoColor(unDoColor)
                    .setDoneColor(doneColor)
                    .addMarks(marks)
                    .setListener(onSelectCalendarListener)
                    .builder(context);
            if (weekSize != 0) {
                calendarView.headView.setTextSize(weekSize);
            }
            if (weekColor != 0) {
                calendarView.headView.setTextColor(weekColor);
            }
            return calendarView;
        }


    }


}

package sang.com.xrecycleview.calendar.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.LinkedList;

import sang.com.xrecycleview.calendar.BaseCalendarView;
import sang.com.xrecycleview.calendar.CalendarUtils;

public abstract class CanlanderAdapter extends PagerAdapter {

    //当前年月日
    private int YEAR;
    private int MONTH;

    private int selectMonth;
    private int selectYear;
    private int selectDay;

    private Context context;
    private LinkedList<CauMonthAnYear> views;
    private LinkedList<View> mViewCache;
    private int currentPosition;


    public CanlanderAdapter(Context context) {
        this.context = context;
        int[] currentYMD = CalendarUtils.getCurrentYMD();
        YEAR = currentYMD[0];
        MONTH = currentYMD[1];
        views = new LinkedList<>();
        mViewCache = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 滑动切换的时候销毁当前的View
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        View contentView = (View) object;
        container.removeView(contentView);

    }

    // 每次滑动的时候生成的View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        BaseCalendarView calendarView;

        calendarView = creatCalendarView(context);
        CauMonthAnYear cauMonthAnYear = new CauMonthAnYear(position, calendarView).invoke();
        int year = cauMonthAnYear.getYear();
        int currentMouth = cauMonthAnYear.getCurrentMouth();
        if (selectYear == year && selectMonth == currentMouth) {
            calendarView.setCurrentSelectData(year, currentMouth, selectDay);
        } else {
            calendarView.setCurrentSelectData(year, currentMouth, 0);
        }

        //将控件储存起来，方便刷新
        if (views.size() < 4) {
            views.add(0, cauMonthAnYear);
        } else {
            views.remove(views.size() - 1);
            views.add(0, cauMonthAnYear);
        }
        views.add(cauMonthAnYear);
        container.addView(calendarView);
        return calendarView;
    }


    /**
     * 设置当前选中日期
     *
     * @param position
     * @param year
     * @param month
     * @param day
     */
    public void setSelectData(int position, int year, int month, int day) {
        this.selectYear = year;
        this.selectMonth = month;
        this.selectDay = day;
        this.currentPosition = position;
        upCalendar();
    }

    //刷新所有日历控件
    public void upCalendar() {
        for (CauMonthAnYear view : views) {
            view.invoke();
            if (view.getPosition() == currentPosition) {
                view.getCalendarView().setCurrentSelectData(selectYear, selectMonth, selectDay);
            } else {
                view.getCalendarView().setCurrentSelectData(view.getYear(), view.getCurrentMouth(), 0);
            }
        }
    }


    public CauMonthAnYear getCauMonthAnYear(int currentPosition) {
        return new CauMonthAnYear(currentPosition, null).invoke();
    }

    public int getPositionByData(int year, int month, int day) {
        int count = (year - YEAR) * 12 + (month - MONTH);
        return getCount() / 2 + count;
    }


    public abstract BaseCalendarView creatCalendarView(Context context);

    /**
     * 更新基准时间,用来无线循环
     *
     * @param year
     * @param month
     */
    public void upCurrentData(int year, int month) {
        YEAR = year;
        MONTH = month;
    }

    private class ViewHolder {
        BaseCalendarView calendarView;
    }

    public class CauMonthAnYear {
        private final BaseCalendarView calendarView;
        private int position;
        private int currentMouth;
        private int year;

        public CauMonthAnYear(int position, BaseCalendarView calendarView) {
            this.position = position;
            this.calendarView = calendarView;

        }

        public int getPosition() {
            return position;
        }

        public BaseCalendarView getCalendarView() {
            return calendarView;
        }

        public int getCurrentMouth() {
            return currentMouth;
        }

        public int getYear() {
            return year;
        }

        public CauMonthAnYear invoke() {
            int count = -getCount() / 2 + position;
            int yCount = (int) (count * 1.0 / 12);
            int monthCount = count % 12 + MONTH;

            int currentYear = yCount;
            if (monthCount > 12) {
                currentMouth = monthCount % 12;
                currentYear++;
            } else if (monthCount < 1) {
                currentYear--;
                currentMouth = monthCount + 12;
            } else {
                currentMouth = monthCount;
            }
            year = YEAR + currentYear;
            return this;
        }
    }
}

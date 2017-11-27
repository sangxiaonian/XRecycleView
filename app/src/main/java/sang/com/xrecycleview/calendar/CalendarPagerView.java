package sang.com.xrecycleview.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;


import java.util.List;

import sang.com.xrecycleview.calendar.adapter.CanlanderAdapter;

/**
 * 作者： ${桑小年} on 2017/8/19.
 * 努力，为梦长留
 */

public class CalendarPagerView extends ViewPager {

    public CalendarPagerView(Context context) {
        this(context, null);
    }

    public CalendarPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private int currentPosition;

    private boolean refrush;

    private void initView(Context context, AttributeSet attrs) {
        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                currentPosition = position;

                //获取当前年月日
                final CanlanderAdapter adapter = (CanlanderAdapter) getAdapter();
                final CanlanderAdapter.CauMonthAnYear cauMonthAnYear = (adapter).getCauMonthAnYear(position);
                final int year = cauMonthAnYear.getYear();
                final int month = cauMonthAnYear.getCurrentMouth();
                final int count = adapter.getCount();
                if (listener != null && !refrush) { //为了防止边界时候两次调用
                    listener.onScrollCalendar(year, month);
                }

                //在快到边界的时候,重新更改数据和位置
                if (currentPosition <= 1 || currentPosition >= count - 2) {
                    refrush = true;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int year = cauMonthAnYear.getYear();
                            int month = cauMonthAnYear.getCurrentMouth();
                            adapter.upCurrentData(year, month);
                            currentPosition = count / 2;
                            adapter.setSelectData(currentPosition, year, month, 0);
                            setCurrentItem(currentPosition, false);
                            refrush = false;
                        }
                    }, 150);//延迟一点时间操作,方式动画终端刷新
                }
            }
        });


    }


    /**
     * 设置选中监听
     *
     * @param listener 选中监听
     */
    public void setListener(XCalendarView.onSelectCalendarListener listener) {
        this.listener = listener;
    }

    private XCalendarView.onSelectCalendarListener listener;

    /**
     * 调到指定日期
     */
    public void jumpToData(int year, int month, int day) {
        CanlanderAdapter adapter = (CanlanderAdapter) getAdapter();
        int position = adapter.getPositionByData(year, month, day);
        int count = adapter.getCount();

        if (listener != null) {
            listener.onSelectDay(year, month, day);
        }
        if (position < 1 || position > count - 2) {//如果时间间隔比较长,就直接刷新
            position = count / 2;
            adapter.upCurrentData(year, month);
            adapter.setSelectData(position, year, month, day);
            setCurrentItem(position, false);
            if (listener != null) {
                listener.onScrollCalendar(year, month);
            }
        } else { //正常跳转
            adapter.setSelectData(position, year, month, day);
            setCurrentItem(position, true);
        }


    }

    /**
     * 设置日历监听
     *
     * @return
     */
    public XCalendarView.onSelectCalendarListener getListener() {
        return listener;
    }


    public static class Builder {
        private CalendarPagerView pagerView;
        private int otherMonthColor;
        private int selectBgColor;
        private int calanderTextSize;
        private int selectTextColor;
        private int unDoColor;
        private int doneColor;
        private List<CalendarMarkBean> marks;
        private int currentMonthColor;
        private XCalendarView.onSelectCalendarListener listener;

        public Builder() {

        }

        public Builder(CalendarPagerView pagerView) {
            this.pagerView = pagerView;
        }

        public Builder setOtherMonthColor(int otherMonthColor) {
            this.otherMonthColor = otherMonthColor;
            return this;
        }

        public Builder setSelectBgColor(int selectBgColor) {
            this.selectBgColor = selectBgColor;
            return this;
        }

        public Builder setCalanderTextSize(int calanderTextSize) {
            this.calanderTextSize = calanderTextSize;
            return this;

        }

        public Builder setCurrentMonthColor(int currentMonthColor) {
            this.currentMonthColor = currentMonthColor;
            return this;
        }

        public Builder setSelectTextColor(int selectTextColor) {
            this.selectTextColor = selectTextColor;
            return this;
        }

        public Builder setUnDoColor(int unDoColor) {
            this.unDoColor = unDoColor;
            return this;
        }

        public Builder setDoneColor(int doneColor) {
            this.doneColor = doneColor;
            return this;
        }

        public Builder addMarks(List<CalendarMarkBean> list) {
            this.marks = list;
            return this;
        }

        public Builder setListener(XCalendarView.onSelectCalendarListener listener) {
            this.listener = listener;
            return this;
        }

        public CalendarPagerView builder(Context context) {
            if (pagerView == null) {
                pagerView = new CalendarPagerView(context);
            }
            pagerView.setListener(listener);
            CanlanderAdapter adapter = new CanlanderAdapter(context) {
                @Override
                public BaseCalendarView creatCalendarView(Context context) {
                    BaseCalendarView calendarView = new BaseCalendarView.Builer(context)
                            .setOtherMonthColor(otherMonthColor)
                            .setCalanderTextSize(calanderTextSize)
                            .setSelectBgColor(selectBgColor)
                            .currentMonthColor(currentMonthColor)
                            .setSelectTextColor(selectTextColor)
                            .setUndoPoint(unDoColor)
                            .setDonePoint(doneColor)
                            .setMarks(marks)
                            .builder();
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    calendarView.setLayoutParams(params);
                    calendarView.setOnCalendarSelectDayListener(new BaseCalendarView.OnCalendarSelectDayListener() {
                        @Override
                        public void onSelectCurrentMonthDay(int year, int month, int day) {
                            setSelectData(pagerView.getCurrentItem(), year, month, day);
                            XCalendarView.onSelectCalendarListener listener = pagerView.getListener();
                            if (listener != null) {
                                listener.onSelectDay(year, month, day);
                            }
                        }

                        @Override
                        public void onSelectNextMonthDay(int year, int month, int day) {
                            int position = pagerView.getCurrentItem() + 1;
                            setSelectData(position, year, month, day);
                            pagerView.setCurrentItem(position);
                            XCalendarView.onSelectCalendarListener listener = pagerView.getListener();
                            if (listener != null) {
                                listener.onSelectDay(year, month, day);
                            }
                        }

                        @Override
                        public void onSelectLastMonthDay(int year, int month, int day) {
                            int position = pagerView.getCurrentItem() - 1;
                            setSelectData(position, year, month, day);
                            pagerView.setCurrentItem(position);
                            pagerView.jumpToData(year, month, day);
                            XCalendarView.onSelectCalendarListener listener = pagerView.getListener();
                            if (listener != null) {
                                listener.onSelectDay(year, month, day);
                            }
                        }
                    });
                    return calendarView;
                }
            };
            pagerView.setAdapter(adapter);
            int position = adapter.getCount() / 2;
            pagerView.setCurrentItem(position);
            return pagerView;
        }
    }


}

package sang.com.xrecycleview.calendar;

/**
 * 日历类的标记
 */
public class CalendarMarkBean {
    public static final int UNDO = 0;
    public static final int DONE = 1;
    public static final int NO_MARK = -1;

    public int year;
    public int month;
    public int day;
    public int marck;

    public CalendarMarkBean() {
    }

    public CalendarMarkBean(int year, int month, int day, int marck) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.marck = marck;
    }
}

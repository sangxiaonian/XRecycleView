package sang.com.xrecycleview.calendar;


import java.util.Calendar;

/**
 * 作者： ${桑小年} on 2017/8/18.
 * 努力，为梦长留
 */

public class CalendarUtils {



    /**
     * 获取上月天数
     * @param year
     * @param month
     * @return
     */
    public static int getLastMonthDays(int year,int month){
        if (month==1){
            month=12;
            year-=1;
        }else {
            month--;
        }
       return getDaysByYearMonth(year,month);
    }

    /**
     * 获取下月天数
     * @param year
     * @param month
     * @return
     */
    public static int getNextMonthDays(int year,int month){
        if (month==12){
            month=1;
            year+=1;
        }else {
            month++;
        }
        return getDaysByYearMonth(year,month);
    }

    /**
     * 获取当月第一天是周几
     *
     * @return 星期 从周日开始
     */
    public static int getDataOfWeek() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * 指定日期是周几
     *
     * @return 星期 从周日开始
     * @param year
     * @param month
     */
    public static int getDataOfWeek(int year, int month,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);
        StringBuffer sb = new StringBuffer();
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }

    public static int[] getCurrentYMD(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
        int day = cal.get(Calendar.DAY_OF_MONTH );
        return new int[]{year,month,day};
    }


    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据指定的年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year-1);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }
    /**
     * 根据指定的年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth_(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year-1);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }
    public static int getDayOfWeekByYearMonth(int year, int month){
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year-1);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DAY_OF_WEEK);
    };


}

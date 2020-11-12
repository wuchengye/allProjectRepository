package com.cs.mis.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wcy
 */
public class DateUtil {

    /**
     * @date 2020-11-12 14:56
     * 获取今天的日期
     */
    public static String getDateOfToday(){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return sf.format(date);
    }

    /**
     * @date 2020-11-12 14:51
     * 获取上个月最后一天
     */
    public static String getDateOfLastMonth(){

        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar=Calendar.getInstance();

        int month=calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH, month-1);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return sf.format(calendar.getTime());

    }
}

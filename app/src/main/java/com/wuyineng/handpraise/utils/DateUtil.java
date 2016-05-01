package com.wuyineng.handpraise.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wuyineng on 2016/5/1.
 * 描述：把时间戳转换成年月日
 */
public class DateUtil {

    public static int[] changeLongToDate(long time){


        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(time);

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH);

        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new int[]{year,month,day};
    }

    public static boolean compareCurrent(int year, int month, int day) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = year + "-" + month + "-" + day;

        Date mDate = sdf.parse(sDate);

        return mDate.getTime() > System.currentTimeMillis();

    }
}

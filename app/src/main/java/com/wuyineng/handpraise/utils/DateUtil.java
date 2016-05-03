package com.wuyineng.handpraise.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wuyineng on 2016/5/1.
 * 描述：把时间戳与时间转换的工具类
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

    /**
     * @param year
     * @param month
     * @param day
     * @return
     * @throws ParseException
     * 比较设定与当前时间
     */
    public static boolean compareCurrent(int year, int month, int day) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = year + "-" + month + "-" + day;

        Date mDate = sdf.parse(sDate);

        return mDate.getTime() > System.currentTimeMillis();

    }

    /**
     * @param context
     * @return
     * @throws ParseException
     * 描述：获取目标时间，将年月日转化为时间戳
     */
    public static long getTargetDayToTime(Context context) throws ParseException {
        int tar_year = SpTool.getInt(context,MyConstants.INITIAL_YEAR,0);
        int tar_month =SpTool.getInt(context,MyConstants.INITIAL_MONTH,0);
        int tar_day =SpTool.getInt(context,MyConstants.INITIAL_DAY,0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = tar_year + "-" + tar_month + "-" + tar_day;

        Date mDate = sdf.parse(sDate);

        return mDate.getTime();
    }
}

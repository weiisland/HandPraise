package com.wuyineng.handpraise.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.ui.SplashActivity;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuyineng on 2016/4/23.
 * 描述：
 */
public class NotificationService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        /*int hour = SpTool.getInt(getApplicationContext(), MyConstants.NOTIFY_HOUR, 0);
        int minute = SpTool.getInt(getApplicationContext(),MyConstants.NOTIFY_MIMUTE,0);
//        string装换成时间戳
        SimpleDateFormat format =  new SimpleDateFormat("HH:mm:ss");
        String time = hour + ":" + minute + ":" + "00";
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
       // System.out.print("Format To times:"+date.getTime());



        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        long[] vibrates = new long[]{0,1000,1000,1000};

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Notification notification = new Notification(R.drawable.icon1,"该记账咯！", System.currentTimeMillis());

        intent = new Intent(this, SplashActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hand);

        float progress = SpTool.getFloat(getApplicationContext(), MyConstants.CURRENT_PROGRESS, 0f);
//        转化为百分比
        int mProgress = (int) (progress * 100);

        Notification mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_hand)
                .setTicker("手赞：现在进度是——" + mProgress + "%")
                .setContentTitle("该记账咯！")
                .setContentText("今天是否距离目标更进一步呢？")
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setVibrate(vibrates)
                .setContentIntent(pi)
                .getNotification();

        manager.notify(0,mBuilder);

        return super.onStartCommand(intent, flags, startId);
    }
}

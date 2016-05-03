package com.wuyineng.handpraise.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.service.NotificationService;
import com.wuyineng.handpraise.utils.DateUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;
import com.wuyineng.handpraise.utils.WindowToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by wuyineng on 2016/4/21.
 * 描述：设置界面的fragment
 */
public class SettingFragment extends BaseFragment implements TextWatcher{

    private static final int ALARMREPEAT = 0;
    private static final int FINISH = 1;
    @ViewInject(R.id.et_setting_current_property)
    private EditText et_currentProperty;

    @ViewInject(R.id.et_setting_target_property)
    private EditText et_targetProperty;

    @ViewInject(R.id.et_setting_want_todo)
    private EditText et_want_todo;

    @ViewInject(R.id.bt_setting_target_day)
    private Button bt_target_day;

    @ViewInject(R.id.switch_setting_notify)
    private Switch switch_notify;

    @ViewInject(R.id.bt_setting_notify_time)
    private Button bt_notify_time;

    @ViewInject(R.id.ll_setting_notify_time)
    private LinearLayout ll_notify;


    private long currentTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private int mHour;
    private int mMinute;
    private AlarmManager mAm;
    //private MyTimeTask task;


    @Override
    protected View initView() {

        View view = View.inflate(mMainActivity, R.layout.fragment_setting,null);
        ViewUtils.inject(this, view);
        setHasOptionsMenu(true);

        if (SpTool.getBoolean(mMainActivity,MyConstants.IS_NOTIFY,true)){
            switch_notify.setChecked(true);
            ll_notify.setVisibility(View.VISIBLE);
            int hour = SpTool.getInt(mMainActivity,MyConstants.NOTIFY_HOUR,0);
            int minute = SpTool.getInt(mMainActivity,MyConstants.NOTIFY_MIMUTE,0);
            bt_notify_time.setText(new StringBuilder()
                    .append(hour < 10 ? "0" + hour : hour).append(":")
                    .append(minute < 10 ? "0" + minute : minute));
        }else {
            ll_notify.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    protected void initData() {
        currentTime = System.currentTimeMillis();

        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        bt_target_day.setText(sdf.format(currentTime));*/

        int year = SpTool.getInt(getActivity(), MyConstants.INITIAL_YEAR, mYear);
        int month = SpTool.getInt(getActivity(), MyConstants.INITIAL_MONTH, mMonth);
        int day = SpTool.getInt(getActivity(), MyConstants.INITIAL_DAY, mDay);
        //更新EditText控件日期 小于10加0
        bt_target_day.setText(new StringBuilder().append(year).append("年")
                .append((month + 1) < 10 ? "0" + (month + 1) : (month + 1))
                .append("月")
                .append((day < 10) ? "0" + day : day).append("日"));

        calendar = Calendar.getInstance();
//      弹出键盘
        WindowToken.getSystemKeyboard(mMainActivity, et_want_todo);

        et_currentProperty.setText(SpTool.getString(mMainActivity, MyConstants.INITIAL_CURRENT_MONEY, "").trim());
        et_targetProperty.setText(SpTool.getString(mMainActivity, MyConstants.INITIAL_TARGET_MONEY, "").trim());
        et_want_todo.setText(SpTool.getString(mMainActivity,MyConstants.INITIAL_WANT,"").trim());

        super.initData();
    }

    @Override
    protected void initEvent() {
//      设定目标时间
        bt_target_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choseTargetDay();
            }
        });


//        选择是否每日提醒时间
        switch_notify.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) {
//                    如果选择提醒就启动服务
                    /*if (bt_notify_time.getVisibility() == View.VISIBLE
                            && !TextUtils.isEmpty(bt_notify_time.getText().toString().trim())){

                        try {
                            startRepeatNotify();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }*/
                    /*Intent intent = new Intent(mMainActivity, NotificationService.class);
                    mMainActivity.startService(intent);*/
//                    设置提醒时间可见
                    ll_notify.setVisibility(View.VISIBLE);

                    SpTool.putBoolean(mMainActivity, MyConstants.IS_NOTIFY, true);
                } else {
                    SpTool.putBoolean(mMainActivity, MyConstants.IS_NOTIFY, false);

//                    AlarmManager取消报警
                    Intent mIntent = new Intent(mMainActivity,NotificationService.class);

                    PendingIntent pi = PendingIntent.getService(mMainActivity, 0, mIntent, 0);

                    mAm= (AlarmManager) mMainActivity.getSystemService(Context.ALARM_SERVICE);

                    mAm.cancel(pi);//不取消就会一直启动服务，就算是stopService也没办法

                    //handler.obtainMessage(FINISH).sendToTarget();



//                  不然就停止服务
                    Intent intent = new Intent(mMainActivity, NotificationService.class);
                    mMainActivity.stopService(intent);

                    ll_notify.setVisibility(View.GONE);
                }
            }
        });
//      设置每日提醒时间
        bt_notify_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(mMainActivity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                mHour = hour;
                                mMinute = minute;
                                /*if (switch_notify.isChecked()){

                                    startRepeatNotify();
                                }*/

                                SpTool.putInt(mMainActivity, MyConstants.NOTIFY_HOUR, mHour);
                                SpTool.putInt(mMainActivity, MyConstants.NOTIFY_MIMUTE, mMinute);

//                                在这里才启动服务

                                Intent intent= new Intent(mMainActivity,NotificationService.class);

                                PendingIntent pi = PendingIntent.getService(mMainActivity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                mAm = (AlarmManager) mMainActivity.getSystemService(Context.ALARM_SERVICE);

                                //mAm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPi);

                                long firstTime = SystemClock.elapsedRealtime();
                                long systemTime = System.currentTimeMillis();

                                Calendar calendar = Calendar.getInstance();

                                calendar.setTimeInMillis(systemTime);
                                // calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                calendar.setTimeZone(TimeZone.getDefault());
                                calendar.set(Calendar.HOUR_OF_DAY, mHour);
                                calendar.set(Calendar.MINUTE, mMinute);
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);

                                long selectTime = calendar.getTimeInMillis();

                                if (systemTime > selectTime){
                                    calendar.add(Calendar.DAY_OF_MONTH,1);
                                    selectTime = calendar.getTimeInMillis();
                                }

                                long time = selectTime - systemTime;

                                firstTime += time;

                                final int DAY = 24 * 60 *60 *1000;

                                mAm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 24 * 60 * 60 * 1000, pi);



                                bt_notify_time.setText(new StringBuilder()
                                        .append(mHour < 10 ? "0" + mHour : mHour).append(":")
                                        .append(mMinute < 10 ? "0" + mMinute : mMinute));
                            }
                        }, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });


        super.initEvent();
    }

    /**
     * 设置每日通知提醒，如果这个方法是在switch_notify中执行，就不能通过改变时间来显示通知，因为switch_notify
     * 改变状态在先，之后才是选择通知时间，逻辑的先后顺序有错，所以既然要改变时间，那么应该先设定时间，再启动服务
     */
/*    private void startRepeatNotify() throws ParseException {
        Intent intent= new Intent(mMainActivity,NotificationService.class);

        PendingIntent pi = PendingIntent.getService(mMainActivity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAm = (AlarmManager) mMainActivity.getSystemService(Context.ALARM_SERVICE);

        //mAm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPi);

        int mHour = SpTool.getInt(mMainActivity, MyConstants.NOTIFY_HOUR, 0);
        int mMinute = SpTool.getInt(mMainActivity, MyConstants.NOTIFY_MIMUTE, 0);

        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(systemTime);
       // calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        long selectTime = calendar.getTimeInMillis();

        if (systemTime > selectTime){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            selectTime = calendar.getTimeInMillis();
        }

        long time = selectTime - systemTime;

        firstTime += time;

        //mAm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, selectTime, DAY, pi);
        //mAm.setRepeating(AlarmManager.RTC_WAKEUP, systemTime + 1000*10, DAY, pi);
        //mAm.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, AlarmManager.INTERVAL_DAY, pi);


        int hour = SpTool.getInt(mMainActivity, MyConstants.NOTIFY_HOUR, 0);
        int minute = SpTool.getInt(mMainActivity,MyConstants.NOTIFY_MIMUTE,0);
//        string装换成时间戳
*//*        SimpleDateFormat format =  new SimpleDateFormat("HH:mm:ss");
        String time2 = hour + ":" + minute + ":" + "00";*//*


        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = SpTool.getInt(getActivity(), MyConstants.CURRENT_YEAR, 0);
        int month = SpTool.getInt(getActivity(), MyConstants.CURRENT_MONTH, 0);
        int day = SpTool.getInt(getActivity(), MyConstants.CURRENT_DAY, 0);
        String time2 = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";


        //Date date = format.parse(time2);
        Date date = format.parse("2016-03-22 21:52:00");



        long interTime = date.getTime() - System.currentTimeMillis();
        // System.out.print("Format To times:"+date.getTime());

//      方法二使用定时器，因为TimerTask是一次性的，要重复使用，只能够先消除，再重新创建
        if (timer != null){
            if (task != null){
                task.cancel();
            }
            MyTimeTask task = new MyTimeTask();
            timer.schedule(task,1000,30*1000);//可以实现重复时间提醒，但这里一直有个bug，第二个延迟参数总是异常，若是数字没问题，但是用interTime就是不行
        }

    }

    Timer timer = new Timer();
    class MyTimeTask extends TimerTask{

        @Override
        public void run() {
            handler.obtainMessage(ALARMREPEAT).sendToTarget();
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ALARMREPEAT:
                    Intent intent= new Intent(mMainActivity,NotificationService.class);
                    mMainActivity.startService(intent);
                    break;

                case FINISH://停止服务要在这边停止
                    Intent i = new Intent(mMainActivity, NotificationService.class);
                    mMainActivity.stopService(i);
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

    /**
     * 选择目标时间
     */
    private void choseTargetDay() {


        new android.app.DatePickerDialog(mMainActivity,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        try {
                            if (DateUtil.compareCurrent(year,monthOfYear,dayOfMonth)){
                                //                      保存时间
                                SpTool.putInt(getActivity(), MyConstants.INITIAL_YEAR, mYear);
                                SpTool.putInt(getActivity(), MyConstants.INITIAL_MONTH, mMonth);
                                SpTool.putInt(getActivity(), MyConstants.INITIAL_DAY, mDay);
                                //更新EditText控件日期 小于10加0
                                bt_target_day.setText(new StringBuilder().append(mYear).append("年")
                                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                        .append("月")
                                        .append((mDay < 10) ? "0" + mDay : mDay).append("日"));
                            }else {
                                Toast.makeText(mMainActivity,"选择的时间必须大于当前时间",Toast.LENGTH_SHORT).show();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) ).show();




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.item_setting_done){

            if (TextUtils.isEmpty(et_currentProperty.getText().toString().trim() )
                    || TextUtils.isEmpty(et_targetProperty.getText().toString().trim())){
                Toast.makeText(mMainActivity,"当前资产或目标资产不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }else {

                setPersonalInfo();

                Toast.makeText(mMainActivity,"保存成功",Toast.LENGTH_SHORT).show();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击完成，保存个人信息
     */
    private void setPersonalInfo() {
        SpTool.putString(mMainActivity, MyConstants.INITIAL_CURRENT_MONEY, et_currentProperty.getText().toString().trim());
        SpTool.putString(mMainActivity, MyConstants.INITIAL_TARGET_MONEY, et_targetProperty.getText().toString().trim());
        SpTool.putString(mMainActivity, MyConstants.INITIAL_WANT, et_want_todo.getText().toString().trim());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /**
         * TODO:待添加功能，数字中间添加逗号
         *
         * */
        if ((et_targetProperty.getText().length() % 3 == 0 && et_targetProperty.getText().length() > 3)
                | (et_currentProperty.getText().length() % 3 == 0 && et_currentProperty.getText().length() > 3)){

            int end = et_currentProperty.getSelectionEnd();

            et_currentProperty.getText().insert(end-3, ",");

        }
    }

    @Override
    public void afterTextChanged(Editable s) {



    }
}

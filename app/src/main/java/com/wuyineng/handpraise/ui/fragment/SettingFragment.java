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

import com.rey.material.widget.Button;
import com.rey.material.widget.Switch;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.service.NotificationService;
import com.wuyineng.handpraise.utils.DateUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;
import com.wuyineng.handpraise.utils.WindowToken;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuyineng on 2016/4/21.
 * 描述：设置界面的fragment
 */
public class SettingFragment extends BaseFragment{

    @Bind(R.id.et_setting_current_property)
    EditText et_currentProperty;
    @Bind(R.id.et_setting_target_property)
    EditText et_targetProperty;
    @Bind(R.id.et_setting_want_todo)
    EditText et_want_todo;
    @Bind(R.id.bt_setting_target_day)
    Button bt_target_day;
    @Bind(R.id.switch_setting_notify)
    Switch switch_notify;
    @Bind(R.id.bt_setting_notify_time)
    Button bt_notify_time;
    @Bind(R.id.ll_setting_notify_time)
    LinearLayout ll_notify;

    private long currentTime;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private int mHour;
    private int mMinute;
    private AlarmManager mAm;

    @Override
    protected View initView() {

        View view = View.inflate(mMainActivity, R.layout.fragment_setting,null);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        if (SpTool.getBoolean(mMainActivity,MyConstants.IS_NOTIFY,false)){
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

}

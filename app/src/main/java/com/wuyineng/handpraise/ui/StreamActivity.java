package com.wuyineng.handpraise.ui;

import android.content.Intent;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.utils.KeyboardUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wuyineng on 2016/4/20.
 * 描述：增加流水的界面
 */
public class StreamActivity extends BaseActivity implements View.OnTouchListener{

    @ViewInject(R.id.ib_back)
    private ImageButton mIb_back;

    @ViewInject(R.id.tv_stream_date)
    private TextView mTv_date;

    @ViewInject(R.id.et_stream_income)
    private android.widget.EditText mEt_income;

    @ViewInject(R.id.et_stream_pay)
    private EditText mEt_pay;

    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;


    @Override
    protected void initView() {
        setContentView(R.layout.item_stream_detail);

        ViewUtils.inject(this);

    }

    @Override
    protected void initData() {

        long currentTime = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        mTv_date.setText(sdf.format(currentTime));

        mCalendar = Calendar.getInstance();

        super.initData();
    }

    @Override
    protected void initEvent() {
//      点击输入框时显示键盘
        mEt_income.setOnTouchListener(this);

        mEt_pay.setOnTouchListener(this);
//      后退返回主页
        mIb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StreamActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        /**
         * TODO 日期对应着数据
         * */

//        点击按钮切换日期
        mTv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.DatePickerDialog(StreamActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
//                      保存时间
                                /*SpTool.putInt(getApplicationContext(), MyConstants.INITIAL_YEAR, mYear);
                                SpTool.putInt(getApplicationContext(), MyConstants.INITIAL_MONTH, mMonth);
                                SpTool.putInt(getApplicationContext(), MyConstants.INITIAL_DAY, mDay);*/
                                //更新EditText控件日期 小于10加0
                                mTv_date.setText(new StringBuilder().append(mYear).append("年")
                                        .append((mMonth + 1) < 10 ? 0 + (mMonth + 1) : (mMonth + 1))
                                        .append("月")
                                        .append((mDay < 10) ? 0 + mDay : mDay).append("日"));
                            }
                        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH) ).show();
            }
        });
        super.initEvent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        EditText et_text = (EditText) v;
//      获取文本框的输入类型
        int inputback = et_text.getInputType();
        et_text.setInputType(InputType.TYPE_NULL);
        new KeyboardUtil(this, this, et_text).showKeyboard();
        et_text.setInputType(inputback);

        return false;
    }
}

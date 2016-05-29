package com.wuyineng.handpraise.ui;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TextView;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.dao.StreamDao;
import com.wuyineng.handpraise.domain.Stream;
import com.wuyineng.handpraise.utils.KeyboardUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuyineng on 2016/4/20.
 * 描述：增加流水的界面
 */
public class StreamActivity extends BaseActivity implements View.OnTouchListener{
    @Bind(R.id.ib_back)
    ImageButton mIb_back;
    @Bind(R.id.tv_stream_date)
    TextView mTv_date;
    @Bind(R.id.et_stream_income)
    android.widget.EditText mEt_income;
    @Bind(R.id.et_stream_pay)
    EditText mEt_pay;
    @Bind(R.id.et_stream_comment)
    EditText mEt_comment;
    @Bind(R.id.ib_done)
    ImageButton mIb_done;

    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar mCalendar;
    private StreamDao mDao;
    private Stream mBean;
    private SimpleDateFormat mSdf;
    private KeyboardUtil mKeyboardUtil;
    private long mCurrentTime;


    @Override
    protected void initView() {
        setContentView(R.layout.item_stream_detail);

        mDao = new StreamDao(getApplicationContext());
        mBean = new Stream();

        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mCurrentTime = System.currentTimeMillis();
        mSdf = new SimpleDateFormat("yyyy年MM月dd日");
        mTv_date.setText(mSdf.format(mCurrentTime));
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
                Intent intent = new Intent(StreamActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


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
                                SpTool.putInt(getApplicationContext(), MyConstants.STREAM_YEAR, mYear);
                                SpTool.putInt(getApplicationContext(), MyConstants.STREAM_MONTH, mMonth);
                                SpTool.putInt(getApplicationContext(), MyConstants.STREAM_DAY, mDay);
                                //更新EditText控件日期 小于10加0
                                mTv_date.setText(new StringBuilder().append(mYear).append("年")
                                        .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                                        .append("月")
                                        .append((mDay < 10) ? "0" + mDay : mDay).append("日"));


                            }
                        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        点击完成保存数据
        mIb_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String income = mEt_income.getText().toString().trim();
//                如果没有判断空，就不能够在主页上立即更新收支数据
                if (TextUtils.isEmpty(income)){
                    income = "0";
                }
                mBean.setIncome(income);
                String pay = mEt_pay.getText().toString().trim();
                if (TextUtils.isEmpty(pay)){
                    pay = "0";
                }
                mBean.setPay(pay);
                String comment = mEt_comment.getText().toString().trim();
                mBean.setComment(comment);

                String date = mTv_date.getText().toString().trim();

                Log.d("isPressed", "onClick: " + date);

                try {
                    java.util.Date uDate = mSdf.parse(date);

                        mBean.setDate(uDate.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mDao.add(mBean);

//                跳转至主界面
                Intent intent = new Intent(StreamActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(),"数据保存完成",Toast.LENGTH_SHORT).show();
            }
        });

        super.initEvent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        EditText et_text = (EditText) v;
//      获取文本框的输入类型
        int input_back = et_text.getInputType();
        et_text.setInputType(InputType.TYPE_NULL);
        mKeyboardUtil = new KeyboardUtil(this, this, et_text);

        mKeyboardUtil.showKeyboard();

/*        List<Keyboard.Key> keys = mKeyboardUtil.getKey();

        Log.d("KEYS", String.valueOf(keys.toString()));

        int size = keys.size();

        Keyboard.Key OK = keys.get(size - 1);

        Log.d("OK", (String) OK.label);//输出是OK

        if (OK.pressed){//这个方法无效，不知为何
            String income = mEt_income.getText().toString().trim();
            mBean.setIncome(income);
            String pay = mEt_pay.getText().toString().trim();
            mBean.setPay(pay);
            String comment = mEt_comment.getText().toString().trim();
            mBean.setComment(comment);
            mDao.add(mBean);
        }*/

        et_text.setInputType(input_back);

        return false;
    }


}

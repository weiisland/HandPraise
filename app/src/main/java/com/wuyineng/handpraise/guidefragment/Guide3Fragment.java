package com.wuyineng.handpraise.guidefragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wuyineng.handpraise.ui.MainActivity;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.utils.DateUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by wuyineng on 2016/4/18.
 * 描述：滚动的第一个界面
 */
public class Guide3Fragment extends BaseGuideFragment{


    @ViewInject(R.id.tp_guide3_chooseDay)
    private DatePicker mDatePicker;

    @ViewInject(R.id.bt_guide3_start_exp)
    private Button mStartEX;

    @ViewInject(R.id.et_guide3_target)
    private EditText mTargetProperty;

    @ViewInject(R.id.et_guide3_what_want)
    private EditText mEt_Want;

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean isPrepared;//当前fragment的界面加载完毕

    @Override
    protected void initData() {

        if (!isPrepared || !isVisible){
            return;
        }

        Calendar calendar = Calendar.getInstance();

        final int Nyear = calendar.get(Calendar.YEAR);
        final int Nmonth = calendar.get(Calendar.MONTH);
        final int Nday = calendar.get(Calendar.DAY_OF_MONTH);
//      初始化mDatePicker，初始化监听器
        mDatePicker.init(Nyear, Nmonth, Nday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                try {
                    if (DateUtil.compareCurrent(year,monthOfYear,dayOfMonth)) {

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        SpTool.putInt(getActivity(), MyConstants.INITIAL_YEAR, mYear);
                        SpTool.putInt(getActivity(), MyConstants.INITIAL_MONTH, mMonth);
                        SpTool.putInt(getActivity(), MyConstants.INITIAL_DAY, mDay);
                    } else {
                        Toast.makeText(getActivity(), "日期不符合要求", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        super.initData();
    }

    @Override
    protected void initEvent() {

        mStartEX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (DateUtil.getTargetDayToTime(getActivity()) < System.currentTimeMillis()){
                        Toast.makeText(getActivity(),"请设定目标时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String targetMoney = mTargetProperty.getText().toString().trim();
                SpTool.putString(getActivity(), MyConstants.INITIAL_TARGET_MONEY, targetMoney);

                String wantToDo = mEt_Want.getText().toString().trim();

                SpTool.putString(getActivity(), MyConstants.INITIAL_WANT, wantToDo);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                SpTool.putBoolean(getActivity(), MyConstants.IS_FINISH_SETUP,true);
                startActivity(intent);
                getActivity().finish();
            }
        });
        super.initEvent();
    }

    @Override
    public View initView() {

        View root = View.inflate(mGuideFragmentActivity, R.layout.item_guide_3, null);
        ViewUtils.inject(this,root);
        isPrepared = true;//界面加载完毕
        return root;
    }
}

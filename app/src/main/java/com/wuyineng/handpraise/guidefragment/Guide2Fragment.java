package com.wuyineng.handpraise.guidefragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wuyineng.handpraise.ui.MainActivity;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuyineng on 2016/4/18.
 * 描述：滚动的第一个界面
 */
public class Guide2Fragment extends BaseGuideFragment{

    @Bind(R.id.tv_guide2_currentDay)
    TextView mShowCurrentDay;

    @Bind(R.id.bt_item_guide2_skip)
    Button mSkipSet;

    @Bind(R.id.et_guide2_current_property)
    EditText mCurrentProperty;


    @Override
    protected void initData() {
//      设置显示当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        //mShowCurrentDay.setText(sdf.format(new Date()));
        mShowCurrentDay.setText(sdf.format(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SpTool.putInt(getActivity(), MyConstants.CURRENT_YEAR, year);
        SpTool.putInt(getActivity(), MyConstants.CURRENT_MONTH, month);
        SpTool.putInt(getActivity(), MyConstants.CURRENT_DAY, day);
        super.initData();
    }

    @Override
    protected void initEvent() {

        mCurrentProperty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//        保存当前资产
                String current_money = mCurrentProperty.getText().toString().trim();
                SpTool.putString(getActivity(), MyConstants.INITIAL_CURRENT_MONEY, current_money);
            }
        });

        mSkipSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                SpTool.putBoolean(getActivity(), MyConstants.IS_SKIP, true);
                startActivity(intent);
                getActivity().finish();
            }
        });
        super.initEvent();
    }

    @Override
    public View initView() {

        View root = View.inflate(mGuideFragmentActivity, R.layout.item_guide_2, null);

        ButterKnife.bind(this,root);

        return root;
    }
}

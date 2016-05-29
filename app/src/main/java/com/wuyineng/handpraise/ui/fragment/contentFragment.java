package com.wuyineng.handpraise.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.dao.StreamDao;
import com.wuyineng.handpraise.domain.Stream;
import com.wuyineng.handpraise.ui.MainActivity;
import com.wuyineng.handpraise.ui.StreamActivity;
import com.wuyineng.handpraise.utils.DateUtil;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuyineng on 2016/4/19.
 * 描述：主页显示的图片
 */
public class ContentFragment extends BaseFragment {
    @Bind(R.id.ts_main_textChange)
    TextSwitcher mTextSwitcher;
    @Bind(R.id.tv_main_remain_days)
    TextView mRemainDays;
    @Bind(R.id.tv_main_init_property)
    TextView mInitProperty;
    @Bind(R.id.number_progress_bar)
    NumberProgressBar mNumPB;
    @Bind(R.id.tv_content_current_money)
    TextView mCurrentProperty;
    @Bind(R.id.tv_content_money_income)
    TextView mIncome;
    @Bind(R.id.tv_content_money_pay)
    TextView mPay;

    private String[] mTextSwitch;//文本切换的数组
    private int curStr;//文本切换数组的下标

    private StreamDao mDao;
    private long mCurrentTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mMainActivity,R.layout.fragment_content, null);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);

        mCurrentTime = System.currentTimeMillis();

        mDao = new StreamDao(mMainActivity);

        try {
            showData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     * 显示数据
     */
    private void showData() throws ParseException {

        String tar_money = SpTool.getString(mMainActivity, MyConstants.INITIAL_TARGET_MONEY, "");
        String cur_money = SpTool.getString(mMainActivity,MyConstants.INITIAL_CURRENT_MONEY,"");

        double target = 0;
        double current = 0;

        try {

            target = Double.parseDouble(tar_money);
            current = Double.parseDouble(cur_money);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Stream> allData = mDao.getAllData();

        double TotalIncome = 0;
        double TotalPay = 0;

        for (int i = 0; i < allData.size(); i++){
            Stream bean = allData.get(i);

            try {

                TotalIncome += Double.parseDouble(bean.getIncome().trim());
                TotalPay += Double.parseDouble(bean.getPay().trim());

                Log.d("TotalIncome", TotalIncome + "");
                Log.d("TotalPay", TotalPay + "");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//      设置进度条数据
        double remain =current + (TotalIncome - TotalPay);
        mNumPB.setMax((int)target);
        //如果剩余的资产为负资产，则令进度条为零
        if (remain <= 0){
            mNumPB.setProgress(0);
            //如果当前时间小于目标时间且当前资产小于总资产
        }else if (mCurrentTime < DateUtil.getTargetDayToTime(mMainActivity) && remain < target){
            mNumPB.setProgress((int)remain);
            //如果当前时间小于目标时间且当前资产大于总资产
        }else if (mCurrentTime < DateUtil.getTargetDayToTime(mMainActivity) && remain > target){
            mNumPB.setProgress((int) target);
            Toast.makeText(mMainActivity,"恭喜目标达成 ╭(●｀∀´●)╯╰(●’◡’●)╮\n 请设置新的目标", Toast.LENGTH_SHORT).show();
            //如果当前时间大于目标时间且当前资产小于总资产
        }else if (mCurrentTime > DateUtil.getTargetDayToTime(mMainActivity) && remain < target){
            mNumPB.setProgress((int)remain);
            Toast.makeText(mMainActivity,"加油 (ง •̀_•́)ง (*•̀ㅂ•́)و\n 请重新设定日期", Toast.LENGTH_SHORT).show();
            //如果当前时间大于目标时间且当前资产大于总资产
        }else {
            mNumPB.setProgress((int) target);
            Toast.makeText(mMainActivity,"请重新设定目标时间和资产", Toast.LENGTH_SHORT).show();
        }

        float progress = (float) (remain/target);

        Log.d("Progress", progress + "");

        //保存百分比
        SpTool.putFloat(mMainActivity, MyConstants.CURRENT_PROGRESS, progress);

        mCurrentProperty.setText("当前资产：" + remain + "元");
        mIncome.setText("收入：" + TotalIncome + "元");
        mPay.setText("支出：-" + TotalPay + "元");
    }


    protected void initEvent() {

        final String tar_property = SpTool.getString(mMainActivity, MyConstants.INITIAL_TARGET_MONEY, "");
        //        点击文本进行切换
        mTextSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                如果想要做的事情没有，那么就只显示目标金额

                if ((TextUtils.isEmpty(tar_property))){

                    mTextSwitcher.setText(0.0 + " 元");
                }else if (TextUtils.isEmpty(SpTool.getString(mMainActivity, MyConstants.INITIAL_WANT, ""))
                        && (!TextUtils.isEmpty(tar_property))){
                    mTextSwitcher.setText(tar_property + " 元");
                }else {

                    mTextSwitcher.setText(mTextSwitch[(1 + curStr++) % mTextSwitch.length]);
                }

            }
        });
    }

    protected void initData() {

//        设置文本切换

        String tar_property = SpTool.getString(mMainActivity, MyConstants.INITIAL_TARGET_MONEY, "");

        mTextSwitch = new String[]{tar_property  + " 元"
                ,SpTool.getString(mMainActivity,MyConstants.INITIAL_WANT,"")};

        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView textView = new TextView(mMainActivity);

                textView.setTextSize(20);

                textView.setGravity(Gravity.CENTER);

                textView.setText(mTextSwitch[0]);

                textView.setTextColor(Color.BLACK);
                return textView;
            }
        });
//      设置剩余的天数
        int tar_year = SpTool.getInt(mMainActivity,MyConstants.INITIAL_YEAR,0);
        int tar_month =SpTool.getInt(mMainActivity,MyConstants.INITIAL_MONTH,0) + 1;//因为月份从0开始算起
        int tar_day =SpTool.getInt(mMainActivity,MyConstants.INITIAL_DAY,0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date tar_date = sdf.parse(tar_year + "-" + tar_month + "-" + tar_day);

            long diff = tar_date.getTime() - System.currentTimeMillis();

            if (diff >= 0){

                long days = diff / (1000 * 60 * 60 * 24);
                mRemainDays.setText(days + "天");
            }else {
                mRemainDays.setText(0 + "天");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

//        设置进度条
        mNumPB.setProgressTextSize(20);

        String cur_property = SpTool.getString(mMainActivity,MyConstants.INITIAL_CURRENT_MONEY,"");
        if (TextUtils.isEmpty(cur_property)){
            mInitProperty.setText("初始资产：" + 0.0 + "元");
        }else {
            mInitProperty.setText("初始资产：" + cur_property + "元");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_main_add) {
            Intent intent = new Intent(mMainActivity, StreamActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.wuyineng.handpraise.ui.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.wuyineng.handpraise.R;

/**
 * Created by wuyineng on 2016/4/24.
 * 描述：日历显示
 */
public class CalendarViewFragment extends BaseFragment {

    @ViewInject(R.id.calendarView)
    private MaterialCalendarView mCalendarView;
    @Override
    protected View initView() {

        View view = View.inflate(mMainActivity, R.layout.fragment_calendar, null);

        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    protected void initEvent() {

        /**
         * TODO 点击日历显示当天收入与支出，点击按钮会显示详细列表
         *
         * */

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

            }
        });
        super.initEvent();
    }
}

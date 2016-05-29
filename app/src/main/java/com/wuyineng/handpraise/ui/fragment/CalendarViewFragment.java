package com.wuyineng.handpraise.ui.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.rey.material.widget.Button;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.dao.StreamDao;
import com.wuyineng.handpraise.domain.Stream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wuyineng on 2016/4/24.
 * 描述：日历显示
 */
public class CalendarViewFragment extends BaseFragment{

    private static final int FINISH = 1;
    @Bind(R.id.calendarView)
    MaterialCalendarView mCalendarView;
    @Bind(R.id.bt_calendar_detail)
    Button mBt_showData;
    @Bind(R.id.tv_calendar_click)
    TextView mTv_calendar_click;
    @Bind(R.id.lv_calendar_item)
    ListView mLv_detail;

    private StreamDao mDao;
    private List<Stream> mAllData;
    private List<Stream> mMoreData = new ArrayList<>();
    private MyAdapter mAdapter;

    List<CalendarDay> calendarDays = new ArrayList<>();

    @Override
    protected View initView() {

        View view = View.inflate(mMainActivity, R.layout.fragment_calendar, null);

        mDao = new StreamDao(getActivity());

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void initData() {

        mAllData = mDao.getAllData();

        mAdapter = new MyAdapter();

        Calendar cal = Calendar.getInstance();
//      从数据库取出数据，如果日期有记账的就进行标记
        for (int i = 0; i < mAllData.size(); i++){
            Stream stream = mAllData.get(i);
            long date = stream.getDate();
            cal.setTimeInMillis(date);
//            转化
            CalendarDay day = CalendarDay.from(cal);
            calendarDays.add(day);
        }

        mCalendarView.addDecorator(new EventDecorator(Color.RED,calendarDays));

        super.initData();
    }

    class MyAdapter extends BaseAdapter{

        private float mX;

        @Override
        public int getCount() {
            return mMoreData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Stream bean = mMoreData.get(position);
            final View view;
            final ViewHolder viewHolder;
            if (convertView == null){
                view = View.inflate(mMainActivity,R.layout.item_calendar_detail,null);

                viewHolder = new ViewHolder();

                viewHolder.incomeAndPay = (TextView) view.findViewById(R.id.tv_listView_calendar_item_detail);

                viewHolder.comment= (TextView) view.findViewById(R.id.tv_listView_calendar_item_comment);

                viewHolder.delete = (android.widget.Button) view.findViewById(R.id.bt_listView_item_delete);
                view.setTag(viewHolder);

            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.incomeAndPay.setText("收入：" + bean.getIncome() + "元 ；" + "支出：" + bean.getPay() + "元");
            viewHolder.comment.setText("备注：" + bean.getComment());
//            为listView设置侧滑删除功能
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final ViewHolder holder = (ViewHolder) v.getTag();

                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        mX = event.getX();

                    }else if (event.getAction() == MotionEvent.ACTION_UP){
                        float ux = event.getX();

                        if (holder.delete != null){
                            if (ux - mX > 20){
                                holder.delete.setVisibility(View.GONE);
                            }else if (mX - ux > 20){
                                holder.delete.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    return true;
                }
            });
//          listView的删除事件
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMoreData.remove(position);
                    mDao.delete(bean.getId());
                    notifyDataSetChanged();
                    viewHolder.delete.setVisibility(View.GONE);
//                    再调用一遍用于刷新
                    mCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
                }
            });
            return view;
        }
    }


    class ViewHolder{
        TextView incomeAndPay;
        TextView comment;
        android.widget.Button delete;
    }

    @Override
    protected void initEvent() {

//      当点击日历中的日期
        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
//              遍历数据库
                for (int i = 0; i < mAllData.size(); i++){
                    Stream bean = mAllData.get(i);
                    long beanDate = bean.getDate();
//                    获得日期实例
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getDefault());
//                    设定日历，通过日历获取年月日
                    cal.setTimeInMillis(beanDate);

                    Date data_time = cal.getTime();

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
//                  选择日期所对应的年月日
                    int select_year = date.getYear();
                    int select_month = date.getMonth();
                    int select_day = date.getDay();

                    //mBt_showData.setVisibility(View.VISIBLE);

                    Log.d("data_year", "data_year: " + year + "年" +month + "月" + day + "日");
                    Log.d("select_date", "onDateSelected: " + select_year + "年" + select_month + "月" + select_day + "日");

                    //if (date.getDate() == data_time)//要是用这条语句就判断不出来
//                    如果选择的日期与数据库中的日期存在相等，就把当天的总的收支情况计算出来
                    if (year == select_year && month == select_month && day == select_day){

                        //                        根据日期获取当天的项目
                        mMoreData = mDao.getMoreData(beanDate);

                        Log.d("date.getDate()", date.getDate() + "");
                        Log.d("data_time", data_time + "");

                        new Thread(){
                            @Override
                            public void run() {
                                handler.obtainMessage(FINISH).sendToTarget();

                                super.run();
                            }
                        }.start();

//                      获取一天当中的所有数据

                        double[] sameDateData = mDao.getSameDateData(beanDate);

                        int monthAdd = select_month + 1;
                        mBt_showData.setText(select_year + "年" + monthAdd + "月" + select_day + "日\n" +
                                "总收入：" + sameDateData[0] + "元 ; "
                                + "总支出：" + sameDateData[1] +"元");

                    }else {
                        mLv_detail.setVisibility(View.GONE);
                        mTv_calendar_click.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
        super.initEvent();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FINISH){
                showListItem();
            }
            super.handleMessage(msg);
        }
    };



//    内部类，用来装饰被选中的日期

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }

    /**
     * 点击按钮查看详情，显示listView
     */
    private void showListItem() {
        mBt_showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_calendar_click.setVisibility(View.GONE);
                mLv_detail.setVisibility(View.VISIBLE);

                mLv_detail.setAdapter(mAdapter);
            }
        });
    }
}

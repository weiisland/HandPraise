package com.wuyineng.handpraise.guidefragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyineng on 2016/4/18.
 * 描述：引导界面
 */
public class GuideFragmentActivity extends FragmentActivity {

    private List<Fragment> mFragments;//fragment的容器
    private ViewPager mVp_guides;
    private LinearLayout mLl_points;//一个线性布局用来装载底部的圆点
    private View mV_redPoints;
    private int mDisPoints;//圆点之间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

        initEvent();
    }

    private void initEvent() {
        
        mV_redPoints.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mV_redPoints.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                mDisPoints = mLl_points.getChildAt(1).getLeft() - mLl_points.getChildAt(0).getLeft();
            }
        });

        mVp_guides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//                间距乘以主界面偏移比例等于红点应该移动的距离,为了能够停在第二点，且能够往第三个点移动，所以加上position
                float leftMargin = mDisPoints * (position + positionOffset);
//                 红点位于相对布局中
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mV_redPoints.getLayoutParams();

                layoutParams.leftMargin = Math.round(leftMargin);//对float类型四舍五入

                mV_redPoints.setLayoutParams(layoutParams);


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {

        mFragments = new ArrayList<>();

        Fragment[] fragments = new Fragment[]{new Guide1Fragment(),new Guide2Fragment(),new Guide3Fragment()};

        for (int i =0 ; i < fragments.length; i++){

            mFragments.add(fragments[i]);

            View v_point = new View(getApplicationContext());

            v_point.setBackgroundResource(R.drawable.gray_point);

            int dip = 10;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(),dip),
                    DensityUtil.dip2px(getApplicationContext(), dip));//单位是px
//          设置点与点之间的空隙
//            第一个点不需要指定
            if (i != 0){
                params.leftMargin = 20;
            }

            v_point.setLayoutParams(params);

            mLl_points.addView(v_point);

        }




/*        mFragments.add(new Guide1Fragment());
        mFragments.add(new Guide2Fragment());
        mFragments.add(new Guide3Fragment());*/

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments);

        mVp_guides.setAdapter(adapter);
    }

    private void initView() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_guide);

        mVp_guides = (ViewPager) findViewById(R.id.vp_guide_pages);

        mLl_points = (LinearLayout) findViewById(R.id.ll_guide_points);

        mV_redPoints = findViewById(R.id.v_guide_redpoint);

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter{

        /**
         * @param fm
         * @param fragments
         * 自己重载过的构造函数
         */
        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}

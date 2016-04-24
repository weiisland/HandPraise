package com.wuyineng.handpraise.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.guidefragment.GuideFragmentActivity;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

/**
 * Created by wuyineng on 2016/4/17.
 * 描述：
 */
public class SplashActivity extends Activity {

    private LinearLayout mLl_mainView;
    private AlphaAnimation mAa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initAnimation();

        initEvent();
    }

    private void initEvent() {
        mAa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (SpTool.getBoolean(getApplicationContext(), MyConstants.IS_SKIP, false) ||
                        SpTool.getBoolean(getApplicationContext(),MyConstants.IS_FINISH_SETUP,false)){
//                    true,设置过，进入主界面
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
//                    false，没设置过，进入向导界面
                    Intent intent = new Intent(SplashActivity.this, GuideFragmentActivity.class);
                    startActivity(intent);


                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void initAnimation() {

        mAa = new AlphaAnimation(0,1);
        mAa.setDuration(2000);
        mAa.setFillAfter(true);
        mLl_mainView.startAnimation(mAa);

    }

    private void initView() {
        setContentView(R.layout.activity_splash);

        mLl_mainView = (LinearLayout) findViewById(R.id.ll_splash);

    }


}

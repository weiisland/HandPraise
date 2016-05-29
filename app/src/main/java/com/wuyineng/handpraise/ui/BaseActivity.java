package com.wuyineng.handpraise.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wuyineng on 2016/4/19.
 * 描述：
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();

        initData();

        initEvent();
    }


    protected void replaceFragment(int id , Fragment fragment){

        FragmentManager sfm = getSupportFragmentManager();
        FragmentTransaction transaction = sfm.beginTransaction();
        transaction.replace(id,fragment);
        transaction.commit();

    }

    protected void initData() {

    }


    protected void initEvent() {

    }

    protected abstract void initView();

}

package com.wuyineng.handpraise.ui;

import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.ui.fragment.CalendarViewFragment;
import com.wuyineng.handpraise.ui.fragment.ContentFragment;
import com.wuyineng.handpraise.ui.fragment.SettingFragment;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    private TextView tv_today;
    private ActionBarDrawerToggle mToggle;

    public void initData() {
        mToolbar.setBackgroundColor(Color.rgb(00, 119, 217));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name,R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);

        mToolbar.setTitle(R.string.main);
    }

    @Override
    protected void initEvent() {
        mNavigationView.setNavigationItemSelectedListener(this);

        super.initEvent();
    }

    protected void initView() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        replaceFragment(R.id.fl_home_content, new ContentFragment());
//      获取抽屉顶部的view
        View headerView = mNavigationView.getHeaderView(0);
        tv_today = (TextView) headerView.findViewById(R.id.today_time);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        tv_today.setText(sdf.format(System.currentTimeMillis()));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//          如果抽屉一开始是打开的
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }else {
                finish();
            }
            return true;//第二次按下会退出
        }
        return super.onKeyDown(keyCode, event);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mToolbar.setTitle(R.string.main);
            replaceFragment(R.id.fl_home_content, new ContentFragment());
        } else if (id == R.id.nav_stream) {

            mToolbar.setTitle(R.string.calendar);
            replaceFragment(R.id.fl_home_content,new CalendarViewFragment());

        } else if (id == R.id.nav_setting) {

            mToolbar.setTitle(R.string.setting);

            replaceFragment(R.id.fl_home_content, new SettingFragment());
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}

package com.wuyineng.handpraise.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.util.ThemeUtil;
import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.ui.fragment.CalendarViewFragment;
import com.wuyineng.handpraise.ui.fragment.ContentFragment;
import com.wuyineng.handpraise.ui.fragment.SettingFragment;
import com.wuyineng.handpraise.utils.MyConstants;
import com.wuyineng.handpraise.utils.SpTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;

    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;

    private TextView tv_today;


    private ActionBarDrawerToggle mToggle;

    private long exitTime;



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
        ViewUtils.inject(this);

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
            }else if (System.currentTimeMillis() - exitTime > 2000){
                Toast.makeText(getApplicationContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
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

            mToolbar.setTitle("流水日历");
            replaceFragment(R.id.fl_home_content,new CalendarViewFragment());

        } else if (id == R.id.nav_setting) {

            mToolbar.setTitle("设置");

            replaceFragment(R.id.fl_home_content, new SettingFragment());

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}

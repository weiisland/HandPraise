package com.wuyineng.handpraise.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyineng.handpraise.ui.MainActivity;

/**
 * Created by wuyineng on 2016/4/19.
 * 描述：
 */
public abstract class BaseFragment extends Fragment {

    protected MainActivity mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    protected abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

        initEvent();
    }

    protected void initEvent() {

    }

    protected void initData() {

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}

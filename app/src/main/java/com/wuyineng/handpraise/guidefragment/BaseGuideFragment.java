package com.wuyineng.handpraise.guidefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wuyineng on 2016/4/18.
 * 描述：
 */
public abstract class BaseGuideFragment extends Fragment {

    protected GuideFragmentActivity mGuideFragmentActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mGuideFragmentActivity = (GuideFragmentActivity) getActivity();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = initView();

        return root;
    }

    protected boolean isVisible;//当前fragment是否可见

    /**
     * @param isVisibleToUser
     * viewPager的懒加载，若是当前fragment不可见就不加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible = true;
            onVisible();

        }else {
            isVisible = false;
            onInVisible();
        }
    }

    protected void onInVisible() {

    }

    protected void onVisible() {
        initData();
    }

    protected abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        initEvent();

        initData();

        super.onActivityCreated(savedInstanceState);
    }

    protected void initData() {

    }

    protected void initEvent(){}
}

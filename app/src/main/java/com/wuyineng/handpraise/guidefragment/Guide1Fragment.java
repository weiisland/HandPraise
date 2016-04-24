package com.wuyineng.handpraise.guidefragment;

import android.view.View;

import com.wuyineng.handpraise.R;

/**
 * Created by wuyineng on 2016/4/18.
 * 描述：滚动的第一个界面
 */
public class Guide1Fragment extends BaseGuideFragment{
    @Override
    public View initView() {

        View root = View.inflate(mGuideFragmentActivity, R.layout.item_guide_1, null);
        return root;
    }
}

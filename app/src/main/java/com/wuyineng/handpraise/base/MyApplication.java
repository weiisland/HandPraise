package com.wuyineng.handpraise.base;

import android.app.Application;
import android.content.Context;

import java.util.logging.Logger;

/**
 * Created by wuyineng on 2016/4/24.
 * 描述：
 */
public class MyApplication extends Application {

        private static Context mContext;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = this;
        }

        public static Context getContext() {
            return mContext;
        }

}

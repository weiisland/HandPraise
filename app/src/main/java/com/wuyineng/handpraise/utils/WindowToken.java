package com.wuyineng.handpraise.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by wuyineng on 2016/4/20.
 * 描述：调出系统软键盘
 */
public class WindowToken {


    public static void getSystemKeyboard(Context context ,EditText editText){
        InputMethodManager imm =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInputFromInputMethod(editText.getWindowToken(),0);
    }

}

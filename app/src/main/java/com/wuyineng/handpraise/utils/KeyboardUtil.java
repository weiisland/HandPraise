package com.wuyineng.handpraise.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.wuyineng.handpraise.R;
import com.wuyineng.handpraise.ui.MainActivity;

/**
 * Created by wuyineng on 2016/4/24.
 * 描述：自定义键盘
 */
public class KeyboardUtil {
    private Context mContext;
    private KeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private EditText ed;
    public KeyboardUtil(Activity activity, Context context, EditText edit) {
        mContext = context;
        this.ed = edit;
        k = new Keyboard(context, R.xml.keyboard);
        keyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setVisibility(View.VISIBLE);
        keyboardView.setOnKeyboardActionListener(listener);
    }
    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {

        }
        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            //Editable :This is the interface for text whose content and markup can be changed

            Editable editable = ed.getText();
            int start = ed.getSelectionStart();



            if (primaryCode == Keyboard.KEYCODE_DELETE) {// back
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 5777) {// OK

                    Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
                    mContext.startActivity(intent);


            } else if (primaryCode == 5789) {// 清空
                editable.clear();
            } else if (primaryCode == 61){//  =
                String[] strings = editable.toString().split("\\+");

                int total = 0;

                for (int i = 0; i < strings.length; i++){
                    total += Integer.parseInt(strings[i]);
                }
                ed.setText(total + "");
                ed.setSelection((total + "").length());
            }

            else { //将要输入的数字现在编辑框中

                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
}


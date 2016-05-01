package com.wuyineng.handpraise.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wuyineng on 2016/4/26.
 * 描述：存储收支流水的数据库
 */
public class StreamDB extends SQLiteOpenHelper {


    public StreamDB(Context context) {
        super(context, "stream.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE stream(_id integer primary key autoincrement, " +
                "income text, pay text, comment text, record_time integer)");//保存long类型，可用数据库的integer类型
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop TABLE stream");
        onCreate(db);
    }
}

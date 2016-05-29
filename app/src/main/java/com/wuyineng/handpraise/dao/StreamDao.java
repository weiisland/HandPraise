package com.wuyineng.handpraise.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wuyineng.handpraise.db.StreamDB;
import com.wuyineng.handpraise.domain.Stream;
import com.wuyineng.handpraise.utils.StreamTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyineng on 2016/4/26.
 * 描述：对StreamDB进行CRUD操作
 */
public class StreamDao {

    private StreamDB mStreamDB;
    private List<Stream> data;
    private static StreamDao mDao;

    public StreamDao(Context context){
        mStreamDB= new StreamDB(context);
    }

    public static StreamDao get(Context c){
        if (mDao == null){
            mDao = new StreamDao((c.getApplicationContext()));//application context是针对应用的全局性Context
        }
        return  mDao;
    }

    /**
     * @param time
     * @return
     * 根据日期返回部分数据
     */
    public List<Stream> getMoreData(long time){
        data = new ArrayList<>();

        SQLiteDatabase db = mStreamDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + StreamTable.ID+ "," + StreamTable.INCOME + "," + StreamTable.PAY  + "," + StreamTable.COMMENT
                + " from " + StreamTable.STREAM_TABLE
                + " where " + StreamTable.RECORD_TIME + " = " + time, null);

        while (cursor.moveToNext()){
            Stream bean = new Stream();
            bean.setId(cursor.getInt(0));
            bean.setIncome(cursor.getString(1));
            bean.setPay(cursor.getString(2));
            bean.setComment(cursor.getString(3));
            data.add(bean);
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * @return
     * 返回所有的数据
     */
    public List<Stream> getAllData(){
        data = new ArrayList<>();

        SQLiteDatabase db = mStreamDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + StreamTable.ID+ "," +  StreamTable.INCOME + "," + StreamTable.PAY
                + "," + StreamTable.COMMENT+ "," + StreamTable.RECORD_TIME + " from " + StreamTable.STREAM_TABLE, null);

        while (cursor.moveToNext()){
            Stream bean = new Stream();
            bean.setId(cursor.getInt(0));
            bean.setIncome(cursor.getString(1));
            bean.setPay(cursor.getString(2));
            bean.setComment(cursor.getString(3));
            bean.setDate(cursor.getLong(4));
            data.add(bean);
        }
        cursor.close();
        db.close();
        return data;
    }

    /**
     * @param date
     *        传入的时间戳
     * @return
     *        根据给定的时间戳查询数据库中相同的时间的数据段，并把相应的数据段相加
     */
    public double[] getSameDateData(long date){

        SQLiteDatabase db = mStreamDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + StreamTable.INCOME + "," + StreamTable.PAY + " from " + StreamTable.STREAM_TABLE
                + " where " + StreamTable.RECORD_TIME + " = " + date, null);

        double total_day_income = 0;
        double total_day_pay = 0;

        while (cursor.moveToNext()){
            try {

                String income = cursor.getString(0);
                double day_income = Double.parseDouble(income.trim());
                total_day_income += day_income;

                String pay = cursor.getString(1);
                double day_pay = Double.parseDouble(pay.trim());
                total_day_pay += day_pay;

            }catch (Exception e){

            }
        }

        double[] d = new double[]{total_day_income, total_day_pay};

        return d;


    }

    /**
     * 根据标题进行删除
     * @param id
     */
    public void delete(int id){
        SQLiteDatabase db = mStreamDB.getWritableDatabase();
        db.delete(StreamTable.STREAM_TABLE, StreamTable.ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


    /**
     *
     * 对数据库进行更新
     * @param id
     *          自动更新的id
     * @param income
     * @param pay
     * @param comment
     * @param date
     */
    public void update(int id, String income,String pay, String comment, long date){
        SQLiteDatabase db = mStreamDB.getReadableDatabase();

        ContentValues values = new ContentValues();
//        更新内容
        values.put(StreamTable.INCOME, income);

        values.put(StreamTable.PAY, pay);

        values.put(StreamTable.COMMENT, comment);

        values.put(StreamTable.RECORD_TIME, date);
//        根据标题内容进行更新
        db.update(StreamTable.STREAM_TABLE, values, StreamTable.ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }


    /**
     * @param bean
     */
    public void add(Stream bean){
        add(bean.getIncome(),bean.getPay(),bean.getComment(),bean.getDate());
    }


    /**
     * @param income
     * @param pay
     * @param comment
     * @param date
     *
     * 往数据库中添加数据
     */
    public void add(String income, String pay, String comment, long date){
        SQLiteDatabase db = mStreamDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StreamTable.INCOME, income);

        values.put(StreamTable.PAY, pay);

        values.put(StreamTable.COMMENT, comment);

        values.put(StreamTable.RECORD_TIME, date);

//        往表中插入一条记录
        db.insert(StreamTable.STREAM_TABLE,null,values);
        db.close();

    }
}

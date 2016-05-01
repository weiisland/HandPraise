package com.wuyineng.handpraise.domain;

import java.util.Date;

/**
 * Created by wuyineng on 2016/4/26.
 * 描述：收支的bean
 */
public class Stream {

    private int mId;//对应的数据库id编号

    private String mIncome;//收入的金额
    private String mPay;//花费的金额
    private String mComment;//备注

    private long mDate;//记录的日期,用数据库保存的

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getIncome() {
        return mIncome;
    }

    public void setIncome(String income) {
        mIncome = income;
    }

    public String getPay() {
        return mPay;
    }

    public void setPay(String pay) {
        mPay = pay;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }
}

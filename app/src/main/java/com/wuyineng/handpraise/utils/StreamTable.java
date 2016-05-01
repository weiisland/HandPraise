package com.wuyineng.handpraise.utils;

/**
 * Created by wuyineng on 2016/4/26.
 * 描述：用于指示stream数据库的列名
 */
public interface StreamTable {

    String STREAM_TABLE = "stream";//数据表名

    String ID = "_id";//自动增长的id

    String INCOME = "income";//收入

    String PAY = "pay";//支出

    String COMMENT = "comment";//备注

    String RECORD_TIME = "record_time";//记录的时间

}

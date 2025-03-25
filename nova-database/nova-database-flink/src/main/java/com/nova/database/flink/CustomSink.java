package com.nova.database.flink;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class CustomSink extends RichSinkFunction<String> {

    @Override
    public void invoke(String value, Context context) throws Exception {
        //OP字段，该字段也有4种取吃，分别是C（创建），U（修改），R（读取），D（删除）

        //对于U操作，其数据部分同时包含了，before和after
        System.err.println("value = " + value);
    }

    @Override
    public void open(Configuration parameters) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }
}

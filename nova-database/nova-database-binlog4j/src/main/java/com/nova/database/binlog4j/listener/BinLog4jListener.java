package com.nova.database.binlog4j.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import com.github.shyiko.mysql.binlog.event.deserialization.json.JsonBinary;
import com.nova.database.binlog4j.bean.BinLog4j;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author: wzh
 * @description: binlog监听
 * （1）IBinlogEventHandler<Object>支持序列化成表对象，但是要注意字段映射问题。
 * @date: 2024/07/30 13:57
 */
@Slf4j(topic = "BinLog4jListener")
@BinlogSubscriber(clientName = "master")
public class BinLog4jListener implements IBinlogEventHandler<BinLog4j> {

    /**
     * 判断当前库，当前表是否执行（可以做定制化，专门处理某一个表）
     */
    @Override
    public boolean isHandle(String database, String table) {
        if (StrUtil.equals("avicare_merchant", database) && StrUtil.equals("bin_log4j", table)) {
            log.info("isHandle --- database：{}，table：{}", database, table);
            return true;
        }
        return false;
    }

    @Override
    public void onInsert(BinlogEvent<BinLog4j> binlogEvent) {
        log.error("onInsert：{}", JSONObject.toJSONString(binlogEvent));
    }

    @Override
    public void onUpdate(BinlogEvent<BinLog4j> binlogEvent) {
        log.error("onUpdate：{}", JSONObject.toJSONString(binlogEvent));
        BinLog4j after = binlogEvent.getData();
        System.err.println("after = " + JSONObject.toJSONString(after));

        BinLog4j before = binlogEvent.getOriginalData();
        System.err.println("before = " + JSONObject.toJSONString(before));
    }

    @Override
    public void onDelete(BinlogEvent<BinLog4j> binlogEvent) {
        BinLog4j data = binlogEvent.getData();
        log.error("onDelete：{}", JSONObject.toJSONString(binlogEvent));
    }

    @SneakyThrows
    public static void main(String[] args) {
        String s = "[76, 97, 100, 105, 101, 115, 32, 97, 110, 100, 32, 103, 101, 110, 116, 108, 101, 109, 101, 110]";
        // 去除字符串中的空格和方括号
        String[] numbers = s.replaceAll("\\[|\\]|\\s", "").split(",");
        // 创建字节数组
        byte[] byteArray = new byte[numbers.length];
        // 将字符串中的数字转换为字节值
        for (int i = 0; i < numbers.length; i++) {
            byteArray[i] = Byte.parseByte(numbers[i].trim());
        }
        String decodedString = new String(byteArray, StandardCharsets.UTF_8);
        System.err.println("decodedString = " + decodedString);

        String s2 = JsonBinary.parseAsString(new byte[]{});
        System.err.println("s2 = " + s2);

        String s3 = new String(Base64.getDecoder().decode("123"));

    }
}

package com.nova.database.binlog4j;

import cn.hutool.json.JSONUtil;
import com.gitee.Jmysy.binlog4j.core.BinlogEvent;
import com.gitee.Jmysy.binlog4j.core.IBinlogEventHandler;
import com.gitee.Jmysy.binlog4j.springboot.starter.annotation.BinlogSubscriber;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wzh
 * @description: binlog监听
 * （1）IBinlogEventHandler<Object>支持序列化成表对象，但是要注意字段映射问题。
 * @date: 2024/07/30 13:57
 */
@Slf4j(topic = "BinlogListener")
@BinlogSubscriber(clientName = "master")
public class BinlogListener implements IBinlogEventHandler<Object> {

    /**
     * 判断当前库，当前表是否执行（可以做定制化，专门处理某一个表）
     */
    @Override
    public boolean isHandle(String database, String table) {
        log.info("isHandle --- database：{}，table：{}", database, table);
        return true;
    }

    @Override
    public void onInsert(BinlogEvent<Object> binlogEvent) {
        log.error("onInsert：{}", JSONUtil.toJsonStr(binlogEvent));
    }

    @Override
    public void onUpdate(BinlogEvent<Object> binlogEvent) {
        log.error("onUpdate：{}", JSONUtil.toJsonStr(binlogEvent));
    }

    @Override
    public void onDelete(BinlogEvent<Object> binlogEvent) {
        log.error("onDelete：{}", JSONUtil.toJsonStr(binlogEvent));
    }
}

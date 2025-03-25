package com.nova.database.flink;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author: wzh
 * @description: 启动类
 * @date: 2025/03/25 09:38
 */
public class MySqlJob {

    public static void main(String[] args) throws Exception {
        // 配置监听数据源
        MySqlSource<String> source = MySqlSource.<String>builder()
                .hostname("127.0.0.1")
                .port(3306)
                // 数据库集合，可以配置多个
                .databaseList("study")
                // 表集合，可以配置多个
                .tableList("study.bin_log4j")
                .username("root")
                .password("123456")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .includeSchemaChanges(true)
                .build();

        // 配置 Flink WebUI，指定一个端口
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8081);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

        // 检查点间隔时间
        // checkpoint的侧重点是“容错”，即Flink作业意外失败并重启之后，能够直接从早先打下的checkpoint恢复运行，且不影响作业逻辑的准确性。
        env.enableCheckpointing(5000);
        DataStreamSink<String> sink = env.fromSource(source, WatermarkStrategy.noWatermarks(), "MySQL Source")
                .addSink(new CustomSink());
        env.execute();
    }
}

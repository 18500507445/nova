//package com.starter.xxl;
//
//import cn.hutool.core.util.StrUtil;
//import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * xxl-job config
// *
// * @author xuxueli 2017-04-28
// */
//@Slf4j
//@Configuration
//public class XxlJobConfig {
//
//    @Value("${xxl.job.admin.addresses:default}")
//    private String adminAddresses;
//
//    @Value("${xxl.job.executor.appname:default}")
//    private String appName;
//
//    @Value("${xxl.job.executor.ip:default}")
//    private String ip;
//
//    @Value("${xxl.job.executor.port:-1}")
//    private int port;
//
//    @Value("${xxl.job.accessToken:default}")
//    private String accessToken;
//
//    @Value("${xxl.job.executor.logpath:default}")
//    private String logPath;
//
//    @Value("${xxl.job.executor.logretentiondays:-1}")
//    private int logRetentionDays;
//
//    @Bean
//    public XxlJobSpringExecutor xxlJobExecutor() {
//        if (!StrUtil.equals("default", adminAddresses)) {
//            log.info(">>>>>>>>>>> xxl-job config init.");
//            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//            xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//            xxlJobSpringExecutor.setAppname(appName);
//            xxlJobSpringExecutor.setIp(ip);
//            xxlJobSpringExecutor.setPort(port);
//            xxlJobSpringExecutor.setAccessToken(accessToken);
//            xxlJobSpringExecutor.setLogPath(logPath);
//            xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
//            return xxlJobSpringExecutor;
//        }
//        return null;
//    }
//
//    /**
//     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
//     *
//     *      1、引入依赖：
//     *          <dependency>
//     *             <groupId>org.springframework.cloud</groupId>
//     *             <artifactId>spring-cloud-commons</artifactId>
//     *             <version>${version}</version>
//     *         </dependency>
//     *
//     *      2、配置文件，或者容器启动变量
//     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
//     *
//     *      3、获取IP
//     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
//     */
//
//
//}
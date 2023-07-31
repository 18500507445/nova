package com.nova.tools;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description 方案A
 * @date: 2023/05/25 10:52
 */
@Slf4j(topic = "SpiderPlanA")
public class SpiderPlan {

    /**
     * 计时器
     */
    private static final TimeInterval timer = DateUtil.timer();

    /**
     * 获取机器核数，作为线程池数量
     */
    private static final int THREAD_COUNT = 16 * Runtime.getRuntime().availableProcessors();

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNamePrefix("spider").build();

    /**
     * CompletableFuture使用的线程池
     */
    private static final ExecutorService FUTURE_POOL = Executors.newFixedThreadPool(THREAD_COUNT / 5);

    /**
     * 手动创建线程池
     */
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
            30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 模拟任务数量
     */
    private static final int COUNT = 7_200_000;

    private static final int PAGE_SIZE = 10_000;

    private static final String PROXY_URL = "http://http.tiqu.alibabaapi.com/getip?num=3&type=2&pack=119455&port=1&lb=1&pb=45&regions=";

    private static final String SEARCH_URL = "{\"area\":\"1_72_55657_0\",\"pin\":\"\",\"fields\":\"11100000\",\"skuIds\":\"%s\",\"source\":\"pc-item\"}";

    private static final String BASE_SEARCH_URL = "https://api.m.jd.com/?appid=item-v3&functionId=pctradesoa_getprice&client=pc&clientVersion=1.0.0&t=%s&body=";

    /**
     * 单纯的IO型，最佳线程数目 = （（线程等待时间+线程CPU时间）/线程CPU时间 ）* CPU数目
     * 线程等待（网络IO、磁盘IO），线程cpu计算
     * 理论720w数据，分片720，每片1w对应开启一个线程。单片，1w数据，平均每次请求0.5s，请求超时设置尽量短些到时间断开，失败重试策略（暂不考虑，可用mq做补偿，不要影响主task）
     * <p>
     * 本地测试
     * coreNum = 8，耗时：1min，次数：1000
     * coreNum = 50，耗时：10s，次数：1000
     * coreNum = 80，耗时：7.8s，次数：1000
     * coreNum = 100，耗时：7.7s，次数：1000
     * coreNum = 700，耗时：7.7s，次数：1000
     * <p>
     * <p>
     * 满足上述公式所以可以根据网络IO时间测算coreNum
     * <p>
     * 网络IO 500ms，临界值coreNum = 80
     * 耗时：13min，次数：100000
     * 耗时：130min，次数：1000000
     * 耗时：862min，次数：7200000 理论15.6h
     * <p>
     * 网络IO 1000ms，临界值coreNum = 160
     * 耗时：7.3s，次数：1000
     *
     * @param args
     */
    public static void main(String[] args) {
        String skuId = "100038004347";
        List<String> queryList = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            queryList.add(skuId);
        }
        List<List<String>> partition = ListUtil.partition(queryList, PAGE_SIZE);
        LongAdder longAdder = new LongAdder();

        for (List<String> skuIds : partition) {
            EXECUTOR_POOL.submit(new Task(skuIds, longAdder));
        }
    }

    @Test
    public void urlTest() {
        String price = "-1";
        try {
            String ipJson = HttpUtil.createGet("https://dps.kdlapi.com/api/getdps/?secret_id=odtvpvb9ur5r3r3lbol8&num=30&signature=wx8jhlptw89teyuydrxgbh4mg4wzm7lh&pt=1&format=json&sep=1").timeout(1500).execute().body();
            JSONObject jsonObject = JSON.parseObject(ipJson);
            JSONArray array = jsonObject.getJSONArray("data");
            List<Object> randomList = RandomUtil.randomEles(array, 2);
            JSONObject ipObject = JSON.parseObject(Convert.toStr(randomList.get(0)));

            String skuId = "100037437645";
            String url = String.format(SEARCH_URL, skuId);
            url = String.format(BASE_SEARCH_URL, System.currentTimeMillis()) + URLEncodeUtil.encode(url, Charset.defaultCharset());
            price = okHttp(url, ipObject.getString("ip"), ipObject.getIntValue("port"));
        } catch (Exception e) {
            System.err.println("e = " + e);
        }
        System.err.println("耗时：" + timer.interval() + " ms，price：" + price);
    }

    static class Task implements Runnable {
        /**
         * 每次1w条
         */
        private final List<String> skuIds;

        /**
         * 计数器
         */
        private final LongAdder longAdder;

        public Task(List<String> skuIds, LongAdder longAdder) {
            this.skuIds = skuIds;
            this.longAdder = longAdder;
        }

        @Override
        public void run() {
            for (String skuId : skuIds) {
                ThreadUtil.sleep(600);
                longAdder.increment();
                log.info("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，价格：0，次数：" + longAdder.longValue());
                //查询到价格后发送mq，利用消息队列解耦，提高吞吐
                sendMq(skuId, "0");
            }
        }

        /**
         * 模拟发送mq
         *
         * @param skuId
         * @param price
         */
        public void sendMq(String skuId, String price) {
            ThreadUtil.sleep(5);
        }
    }

    /**
     * 爬虫任务
     */
    static class TaskA implements Runnable {

        /**
         * 随机的ipList
         */
        private final List<Object> randomList;

        /**
         * 最终的请求url
         */
        private final String finalUrl;

        /**
         * 计数器
         */
        private final LongAdder longAdder;

        public TaskA(List<Object> randomList, String finalUrl, LongAdder longAdder) {
            this.randomList = randomList;
            this.finalUrl = finalUrl;
            this.longAdder = longAdder;
        }

        @Override
        public void run() {
            String price = "";
            List<CompletableFuture<String>> completableFutures = randomList.stream().map(s -> CompletableFuture.supplyAsync(() -> {
                JSONObject ipObject = JSON.parseObject(Convert.toStr(s));
                try {
                    return toolHttp(finalUrl, ipObject.getString("ip"), ipObject.getIntValue("port"));
                } catch (Exception ignored) {

                }
                return "";
            }, FUTURE_POOL)).collect(Collectors.toList());

            //阻塞主线程，任意完成anyOf立即返回，allOf全部完成再返回
            CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0])).join();
            //获得执行结果
            for (CompletableFuture<String> result : completableFutures) {
                try {
                    if (StrUtil.isNotBlank(result.get())) {
                        price = result.get();
                        longAdder.increment();
                        System.err.println("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，价格：" + price + "，次数：" + longAdder.longValue());
                        timer.restart();
                        break;
                    }
                } catch (Exception ignored) {

                }
            }

            if (StrUtil.isNotBlank(price)) {
                //模拟改库
                insert(price);
            }
        }

        static public void insert(String price) {
            ThreadUtil.sleep(50);
        }
    }


    private static String okHttp(String url, String ip, int port) throws IOException {
        String result = "";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //代理服务器的IP和端口号
        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
        OkHttpClient httpClient = builder
                //设置读取超时时间
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .connectionPool(new ConnectionPool(5, 10, TimeUnit.SECONDS))
                //设置写的超时时间
                .writeTimeout(200, TimeUnit.MILLISECONDS)
                .connectTimeout(200, TimeUnit.MILLISECONDS).build();

        Request request = new Request.Builder().url(url).build();
        Response httpResponse = httpClient.newCall(request).execute();
        //得到服务响应状态码
        if (httpResponse.code() == 200) {
            if (ObjectUtil.isNotNull(httpResponse.body())) {
                JSONArray jsonArray = JSON.parseArray(httpResponse.body().string());
                result = jsonArray.getJSONObject(0).getString("p");
            }
        }
        return result;
    }

    private static String toolHttp(String url, String ip, int port) {
        String result = "";
        HttpResponse execute = HttpUtil.createGet(url).setHttpProxy(ip, port).timeout(500).execute();
        if (ObjectUtil.isNotNull(execute) && 200 == execute.getStatus()) {
            JSONArray jsonArray = JSON.parseArray(Convert.toStr(execute.body()));
            result = jsonArray.getJSONObject(0).getString("p");
        }
        return result;
    }


}

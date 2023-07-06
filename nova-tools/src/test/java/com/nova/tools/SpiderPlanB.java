package com.nova.tools;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
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
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description 方案B
 * @date: 2023/05/22 17:59
 */
@Slf4j(topic = "SpiderPlanB")
public class SpiderPlanB {

    /**
     * 太阳代理
     */
    private static final String PROXY_URL = "http://http.tiqu.alibabaapi.com/getip?num=3&type=2&pack=119455&port=1&lb=1&pb=45&regions=";

    private static final String SEARCH_URL = "{\"area\":\"1_72_55657_0\",\"pin\":\"\",\"fields\":\"11100000\",\"skuIds\":\"%s\",\"source\":\"pc-item\"}";

    private static final String BASE_SEARCH_URL = "https://api.m.jd.com/?appid=item-v3&functionId=pctradesoa_getprice&client=pc&clientVersion=1.0.0&t=%s&body=";

    /**
     * 计时器
     */
    private static final TimeInterval timer = DateUtil.timer();

    /**
     * 获取机器核数，作为线程池数量
     */
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

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
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(THREAD_COUNT / 5, THREAD_COUNT / 5,
            30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 信号量，控制获取ip的并发数，暂时不用了
     */
    private final Semaphore SEMAPHORE = new Semaphore(THREAD_COUNT / 5);

    /**
     * 模拟任务数量
     */
    private static final int COUNT = 400;

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
            System.out.println("e = " + e);
        }
        System.out.println("耗时：" + timer.interval() + " ms，price：" + price);
    }

    /**
     * 线程池版本+并行http版
     * planA：2core，2max，2-future-core，120s，成功率98%
     * planB：3core，3max，2-future-core，150s，成功率95%
     * planC：10core，20max，max-future-core，15s，成功率77%
     * planD：20core，50max，max-future-core，15s，成功率72%
     * planE：50core，100max，max-future-core，15s，成功率72%
     * </p>
     * 最优方案A，进性测算，单服务，60s，200条，64台1min也就是12800，7200000w数据/12800/60 = 9.3h
     */
    @Test
    public void demoA() {
        String ipJson = HttpUtil.createGet(PROXY_URL).timeout(1500).execute().body();
        JSONObject jsonObject = JSON.parseObject(ipJson);
        JSONArray array = jsonObject.getJSONArray("data");

        String skuId = "100038004347";
        String url = String.format(SEARCH_URL, skuId);
        url = String.format(BASE_SEARCH_URL, System.currentTimeMillis()) + URLEncodeUtil.encode(url, Charset.defaultCharset());

        LongAdder longAdder = new LongAdder();
        if (ObjectUtil.isNotNull(array)) {
            //模拟多线程处理task
            for (int i = 0; i < COUNT; i++) {
                //方案1 每次从list取不同的2个ip+port去并行请求，方案2 每次拿相同的ip+port去并行请求（randomEles）
                List<Object> randomList = RandomUtil.randomEleList(array, 2);
                EXECUTOR_POOL.submit(new TaskA(randomList, url, longAdder));
            }
        }
        ThreadUtil.sleep(5 * 60 * 3600);
    }

    /**
     * spider目前版本，不用线程池，用sleep控制并发数
     * sleep 600，time：240s，成功率：93%，并发2
     * sleep 10，time：5s，成功率：50-55%，并发100
     * </p>
     * 测算，单服务60s，100条，64台1min也就是6400，7200000w数据/6400/60 = 18.6h，再加上代理ip队列阻塞重试时间
     */
    @Test
    public void demoB() {
        String ipJson = HttpUtil.createGet(PROXY_URL).timeout(1500).execute().body();
        JSONObject jsonObject = JSON.parseObject(ipJson);
        JSONArray array = jsonObject.getJSONArray("data");

        String skuId = "100038004347";
        String url = String.format(SEARCH_URL, skuId);
        url = String.format(BASE_SEARCH_URL, System.currentTimeMillis()) + URLEncodeUtil.encode(url, Charset.defaultCharset());

        LongAdder longAdder = new LongAdder();
        if (ObjectUtil.isNotNull(array)) {
            for (int i = 0; i < COUNT; i++) {
                List<Object> randomList = RandomUtil.randomEleList(array, 1);
                Thread t = new Thread(new TaskB(randomList, url, longAdder));
                t.start();
                ThreadUtil.sleep(600);
            }
        }
    }

    /**
     * 测试线程，cpu空闲占比
     *
     */
    @Test
    public void demoC() throws ExecutionException, InterruptedException {
        doJob(8);
    }

    public static void doJob(int threadNum) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
        List<Future<?>> taskList = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            taskList.add(threadPoolExecutor.submit(() -> {
                taskC();
            }));
        }
        for (Future<?> future : taskList) {
            future.get();
        }
        System.out.println(threadNum + "个线程，耗时：" + timer.interval() / 1000 + "秒，停顿占比：" + NumberUtil.roundStr(Convert.toDouble(PARK_TIME * 100.0 / timer.interval()), 2) + "%");
        threadPoolExecutor.shutdown();
    }


    /**
     * 任务a
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
                        System.out.println("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，价格：" + price + "，次数：" + longAdder.longValue());
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
    }

    /**
     * 任务b
     */
    static class TaskB implements Runnable {

        private final List<Object> randomList;

        private final String finalUrl;

        private final LongAdder longAdder;

        public TaskB(List<Object> randomList, String finalUrl, LongAdder longAdder) {
            this.randomList = randomList;
            this.finalUrl = finalUrl;
            this.longAdder = longAdder;
        }

        @Override
        public void run() {
            JSONObject ipObject = JSON.parseObject(Convert.toStr(randomList.get(0)));
            try {
                String price = okHttp(finalUrl, ipObject.getString("ip"), ipObject.getIntValue("port"));
                if (StrUtil.isNotBlank(price)) {
                    longAdder.increment();
                    System.out.println("time：" + DateUtil.now() + "，耗时：" + timer.interval() + "ms，价格：" + price + "，次数：" + longAdder.longValue());
                    timer.restart();

                    //模拟改库
                    insert(price);
                }
            } catch (Exception ignored) {

            }
        }
    }

    static public void insert(String price) {
        ThreadUtil.sleep(50);
    }

    public static int PARK_TIME = 0;

    public static Long taskC() {
        long result = 0L;
        PARK_TIME = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (i % 10_000_000 == 0) {
                try {
                    ++PARK_TIME;
                    // 模拟IO
                    LockSupport.parkNanos(100_000_000);
                } catch (Exception ignore) {
                }
            }
            result += i;
        }
        return result;
    }

    public static Long taskD() {
        long result = 0L;
        PARK_TIME = 0;
        for (int i = 0; i < 100; i++) {
            ++PARK_TIME;
            ThreadUtil.sleep(500);
            result += i;
        }
        return result;
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

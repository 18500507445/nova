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
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.nova.common.utils.thread.Threads;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description 测试爬虫
 * @date: 2023/05/22 17:59
 */
@Slf4j(topic = "TestSpider")
public class TestSpider {

    /**
     * 太阳代理
     */
    private static final String PROXY_URL = "http://http.tiqu.alibabaapi.com/getip?num=3&type=2&pack=119426&port=1&lb=1&pb=45&regions=";

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

    /**
     * Excel实体类
     */
    @Data
    static class Product {
        @ExcelProperty(value = "sku_id", index = 0)
        private String skuId;
    }

    @Test
    public void randomTest() {
        int j = RandomUtil.randomInt(1000);
        System.out.println(j * 10);
    }

    @Test
    public void excelTest() {
        String fileName = "/Users/wangzehui/Documents/IdeaProjects/nova/nova-tools/src/main/resources/sku_id.xlsx";
        EasyExcel.read(fileName, Product.class, new ReadListener<Product>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 500000;

            /**
             * 临时存储
             */
            private List<Product> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(Product data, AnalysisContext context) {
                cachedDataList.add(data);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    saveData();
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                saveData();
            }

            /**
             * 加上存储数据库
             */
            private void saveData() {
                log.info("{}条数据，开始存储数据库！", cachedDataList.size());
                log.info("存储数据库成功！");
            }
        }).sheet().doRead();

    }

    @Test
    public void urlTest() {
        try {
            String github = HttpUtil.createGet("https://github.com/").timeout(1200).execute().body();
        } catch (Exception ignored) {

        }
        System.out.println(timer.interval() + " ms");
    }

    /**
     * 2core，2max，2-future-core，120s，成功率98%
     * 3core，3max，2-future-core，150s，成功率95%
     * 10core，20max，max-future-core，15s，成功率77%
     * 20core，50max，max-future-core，15s，成功率72%
     * 50core，100max，max-future-core，15s，成功率72%
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
                List<Object> randomList = RandomUtil.randomEleList(array, 2);
                EXECUTOR_POOL.submit(new TaskA(randomList, url, longAdder));
            }
        }
        ThreadUtil.sleep(5 * 60 * 3600);
        Threads.stop(EXECUTOR_POOL);
    }

    /**
     * 不用线程池，用sleep控制并发数，spider方式
     * sleep 600，time：240s，成功率：93%，并发2
     * sleep 10，time：5s，成功率：50-55%，并发100
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
//        ThreadUtil.sleep(5 * 60 * 3600);
    }

    /**
     * 测试线程，cpu空闲占比
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void demoC() throws ExecutionException, InterruptedException {
        doJob(10);
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
                    HttpResponse execute = HttpUtil.createGet(finalUrl).setHttpProxy(ipObject.getString("ip"), ipObject.getIntValue("port")).timeout(1500).execute();
                    if (ObjectUtil.isNotNull(execute) && 200 == execute.getStatus()) {
                        JSONArray jsonArray = JSON.parseArray(Convert.toStr(execute.body()));
                        return jsonArray.getJSONObject(0).getString("p");
                    }
                } catch (Exception ignored) {

                }
                return "";
            }, FUTURE_POOL)).collect(Collectors.toList());

            //阻塞主线程，任意完成anyOf立即返回，allOf全部完成再返回
            CompletableFuture.anyOf(completableFutures.toArray(new CompletableFuture[0])).join();
            //获得执行结果
            for (CompletableFuture<String> result : completableFutures) {
                try {
                    if (ObjectUtil.isNotNull(result.get())) {
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
            String price = "";
            JSONObject ipObject = JSON.parseObject(Convert.toStr(randomList.get(0)));
            try {
                HttpResponse execute = HttpUtil.createGet(finalUrl).setHttpProxy(ipObject.getString("ip"), ipObject.getIntValue("port")).timeout(1500).execute();
                if (ObjectUtil.isNotNull(execute) && 200 == execute.getStatus()) {
                    JSONArray jsonArray = JSON.parseArray(Convert.toStr(execute.body()));
                    price = jsonArray.getJSONObject(0).getString("p");
                }
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

}

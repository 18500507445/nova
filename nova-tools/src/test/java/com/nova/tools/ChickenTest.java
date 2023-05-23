package com.nova.tools;

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

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author: wzh
 * @description 鸡翅老哥帮帮忙，机器8核64g，看看还能有优化吗
 * @date: 2023/05/23 19:16
 */
public class ChickenTest {

    //太阳代理，每次获取3个ip，num=3
    private static final String PROXY_URL = "http://http.tiqu.alibabaapi.com/getip?num=3&type=2&pack=119426&port=1&lb=1&pb=45&regions=";

    private static final String SEARCH_URL = "{\"area\":\"1_72_55657_0\",\"pin\":\"\",\"fields\":\"11100000\",\"skuIds\":\"%s\",\"source\":\"pc-item\"}";

    private static final String BASE_SEARCH_URL = "https://api.m.jd.com/?appid=item-v3&functionId=pctradesoa_getprice&client=pc&clientVersion=1.0.0&t=%s&body=";

    //计时器
    private static final TimeInterval timer = DateUtil.timer();

    //获取机器核数，作为线程池数量
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    //线程工厂
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNamePrefix("spider").build();

    //CompletableFuture使用的线程池
    private static final ExecutorService FUTURE_POOL = Executors.newFixedThreadPool(2);

    //手动创建线程池
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(2, 2,
            30L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardPolicy());

    //信号量，控制获取ip的并发数，暂时不用了
    private final Semaphore SEMAPHORE = new Semaphore(1);

    //模拟任务数量
    private static final int COUNT = 400;

    /**
     * 2core，2max，2-future-core，120s，成功率98%
     * 3core，3max，2-future-core，150s，成功率95%
     * 10core，20max，max-future-core，15s，成功率77%
     * 20core，50max，max-future-core，15s，成功率72%
     * 50core，100max，max-future-core，15s，成功率72%
     */
    public static void main(String[] args) {
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
                EXECUTOR_POOL.submit(new Task(randomList, url, longAdder));
            }
        }
        ThreadUtil.sleep(5 * 60 * 3600);
        EXECUTOR_POOL.shutdown();
    }

    /**
     * 任务类
     */
    static class Task implements Runnable {

        //随机的ipList
        private final List<Object> randomList;

        //最终的请求url
        private final String finalUrl;

        //计数器
        private final LongAdder longAdder;

        public Task(List<Object> randomList, String finalUrl, LongAdder longAdder) {
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

    static public void insert(String price) {
        ThreadUtil.sleep(50);
    }


}

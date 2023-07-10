package com.nova.tools.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nova.common.core.model.result.AjaxResult;
import com.nova.common.utils.common.ServletUtils;
import com.nova.common.utils.ip.IpUtils;
import com.nova.tools.demo.entity.People;
import com.starter.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: wzh
 * @description 测试Controller
 * @date: 2023/05/26 12:09
 */
@Slf4j(topic = "DemoController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController {

    private final RedisService redisService;

    @PostMapping("setList")
    public AjaxResult setList(@RequestParam String name) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            People build = People.builder().id(i).age(1).createTime(new DateTime()).build();
            list.add(build);
        }
        redisService.setList(name, list.subList(1, list.size()));
        return AjaxResult.success(list.get(0));
    }

    @PostMapping("getList")
    public AjaxResult getList(@RequestParam String name) {
        People people = (People) redisService.listPop(name);
        return AjaxResult.success(people);
    }

    @PostMapping("host")
    public AjaxResult host() {
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        boolean lock = redisService.lock(ip, ip, 50);
        try {
            if (lock) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error();
            }
        } finally {
            redisService.unlock(ip, ip);
        }
    }

    public static final String PRODUCT_POLL = "productPoll";

    public static final String SPECIAL_POLL = "specialPoll";

    /**
     * 获取机器核数，作为线程池数量
     */
    private static final int THREAD_COUNT =  Runtime.getRuntime().availableProcessors()/2;

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNamePrefix("pricePlan").build();

    /**
     * 手动创建线程池
     */
    private static final ExecutorService EXECUTOR_POOL = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT,
            30L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 40线程，60秒 --> 1.2w
     * 20线程，60秒 --> 6000
     */
    @PostMapping("pricePlan")
    public AjaxResult pricePlan() {
        LongAdder longAdder = new LongAdder();
        TimeInterval timer = DateUtil.timer();
        TimeInterval totalTimer = DateUtil.timer();
        for (int i = 0; i < THREAD_COUNT; i++) {
            EXECUTOR_POOL.submit(new Task(longAdder, timer, totalTimer));
        }
        return AjaxResult.success();
    }

    /**
     * 5,60,1500
     * @return
     */
    @PostMapping("pricePlanA")
    public AjaxResult pricePlanA() {
        LongAdder longAdder = new LongAdder();
        TimeInterval timer = DateUtil.timer();
        TimeInterval totalTimer = DateUtil.timer();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> updatePrice(longAdder, timer, totalTimer)).start();
        }
        return AjaxResult.success();
    }

    class Task implements Runnable {

        /**
         * 计数器
         */
        private final LongAdder longAdder;

        /**
         * 记录单线程耗时
         */
        private final TimeInterval timer;

        /**
         * 总耗时
         */
        private final TimeInterval totalTimer;

        public Task(LongAdder longAdder, TimeInterval timer, TimeInterval totalTimer) {
            this.longAdder = longAdder;
            this.timer = timer;
            this.totalTimer = totalTimer;
        }

        @Override
        public void run() {
            updatePrice(longAdder, timer, totalTimer);
        }
    }

    private void updatePrice(LongAdder longAdder, TimeInterval timer, TimeInterval totalTimer) {
        while (true) {
            Object o = redisService.listPop(PRODUCT_POLL);
            if (ObjectUtil.isNull(o)) {
                break;
            }
            List<String> split = StrUtil.split(o.toString(), "-");
            if (CollUtil.isNotEmpty(split) && split.size() > 5) {
                Boolean special = redisService.containsSet(SPECIAL_POLL, split.get(1));
                if (!special) {
                    //执行算价，新价格保留2为小数
                    String skuId = split.get(0);
                    String cateId = split.get(1);
                    String rate = split.get(2);
                    String flag = split.get(3);
                    String costPrice = split.get(4);
                    String elePrice = split.get(5);


                    BigDecimal percent = NumberUtil.add(NumberUtil.div(rate, "100"), BigDecimal.ONE);
                    //单位分
                    BigDecimal newPrice = NumberUtil.mul(percent, new BigDecimal(costPrice)).setScale(2, RoundingMode.HALF_UP);

                    //允许超过电商价
                    if (StrUtil.equals("1", flag)) {
                        newPrice = newPrice.compareTo(new BigDecimal(elePrice)) > 0 ? new BigDecimal(elePrice) : newPrice;
                    }

                    //先去查询 有就修改，没有就插入
                    getData(cateId, skuId);

                    //插入表 newPrice、操作人 、skuId、cateId，创建、修改时间自动生成，status（0已更新，1更新中），这个最好也默认值
                    saveOrUpdate(cateId, skuId, newPrice);

                    //发送队列
                    sendMq(skuId, newPrice.toString());
                    longAdder.increment();
                    log.info("time：{}，耗时：{} ms，总耗时：{} ms，价格：{}，次数{}", DateUtil.now(), timer.interval(), totalTimer.interval(), newPrice, longAdder.longValue());
                    timer.restart();
                }
            }
        }
    }


    //模拟数据库IO耗时50ms
    public void getData(String cateId, String skuId) {
        ThreadUtil.sleep(50);
    }

    //模拟数据库IO耗时50ms
    public void saveOrUpdate(String cateId, String skuId, BigDecimal price) {
        ThreadUtil.sleep(50);
    }

    //模拟网络IO耗时20ms
    public void sendMq(String skuId, String price) {
        ThreadUtil.sleep(50);
    }


}

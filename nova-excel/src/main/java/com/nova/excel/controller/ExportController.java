package com.nova.excel.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.nova.common.core.controller.BaseController;
import com.nova.common.utils.list.PageUtils;
import com.nova.excel.entity.ExportDO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @description: 线程合并结果后导出
 * @author: wzh
 * @date: 2023/1/12 13:30
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class ExportController extends BaseController {

    /**
     * 线程数
     */
    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final int TOTAL = 1000000;

    public static String path;

    public static List<ExportDO> LIST = new ArrayList<>();

    static {
        path = Objects.requireNonNull(ExportController.class.getResource("/")).getPath();
        for (int i = 1; i <= TOTAL; i++) {
            ExportDO data = new ExportDO();
            data.setId(Convert.toLong(i));
            data.setName("名称" + i);
            LIST.add(data);
        }
    }

    public static final ThreadPoolExecutor THREAD_POOL = ExecutorBuilder.create().setCorePoolSize(THREAD_POOL_SIZE).setMaxPoolSize(THREAD_POOL_SIZE * 2).setHandler(RejectPolicy.BLOCK.getValue()).build();

    /**
     * 阿里easyExcel测试，多线程查询后合并100w然后导出
     */
    @SneakyThrows
    @GetMapping("exportEasyExcel")
    public void exportEasyExcel() {
        TimeInterval timer = DateUtil.timer();
        String fileName = path + UUID.fastUUID() + ".xlsx";
        HttpServletResponse response = getResponse();
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        // 防止下载的文件名字乱码
        response.setCharacterEncoding("UTF-8");
        // 文件以附件形式下载
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(UUID.fastUUID() + ".xlsx", "utf-8"));
        //模拟数据库数据
        List<ExportDO> exportList = selectAll(LIST.size(), 50000);

        //可以浏览器下载
        EasyExcel.write(response.getOutputStream(), ExportDO.class).excelType(ExcelTypeEnum.XLSX).sheet("模板").doWrite(exportList);

        //可以直接EasyExcel.write(fileName, ExportDO.class)下载到本地或者服务器
//        EasyExcel.write(fileName, ExportDO.class).excelType(ExcelTypeEnum.XLSX).sheet("模板").doWrite(exportList);

        log.info("exportEasyExcel接口耗时：{}ms", timer.interval());
        System.gc();
    }

    /**
     * 多线程查询，10w一个分区，然后写入不同sheet
     */
    @SneakyThrows
    @GetMapping("threadExportExcel")
    public void threadExportExcel() {
        TimeInterval timer = DateUtil.timer();
        HttpServletResponse response = getResponse();
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        // 防止下载的文件名字乱码
        response.setCharacterEncoding("UTF-8");
        // 文件以附件形式下载
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(UUID.fastUUID() + ".xlsx", "utf-8"));

        //异步写入
        threadPoolWrite(LIST.size(), 100000, response);
        log.info("threadExportExcel接口耗时：{}ms", timer.interval());
        System.gc();
    }

    /**
     * 线程池版本
     *
     * @param totalCount
     * @param shardingSize
     * @param response
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public void threadPoolWrite(Integer totalCount, Integer shardingSize, HttpServletResponse response) throws InterruptedException, ExecutionException, IOException {
        TimeInterval timer = DateUtil.timer();
        int sum = 0;
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        System.err.println("本次任务量: " + totalNum);
        CountDownLatch cd = new CountDownLatch(totalNum);
        List<MyCallableTask> taskList = new ArrayList<>();
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new MyCallableTask(i, shardingSize, cd));
        }
        List<Future<List<ExportDO>>> futures = THREAD_POOL.invokeAll(taskList);
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), ExportDO.class).build()) {
            for (int i = 0; i < futures.size(); i++) {
                List<ExportDO> pageList = futures.get(i).get();
                if (CollUtil.isNotEmpty(pageList)) {
                    // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + (i + 1)).build();
                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                    excelWriter.write(pageList, writeSheet);
                    sum += pageList.size();
                }
            }
        }
        cd.await();
        System.err.println("写入表格完成,共：" + sum + " 条,耗时 ：" + timer.interval() + "ms");
    }

    /**
     * 并行编排版本
     *
     * @param totalCount
     * @param shardingSize
     * @param response
     */
    public void completableWrite(Integer totalCount, Integer shardingSize, HttpServletResponse response) throws IOException, InterruptedException {
        TimeInterval timer = DateUtil.timer();
        int sum = 0;
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        CountDownLatch cd = new CountDownLatch(totalNum);
        List<CompletableTask> taskList = new ArrayList<>();
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new CompletableTask(i, shardingSize, cd));
        }
        List<CompletableFuture<List<ExportDO>>> completableFutures = taskList.stream().map(task -> CompletableFuture.supplyAsync(() -> {
            TimeInterval threadTimer = DateUtil.timer();
            long threadId = Thread.currentThread().getId();
            //随机等待[2-5)秒，模拟数据库IO耗时
            int i = RandomUtil.randomInt(2, 5);
            if (i == 4) {
                throw new RuntimeException("第" + task.getPageNum() + "页运行异常，当前线程id：" + threadId);
            }
            ThreadUtil.sleep(RandomUtil.randomInt(2, 5), TimeUnit.SECONDS);
            List<ExportDO> pageList = PageUtils.startPage(LIST, task.getPageNum(), task.getPageSize());
            if (ObjectUtil.isNotNull(pageList)) {
                System.err.println("线程Id：" + threadId + ", 查询数据：" + pageList.size() + "条, 页码：" + task.getPageNum() + ", 耗时：" + threadTimer.interval() + "ms");
            }
            task.getCd().countDown();
            System.err.println("剩余任务数  ================> " + cd.getCount());
            return pageList;
        }, THREAD_POOL).exceptionally(e -> {
            //异常与否都要递减，否则主线程不会等待，这种写法不好理解可以上面去try-catch-finally
            task.getCd().countDown();
            log.error("异常消息: {}", e.getMessage());
            return new ArrayList<>();
        })).collect(Collectors.toList());

        //阻塞主线程，全部完成再返回
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        System.err.println("主线程等候完成，合并数据，耗时 ：" + timer.interval() + "ms");
        timer.restart();
        //遍历写入sheet
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), ExportDO.class).build()) {
            for (int i = 0; i < completableFutures.size(); i++) {
                List<ExportDO> pageList = completableFutures.get(i).join();
                if (CollUtil.isNotEmpty(pageList)) {
                    // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + (i + 1)).build();
                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                    excelWriter.write(pageList, writeSheet);
                    sum += pageList.size();
                }
            }
        }
        cd.await();
        System.err.println("写入表格完成,共：" + sum + " 条,耗时 ：" + timer.interval() + "ms");
    }

    public List<ExportDO> selectAll(Integer totalCount, Integer shardingSize) throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        List<MyCallableTask> taskList = new ArrayList<>();
        // 计算出多少页，即循环次数
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        System.err.println("本次任务量: " + totalNum);
        CountDownLatch cd = new CountDownLatch(totalCount);
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new MyCallableTask(i, shardingSize, cd));
        }
        List<ExportDO> resultList = new ArrayList<>();
        try {
            ThreadPoolExecutor threadPoolExecutor = ExecutorBuilder.create().setCorePoolSize(THREAD_POOL_SIZE).setMaxPoolSize(THREAD_POOL_SIZE * 2).setHandler(RejectPolicy.BLOCK.getValue()).build();
            List<Future<List<ExportDO>>> futures = threadPoolExecutor.invokeAll(taskList);
            for (Future<List<ExportDO>> future : futures) {
                resultList.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("selectAll异常", e);
        }
        cd.await();
        System.err.println("主线程：" + Thread.currentThread().getName() + " , 导出指定数据成功 , 共导出数据：" + resultList.size() + " , 查询数据任务执行完毕共消耗时 ：" + timer.interval() + "ms");
        return resultList;
    }

    static class MyCallableTask implements Callable<List<ExportDO>> {
        private final CountDownLatch cd;
        private final Integer pageNum;
        private final Integer pageSize;

        public MyCallableTask(Integer pageNum, Integer pageSize, CountDownLatch cd) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.cd = cd;
        }

        @Override
        public List<ExportDO> call() {
            TimeInterval timer = DateUtil.timer();
            int i = RandomUtil.randomInt(2, 5);
            long threadId = Thread.currentThread().getId();
            List<ExportDO> pageList = new ArrayList<>();
            try {
                if (i == 4) {
                    throw new RuntimeException("第" + pageNum + "页运行异常，当前线程id：" + threadId);
                }
                ThreadUtil.sleep(RandomUtil.randomInt(2, 5), TimeUnit.SECONDS);
                pageList = PageUtils.startPage(LIST, pageNum, pageSize);
            } catch (RuntimeException e) {
                log.error("异常消息: {}", e.getMessage());
            } finally {
                cd.countDown();
                System.err.println("线程Id：" + threadId + ", 查询数据：" + pageList.size() + "条, 页码：" + pageNum + ", 耗时：" + timer.interval() + "ms");
                System.err.println("剩余任务数  ================> " + cd.getCount());
            }
            return pageList;
        }
    }

    @Data
    static class CompletableTask {
        private final CountDownLatch cd;
        private final Integer pageNum;
        private final Integer pageSize;

        public CompletableTask(Integer pageNum, Integer pageSize, CountDownLatch cd) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.cd = cd;
        }
    }

}

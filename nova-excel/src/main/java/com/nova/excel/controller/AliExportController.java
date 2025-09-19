package com.nova.excel.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
import com.alibaba.excel.write.metadata.WriteSheet;
import com.nova.common.core.controller.BaseController;
import com.nova.excel.entity.AliEasyExportDO;
import com.nova.excel.entity.WaterMark;
import com.nova.excel.utils.WaterMarkHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
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
public class AliExportController extends BaseController {

    /**
     * 线程数
     */
    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final int TOTAL = 1000000;

    public static String path;

    public static List<AliEasyExportDO> LIST = new ArrayList<>();

    public static WaterMark WATER_MARK = new WaterMark();

    static {
        path = Objects.requireNonNull(AliExportController.class.getResource("/")).getPath();
        for (int i = 1; i <= TOTAL; i++) {
            AliEasyExportDO data = new AliEasyExportDO();
            data.setId(Convert.toLong(i));
            data.setName("名称" + i);
            data.setAge(i);
            data.setOrderId(UUID.fastUUID().toString());
            data.setStatus(1);
            data.setCreateTime(new Date());
            data.setUpdateTime(DateUtil.now());
            LIST.add(data);
        }

        WATER_MARK.setContent("仅供Nova项目使用，翻版必究");
        WATER_MARK.setWidth(600);
        WATER_MARK.setHeight(400);
        WATER_MARK.setyAxis(200);
    }

    public static final ThreadPoolExecutor THREAD_POOL = ExecutorBuilder.create().setCorePoolSize(THREAD_POOL_SIZE).setMaxPoolSize(THREAD_POOL_SIZE * 2).setHandler(RejectPolicy.BLOCK.getValue()).build();


    /**
     * 普通导出，单线程查询，一次查询循环写入（开启内存模式，数据超过一定的量，越写越慢）
     */
    @SneakyThrows
    @GetMapping("exportEasyExcel")
    public void exportEasyExcel() {
        TimeInterval timer = DateUtil.timer();
        int sum = 0;

        HttpServletResponse response = getResponse();
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        // 防止下载的文件名字乱码
        response.setCharacterEncoding("UTF-8");
        // 文件以附件形式下载
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(UUID.fastUUID() + ".xlsx", "utf-8"));

        //todo 注意开启了内存模式
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), AliEasyExportDO.class)
                /**
                 * 是否在内存处理，默认会生成临时文件以节约内存。内存模式效率会更好，但是容易OOM。大文件⚠️不要打开
                 * ⚠️水印导出一定要开启
                 */
                .inMemory(true)
                .registerWriteHandler(new WaterMarkHandler(WATER_MARK))
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet("单sheet").build();

        int pageNo = 0;
        int pageSize = TOTAL / 10;
        while (true) {
            List<AliEasyExportDO> pageList = ListUtil.page(pageNo, pageSize, LIST);
            if (CollUtil.isNotEmpty(pageList)) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(pageList, writeSheet);
                sum += pageList.size();
                pageNo++;
                System.err.println("写入pageNo" + pageNo);
            } else {
                break;
            }
        }
        excelWriter.finish();
        System.err.println("写入表格完成,共：" + sum + " 条,耗时 ：" + timer.interval() + "ms");
    }

    /**
     * 普通导出，单线程查询，一次循环写入多sheet（可以改造成数据分页，写入同一个sheet，类似流式写入，具体看pageEasyExcelSheets方法）
     */
    @SneakyThrows
    @GetMapping("exportEasyExcelSheets")
    public void exportEasyExcelSheets() {
        TimeInterval timer = DateUtil.timer();
        int sum = 0;

        HttpServletResponse response = getResponse();
        // 设置响应内容
        response.setContentType("application/vnd.ms-excel");
        // 防止下载的文件名字乱码
        response.setCharacterEncoding("UTF-8");
        // 文件以附件形式下载
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(UUID.fastUUID() + ".xlsx", "utf-8"));

        List<List<AliEasyExportDO>> split = ListUtil.split(LIST, 100000);

        //遍历写入不同的sheet
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), AliEasyExportDO.class).build()) {
            for (int i = 0; i < split.size(); i++) {
                List<AliEasyExportDO> pageList = split.get(i);
                if (CollUtil.isNotEmpty(pageList)) {
                    // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                    WriteSheet writeSheet = EasyExcel.writerSheet("单sheet" + i).build();
                    excelWriter.write(pageList, writeSheet);
                    sum += pageList.size();
                }
            }
        }
        System.err.println("写入表格完成,共：" + sum + " 条,耗时 ：" + timer.interval() + "ms");
    }


    /**
     * 普通导出，单线程查询，数据分页写入同一个sheet，⚠️单sheet最大数据100w
     */
    @SneakyThrows
    @GetMapping("pageEasyExcelSheet")
    public void pageEasyExcelSheet() {
        TimeInterval timer = DateUtil.timer();
        int totalCount = LIST.size();

        int pageSize = 10000, maxCount = 1000000;
        //数据总页数
        int totalPage = totalCount / pageSize + (totalCount % pageSize > 0 ? 1 : 0);
        //excel sheet总页数
        int excelPage = totalCount / maxCount + (totalCount % maxCount > 0 ? 1 : 0);
        //游标翻页 最后一个id
        long lastId = 0L, sum = 0L;

        String path = FilenameUtils.concat("/Users/wangzehui/Documents/IdeaProjects/nova/nova-excel/src/main/resources/", "pageEasyExcelSheet");
        File file = new File(path);
        try (ExcelWriter excelWriter = EasyExcel.write(file).build()) {
            //多循环一页，最后一页放链接，excelPage就一页那么从0开始循环
            for (int i = excelPage > 1 ? 1 : 0; i < excelPage + 1; i++) {
                if (i != excelPage) {
                    for (int j = 0; j < totalPage; j++) {
                        WriteSheet writeSheet = EasyExcel.writerSheet(j / 100, "sheet" + (j / 100 + 1)).head(AliEasyExportDO.class).build();
                        List<AliEasyExportDO> list = ListUtil.page(j, pageSize, LIST);
                        excelWriter.write(list, writeSheet);

                        //游标翻页 最后一个id
                        lastId = CollUtil.getLast(list).getId();
                        sum += list.size();
                        log.info("pageEasyExcelSheet ——> pageIndex：{}", j);
                    }
                } else {
                    WriteSheet writeSheet = EasyExcel.writerSheet(excelPage + 1, "链接sheet").head(AliEasyExportDO.URL.class).build();
                    excelWriter.write(Collections.singletonList(new AliEasyExportDO.URL()), writeSheet);
                }
            }
        }
        log.info("pageEasyExcelSheet ——> 共计写入excel数据：{} 条，耗时：{} ms", sum, timer.interval());
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
//        threadPoolWrite(LIST.size(), 100000, response);
        completableWrite(LIST.size(), 100000, response);
        log.info("threadExportExcel接口耗时：{}ms", timer.interval());
        System.gc();
    }

    /**
     * 线程池版本
     *
     * @param totalCount
     * @param shardingSize
     * @param response
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException   ExecutionException
     * @throws IOException          IOException
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
        List<Future<List<AliEasyExportDO>>> futures = THREAD_POOL.invokeAll(taskList);
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), AliEasyExportDO.class).build()) {
            for (int i = 0; i < futures.size(); i++) {
                List<AliEasyExportDO> pageList = futures.get(i).get();
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
     * 并行编排版本，分批写入多个sheet
     * todo 纯装逼，这个CountDownLatch不需要用，CompletableFuture.allOf就代表了countDown后await进行主线程阻塞，这为了计数才用它的
     *
     * @param totalCount   totalCount
     * @param shardingSize shardingSize
     * @param response     response
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
        List<CompletableFuture<List<AliEasyExportDO>>> completableFutures = taskList.stream().map(task -> CompletableFuture.supplyAsync(() -> {
            TimeInterval threadTimer = DateUtil.timer();
            long threadId = Thread.currentThread().getId();
            //随机等待[1-10)秒，模拟数据库IO耗时
            int i = RandomUtil.randomInt(1, 10);
            if (i == 3) {
                throw new RuntimeException("第" + task.getPageNum() + "页运行异常，当前线程id：" + threadId);
            }
            ThreadUtil.sleep(i, TimeUnit.SECONDS);
            List<AliEasyExportDO> pageList = ListUtil.page(task.getPageNum(), task.getPageSize(), LIST);
            if (ObjectUtil.isNotNull(pageList)) {
                System.err.println("线程Id：" + threadId + ", 查询数据：" + pageList.size() + "条, 页码：" + task.getPageNum() + ", 耗时：" + threadTimer.interval() + "ms");
            }
            System.err.println("剩余任务数  ================> " + cd.getCount());
            return pageList;
        }, THREAD_POOL).exceptionally(e -> {
            log.error("异常消息: {}", e.getMessage());
            return new ArrayList<>();
        }).thenApply(list -> {
            //后置处理，异常与否都要递减，否则主线程不会等待，这种写法不好理解可以在上面的代码块去try-catch-finally
            task.getCd().countDown();
            return list;
        })).collect(Collectors.toList());

        //阻塞主线程，全部完成再返回
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        System.err.println("主线程等候完成，合并数据，耗时 ：" + timer.interval() + "ms");
        timer.restart();
        //遍历写入sheet
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), AliEasyExportDO.class).build()) {
            for (int i = 0; i < completableFutures.size(); i++) {
                List<AliEasyExportDO> pageList = completableFutures.get(i).join();
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


    private static final class MyCallableTask implements Callable<List<AliEasyExportDO>> {
        private final CountDownLatch cd;
        private final Integer pageNum;
        private final Integer pageSize;

        public MyCallableTask(Integer pageNum, Integer pageSize, CountDownLatch cd) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.cd = cd;
        }

        @Override
        public List<AliEasyExportDO> call() {
            TimeInterval timer = DateUtil.timer();
            int i = RandomUtil.randomInt(1, 10);
            long threadId = Thread.currentThread().getId();
            List<AliEasyExportDO> pageList = new ArrayList<>();
            try {
                if (i == 3) {
                    throw new RuntimeException("第" + pageNum + "页运行异常，当前线程id：" + threadId);
                }
                ThreadUtil.sleep(i, TimeUnit.SECONDS);
                pageList = ListUtil.page(pageNum, pageSize, LIST);
            } catch (RuntimeException e) {
                log.error("异常消息: {}", e.getMessage());
            } finally {
                cd.countDown();
                assert pageList != null;
                System.err.println("线程Id：" + threadId + ", 查询数据：" + pageList.size() + "条, 页码：" + pageNum + ", 耗时：" + timer.interval() + "ms");
                System.err.println("剩余任务数  ================> " + cd.getCount());
            }
            return pageList;
        }
    }

    @Data
    private static final class CompletableTask {
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

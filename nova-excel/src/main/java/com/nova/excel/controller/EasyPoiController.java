package com.nova.excel.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.util.RandomUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.excel.entity.EasyPoiExportDO;
import com.nova.excel.utils.EasyPoiUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: wzh
 * @description easyPoi导出
 * @date: 2023/08/11 22:27
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class EasyPoiController extends BaseController {

    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final int TOTAL = 1000000;

    public static List<EasyPoiExportDO> LIST = new ArrayList<>();

    public static final ThreadPoolExecutor POOL = ExecutorBuilder.create().setCorePoolSize(THREAD_POOL_SIZE).setMaxPoolSize(THREAD_POOL_SIZE * 2).setHandler(RejectPolicy.BLOCK.getValue()).build();

    static {
        for (int i = 1; i <= TOTAL; i++) {
            EasyPoiExportDO data = new EasyPoiExportDO();
            data.setId(Convert.toLong(i));
            data.setName("名称" + i);
            data.setAge(i);
            data.setOrderId(UUID.fastUUID().toString());
            data.setStatus(1);
            data.setCreateTime(DateUtil.now());
            data.setUpdateTime(DateUtil.now());
            LIST.add(data);
        }
    }

    @SneakyThrows
    @GetMapping("exportEasyPoi")
    public void exportEasyPoi() {
        String fileName = UUID.fastUUID() + ".xlsx";
        HttpServletResponse response = getResponse();

        //普通导出
//        normalExport(fileName, response, LIST.size(), 50000);

        //大数据量
        bigExport(fileName, response, LIST.size(), 50000);
        System.gc();
    }

    /**
     * 大数据量导出
     *
     * @param fileName
     * @param response
     * @param totalCount
     * @param shardingSize
     * @throws InterruptedException
     */
    public void bigExport(String fileName, HttpServletResponse response, Integer totalCount, Integer shardingSize) throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        System.err.println("本次任务量: " + totalNum);
        List<MyCallableTask> taskList = new ArrayList<>();
        CountDownLatch cd = new CountDownLatch(totalNum);
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new MyCallableTask(i, shardingSize, cd));
        }
        List<Future<List<EasyPoiExportDO>>> futures = POOL.invokeAll(taskList);
        ExportParams exportParams = new ExportParams(null, "模板");
        cd.await();
        Workbook workbook = ExcelExportUtil.exportBigExcel(exportParams, EasyPoiExportDO.class, (queryParams, page) -> {
            if (page > totalNum) {
                return null;
            }
            List<Object> list;
            try {
                list = new ArrayList<>(futures.get(page - 1).get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            System.err.println("共：" + totalNum + " 页，当前写入第 ：" + page + " 页，size：" + list.size() + " 条");
            return list;
        }, totalNum);
        EasyPoiUtils.downLoadExcel(fileName, response, workbook);
        System.err.println("主线程：" + Thread.currentThread().getName() + " , 共导出数据：" + totalCount + " , 整体耗时 ：" + timer.interval() + "ms");
    }

    /**
     * @param totalCount
     * @param shardingSize
     * @return
     * @throws InterruptedException
     */
    public void normalExport(String fileName, HttpServletResponse response, Integer totalCount, Integer shardingSize) throws InterruptedException {
        List<MyCallableTask> taskList = new ArrayList<>();
        // 计算出多少页，即循环次数
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        System.err.println("本次任务量: " + totalNum);
        CountDownLatch cd = new CountDownLatch(totalNum);
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new MyCallableTask(i, shardingSize, cd));
        }
        List<EasyPoiExportDO> resultList = new ArrayList<>();
        try {
            List<Future<List<EasyPoiExportDO>>> futures = POOL.invokeAll(taskList);
            for (Future<List<EasyPoiExportDO>> future : futures) {
                resultList.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("selectAll异常", e);
        }
        cd.await();
        TimeInterval timer = DateUtil.timer();
        EasyPoiUtils.exportExcel(resultList, null, "模板", EasyPoiExportDO.class, fileName, response);
        System.err.println("主线程：" + Thread.currentThread().getName() + " , 共导出数据：" + resultList.size() + " , 写入表格耗时 ：" + timer.interval() + "ms");
    }

    static class MyCallableTask implements Callable<List<EasyPoiExportDO>> {
        private final CountDownLatch cd;
        private final Integer pageNum;
        private final Integer pageSize;

        public MyCallableTask(Integer pageNum, Integer pageSize, CountDownLatch cd) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.cd = cd;
        }

        @Override
        public List<EasyPoiExportDO> call() {
            TimeInterval timer = DateUtil.timer();
            int i = RandomUtil.randomInt(2, 5);
            long threadId = Thread.currentThread().getId();
            List<EasyPoiExportDO> pageList = new ArrayList<>();
            try {
                pageList = ListUtil.page(pageNum, pageSize, LIST);
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
}

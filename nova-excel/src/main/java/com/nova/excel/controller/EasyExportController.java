package com.nova.excel.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.util.RandomUtil;
import com.nova.common.core.controller.BaseController;
import com.nova.common.utils.list.PageUtils;
import com.nova.excel.entity.EasyPoiExportDO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class EasyExportController extends BaseController {

    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final int TOTAL = 1000000;

    public static String path;

    public static List<EasyPoiExportDO> LIST = new ArrayList<>();

    static {
        path = Objects.requireNonNull(AliExportController.class.getResource("/")).getPath();
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
        TimeInterval timer = DateUtil.timer();
        String fileName = UUID.fastUUID() + ".xlsx";
        HttpServletResponse response = getResponse();
        //模拟数据库数据
        List<EasyPoiExportDO> list = selectAll(LIST.size(), 50000);

        ExportParams params = new ExportParams(null, "模板");
        //可以设置样式和样式文件
        params.setType(ExcelType.XSSF);
        params.setStyle(EasyPoiExportDO.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, EasyPoiExportDO.class, list);
        downLoadExcel(fileName, response, workbook);
        log.info("exportEasyExcel接口耗时：{}ms", timer.interval());
        System.gc();
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try (OutputStream out = response.getOutputStream()) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            ByteArrayOutputStream bAos = new ByteArrayOutputStream();
            workbook.write(bAos);
            response.setHeader("Content-Length", String.valueOf(bAos.size()));
            out.write(bAos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<EasyPoiExportDO> selectAll(Integer totalCount, Integer shardingSize) throws InterruptedException {
        TimeInterval timer = DateUtil.timer();
        List<MyCallableTask> taskList = new ArrayList<>();
        // 计算出多少页，即循环次数
        int totalNum = totalCount / shardingSize + (totalCount % shardingSize > 0 ? 1 : 0);
        System.err.println("本次任务量: " + totalNum);
        CountDownLatch cd = new CountDownLatch(totalCount);
        for (int i = 1; i <= totalNum; i++) {
            taskList.add(new MyCallableTask(i, shardingSize, cd));
        }
        List<EasyPoiExportDO> resultList = new ArrayList<>();
        try {
            ThreadPoolExecutor threadPoolExecutor = ExecutorBuilder.create().setCorePoolSize(THREAD_POOL_SIZE).setMaxPoolSize(THREAD_POOL_SIZE * 2).setHandler(RejectPolicy.BLOCK.getValue()).build();
            List<Future<List<EasyPoiExportDO>>> futures = threadPoolExecutor.invokeAll(taskList);
            for (Future<List<EasyPoiExportDO>> future : futures) {
                resultList.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("selectAll异常", e);
        }
        cd.await();
        System.err.println("主线程：" + Thread.currentThread().getName() + " , 导出指定数据成功 , 共导出数据：" + resultList.size() + " , 查询数据任务执行完毕共消耗时 ：" + timer.interval() + "ms");
        return resultList;
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
}

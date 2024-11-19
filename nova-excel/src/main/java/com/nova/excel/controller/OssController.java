package com.nova.excel.controller;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.UUID;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.nova.common.utils.file.AliOssUtils;
import com.nova.excel.entity.AliEasyExportDO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: wzh
 * @description: 阿里oss测试
 * @date: 2024/10/09 16:02
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class OssController {

    private final AliOssUtils aliOssUtils;

    public static List<AliEasyExportDO> LIST = new ArrayList<>();

    public static final int TOTAL = 10000000;

    static {
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
    }

    /**
     * 多线程查询，10w一个分区，然后写入不同sheet
     */
    @SneakyThrows
    @GetMapping("a")
    public void demoA() {
        TimeInterval timer = DateUtil.timer();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            log.info("准备写入excel");
            EasyExcel.write(outputStream, AliEasyExportDO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("测试")
                    .doWrite(LIST);
            log.info("准备写入oss");

            // 创建MockMultipartFile实例
            MultipartFile multipartFile = new MockMultipartFile(
                    "导出", // 文件名
                    ".xlsx", // 类型
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // 内容类型
                    outputStream.toByteArray() // 文件内容
            );
            aliOssUtils.upload(multipartFile);
            log.info("oss写入完成");
        }
    }

    //这个测试了，大内存不行
    @SneakyThrows
    @GetMapping("b")
    public void demoB() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            log.info("准备写入excel");
            EasyExcel.write(outputStream, AliEasyExportDO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("测试")
                    .doWrite(LIST);
            log.info("准备写入oss");
            aliOssUtils.upload(outputStream.toByteArray());
            log.info("oss写入完成");
        }
    }

    //单sheet
    @SneakyThrows
    @GetMapping("c")
    public void demoC() {
        File outputFile = new File("/Users/wangzehui/Documents/IdeaProjects/nova/nova-excel/src/main/resources/测试demoC.xlsx");
        log.info("准备写入excel");
        EasyExcel.write(outputFile, AliEasyExportDO.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("测试")
                .doWrite(LIST);
        log.info("准备写入oss");
        String upload = aliOssUtils.upload(outputFile);
        log.info("oss写入完成：{}", upload);
        outputFile.deleteOnExit();
    }

    //多sheet
    @SneakyThrows
    @GetMapping("d")
    public void demoD() {
        File outputFile = new File("/Users/wangzehui/Documents/IdeaProjects/nova/nova-excel/src/main/resources/测试demoC.xlsx");
        try (ExcelWriter excelWriter = EasyExcel.write(outputFile, AliEasyExportDO.class).build()) {
            log.info("准备写入excel");
            int pageSize = 1000000;
            List<List<AliEasyExportDO>> split = ListUtil.split(LIST, pageSize);
            for (int i = 0; i < split.size(); i++) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "sheet" + (i + 1)).build();
                excelWriter.write(split.get(i), writeSheet);
                log.info("循环次数：{}", i + 1);
            }
        }
        log.info("准备写入oss");
        String upload = aliOssUtils.upload(outputFile);
        log.info("oss写入完成：{}", upload);
        outputFile.deleteOnExit();
    }

}

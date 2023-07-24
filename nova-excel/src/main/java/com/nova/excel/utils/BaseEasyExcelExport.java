package com.nova.excel.utils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.nova.common.utils.common.ServletUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: wzh
 * @description easyExcel抽象类
 * @date: 2023/07/24 13:39
 */
@Slf4j
public abstract class BaseEasyExcelExport<T> {

    /**
     * 导出到excel，支持大数量的分批写入
     */
    protected void exportExcel(String fileName, Map<String, Object> queryCondition) {
        HttpServletResponse response = ServletUtils.getResponse();
        //根据条件查询总记录
        Long totalCount = dataTotalCount(queryCondition);
        //每一个Sheet存放的记录数
        Long sheetDataRows = eachSheetTotalCount();
        //每次写入的数据量
        Long writeDataRows = eachTimesWriteSheetTotalCount();
        if (totalCount < sheetDataRows) {
            sheetDataRows = totalCount;
        }
        if (sheetDataRows < writeDataRows) {
            writeDataRows = sheetDataRows;
        }
        doExport(response, fileName, queryCondition, totalCount, sheetDataRows, writeDataRows);
    }

    /**
     * 导出到excel
     */
    private void doExport(HttpServletResponse response, String fileName, Map<String, Object> queryCondition,
                          Long totalCount, Long sheetDataRows, Long writeDataRows) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            WriteWorkbook writeWorkbook = new WriteWorkbook();
            writeWorkbook.setOutputStream(outputStream);
            writeWorkbook.setExcelType(ExcelTypeEnum.XLSX);
            ExcelWriter writer = new ExcelWriter(writeWorkbook);
            WriteTable table = new WriteTable();
            table.setHead(getExcelHead());
            // 计算需要的Sheet数量
            long sheetNum = totalCount % sheetDataRows == 0 ? (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
            // 计算一般情况下每一个Sheet需要写入的次数
            long oneSheetWriteCount = totalCount > sheetDataRows ? sheetDataRows / writeDataRows :
                    totalCount % writeDataRows > 0 ? totalCount / writeDataRows + 1 : totalCount / writeDataRows;
            // 计算最后一个sheet需要写入的次数
            long lastSheetWriteCount = totalCount % sheetDataRows == 0 ? oneSheetWriteCount :
                    (totalCount % sheetDataRows % writeDataRows == 0 ? (totalCount / sheetDataRows / writeDataRows) :
                            (totalCount / sheetDataRows / writeDataRows + 1));
            // 分批查询分次写入
            List<List<String>> dataList = new ArrayList<>();
            for (int i = 0; i < sheetNum; i++) {
                WriteSheet sheet = new WriteSheet();
                sheet.setSheetNo(i);
                sheet.setSheetName(sheetNum == 1 ? fileName : fileName + i);
                for (int j = 0; j < (i != sheetNum - 1 || i == 0 ? oneSheetWriteCount : lastSheetWriteCount); j++) {
                    dataList.clear();
                    buildDataList(dataList, queryCondition, j + 1 + oneSheetWriteCount * i, writeDataRows);
                    writer.write(dataList, sheet, table);
                }
            }
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName).getBytes("gb2312"),
                    StandardCharsets.ISO_8859_1) + ".xlsx");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            writer.close();
            outputStream.flush();
        } catch (Exception e) {
            log.error("AbstractEasyExcelExport.exportWithBigData.error:{}", e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("AbstractEasyExcelExport.exportWithBigData.close.error:{}", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 获取导出的表头
     */
    protected abstract List<List<String>> getExcelHead();

    /**
     * 计算导出数据的总数
     */
    protected abstract Long dataTotalCount(Map<String, Object> conditions);

    /**
     * 每一个sheet存放的数据总数
     */
    protected abstract Long eachSheetTotalCount();

    /**
     * 每次写入sheet的总数
     */
    protected abstract Long eachTimesWriteSheetTotalCount();

    /**
     * 构建每次查询数量
     */
    protected abstract void buildDataList(List<List<String>> resultList, Map<String, Object> queryCondition,
                                          Long pageNo, Long pageSize);
}

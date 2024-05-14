package com.nova.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description easyExcel 工具类
 * @date: 2023/11/03 16:28
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class EasyExcelUtils {

    private static final short FONT_SIZE_TEN = 10;

    /**
     * easyExcel 导出（适合上万的数据导出）
     *
     * @param response  http响应
     * @param dataList  数据内容
     * @param fileName  文件名
     * @param sheetName sheet名
     * @param clazz     导出对象类
     */
    public static <T> void write(HttpServletResponse response, List<T> dataList, String fileName, String sheetName, Class<T> clazz) {
        try {
            // 样式简单设置（取个样式模板）
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = getStyle();
            // 开始导出
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ExcelTypeEnum.XLSX.getValue());
            response.setCharacterEncoding("UTF-8");
            // easyExcel 导出完成自动关闭流，也是可以再手动关一次的（官网这么写的）
            EasyExcel.write(response.getOutputStream(), clazz).excelType(ExcelTypeEnum.XLSX).sheet(sheetName).registerWriteHandler(horizontalCellStyleStrategy).doWrite(dataList);
        } catch (IOException e) {
            log.error("导出失败异常", e);
        }
    }

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                .doReadAllSync();
    }

    /**
     * easyExcel 导入解析
     *
     * @param file  导入文件
     * @param clazz 导入对象类
     */
    public static <T> List<T> parse(MultipartFile file, Class<T> clazz) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            List<T> resultList = new ArrayList<>();
            EasyExcel.read(inputStream, clazz, new ReadListener<T>() {
                @Override
                public void invoke(T t, AnalysisContext analysisContext) {
                    resultList.add(t);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    log.info("解析数据完成：{}", resultList);
                }
            }).sheet().doRead();
            return resultList;
        }
    }

    public List<T> parseExcel(InputStream inputStream, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        EasyExcel.read(inputStream, clazz, new ReadListener<T>() {
            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                resultList.add(t);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("解析数据完成：{}", resultList);
            }
        }).sheet().doRead();
        return resultList;
    }

    /**
     * easyExcel样式模板
     * 格表头 11号、加粗、宋体
     * 内容 10号
     *
     * @return 样式模板
     */
    private static HorizontalCellStyleStrategy getStyle() {
        // 表头样式居中对齐、字体宋体11号不加粗（默认加粗）、自动换行（已默认）
        WriteFont writeFont = new WriteFont();
        writeFont.setBold(false);
        writeFont.setFontName("宋体");
        writeFont.setFontHeightInPoints(FONT_SIZE_TEN);

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWriteFont(writeFont);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容样式垂直、水平居中、字体宋体10号不加粗（默认不加粗）、自动换行
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setWriteFont(writeFont);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

}

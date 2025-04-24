package com.nova.excel.controller;

import com.nova.common.core.controller.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author: wzh
 * @description: Xss控制层
 * @date: 2023/09/21 13:34
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class XssController extends BaseController {

    /**
     * 测试表格导入
     * @param file
     */
    @SneakyThrows
    @RequestMapping("xssImport")
    public void xssImport(@RequestParam("file") MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        Workbook wk = new XSSFWorkbook(inputStream);
        Sheet sheet = wk.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(0);
            System.out.println("cell = " + cell.getStringCellValue());
        }
        String one = sheet.getRow(0).getCell(0).getStringCellValue();
        String two = sheet.getRow(0).getCell(1).getStringCellValue();
        System.out.println("one = " + one);
        System.out.println("two = " + two);
    }

}

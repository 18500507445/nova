package com.nova.excel.controller;

import com.nova.excel.entity.AliEasyExportDO;
import com.nova.excel.template.EasyExcelExportBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author: wzh
 * @description 模板导出
 * @date: 2023/07/24 13:46
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class TemplateExportController extends EasyExcelExportBase<AliEasyExportDO> {

    @GetMapping("exportTemplateExcel")
    public void exportTemplateExcel() {
        //指定数据条件
        Map<String, Object> map = new HashMap<>();
        this.exportExcel("用户列表", map);
    }

    @Override
    protected List<List<String>> getExcelHead() {
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("id"));
        head.add(Collections.singletonList("姓名"));
        return head;
    }

    @Override
    protected Long dataTotalCount(Map<String, Object> conditions) {
        //sql查询count
        return 5000L;
    }

    @Override
    protected Long eachSheetTotalCount() {
        return 1000L;
    }

    @Override
    protected Long eachTimesWriteSheetTotalCount() {
        return 5000L;
    }

    private long lastNum = 1;

    @Override
    protected void buildDataList(List<List<String>> resultList, Map<String, Object> queryCondition, Long pageNo, Long pageSize) {
        //分页参数进行查询

        //拼装数据，遍历放入结果集
        List<AliEasyExportDO> list = new ArrayList<>();
        for (long aLong = lastNum; aLong <= pageNo * pageSize; aLong++) {
            AliEasyExportDO aliEasyExportDO = new AliEasyExportDO();
            aliEasyExportDO.setId(aLong);
            aliEasyExportDO.setName(String.valueOf(aLong));
            list.add(aliEasyExportDO);
            lastNum++;
        }
        list.forEach(aliEasyExportDO -> resultList.add(Arrays.asList(aliEasyExportDO.getId().toString(), aliEasyExportDO.getName())));
    }
}

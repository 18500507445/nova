package com.nova.tools.utils.dataresult;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author wangzehui
 */
@Data
public class PageEntity<T> {
    //总页数
    private int totalPages;
    //总行数
    private int totalRows;
    //当前页
    private int currentPage;
    //每页最大行数
    private int rows;
    //数据列表
    private List<T> list;

    public PageEntity() {

    }

    /**
     * 一次查出全部的数据
     *
     * @param list 全部数据
     */
    public PageEntity(List<T> list) {
        this.list = list;
        int trows = 0;
        if (!CollectionUtils.isEmpty(this.list)) {
            trows = this.list.size();
        }
        this.currentPage = 1;
        this.rows = trows;
        this.totalRows = trows;
        this.totalPages = 1;
    }

    public PageEntity(String error) {
        this.currentPage = 1;
        this.rows = 0;
        this.totalRows = 0;
        this.totalPages = 1;
        this.list = Collections.emptyList();
    }

    /**
     * @param currentPage 当前页
     * @param rows        每页最大行数
     * @param totalRows   总行数
     * @param list        当前数据列表
     */
    public PageEntity(int currentPage, int rows, int totalRows, List<T> list) {
        this.currentPage = currentPage;
        this.rows = rows;
        this.totalRows = totalRows;
        this.list = list;
        int tp = 1;
        if (this.totalRows > 0) {
            tp = (int) Math.ceil(this.totalRows / (this.rows * 1.0));
        }
        this.totalPages = tp;
    }

}

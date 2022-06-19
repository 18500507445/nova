package com.nova.tools.utils.dataresult;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wangzehui
 */
public class PageParam<T> extends ObjectParam implements Serializable {

    private static final long serialVersionUID = 3596410033389755388L;
    private List<T> datas;
    private int totalRows = 0;
    private int totalPage = 1;
    private int pageSize = 10;
    private int currentPage = 1;

    public PageParam() {
    }

    /**
     * 分页构造方法
     *
     * @param list        当前页对象列表
     * @param totalRows   总记录条数
     * @param currentPage 当前页
     * @param pageSize    每页大小
     */
    public PageParam(List<T> list, int totalRows, int currentPage, int pageSize) {
        this.datas = list;
        this.totalRows = totalRows;
        this.currentPage = currentPage;
        this.pageSize = pageSize;

        this.totalPage = this.totalRows % pageSize > 0 ? (this.totalRows / pageSize + 1) : (this.totalRows / pageSize);
        // 当前页不能大于总页数
        if (currentPage > this.totalPage) {
            this.setCurrentPage(this.totalPage);
        }

        // 当前页不能小于1
        if (currentPage < 1) {
            this.setCurrentPage(1);
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}

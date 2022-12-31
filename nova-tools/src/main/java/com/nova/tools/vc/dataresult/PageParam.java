package com.nova.tools.vc.dataresult;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wzh
 */
@EqualsAndHashCode(callSuper = true)
@Data
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

}

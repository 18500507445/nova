package com.nova.common.core.model.business;

import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @description: 基础分页对象
 * @author: wzh
 * @date: 2022/12/19 20:58
 */
@SuperBuilder
public class BasePage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer DEFAULT_PAGE_NO = 1;

    public static final Integer DEFAULT_SIZE = 10;

    /**
     * 页号
     */
    private Integer pageNo;

    /**
     * 每页显示大小
     */
    private Integer pageSize;

    public BasePage() {
        this.pageNo = DEFAULT_PAGE_NO;
        this.pageSize = DEFAULT_SIZE;
    }

    public BasePage(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        if (this.pageNo == null || this.pageNo <= 0) {
            this.pageNo = DEFAULT_PAGE_NO;
        }
        return this.pageNo;
    }

    public void setPageNo(Integer pageIndex) {
        this.pageNo = pageIndex;
    }

    public Integer getPageSize() {
        if (this.pageSize == null || this.pageSize <= 0) {
            this.pageSize = DEFAULT_SIZE;
        }
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
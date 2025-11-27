package com.nova.orm.mybatisplus.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description: 分页返回类
 * @date: 2024/03/14 08:36
 */
@Data
@NoArgsConstructor
public class PageResult<T> {

    // 页码
    private long pageIndex;

    // 页大小
    private long pageSize;

    // 总条数
    private long totalCount;

    // 总页数
    private long totalPage;

    // result
    private List<T> list;

    private PageResult(List<T> list, Page<T> page) {
        if (null == page) {
            return;
        }
        this.pageIndex = page.getPageNum();
        this.pageSize = page.getPageSize();
        this.totalCount = page.getTotal();
        this.totalPage = page.getPages();
        if (null != list) {
            this.list = list;
        } else {
            this.list = page.getResult();
        }
    }

    private PageResult(List<T> list, PageInfo<T> pageInfo) {
        if (null == pageInfo) {
            return;
        }
        this.pageIndex = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.totalCount = pageInfo.getTotal();
        this.totalPage = pageInfo.getPages();
        if (null != list) {
            this.list = list;
        } else {
            this.list = pageInfo.getList();
        }
    }

    private PageResult(List<T> list, IPage<T> iPage) {
        if (null == iPage) {
            return;
        }
        this.pageIndex = iPage.getCurrent();
        this.pageSize = iPage.getSize();
        this.totalCount = iPage.getTotal();
        this.totalPage = iPage.getPages();
        if (null != list) {
            this.list = list;
        } else {
            this.list = iPage.getRecords();
        }
    }

    private PageResult(List<T> list, long pageIndex, long pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        // 处理数据
        if (CollUtil.isEmpty(list)) {
            this.list = new ArrayList<>();
            this.totalCount = 0;
            this.totalPage = 0;
        } else {
            PageUtil.setFirstPageNo(1);
            this.list = ListUtil.page(pageIndex > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) pageIndex, pageSize > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) pageSize, list);
            this.totalCount = list.size();
            this.totalPage = (totalCount + pageSize - 1) / pageSize;
        }
    }

    private PageResult(List<T> list, long pageIndex, long pageSize, long totalCount) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.list = list;
        this.totalCount = totalCount;
        if (pageSize > 0) {
            this.totalPage = (totalCount + pageSize - 1) / pageSize;
        }
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(null, 0, 0);
    }

    /**
     * 针对PageHelper-page对象，进行包装处理
     *
     * @param list<T> 手动传入list数据
     * @param page    {@link Page}
     * @return <T>
     */
    public static <T> PageResult<T> page(List<T> list, Page<T> page) {
        return new PageResult<>(list, page);
    }

    /**
     * 同上，list取page对象里的
     *
     * @param page {@link Page}
     * @return <T>
     */
    public static <T> PageResult<T> page(Page<T> page) {
        return new PageResult<>(null, page);
    }

    /**
     * 针对PageHelper-pageInfo对象，进行包装处理
     *
     * @param list<T>  手动传入list数据
     * @param pageInfo {@link PageInfo}
     * @return <T>
     */
    public static <T> PageResult<T> pageInfo(List<T> list, PageInfo<T> pageInfo) {
        return new PageResult<>(list, pageInfo);
    }

    /**
     * 同上，list取pageInfo对象里的
     *
     * @param pageInfo {@link PageInfo}
     * @return <T>
     */
    public static <T> PageResult<T> pageInfo(PageInfo<T> pageInfo) {
        return new PageResult<>(null, pageInfo);
    }

    /**
     * 针对mybatisPlus-IPage对象，进行包装处理
     *
     * @param list<T> 手动传入list数据
     * @param iPage   {@link IPage}
     * @return <T>
     */
    public static <T> PageResult<T> iPage(List<T> list, IPage<T> iPage) {
        return new PageResult<>(list, iPage);
    }

    /**
     * 同上，list取iPage对象里的
     *
     * @param iPage {@link IPage}
     * @return <T>
     */
    public static <T> PageResult<T> iPage(IPage<T> iPage) {
        return new PageResult<>(null, iPage);
    }

    /**
     * list-不做处理，totalCount取传入的
     *
     * @param list<T>    数据
     * @param pageIndex  页码
     * @param pageSize   页大小
     * @param totalCount 总数
     * @return <T>
     */
    public static <T> PageResult<T> page(List<T> list, long pageIndex, long pageSize, long totalCount) {
        return new PageResult<>(list, pageIndex, pageSize, totalCount);
    }

    /**
     * list-手动翻页
     *
     * @param list<T>   数据
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @return <T>
     */
    public static <T> PageResult<T> page(List<T> list, long pageIndex, long pageSize) {
        return new PageResult<>(list, pageIndex, pageSize);
    }


}

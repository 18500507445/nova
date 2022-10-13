package com.nova.common.utils.list;

import java.util.List;

/**
* @Description
*
* //总记录数
* int totalRows = result.size();
* //总页数
* int pageCount = totalRows % pageView.getPageSize() == 0 ? totalRows / pageView.getPageSize() : totalRows / pageView.getPageSize() + 1;
* pageView.setRowCount(totalRows);
* pageView.setPageCount(pageCount);
* //切割list 手动分页
* List<ReviewAndPrepare> reviewAndPrepares = PageUtil.startPage(result, pageView.getPageNow(), pageView.getPageSize());
* pageView.setRecords(reviewAndPrepares);
* @Date 2020/2/11 13:48
 */
public class PageUtil {
    /**
     * 开始分页
     *
     * @param list
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static <T> List<T> startPage(List<T> list, Integer pageNum,
                                        Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        int count = list.size(); // 记录总数
        int pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        return list.subList(fromIndex, toIndex);
    }

}

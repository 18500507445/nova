package com.nova.tools.utils.dataresult;


import java.io.Serializable;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/8/27 19:10
 */
public class QueryPager implements Serializable {

	private static final long serialVersionUID = 3284770044910817749L;
	/**
	 * 每页显示
	 * 默认是20页
	 */
	private int pageSize = 20;
	/**
	 * 页码
	 */
	private int pageNum = 1;
	/**
	 * 开始数
	 */
	private int start = 0;
	/**
	 * 排序方式
	 * 0 desc 倒序
	 * 1 asc 正序
	 * 默认是正序
	 */
	private Integer orderWay = 1;
	/**
	 * 排序字段
	 */
	private String orderBy;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getStart() {
		return (pageNum-1)*pageSize;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderWay() {
		return orderWay;
	}

	public void setOrderWay(Integer orderWay) {
		this.orderWay = orderWay;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("QueryPager{");
		sb.append("pageSize=").append(pageSize);
		sb.append(", pageNum=").append(pageNum);
		sb.append(", start=").append(start);
		sb.append(", orderWay=").append(orderWay);
		sb.append(", orderBy=").append(orderBy);
//		sb.append(", orderBy='").append(orderBy).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

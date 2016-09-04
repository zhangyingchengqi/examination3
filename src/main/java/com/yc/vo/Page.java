package com.yc.vo;





import java.util.ArrayList;

import java.util.List;

public class Page<T> {

	// 当前页数

	private int currentPage=1;

	// 总页数

	private int totalsPage=1000;

	// 每页显示记录条数

	private int pageSize=10;

	// 总记录条数

	private int totalsCount=10;

	// 查询返回结果

	private List<T> result = new ArrayList<T>();

	// 分页链接
	private String uri;

	public String getUri() {

		return uri;

	}

	public void setUri(String uri) {

		this.uri = uri;

	}

	public int getCurrentPage() {

		return this.currentPage;

	}

	public void setCurrentPage(int currentPage) throws Exception {

		if (currentPage <= 0) {
			this.currentPage = 1;
		}else if(currentPage>this.totalsPage){
			this.currentPage = this.totalsPage;

		}else{
			this.currentPage=currentPage;

		}
	}

	public int getTotalsPage() {

		try {

			if (totalsCount % pageSize == 0) {

				totalsPage = totalsCount / pageSize;

			} else {

				totalsPage = (totalsCount / pageSize) + 1;
			}

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

		return totalsPage;

	}

	public void setTotalsPage(int totalsPage) {

		if (totalsPage < 0) {

			totalsPage = 0;

		}

		this.totalsPage = totalsPage;

	}

	public int getPageSize() {

		return pageSize;

	}

	public void setPageSize(int pageSize) {

		if (pageSize <= 0) {

			pageSize = 20;

		}

		this.pageSize = pageSize;

	}

	public int getTotalsCount() {

		return totalsCount;

	}

	public void setTotalsCount(int totalsCount) {

		if (totalsCount < 0) {

			totalsCount = 0;

		}

		this.totalsCount = totalsCount;

	}

	public List<T> getResult() {

		return result;

	}

	public void setResult(List<T> result) {

		this.result = result;

	}

	@Override
	public String toString() {
		return "Page [currentPage=" + currentPage + ", totalsPage="
				+ totalsPage + ", pageSize=" + pageSize + ", totalsCount="
				+ totalsCount + ", result=" + result + ", uri=" + uri + "]";
	}
	
}
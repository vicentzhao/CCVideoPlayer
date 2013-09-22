package com.ccibs.ccvideoplayer.bean;

/**
 * 
 * @author CCDrive.ZhaoYiqun
 * 
 * 
 *         "page": { "currentPage": "1", "endset": "8", "firstPage": "true",
 *         "hasNextPage": "true", "hasPreviousPage": "false", "lastPage":
 *         "false", "offset": "0", "pageSize": "8", "totalPage": "2",
 *         "totalRows": "9" },
 * 
 */

public class PageBean {

	private String currentPage, endset, firstPage, hasNextPage,
			hasPreviousPage, lastPage, offset, pageSize, totalPage, totalRows;

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getEndset() {
		return endset;
	}

	public void setEndset(String endset) {
		this.endset = endset;
	}

	public String getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(String firstPage) {
		this.firstPage = firstPage;
	}

	public String getHasNextPage() {
		return hasNextPage;
	}

	public void setHasNextPage(String hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public String getHasPreviousPage() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(String hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public String getLastPage() {
		return lastPage;
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}

}

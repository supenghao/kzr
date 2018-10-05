package com.sunnada.kernel.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页数据
 * @author Administrator
 * @param <T>
 *
 */
public class Pager<T> {
	private int curPageNum;
	private int totalCount;
	private int pageSize;
	private int totalPageNum;
	
	private List<T> lists;
	
	private List<Integer> pages = new ArrayList<Integer>();
	
	private List<Map<String,Object>> maps;
	
	public Pager(){
		pages.add(20);
		pages.add(50);
		pages.add(100);
	}
	
	public void setPages(List<Integer> pages){
		this.pages = pages;
	}
	
	public List<Integer> getPages(){
		return pages;
	}

	public int getPageStart() {
		if (curPageNum > 0) {
			return (curPageNum - 1) * pageSize;
		}
		return 0;
	}

	public int getPageEnd() {
		return curPageNum * pageSize;
	}

	public int getCurPageNum() {
		return curPageNum;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}

	public int getTotalPageNum() {
		totalPageNum = this.totalCount / this.pageSize + (totalCount % pageSize == 0 ? 0 : 1);
		return totalPageNum;
	}

	public void setPageStart(int pageStart) {
	}

	public List<T> getLists() {
		return lists;
	}

	public void setLists(List<T> lists) {
		this.lists = lists;
	}

	public List<Map<String, Object>> getMaps() {
		return maps;
	}

	public void setMaps(List<Map<String, Object>> maps) {
		this.maps = maps;
	}
	public static Pager getPager() {
		Pager pager = new Pager();
		pager.setCurPageNum(1);
		pager.setPageSize(2);

		return pager;

	}
	

	
}


package com.dhk.api.dto;

public class TransWaterDto extends IdentityDto {
	
	private String keywordsType, keywords, filters, pageSize, pageNumber;
	
	/**
	 * 获取 keywordsType 变量
	 * 
	 * @return 返回 keywordsType 变量
	 */
	public String getKeywordsType() {
		return keywordsType;
	}

	/**
	 * 设置 keywordsType 变量
	 * 
	 * @param keywordsType
	 */
	public void setKeywordsType(String keywordsType) {
		this.keywordsType = keywordsType;
	}

	/**
	 * 获取 keywords 变量
	 * 
	 * @return 返回 keywords 变量
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 设置 keywords 变量
	 * 
	 * @param keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * 获取 filters 变量
	 * 
	 * @return 返回 filters 变量
	 */
	public String getFilters() {
		return filters;
	}

	/**
	 * 设置 filters 变量
	 * 
	 * @param filters
	 */
	public void setFilters(String filters) {
		this.filters = filters;
	}

	/**
	 * 获取 pageSize 变量
	 * 
	 * @return 返回 pageSize 变量
	 */
	public String getPageSize() {
		return pageSize;
	}

	/**
	 * 设置 pageSize 变量
	 * 
	 * @param pageSize
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取 pageNumber 变量
	 * 
	 * @return 返回 pageNumber 变量
	 */
	public String getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置 pageNumber 变量
	 * 
	 * @param pageNumber
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return super.toString() + "TransWaterDto [keywordsType=" + keywordsType
				+ ", keywords=" + keywords + ", filters=" + filters
				+ ", pageSize=" + pageSize + ", pageNumber=" + pageNumber + "]";
	}

}

package com.reps.es.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用于ES 全文检索（通用）
 * 
 * @author qianguobing
 * @date 2018年1月3日 下午2:51:06
 */
public class ContentVo {

	private String title;// 标题

	private String summary;// 概要

	private String url;// 内容的url

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date time;// 日期

	public ContentVo() {
		super();
	}

	public ContentVo(String title, String summary, String url, Date time) {
		super();
		this.title = title;
		this.summary = summary;
		this.url = url;
		this.time = time;
	}

	/**
	 * 名称<br>
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 综述<br>
	 * 
	 * @return
	 */
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 时间（显示给搜索用户）<br>
	 * 
	 * @return
	 */
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * 完整跳转地址<br>
	 * 必填，从HTTP开始
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

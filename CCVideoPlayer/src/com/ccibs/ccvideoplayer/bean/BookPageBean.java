package com.ccibs.ccvideoplayer.bean;

import java.io.Serializable;

public class BookPageBean implements Serializable{
	
	/**
	 * "ID": "137715894536000002",
            "NAME": "第十节 用来奔跑的雨天（3）",
            "URL": "http://vip.book.sina.com.cn/chapter/230324/377654.html",
            "TXTPATH": "/mnt/android_Book/Chapter/137715894536000002.txt",
            "SORT": "3",
            "VID": "137715894532970002",
            "WID": "137705335206980622",
            "STATUS": "10",
            "RR": "21"
	 */
	
	private String id,name,url,textPath,sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTextPath() {
		return textPath;
	}

	public void setTextPath(String textPath) {
		this.textPath = textPath;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	

}

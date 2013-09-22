package com.ccibs.ccvideoplayer.bean;

import java.util.ArrayList;

public class BookBean {
	
	/**
	 "ID": "137705321473080622",
    "NAME": "超越感悟",
    "PIC": "http://vipbook.sinaedge.com/bookcover/pics/166/cover_cc48625bbf9b55ed662a5a0a02674f35.jpg",
    "URL": "http://vip.book.sina.com.cn/book/index_230310.html",
    "AUTHOR": "薇薇",
    "TYPE": "自我完善",
    "STATUS": "10",
    "PRESS": "北方文艺出版社",
    "NEWTIME": "2013-03-01 21:53:12",
    "SYNOPSIS": "《超越感悟》——一部震撼生命的心灵福音书。 积极的成功处世哲学，温馨的亲情爱情故事，您只需要静静阅读五分钟，就能学会珍惜，学会豁达，学会爱与被爱，学会选择，学会放弃，学会把握一念之间的幸福。 150个感人至深的小故事，简明生动，荡气回肠，帮助你找到隐藏在心中的满满的爱。",
    "KEYWORDS": "励志 情感",
    "STATE": "全本",
    "TOP": "",
    "NUM": "81"
	 */
	
	private String id,name,pic,url,author,type,press,keywords,synopsis,state,num;
	

	private ArrayList<BookPageBean> bookPageBeanList;

	public ArrayList<BookPageBean> getBookPageBeanList() {
		return bookPageBeanList;
	}

	public void setBookPageBeanList(ArrayList<BookPageBean> bookPageBeanList) {
		this.bookPageBeanList = bookPageBeanList;
	}

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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}


	

}

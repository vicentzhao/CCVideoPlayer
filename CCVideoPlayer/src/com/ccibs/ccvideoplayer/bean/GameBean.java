package com.ccibs.ccvideoplayer.bean;

import java.util.ArrayList;

public class GameBean {
	/**
	 * "ID": "137835997648000622",
    "NAME": "成人版DNF《暗影之剑》猎人操作演示首曝aa",
    "SYNOPSIS": "成人横版动作网游《暗影之剑》是由畅游与韩国最大的独立工作室ＪＣＲ联合开发，今日正式曝出猎人操作演示视频。猎人是使用弓箭的远程攻击类职业，是所有职业中操作难度最高的一个职业。猎人的战法极为飘逸，他们在战场上来无影去无踪，风骚的走位让敌人头痛不已。这就决定了猎人是“放风筝”的战法。",
    "TYPE": "2",
    "ADDTIME": "2013-09-05 01:45:03",
    "PIC": "http://images.17173.com/2013/vlog/20130903/caifeiyang0911_1869318_2.jpg",
    "PICPATH": "",
    "RANKING": "",
    "CATEGORY": "24",
    "TIME": "",
    "LABEL": "暗影之剑 DNF 动作网游 猎人 格斗 aa",
    "RATE": "",
    "STATUS": "0",
    "TOP": "0",
    "RR": "1",
	 */
	private String id,name,synopsis,pic,label,type,videopath,htmlpath;
	private ArrayList<GameBean> gameBeanList;//相关的视频连接
	private PageBean pageBean;
	/*================================================*/
	
	
	public PageBean getPageBean() {
		return pageBean;
	}
	
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
	public String getVideopath() {
		return videopath;
	}


	public void setVideopath(String videopath) {
		this.videopath = videopath;
	}

	public String getHtmlpath() {
		return htmlpath;
	}

	public void setHtmlpath(String htmlpath) {
		this.htmlpath = htmlpath;
	}


	public ArrayList<GameBean> getGameBeanList() {
		return gameBeanList;
	}

	public void setGameBeanList(ArrayList<GameBean> gameBeanList) {
		this.gameBeanList = gameBeanList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}

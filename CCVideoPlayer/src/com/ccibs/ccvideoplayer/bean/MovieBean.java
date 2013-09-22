package com.ccibs.ccvideoplayer.bean;

import java.util.ArrayList;
import java.util.HashMap;


public class MovieBean {
    private String id;
	private String name;
	private String director;
	private String orotagonist;
	private String actor;
	private String status;
	private String addtime;
	private String pic;
	private String picpath;
	private String rr;
	private String area;
	private String playingTime;
	private String category,type;
	private ArrayList<String> defaultPlayList;
	public ArrayList<String> getDefaultPlayList() {
		return defaultPlayList;
	}
	public void setDefaultPlayList(ArrayList<String> defaultPlayList) {
		this.defaultPlayList = defaultPlayList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPlayingTime() {
		return playingTime;
	}
	public void setPlayingTime(String playingTime) {
		this.playingTime = playingTime;
	}
	private ArrayList<String> movieFromList; // 影视来源
	private HashMap<String, ArrayList<VideoPlayInfo>> moviePlayMap; //影视来源对应的视频源
	public ArrayList<String> getMovieFromList() {
		return movieFromList;
	}
	public void setMovieFromList(ArrayList<String> movieFromList) {
		this.movieFromList = movieFromList;
	}
	public HashMap<String, ArrayList<VideoPlayInfo>> getMoviePlayMap() {
		return moviePlayMap;
	}
	public void setMoviePlayMap(HashMap<String, ArrayList<VideoPlayInfo>> moviePlayMap) {
		this.moviePlayMap = moviePlayMap;
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
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getOrotagonist() {
		return orotagonist;
	}
	public void setOrotagonist(String orotagonist) {
		this.orotagonist = orotagonist;
	}
	
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public String getRr() {
		return rr;
	}
	public void setRr(String rr) {
		this.rr = rr;
	}
	
}

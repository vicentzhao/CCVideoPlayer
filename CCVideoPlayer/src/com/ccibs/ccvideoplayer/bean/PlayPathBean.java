package com.ccibs.ccvideoplayer.bean;

/**
 * "ID": "137691095149040002",
        "PLAYPATH": "http://www.letv.com/ptv/pplay/44199/1.html",
        "STATUS": "1",
        "SORT": "1",
        "UNITSNAME": "¿÷ ”",
        "RR": "1"

 */
public class PlayPathBean {
		
	private String id,unitsname,videoPath,htmlPath;

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getUnitsname() {
		return unitsname;
	}

	public void setUnitsname(String unitsname) {
		this.unitsname = unitsname;
	}
	
}

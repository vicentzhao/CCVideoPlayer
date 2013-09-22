package com.ccibs.ccvideoplayer.bean;

public class EduPlayBean {
	
	/**
	 * "ID": "137645341860620002",
            "STATE": "0",
            "CONTENT": "第一讲",
            "FILEPATH": "/mnt/digital/CCFiles/ccedu/jinghua/gaokao/yuwen/huzhengwei/1.mp4",
            "TITLE": "第一讲",
            "WID": "137645341860600002",
            "PRICE": "0",
            "PID": "136255115841900001",
            "USN": "0086000010234075",
            "ADDTIME": "2013-08-20 07:32:10"
            "QUALITY":"720p"
	 */
        
	 private String filePath,title,id,quality,url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	 
	 
}

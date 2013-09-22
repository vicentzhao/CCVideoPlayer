package com.ccibs.ccvideoplayer.bean;

public class VideoPlayInfo {
	/**
	 *    "ID": "137397245182750002",
                "URL": "http://www.letv.com/ptv/pplay/90907/1.html",
                "WID": "137397245182720002",
                "PATH": "http://g3.letv.cn/vod/v2/MzIvMzkvNTUvbGV0di11dHMvNDMyMDExOS1BVkMtMjUzNTMyLUFBQy0zMTU4NS05NTc2NDAtMzU2MjM5MTYtZmNlMzBjMTE0ODIwZDFhZTdhM2UyOTNjNmQ1ZWI0NzUtMTM3MDc3MjIyNzc1Ni5mbHY=?b=297&mmsid=2670748&tm=1373941386&key=8a9651d1fca43217c4e284bc10d61b0b&platid=1&splatid=101&playid=0&tss=no&retry=1",
                "QUALITY": "350",
                "USN": "1086000002443701",
                "STATUS": "0",
                "ADDTIME": "2013-07-16 07:00:58",
                "PIC": "http://i2.letvimg.com/vrs/201306/13/5b785c66c0ed4bfcaeeb3a2d2b748626.jpg",
                "PICPATH": "",
                "QUALITY_CN": "流畅",
                "UNITSNAME": "乐视"
                "RESOLUTION": "1920×1080",
                "FLAG": "0",
                "普通用户": "30",
                "VIP用户": "20",
                "包月用户": "0"
            },
	 */
	
	 private String id,webUrl,wId,playPath,quality,imagePath,quality_cn,unitsname,resolution,nomaluser,vipuser,monthuser,defaultPlayUrl;

	public String getDefaultPlayUrl() {
		return defaultPlayUrl;
	}

	public void setDefaultPlayUrl(String defaultPlayUrl) {
		this.defaultPlayUrl = defaultPlayUrl;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getNomaluser() {
		return nomaluser;
	}

	public void setNomaluser(String nomaluser) {
		this.nomaluser = nomaluser;
	}

	public String getVipuser() {
		return vipuser;
	}

	public void setVipuser(String vipuser) {
		this.vipuser = vipuser;
	}

	public String getMonthuser() {
		return monthuser;
	}

	public void setMonthuser(String monthuser) {
		this.monthuser = monthuser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getwId() {
		return wId;
	}

	public void setwId(String wId) {
		this.wId = wId;
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getQuality_cn() {
		return quality_cn;
	}

	public void setQuality_cn(String quality_cn) {
		this.quality_cn = quality_cn;
	}

	public String getUnitsname() {
		return unitsname;
	}

	public void setUnitsname(String unitsname) {
		this.unitsname = unitsname;
	}
	 

}

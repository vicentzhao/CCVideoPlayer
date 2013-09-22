package com.ccibs.ccvideoplayer.util;

import android.R.integer;

public class HttpRequest {

	// private String WEB_ROOT = "http://124.193.177.154:9007/";
	// private String WEB_ROOT = "http://119.255.47.242:8080/";
//	private String WEB_ROOT = "http://apk.vocy.com/";
	
	 private String WEB_ROOT="";
	// http://124.193.177.154:9007/index/softshop!getFile.action?token="+mytoken+"&sid=135909554233700001&type=music
	// http://192.168.1.32:8080
	// private String WEB_ROOT = "http://42.121.6.154/";
 
	public void setWEB_ROOT(String wEB_ROOT) {
		WEB_ROOT = wEB_ROOT;
	}

	private static HttpRequest request;
	private String apkuuid;
	private String id;
	private int count;     //页数
	private int pageSize;  //每页的数据
	
	private String type; //频道类型
	private String commicKind;  //漫画类型
	private String Sid;
	
	
	public String getSid() {
		return Sid;
	}

	public void setSid(String sid) {
		Sid = sid;
	}

	public String getCommicKind() {
		return commicKind;
	}

	public void setCommicKind(String commicKind) {
		this.commicKind = commicKind;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMovieFavUpLoadPath() {
		return null;
	}

	public String getMytoken() {
		return mytoken;
	}
	
	public String getURL_LIST_ADDFAVMOVIE(){
		return WEB_ROOT+"androidChannelAction!addFavorite.action?token="+mytoken+"&resultType=json&channelid="+type+"&id="+id;
	}
	
	//判断电视剧是否收藏
	public String getURL_TV_ISCOLLECT(){
		return WEB_ROOT+"androidChannelAction!isWorkFavorite.action?token="+mytoken+"&resultType=json&channelid=22&id="+id;
	}
	
	//收藏教育影片
	public String getURL_LIST_ADDFAVEduMovie(){
		return WEB_ROOT+"educationshop!favorite.action?token="+mytoken+"&resultType=json&wid="+id;
	}
	//收藏相关图书
	public String getURL_LIST_ADDFAVBOOK(){
		return WEB_ROOT+"educationshop!favorite.action?token="+mytoken+"&resultType=json&wid="+id;
	}
	//收藏电视剧
	public String getURL_LIST_ADDFAVTV(){
		return WEB_ROOT+"androidChannelAction!addFavorite.action?token="+mytoken+"&resultType=json&channelid="+type+"&id="+id;
	}
	
	public String getURL_MOVIE_CANCLE_COLLECT(){
		return WEB_ROOT+"androidChannelAction!delFavorite.action?token="+mytoken+"&resultType=json&channelid=1&id="+id;
	}
	//判断影片是否收藏
	public String getURL_isCollect_FAVMOVIE(){
		return WEB_ROOT+"androidChannelAction!isWorkFavorite.action?token="+mytoken+"&resultType=json&channelid="+type+"&id="+id;
	}
	//判断edu是否收藏
	public String getURL_EduIsCollect() {
		return WEB_ROOT+"educationshop!isfavorite.action?token="+mytoken+"&resultType=json&wid="+id;
	}
	// 获取下载的uuid
	public String getURL_UPDATE_APK() {
		return "http://api.vocy.com/android!getFunction.action?arg0=droidpc_app_getversion&arg1=";
	}

	public String getApkuuid() {
		return apkuuid;
	}

	public void setApkuuid(String apkuuid) {
		this.apkuuid = apkuuid;
	}

	public void setMytoken(String mytoken) {
		this.mytoken = mytoken;
	}

	private HttpRequest() {
	}

	public static HttpRequest getInstance() {
		if (request == null) {
			request = new HttpRequest();
		}
		return request;
	}

	private String mytoken = "";

	public String getWEB_ROOT() {
		return WEB_ROOT;
	}
	

	public String getURL_QUERY_SINGLE_IMAGE() {
		return WEB_ROOT + "download.action?token=" + mytoken + "&inputPath=";
	}

	public String getURL_QUERY_DOWNLOAD_URL() {
		return WEB_ROOT + "index/download.action?token=" + mytoken
				+ "&inputPath=";// 下载文件;
	}

	// 更新下载地址
	public String getURL_DOWN_UPDATE_APK() {
		return "http://api.vocy.com/apk_file/" + apkuuid + ".apk";
	}
	
	//public  static final String webServiceURL="http://192.168.1.3:2015/services/";
		public  static final String webServiceURL="http://service.vocy.com/services/";
		//public  static final String webServiceURL="http://192.168.1.199:8080/services/";
		
		//获得级数
		public String getURL_TVEpisode_Play(){
			return "http://192.168.1.3:2014/html/workplay/workplay_"+type+"_"+id+"_"+count+".txt";
		}
		
		//获得教育播放
		public String getURL_EDUEpisode_Play(){
			return "http://192.168.1.3:2014/html/workplay/workplay_"+type+"_"+Sid+"_"+count+".txt";
		}
		
		
		public String getURL_eduDetailPath(){
			return WEB_ROOT+"educationshop!information.action?type="+type+"&token="+mytoken+"&resultType=json&wid="+id;
		}
		
//		public String getURL_eduPlayList(){
//			return WEB_ROOT+"educationshop!source.action?type="+type+"&token="+mytoken+"&resultType=json&wid="+id;
//		}
		 //根据id获得播放地址
		public String getURL_eduPlaySinglePath(){
			return WEB_ROOT+"educationshop!video.action?token="+mytoken+"&resultType=json&id=";
		}
		public String getURL_TVDetail(){
			return "http://192.168.1.3:2014/html/work/work_"+type+"_1_1_"+id+".txt";
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}

		public String getURL_BookDetail(){
			return "http://192.168.1.3:2014/html/work/work_"+type+"_"+pageSize+"_"+count+"_"+id+".txt";
		}

}

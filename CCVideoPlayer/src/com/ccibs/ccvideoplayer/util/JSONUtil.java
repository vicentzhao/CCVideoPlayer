package com.ccibs.ccvideoplayer.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ccibs.ccvideoplayer.bean.BookBean;
import com.ccibs.ccvideoplayer.bean.BookPageBean;
import com.ccibs.ccvideoplayer.bean.EduBean;
import com.ccibs.ccvideoplayer.bean.EduPlayBean;
import com.ccibs.ccvideoplayer.bean.GameBean;
import com.ccibs.ccvideoplayer.bean.MovieBean;
import com.ccibs.ccvideoplayer.bean.PageBean;
import com.ccibs.ccvideoplayer.bean.PlayPathBean;
import com.ccibs.ccvideoplayer.bean.TVBean;
import com.ccibs.ccvideoplayer.bean.VideoPlayInfo;


public class JSONUtil {
	public static MovieBean getMoiveList(String info) {
		System.out.println("电影下载的数据"+info);
		MovieBean movie = new MovieBean();
		VideoPlayInfo vpi =null;
		ArrayList<String> sourceList = new  ArrayList<String>();
		HashMap<String, ArrayList<VideoPlayInfo>> playInfoMap=new HashMap<String, ArrayList<VideoPlayInfo>>();
		if (info == null)
		    return movie;
		try {
				JSONObject jsonO = new JSONObject(info);
			
				movie = new MovieBean();
				movie.setId(jsonO.getString("ID"));
				movie.setName(jsonO.getString("NAME"));
				movie.setOrotagonist(jsonO.getString("SYNOPSIS"));
				movie.setType(jsonO.getString("TYPE"));
				movie.setActor(jsonO.getString("PROTAGONIST"));
				movie.setStatus(jsonO.getString("STATUS"));
				movie.setAddtime(jsonO.getString("DECADE"));
				movie.setPlayingTime(jsonO.getString("TIME"));
//				movie.setArea(jsonO.getString("AREAINCHINESE"));
				
				movie.setPic(jsonO.getString("PIC"));
//				movie.setRr(jsonO.getString("RR"));	
				movie.setCategory(jsonO.getString("CATEGORY"));
				movie.setDirector(jsonO.getString("DIRECTOR"));
				movie.setOrotagonist(jsonO.getString("SYNOPSIS"));
				ArrayList<String> defaultPlayList = new ArrayList<String>();
				
					 ArrayList<VideoPlayInfo> VideoPlayInfoList;
					 JSONArray ja =jsonO.getJSONArray("movieplay");
						for (int j = 0; j < ja.length(); j++) {
							VideoPlayInfoList = new ArrayList<VideoPlayInfo>();
							JSONObject videoJson =ja.getJSONObject(j);
							String type =videoJson.getString("UNITSNAME");
							 if(playInfoMap.containsKey(type))continue;
							 String defaultPlayUrl=videoJson.getString("URL");
							 defaultPlayList.add(defaultPlayUrl);
							sourceList.add(type);
							JSONArray videoPlayInfoArray =videoJson.getJSONArray("moviesource");
							for (int i = 0; i < videoPlayInfoArray.length(); i++) {
								/**
								 *   "ID": "3",
                    "URL": "http://g3.letv.cn/vod/v2/MzAvMzkvNzYvbGV0di11dHMvMzcwODYxOC1BVkMtNTM3OTM1LUFBQy0zMTU4Ni01Mjk2ODgwLTM4OTg0ODIyNy04NzcyOWVjMDI4MmYzODhhMDgxNGJjZGU4NzMwNDVhZi0xMzY5MjMwMTYzNTM2LmZsdg==?b=588&mmsid=336598&tm=1374675631&key=11111171be01a1e880cf48abbf973cc8&platid=1&splatid=101&playid=0&tss=no",
                    "WID": "137465088697000001",
                    "QUALITYID": "1000",
                    "USN": "1086000002443701",
                    "STATUS": "1",
                    "ADDTIME": "2013-07-27 03:09:20",
                    "SORT": "1",
                    "UNITSNAME": "乐视",
                    "NAME": "1000",
                    "QUALITY": "超清",
                    "RESOLUTION": "1920×1080",
                    "FLAG": "0",
                    "普通用户": "30",
                    "VIP用户": "20",
                    "包月用户": "0"
								 */
								vpi = new VideoPlayInfo();
								JSONObject videoPlayInfoJson = videoPlayInfoArray.getJSONObject(i);
								String QUALITY_CN =null;
								if(!videoPlayInfoJson.isNull("QUALITY")){
									QUALITY_CN=videoPlayInfoJson.getString("QUALITY");
								}
								String QUALITYID=videoPlayInfoJson.getString("QUALITYID");
//								String PATH =videoPlayInfoJson.getString("PATH");
								String ID =videoPlayInfoJson.getString("ID");
//								String webUrl=videoPlayInfoJson.getString("URL");
								String nomaluser=videoPlayInfoJson.getString("普通用户");
								String vipuser=videoPlayInfoJson.getString("VIP用户");
								String monthuser=videoPlayInfoJson.getString("包月用户");
								String resolution=videoPlayInfoJson.getString("RESOLUTION");
								vpi.setQuality_cn(QUALITY_CN);
								vpi.setId(ID);
//								vpi.setPlayPath(PATH);
								
								vpi.setWebUrl(defaultPlayUrl);
								vpi.setResolution(resolution);
								vpi.setNomaluser(nomaluser);
								vpi.setVipuser(vipuser);
								vpi.setMonthuser(monthuser);
								vpi.setQuality(QUALITYID);
								VideoPlayInfoList.add(vpi);
							}
							playInfoMap.put(type, VideoPlayInfoList);
							}
						movie.setDefaultPlayList(defaultPlayList);
						movie.setMovieFromList(sourceList);
						movie.setMoviePlayMap(playInfoMap);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movie;
	}
	/**
	 *  "ID": "137663821320130622",
    "NAME": "飞刀又见飞刀",
    "DIRECTOR": "",
    "SYNOPSIS": "李坏是个孤儿，他脸上的微笑仿佛永远不会消失。从来没有人知道，他来自世代簪缨的家族！他的爷爷更是近百年来江湖中独一无二的名侠“小李飞刀”。 漂泊多年，李坏凭借亡母的锦囊里藏着的一张宝图，独处七年，练成了天下无双的飞刀，找到了富可敌国的宝藏。 为了心中不灭的理想和爱情，李坏回到少年时的山城寻找心上人方可可。可惜一切带给李坏的只有无边噩梦。李坏被怀疑与皇宫大内的失窃黄金有关，一时成了江湖人人必诛之而后快的大盗。然而世事难料，奉皇命捕杀李坏的“月神”薛采月竟然爱上了李坏。他们排除万难，有了一个温暖的家。直到有一天李坏醒来发觉已怀了他骨肉的月神不告而别，只留下一张字条，说和李坏情缘已尽。原来月神要向李坏之父李曼青挑战，她要摧毁“小李飞刀”这个江湖神话，替亡父报仇。但是李坏血脉里流着属于“小李飞刀”的血，他绝不可以令家族荣誉受污。",
    "TYPE": "武侠 古装 言情",
    "PROTAGONIST": "张智霖 董洁 林心如 高鑫 韩雪 寇振海 岳跃利 孙兴 郑国霖",
    "STATUS": "1",
    "ADDTIME": "2013-08-19 07:55:07",
    "PIC": "http://img5.hao123.com/data/1_0772284f37d457519c268ddbcfd8bf85",
    "PICPATH": "",
    "RANKING": "",
    "DECADE": "",
    "AREA": "",
    "CATEGORY": "1",
    "   ": "",
    "AREAINCHINESE": "",
    "PRODUCER": "",
    "WRITER": "",
    "PRESENTOR": "",
    "YEAR": "2003",
    "RATE": "",
    "SETNUMBER": "共43集",
    "num": "40"
	 * @param tvbean
	 * @return
	 */
	public static TVBean getTV(String tvbean){
		TVBean tv  =new TVBean();
		try {
			JSONObject jo = new JSONObject(tvbean);
			tv.setName(jo.getString("NAME"));
			tv.setSynopsis(jo.getString("SYNOPSIS"));
			tv.setProtagonist(jo.getString("PROTAGONIST"));
			tv.setDirector(jo.getString("DIRECTOR"));
			tv.setArea(jo.getString("AREA"));
			tv.setType(jo.getString("TYPE"));
			tv.setPic(jo.getString("PIC"));
			tv.setNum(jo.getString("SETNUMBER"));
			if(!jo.isNull("YEAR")){
			tv.setYear(jo.getString("YEAR"));
			}else{
				tv.setYear("未知");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tv;
		
	}
	
	/**
	 * "ID": "137691095149040002",
            "PLAYPATH": "http://www.letv.com/ptv/pplay/44199/1.html",
            "STATUS": "1",
            "SORT": "1",
            "UNITSNAME": "乐视",
            "RR": "1"

	 */
	
	public static ArrayList<PlayPathBean> getTvPlayPath(String info){
		System.out.println("播放的地址为===="+info);
		ArrayList<PlayPathBean> list =new ArrayList<PlayPathBean>();
		JSONArray ja;
		String source;
		try {
			ja = new JSONArray(info);
			 for (int i = 0; i < ja.length(); i++) {
				 PlayPathBean ppb = new PlayPathBean();
				JSONObject jplay= ja.getJSONObject(i);
				if(!jplay.isNull("UNITSNAME")){
					source = jplay.getString("UNITSNAME");
				}else{
					source ="未知";
				}
				if(!jplay.isNull("HTMLPATH")&&!"".equals(jplay.getString("HTMLPATH"))){
					ppb.setHtmlPath(jplay.getString("HTMLPATH").trim());
				}
//				else{
//					ppb.setHtmlPath(jplay.getString("HTMLPATH").trim());
//				}
				ppb.setUnitsname(source);
				list.add(ppb);
			}
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * "ID": "137645341860600002",
    "STATE": "0",
    "ADDTIME": "2013-08-13 08:10:00",
    "DIRECTOR": "",
    "ROSE": "胡正伟",
    "KIND": "高考,语文",
    "COUNTRY": "",
    "COST": "",
    "PIC": "/mnt/digital/CCFiles/ccedu/jinghua/logo.png",
    "NAME": "语文考前叮咛讲座",
    "TYPE": "26",
    "SND": "1",
    "THIRD": "7",
    "NOTE": "精华在线教学视频",
    "USN": "0086000010234075"
	 */
	public static EduBean getEduBean(String info){
		EduBean bean = new EduBean();
		try {
			JSONObject jo = new JSONObject(info);
			bean.setRose(jo.getString("ROSE"));
			bean.setName(jo.getString("NAME"));
			bean.setNote(jo.getString("NOTE"));
			String path = jo.getString("PIC");
			bean.setNum(jo.getString("NUM"));
			if(path.contains("http")){
				bean.setPic(path);
			}else{
				path = HttpRequest.getInstance().getURL_QUERY_SINGLE_IMAGE()+path;
				bean.setPic(path);
			}
			bean.setPic(path);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bean;
	}
	
	/**
	 "ID": "137905886007880001",
        "STATE": "",
        "CONTENT": "",
        "FILEPATH": "",
        "TITLE": "美丽面对面_20130611_打造气血通畅的无斑美人 电视栏目 综艺 关注",
        "WID": "137905886007860001",
        "PRICE": "1",
        "PID": "136255115841900001",
        "USN": "0086000010234075",
        "ADDTIME": "2013-09-13 03:59:14",
        "URL": "http://v.baidu.com/kan/tvshow/?id=8269&site=wasu.cn&n=20130611&url=http://www.wasu.cn/Play/show/id/1108182?refer=video.baidu.com#frp=v.baidu.com/show_intro/",
        "PATH": "http://vodipad.wasu.cn/pcsan08/mrms/vod/201306/16/10/2013061610283302301c3b2fd_a0a55c6c.mp4",
        "RR": "1"
	 */
	
	public static ArrayList<EduPlayBean> getEduPlayBeanList(JSONArray ja){
		ArrayList<EduPlayBean> list=  new ArrayList<EduPlayBean>();
		
		try {
		
			for (int i = 0; i < ja.length(); i++) {
				EduPlayBean epb = new EduPlayBean();
				 JSONObject playBeanOb = ja.getJSONObject(i);
				 String id  = playBeanOb.getString("ID");
				 if(!playBeanOb.isNull("PATH")){
				 String path = playBeanOb.getString("PATH");
				 String url = playBeanOb.getString("URL");
				 epb.setUrl(url);
					if(path.contains("http")){
						epb.setFilePath(path);
					}else{
						path = HttpRequest.getInstance().getURL_QUERY_SINGLE_IMAGE()+path;
						epb.setFilePath(path);
					}
				 }
					epb.setTitle(playBeanOb.getString("TITLE"));
					epb.setId(id);
					list.add(epb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * get BookDetail
	 *     "ID": "137705321473080622",
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
	 * @param info
	 * @return
	 */
	
	public static BookBean getBookBean(String info){
		BookBean bookBean = new BookBean();
		ArrayList<BookPageBean> bPageBeansList= new ArrayList<BookPageBean>();
		try {
			JSONObject jo =new JSONObject(info);
			bookBean.setName(jo.getString("NAME"));
			bookBean.setPic(jo.getString("PIC"));
			bookBean.setUrl(jo.getString("URL"));
			bookBean.setAuthor(jo.getString("AUTHOR"));
			bookBean.setType(jo.getString("TYPE"));
			bookBean.setPress(jo.getString("PRESS"));
			bookBean.setSynopsis(jo.getString("SYNOPSIS"));
			bookBean.setKeywords(jo.getString("KEYWORDS"));
			bookBean.setState(jo.getString("STATE"));
			bookBean.setNum(jo.getString("NUM"));
			JSONArray jArray = jo.getJSONArray("data");
			for (int i = 0; i < jArray.length(); i++) {
				BookPageBean bPageBean =new BookPageBean();
				JSONObject pageJsonObject = jArray.getJSONObject(i);
			    bPageBean.setId(pageJsonObject.getString("ID"));
			    bPageBean.setName(pageJsonObject.getString("NAME"));
			    String path =pageJsonObject.getString("TXTPATH");
			    if(!path.startsWith("http")){
			    	path =HttpRequest.getInstance().getURL_QUERY_SINGLE_IMAGE()+path;
			    }
			    bPageBean.setTextPath(path);
				bPageBean.setUrl(pageJsonObject.getString("URL"));
				bPageBeansList.add(bPageBean);
			}
			bookBean.setBookPageBeanList(bPageBeansList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookBean;
	}
	
	/**
	 * "ID": "137837348271700622",
    "NAME": "51新炫舞CG动画高清 aa",
    "SYNOPSIS": "51新炫舞CG动画高清",
    "TYPE": "网络游戏",
    "ADDTIME": "2013-09-05 05:34:37",
    "PIC": "http://i3.17173.itc.cn/2010/vlog/20100108/shenoo522_604448_2.jpg",
    "PICPATH": "" ",
    "CATEGORY": "24",
    "TIME": "",
    "LABEL": "51新炫舞 CG动画 高清 aa",
    "RATE": "",
    "STATUS": "1",
    "TOP": "0",
    "TYPEID": "2",
    "RR": "1",
    
    "page": {
        "currentPage": "1",
        "endset": "8",
        "firstPage": "true",
        "hasNextPage": "true",
        "hasPreviousPage": "false",
        "lastPage": "false",
        "offset": "0",
        "pageSize": "8",
        "totalPage": "2",
        "totalRows": "9"
    },
	 */
	 public static GameBean getGameBeanInfo(String info){
		 System.out.println("游戏的下载的地址===》"+info);
		 GameBean  gb = new GameBean();
		 ArrayList<GameBean> gbList = new ArrayList<GameBean>();
		 try {
			JSONObject gameJsonObject = new JSONObject(info);
			gb.setId(gameJsonObject.getString("ID"));
			gb.setName(gameJsonObject.getString("NAME"));
			gb.setType(gameJsonObject.getString("TYPE"));
			gb.setPic(gameJsonObject.getString("PIC"));
			gb.setSynopsis(gameJsonObject.getString("SYNOPSIS"));
			gb.setLabel(gameJsonObject.getString("LABEL"));
			if(!gameJsonObject.isNull("videopath")&&!"".equals(gameJsonObject.getString("videopath"))){
				gb.setVideopath(gameJsonObject.getString("videopath").trim());
			}else{
				gb.setHtmlpath(gameJsonObject.getString("htmlpath").trim());
			}
			JSONObject jsonPage = gameJsonObject.getJSONObject("page");
			PageBean pageBean = new PageBean();
			pageBean.setCurrentPage(jsonPage.getString("currentPage"));
			pageBean.setPageSize(jsonPage.getString("pageSize"));
			pageBean.setTotalPage(jsonPage.getString("totalPage"));
			pageBean.setTotalRows(jsonPage.getString("totalRows"));
			gb.setPageBean(pageBean);
			JSONArray  jaArray = gameJsonObject.getJSONArray("data");
			if(null!=jaArray&&jaArray.length()!=0){
			for (int i = 0; i < jaArray.length(); i++) {
				GameBean  gbb = new GameBean();
				JSONObject gameListJson=jaArray.getJSONObject(i);
				gbb.setId(gameListJson.getString("ID"));
				gbb.setName(gameListJson.getString("NAME"));
//				gbb.setType(gameListJson.getString("TYPE"));
				gbb.setPic(gameListJson.getString("PIC"));
//				gbb.setSynopsis(gameListJson.getString("SYNOPSIS"));
//				gbb.setLabel(gameListJson.getString("LABEL"));
//				if(!gameListJson.isNull("videopath")){
//					gbb.setVideopath(gameListJson.getString("videopath"));
//				}else{
//					gbb.setHtmlpath(gameListJson.getString("htmlpath"));
//				}
				gbList.add(gbb);
			}
			}
			gb.setGameBeanList(gbList);
			return gb;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	 }
}

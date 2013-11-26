package com.ccibs.ccvideoplayer.util.pathparse;


import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Letv {

	public ArrayList<String> getLeTv(String url, String type) {
//		System.out.println("解析下来的url地址"+url);
		String string = null;
		ArrayList<String> paths = new ArrayList<String>();

		try {
			String html = NetBox.getHttpContent(url, "utf-8");
			if(html!=null){
				String vid = null;
				String htmlAll = Jsoup.parse(html).html();
//				System.out.println("解析下来的htmlall地址"+htmlAll);
				Matcher matcher = Pattern.compile("vid:(\\d+),").matcher(htmlAll);
				if (matcher.find()) {
					vid = matcher.group(1);
				}
				if (vid != null) {
					Document read;
	
					read = new SAXReader().read(new URL("http://www.letv.com/v_xml/" + vid + ".xml"));
	
					String text = read.selectSingleNode("root/playurl").getText();
					String replaceAll = text.replaceAll("\\\\", "");
	
					if (type == "1300" || type.equals("1300")) {
						Matcher matcher2 = Pattern.compile(
								"1300\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
								.matcher(replaceAll);
						if (matcher2.find()) {
							string = matcher2.group(1);
							paths.add("1");
							paths.add(string);
							return paths;
						}else{
							Matcher matcher5 = Pattern.compile(
									"1000\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
									.matcher(replaceAll);
							if (matcher5.find()) {
								string = matcher5.group(1);
								paths.add("1");
								paths.add(string);
							}else{
								Matcher matcher4 = Pattern.compile(
										"720p\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
										.matcher(replaceAll);
								if (matcher4.find()) {
									string = matcher4.group(1);
									paths.add("1");
									paths.add(string);
								}else{
									Matcher matcher3 = Pattern.compile(
											"350\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
											.matcher(replaceAll);
									if (matcher3.find()) {
										string = matcher3.group(1);
										paths.add("0");
										paths.add(string);
									}
								}
							}
						}
					}
					if (type == "1000" || type.equals("1000")) {
						Matcher matcher5 = Pattern.compile(
								"1000\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
								.matcher(replaceAll);
						if (matcher5.find()) {
							string = matcher5.group(1);
							paths.add("1");
							paths.add(string);
						}
					}
					if (type == "720p" || type.equals("720p")) {
						Matcher matcher4 = Pattern.compile(
								"720p\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
								.matcher(replaceAll);
						if (matcher4.find()) {
							string = matcher4.group(1);
							paths.add("1");
							paths.add(string);
						}else{
							Matcher matcher5 = Pattern.compile(
									"1000\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
									.matcher(replaceAll);
							if (matcher5.find()) {
								string = matcher5.group(1);
								paths.add("1");
								paths.add(string);
							}
						}
					}
					if (type == "350" || type.equals("350")) {
						Matcher matcher3 = Pattern.compile(
								"350\":\\[\"(?=http)([^\\[\\]]*)\",\\d+(?=\\])")
								.matcher(replaceAll);
						if (matcher3.find()) {
							string = matcher3.group(1);
							paths.add("0");
							paths.add(string);
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

		return paths;
	}

	/*
	 * http://www.letv.com/ptv/pplay/90638/1.html type=720p return video url
	 */

	public ArrayList<String> getPath(String url, String type) {
		ArrayList<String> paths= new ArrayList<String>();
		String html = NetBox.getHttpContent(url, "utf-8");
		org.jsoup.nodes.Document document = Jsoup.parse(html);
		String mid = document.select(".likeDown a.like").attr("musicid");
		String string = NetBox
				.getHttpContent(
						"http://antiserver.kuwo.cn/anti.s?type=convert%5Furl&response=url&format=aac%7Cmp3&rid=MUSIC%5F"
								+ mid, "utf-8");
		if (string == null) {
			string = NetBox
					.getHttpContent(
							"http://antiserver.kuwo.cn/anti.s?type=convert%5Furl&response=url&format=aac%7Cmp3&rid=MUSIC%5F"
									+ mid, "utf-8");
		}
		String path = string;
		paths.add(path);
		return paths;
	}

	public ArrayList<String> getVideoUrl(String url, String type) {
		ArrayList<String> result = null;
		if (url.contains("letv.com")) {
			result = getLeTv(url, type);
		}

		if (url.contains("kuwo.cn")) {
			result = getPath(url, type);
		}
		if(result==null){
			result=getFlvcdPath(url, type);
		}
		return result;
	}

	public static void main(String[] args) {
//		String url = new Letv().getVideoUrl("http://v.youku.com/v_show/id_XNjIyNzI3OTQw_ev_1.html", "720p");
		ArrayList<String> videoUrl = new Letv().getVideoUrl("http://www.letv.com/ptv/vplay/2113983.html", "1000");
		if(null!=videoUrl&&videoUrl.size()!=0)
		for (int i = 0; i < videoUrl.size(); i++) {
			System.out.println(videoUrl.get(i));
		}
//		System.out.println(url);
	}
	
	public ArrayList<String> getFlvcdPath(String url, String type){
		ArrayList<String> paths = new ArrayList<String>();
		String path = null;
		url = "http://www.flvcd.com/parse.php?format=&kw=" + url;
		String html = NetBox.getHttpContent(url, "gb2312");
		if (html != null) {
			org.jsoup.nodes.Document document = Jsoup.parse(html);
			Elements elements = document.select(
					"td[class=mn STYLE4]:contains(下载地址：)").select("a");
			if (elements.size() > 0) {
				paths.add("0");
				for (Element element : elements) {
						path = element.attr("href");
					    paths.add(path);
					
				}
			}
			String higeUrl = document.select("td[class=mn STYLE4] a:contains(超清版解析)").attr("href");
			System.out.println("chaoqing");
			if(higeUrl == null || higeUrl.equals("")){
				higeUrl = document.select("td[class=mn STYLE4] a:contains(高清版解析)").attr("href");
				System.out.println("gaichenggaoqingle");
			}
			if (higeUrl != null && !higeUrl.equals("")) {
				higeUrl = "http://www.flvcd.com/" + higeUrl;
				String higrHtml = NetBox.getHttpContent(higeUrl, "utf-8");
				if(higrHtml!=null){
					org.jsoup.nodes.Document document1 = Jsoup.parse(higrHtml);
					Elements elements1 = document1.select(
							"td[class=mn STYLE4]:contains(下载地址：)").select("a");
					if (elements1.size() > 0) {
						paths.clear();
						paths.add("1");
						for (Element element : elements1) {
								path = element.attr("href");
								paths.add(path);
							
						}
					} 
				}
			}
		}
//		if (!NetBox.isAlive(path, "gb2312")) {
//			path = null;
//		}
		return paths;
	}

}

package com.ccibs.ccvideoplayer.util.pathparse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;

public class NetBox{
	
public static String getHttpContent(String url, String charset) {
		try {
//			System.out.println(url);
			GetMethod getMethod = new GetMethod(url);
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			HttpConnectionParams hcp = httpClient.getHttpConnectionManager().getParams();
			hcp.setConnectionTimeout(60000);
			hcp.setParameter("UserAgent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
			hcp.setSoTimeout(60000);
			int retCode = httpClient.executeMethod(getMethod);
			if (retCode == HttpStatus.SC_OK) {
				InputStream in = getMethod.getResponseBodyAsStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte b[] = new byte[64 * 1024];
				int num;
				while ((num = in.read(b)) > 0) {
					out.write(b, 0, num);
				}
				return out.toString(charset);
			}else{
				System.out.println(retCode);
//				System.out.println(url);
			}
		  getMethod.releaseConnection();
		} catch (HttpException e) {
			System.err.println("HTTP ERROR: " + url);
		} catch (IOException e) {
			System.err.println("HTTP ERROR: " + url);
		}
		return null;
	}
	


	public static boolean isAlive(String url, String charset) {
		try {
			boolean flag = false;
			GetMethod getMethod = new GetMethod(url);
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			HttpConnectionParams hcp = httpClient.getHttpConnectionManager().getParams();
			hcp.setConnectionTimeout(60000);
			hcp.setParameter("UserAgent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
			hcp.setSoTimeout(60000);
			int retCode = httpClient.executeMethod(getMethod);
			if (retCode == HttpStatus.SC_OK) {
				flag = true;
			}
		  getMethod.releaseConnection();
		  return flag;
		} catch (HttpException e) {
			System.err.println("HTTP ERROR: " + url);
		} catch (IOException e) {
			System.err.println("HTTP ERROR: " + url);
		}
		return false;
	}

	public static String getHttpZipContent(String url, String charset) {
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient httpClient = new HttpClient();
			HttpConnectionParams hcp = httpClient.getHttpConnectionManager().getParams();
			hcp.setConnectionTimeout(60000);
			hcp.setSoTimeout(60000);
			int retCode = httpClient.executeMethod(getMethod);
			if (retCode == HttpStatus.SC_OK) {
				GZIPInputStream in = new GZIPInputStream(getMethod.getResponseBodyAsStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte b[] = new byte[64 * 1024];
				int num;
				while ((num = in.read(b)) > 0) {
					out.write(b, 0, num);
				}
				return out.toString(charset);
			}
		} catch (HttpException e) {
			System.err.println("HTTP ERROR: " + url);
		} catch (IOException e) {
			System.err.println("HTTP ERROR: " + url);
		}
		return null;
	}

	public static boolean saveHttpContent(String url, String fileName) {
		int idx = fileName.lastIndexOf(File.separatorChar);
		File f = new File(fileName.substring(0, idx));
		if (! f.exists()) {
			f.mkdirs();
		}
		try {
			GetMethod getMethod = new GetMethod(url);
			HttpClient httpClient = new HttpClient();
			HttpConnectionParams hcp = httpClient.getHttpConnectionManager().getParams();
			hcp.setConnectionTimeout(60000);
			hcp.setSoTimeout(60000);
			int retCode = httpClient.executeMethod(getMethod);
			if (retCode == HttpStatus.SC_OK) {
				InputStream in = getMethod.getResponseBodyAsStream();
				FileOutputStream out = new FileOutputStream(fileName);
				byte b[] = new byte[64 * 1024];
				int num;
				while ((num = in.read(b)) > 0) {
					out.write(b, 0, num);
				}
				out.close();
				return true;
			}
		} catch (HttpException e) {
			System.err.println("HTTP ERROR: " + url);
		} catch (IOException e) {
			System.err.println("HTTP ERROR: " + url);
		}
		return false;
	}
	
	/*public static String getJSHttpContent(String url,String charset){
	    String input = null;
		try {
			WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_6);
			HtmlPage page = null;
			page = client.getPage(url);
			client.waitForBackgroundJavaScript(5000l);
			input = page.getElementById("results-list").asXml();
		} catch (Exception e) {
//			e.printStackTrace();
		} 
    	return input;
	}*/
	
	public static String stripHtmlTags(String htmlCode) {
		if (htmlCode == null) {
			return "";
		}
		htmlCode = htmlCode.replace("</p>", "#line#");
		htmlCode = htmlCode.replace("</h3>", "#line#");
		htmlCode = htmlCode.replace("</strong>", "#line#");
		
		Pattern p2 = Pattern.compile("<script type=\"text/javascript\">.*?</script>");
		Matcher m2 = p2.matcher(htmlCode);
		String input = m2.replaceAll("");
		
		String pattern = "([^<]*)(<[^>]+>)?";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		String part;
		while (m.find()) {
			if ((part = m.group(1)) != null) {
				sb.append(part);
			}
		}

		Pattern p1 = Pattern.compile("\r|\n");
		Matcher m1 = p1.matcher(sb.toString());
		String c = m1.replaceAll("");
	
		c = c.replace("#line#", "\r\n");
		return c;
	}

	public static String getUrlPath(String absUrl, String url) {
		url = url.trim();
		if (url.length() > 7
				&& !url.substring(0, 7).equalsIgnoreCase("http://")) {
			String rootPath;
			String currentPath;
			int idx = absUrl.indexOf('/', 7);
			rootPath = absUrl.substring(0, idx + 1);
			idx = absUrl.lastIndexOf('/');
			currentPath = absUrl.substring(0, idx + 1);
			if (url.startsWith("/")) {
				url = rootPath + url.substring(1);
			} else {
				url = currentPath + url;
			}
		}
		return url;
	}

	public static HashMap<String,ArrayList<String>> anayVideoPath(String url) throws UnsupportedEncodingException {
		HashMap<String,ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
		ArrayList<String> paths = new ArrayList<String>();
		url = URLEncoder.encode(url, "UTF-8");
		String queryURL = "http://www.flvcd.com/parse.php?flag=&format=&kw="+url;
		String input = getHttpContent(queryURL, "GBK");
		if(input == null) {
			return null;
		}
//		System.out.print(input);
		StringBuffer regx = new StringBuffer();
//		regx.append("<td align=left class=\"mn STYLE4\">");

//		<a href="" target="_blank" class="link"onclick='_alert();return false;'></a>
//		<a href="" target="_blank" class="link"onclick='_alert();return false;'></a>

//		regx.append("<a href=\"([^\"]+)\" target=\"_blank\" class=\"link\"onclick='_alert\\(\\);return false;'>.*?</a>");

//		regx.append("<a href=\"([^\"]+)\" target=\"_blank\".*?onclick=[\"|']_alert\\(\\);return false;[\"|']>.*?</a>");
		
		regx.append("<U>(.*?)\\s+<X>");
		
//		String title ="<input type=\"hidden\" name=\"name\" value=\"([^\"]+)\">";
		String mp3title ="<N>(.*?)\\s+<P>";
		
		Pattern p = Pattern.compile(regx.toString());
		Matcher m = p.matcher(input);
		while(m.find()) {
//			System.out.println(m.group(1));
			paths.add(m.group(1));
		}
		String t = "";
		Pattern pt = Pattern.compile(mp3title);
		Matcher mt = pt.matcher(input);
		while (mt.find()) {
			t = mt.group(1);
		}
		data.put(t, paths);
		return data;
	 
	}
	 
	public static void main(String []args) throws UnsupportedEncodingException   {
		NetBox net = new NetBox();
		long begin = System.currentTimeMillis();//鍙栧紑濮嬫椂闂�鍗曚綅鏄绉� 
		
		/*long st=System.currentTimeMillis();
		String input = net.getHttpContent("http://www.kooso.cc/SearchPlayFile.aspx?%D7%ED%BA%F3%D2%BB%D2%B9", "GBK");
	    System.out.println(input);
	    long end = System.currentTimeMillis();
	    System.out.println(end-st);*/
		
//		http%3A%2F%2Fwww.yinyuetai.com%2Fvideo%2F498470
//		String str = URLEncoder.encode("http://www.yinyuetai.com/video/497563", "UTF-8");
//		System.out.println(str);
		String url = "http://www.1ting.com/player/34/player_307188.html";
		  String content;
		  int i=0;
		  
		
		  content= getHttpContent("http://192.168.1.3:8080/index/download.action?token=myadmin&inputPath=/mnt/android_Book/Chapter/137715894455610002.txt","GBK");
		  System.out.print(content);

//		while(true)
//		{
//			i++;
//		   content=getHttpContent("http://api2.v.pptv.com/api/page/episodes.js?page=3&channel_id=10020188&cb=JUI.Loader._2012_VIDEO_LIST","UTF-8");
//		   if(content!=null){
//			   break;
//		   }else{
//			   try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		   }
//		}
		//System.out.print(content);
//		Iterator iter = vps.entrySet().iterator();
//		while(iter.hasNext()) {
//			Map.Entry<String, ArrayList<String>> enc = (Map.Entry<String, ArrayList<String>>)iter.next();
//			ArrayList<String> vp = enc.getValue();
//			System.out.println(enc.getKey());
//			for(String str : vp) {
//				System.out.println(str);
//			}
//		}
		
//		net.saveHttpContent("http://player.youku.com/player.php/sid/XNDAyNTY5MzY0/v.swf", "d:\\video\\abc.flv");
		  long end = System.currentTimeMillis();//鍙栫粨鏉熸椂闂�
	}
}
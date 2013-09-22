package com.ccibs.ccvideoplayer.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class LoginUtil {
	  private String macAddress;
	  private Context mContext;
	  private SharedPreferences sharedPreferences;
	  private AQuery aQuery;
	  private String path;
	  private String driveid;
	  private boolean isLogin;
	  private String operatorName;  //运营商
	  private TelephonyManager tm;
	private String version;
	  
	 public LoginUtil(Context mContext) {
		 this.mContext = mContext;
	  }
     public boolean CheckUerInfo(){
    	 isLogin  =false;
    	 sharedPreferences =mContext. getSharedPreferences("userInfo", mContext.MODE_WORLD_READABLE);
    	 String userInfo =  sharedPreferences.getString("userInfo", "");
       if(userInfo.length()!=0){
     	String[] users = userInfo.split(",");
         for (String str : users)
         {
           String name = users[0];
           String password= users[1];
            //path =HttpRequest.URL_QUERY_LOGIN+"username="+name+"&"+"password="+password+"&type=2"+"&driveid="+driveid+"&mac="+macAddress;
           
         }
         aQuery.ajax(path, String.class, new AjaxCallback<String>() {

				@Override
				public void callback(String url, String object,
						AjaxStatus status) {
					
					if(object!=null){
						String ss ="\"fasle\"";
					if(!ss.equals(object)){
						isLogin=true;
					}else{
						isLogin=false;
					}
				}
				else {
					Toast.makeText(aQuery.getContext(), "网络错误，请重试", 1).show();
				}
				}
				 
			});
     }
       return isLogin;
     }
     //获取mac值
     public  String getWifiMacAddress(Context context) {
     	final WifiManager wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
     	if(wifi==null) return"";
     	WifiInfo info=wifi.getConnectionInfo();
     	this.macAddress=info.getMacAddress();
     	if(this.macAddress==null && !wifi.isWifiEnabled()) {
     	 new Thread() {
     	@Override
     	public void run() {
     	wifi.setWifiEnabled(true);
     	for(int i=0;i<10;i++) {
     	WifiInfo _info=wifi.getConnectionInfo();
     	 macAddress=_info.getMacAddress();
     	if(macAddress!=null) break;
     	try {
     		Thread.sleep(500);
     	} catch (InterruptedException e) {
     		// TODO Auto-generated catch block
     		e.printStackTrace();
     	}
     	}
     	wifi.setWifiEnabled(false);
     	}
     	 }.start();
     	}
     	return macAddress;
     }

     //获取序列号
     public String getSerialNumber() {
    	 String serialnum = null;                                                                                                                                        
    	 try {                                                           
    	  Class<?> c = Class.forName("android.os.SystemProperties"); 
    	  Method get = c.getMethod("get", String.class, String.class );     
    	  serialnum = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );   
    	 }                                                                                
    	 catch (Exception ignored)                                                        
    	 {                              
    	}
    	 return serialnum;
     }
     
/*     //APP版本信息
     public String getPackageVersion(Context context) {
    	 String versionName = "";
    	 PackageManager pm = context.getPackageManager();
    	 try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			 versionName = packageInfo.versionName;
			if (versionName == null|| versionName.length() <=0) {
				return "";
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
    	 return versionName;
    	
 }*/
     //获取系统服务
     public static String getSystemVersion() {
    	 String systemVersion = null;
    	 systemVersion = Build.VERSION.RELEASE;
    	 return systemVersion;
     }
     
     /**
      * 获取设备信息
      * @return
      */
     public String getDeviceInfo() {
     	return Build.MODEL;
     }
 
}

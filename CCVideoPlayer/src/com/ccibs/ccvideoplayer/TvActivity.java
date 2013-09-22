package com.ccibs.ccvideoplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.adapter.EpisodeGridAdapter;
import com.ccibs.ccvideoplayer.adapter.TvSourceGridAdapter;
import com.ccibs.ccvideoplayer.bean.PlayPathBean;
import com.ccibs.ccvideoplayer.bean.TVBean;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.JSONUtil;

public class TvActivity extends Activity {
        
	
	private GridView tv_grid_list;
	private LinearLayout tv_lay_list;
	private int episodeGridCount =1;
	private EpisodeGridAdapter episodeGridAdapter;
	private ArrayList<String> myArrayList;
	int[] defaultImage ={R.drawable.leshilogo,R.drawable.qiyi,R.drawable.xunlei,R.drawable.tengxun,R.drawable.mdianyingwang
			,	R.drawable.shiguangwang,R.drawable.xinlang,R.drawable.souhu,R.drawable.ppvt,R.drawable.youku,R.drawable.tudou};
	private RelativeLayout tv_layout_tvsource;
	private GridView tv_grid_source_list;
	private AQuery aQuery;
	private String path="http://192.168.1.3:8080/tvplaywork!getTvPlayWork.action?token=myadmin&resultType=json&id=137663821320130622";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	
		
		
		
	}
	
	 private void setTVDetail(){
		   setContentView(R.layout.tvdetails);
			tv_grid_list = (GridView) findViewById(R.id.tv_grid_list);
			tv_layout_tvsource = (RelativeLayout) findViewById(R.id.tv_layout_tvsource);
			aQuery = new AQuery(getApplicationContext());
			tv_lay_list = (LinearLayout) findViewById(R.id.tv_lay_list);
			aQuery.ajax(path, String.class, new AjaxCallback<String>(){
				private ArrayList<String> myNextList;
				@Override
				public void callback(String url, String object, AjaxStatus status) {
					if(object!=null){
						System.out.println("下载的数据为"+object);
						TVBean tv = JSONUtil.getTV(object);
						myArrayList = new ArrayList<String>();
						String num = tv.getNum();
						for (int i = 1; i <=Integer.parseInt(num); i++) {
							myArrayList.add(i+"");
						}
						if(myArrayList.size()>30){
							myNextList = new ArrayList<String>();
							for (int i = 0; i < 30; i++) {
								myNextList.add(myArrayList.get(i));
							}
							episodeGridAdapter = new EpisodeGridAdapter(TvActivity.this, myNextList);
						}else{
							myNextList =myArrayList;
							episodeGridAdapter = new EpisodeGridAdapter(TvActivity.this, myNextList);
						}
						int count = myArrayList.size()/30;
						System.out.println("myArrayList.size()%count"+myArrayList.size()%count);
						if(myArrayList.size()%30!=0){
							count =count+1;
						}
						System.out.println("count的大小为"+count);
						final Button[] imageviewList = new Button[count];
						for (int i = 0; i < imageviewList.length; i++) {
							final int t =i;
							imageviewList[i]=new Button(TvActivity.this);
							imageviewList[i].setId(2000 * i);
							imageviewList[i].setText((30*i+1)+"~"+(30*(i+1)<(myArrayList.size())?30*(i+1):myArrayList.size()));
							imageviewList[i].setTextSize(20);
							imageviewList[i].setGravity(Gravity.CENTER);
							imageviewList[i]
									.setBackgroundResource(R.drawable.imageview_item_selector);
							if (i >= 0) {
								LinearLayout.LayoutParams layp = new LinearLayout.LayoutParams(
										120, 60);
								layp.setMargins(0, 10, 0, 10);
								imageviewList[i].setLayoutParams(layp);
							}
							tv_lay_list.addView(imageviewList[i]);
							imageviewList[i].setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
								 myNextList = new ArrayList<String>();
									
									for ( int j = (30*t+1); j <= (30*(t+1)>myArrayList.size()?myArrayList.size():30*(t+1)); j++) {
										myNextList.add(j+"");
									}
									episodeGridAdapter.setArrayList(myNextList);
									episodeGridAdapter.notifyDataSetChanged();
									
								}
							});
						}
						tv_grid_list.setAdapter(episodeGridAdapter);
					tv_grid_list.setSelector(new ColorDrawable(0));
//						tv_grid_list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
					tv_grid_list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							System.out.println("position"+position);
							String tag = myNextList.get(position);
							int tvcount =Integer.parseInt(tag);
							String path = HttpRequest.getInstance().getURL_TVEpisode_Play()+tvcount;
							setPlay(path);
							
						}
						
					});
					}
					
				}
			});
	 }
	 private void setPlay(String path) {
			aQuery.ajax(path, String.class, new AjaxCallback<String>(){
				@Override
				public void callback(String url, String object,
						AjaxStatus status) {
					if(object!=null){
						ArrayList<PlayPathBean> PlayAllList = JSONUtil.getTvPlayPath(object);
//						tv_layout_tvsource.setVisibility(View.VISIBLE);
//					
//						tv_grid_list.clearFocus();
//						tv_layout_tvsource.requestFocus();
//						tv_layout_tvsource.setFocusable(true);
						setDialog(PlayAllList);
					}
					super.callback(url, object, status);
				}
				});
		}
	 
	 private void setDialog(final ArrayList<PlayPathBean> tvsourceList){
		 LayoutInflater	inflater =LayoutInflater.from(TvActivity.this);
		View orderPageView = inflater.inflate(R.layout.tv_source_main, null);
		tv_grid_source_list = (GridView) orderPageView.findViewById(R.id.tv_grid_source_list);
		tv_grid_source_list.setSelector(new ColorDrawable(0));
		Dialog builder=new Dialog(TvActivity.this);
		  builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			builder.setContentView(orderPageView);
			Window dialogWindow = builder.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.dimAmount=0.1f;
			lp.alpha=0.8f;
//			dialogWindow.setGravity(Gravity.CENTER);
			tv_grid_source_list.setAdapter(new TvSourceGridAdapter(aQuery.getContext(), tvsourceList));
			int size =tvsourceList.size()/5;
			if(size%5!=0||size==0){
				size=size+1;
			}
			lp.width = 860;
			lp.height = 130*size;
			lp.x =180;
			lp.y=80;
			dialogWindow.setAttributes(lp);
			builder.show();
			tv_grid_source_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
//					PlayPathBean playPathBean = tvsourceList.get(position);
//					if(!playPathBean.getPlayurl().equals("")){
//						String path = playPathBean.getPlayurl();
//						Intent i=new Intent(Intent.ACTION_VIEW);
//						Uri uri=Uri.parse(path);
//						System.out.println("播放的地址为"+path);
//						i.setDataAndType(uri, "video/*");
//						startActivity(i);
//					}else{
//						String webUrl = playPathBean.getPlaypath();
//						Uri uri = Uri.parse(webUrl);
//						Intent it = new Intent(Intent.ACTION_VIEW,uri);
//						startActivity(it);
//					}
//					
				}
			});
			
	 }
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		 if(keyCode==KeyEvent.KEYCODE_BACK){
//		 if(tv_layout_tvsource.getVisibility()==View.VISIBLE){
//			 tv_layout_tvsource.setVisibility(View.GONE);
//			 return true;
//		 }
//		 }
		return super.onKeyDown(keyCode, event);
	}
}

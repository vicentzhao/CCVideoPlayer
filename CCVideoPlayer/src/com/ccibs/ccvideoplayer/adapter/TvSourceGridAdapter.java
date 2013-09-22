package com.ccibs.ccvideoplayer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.R;
import com.ccibs.ccvideoplayer.bean.PlayPathBean;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.ImageAsyncLoader;
import com.ccibs.ccvideoplayer.util.ImageFileCache;
import com.ccibs.ccvideoplayer.util.JSONUtil;

public class TvSourceGridAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<PlayPathBean> array;
	private int selcted = -1;
	private AQuery aQuery;
	private View view;
	private ImageView preTv;
	public TvSourceGridAdapter(Context paramContext,
			ArrayList<PlayPathBean> paramArray) {
		this.context = paramContext;
		if (paramArray != null) {
			this.array = paramArray;
			aQuery = new AQuery(paramContext);
			return;
		}
		this.array = new ArrayList<PlayPathBean>();
	}

	public int getCount() {
		return this.array.size();
	}

	public Object getItem(int position) {
		return this.array.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View paramView, ViewGroup paramViewGroup) {
		
		paramView = LayoutInflater.from(this.context).inflate(
				R.layout.tv_grid_source_item, null);
		ImageView  localTextView = (ImageView) paramView
				.findViewById(R.id.tv_item_source);
		if(selcted>-1){
			localTextView.setSelected(true);
			if(preTv!=null){			
				preTv.setSelected(false);
			}
			preTv=localTextView;
		}
		if ("乐视".equals(array.get(position).getUnitsname())) {
			 getWebLogo("乐视",0,localTextView);
			localTextView.setTag("乐视");
		}
		if ("爱奇艺".equals(array.get(position).getUnitsname())) {
			getWebLogo("爱奇艺",1,localTextView);
			localTextView.setTag("爱奇艺");
		}
		if ("迅雷".equals(array.get(position).getUnitsname())) {
			getWebLogo("迅雷",2,localTextView);
			localTextView.setTag("迅雷");
		}
		if ("腾讯".equals(array.get(position).getUnitsname())) {
			getWebLogo("腾讯",3,localTextView);
			localTextView.setTag("腾讯");
		}
		if ("M1905电影网".equals(array.get(position).getUnitsname())) {
			getWebLogo("M1905电影网",4,localTextView);
			localTextView.setTag("M1905电影网");
		}
		if ("时光网".equals(array.get(position).getUnitsname()) ){
			getWebLogo("时光网",5,localTextView);
			localTextView.setTag("时光网");
		}
		if ("新浪".equals(array.get(position).getUnitsname())) {
			getWebLogo("新浪",6,localTextView);
			localTextView.setTag("新浪");
		}
		if ("搜狐".equals(array.get(position).getUnitsname())) {
			getWebLogo("搜狐",7,localTextView);
			localTextView.setTag("搜狐");
		}
		if ("ppTV聚力".equals(array.get(position).getUnitsname())) {
			getWebLogo("ppTV聚力",8,localTextView);
			localTextView.setTag("ppTV聚力");
		}
		if ("优酷".equals(array.get(position).getUnitsname())) {
			getWebLogo("优酷",9,localTextView);
			localTextView.setTag("优酷");
		}
		if ("土豆".equals(array.get(position).getUnitsname())) {
			getWebLogo("土豆",10,localTextView);
			localTextView.setTag("土豆");
		}
		
//		localTextView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				int count = Integer.parseInt(array.get(position));
//				String path = HttpRequest.getInstance().getURL_TVEpisode_Play()+count;
//				setPlay(path,count);
//			}
//
//			
//		});
		return paramView;
	}

	public void setSelctItem(int paramInt) {
		this.selcted = paramInt;
		notifyDataSetChanged();
	}
	
	 public void setArrayList(ArrayList<PlayPathBean> list){
		 this.array =list;
	 }
	 private void getWebLogo(String name,int defalutLogo,ImageView iv) {
			SharedPreferences sp = context.getSharedPreferences("image",Context.MODE_WORLD_READABLE);
			int[] defaultImage ={R.drawable.leshilogo,R.drawable.qiyi,R.drawable.xunlei,R.drawable.tengxun,R.drawable.mdianyingwang
					,	R.drawable.shiguangwang,R.drawable.xinlang,R.drawable.souhu,R.drawable.ppvt,R.drawable.youku,R.drawable.tudou};
			String url = sp.getString(name, "");
			if(url==null||"".equals(url)){
//				if(defalutLogo<defaultImage.length){
				iv.setImageResource(defaultImage[defalutLogo]);
//				}else{
//				iv.setImageResource(R.drawable.grid_item_default);
//				}
			}else{
				Bitmap image = ImageFileCache.getCashInstance().getImage(url);
				if(image!=null){
					iv.setImageBitmap(image);
				}else{
					ImageAsyncLoader imLoader = new ImageAsyncLoader();
					Bitmap bitmap = imLoader.getImage(url);
					ImageFileCache.getCashInstance().saveBmpToSd(bitmap, url);
					iv.setImageBitmap(image);
				}
			}
			
		}
	
}

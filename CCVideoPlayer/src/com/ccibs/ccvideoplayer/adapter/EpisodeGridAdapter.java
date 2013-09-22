package com.ccibs.ccvideoplayer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.R;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.JSONUtil;

public class EpisodeGridAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> array;
	private int selcted = -1;
	private AQuery aQuery;
	private View view;
	private TextView preTv;
	public EpisodeGridAdapter(Context paramContext,
			ArrayList<String> paramArray) {
		this.context = paramContext;
		if (paramArray != null) {
			this.array = paramArray;
			aQuery = new AQuery(paramContext);
			return;
		}
		this.array = new ArrayList<String>();
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
				R.layout.grid_item, null);
		TextView  localTextView = (TextView) paramView
				.findViewById(R.id.tv_item_esp);
		if(selcted>-1){
			localTextView.setSelected(true);
			if(preTv!=null){			
				preTv.setSelected(false);
			}
			preTv=localTextView;
		}
		
		localTextView.setText(array.get(position));
		localTextView.setTag(array.get(position));
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
	
	 public void setArrayList(ArrayList<String> list){
		 this.array =list;
	 }
	 
	
}

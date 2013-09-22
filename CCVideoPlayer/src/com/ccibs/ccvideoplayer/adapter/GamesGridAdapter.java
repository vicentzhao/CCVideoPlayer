package com.ccibs.ccvideoplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ccibs.ccvideoplayer.R;
import com.ccibs.ccvideoplayer.bean.GameBean;
import com.ccibs.ccvideoplayer.util.GridItemCache;
import com.ccibs.ccvideoplayer.util.ImageAsyncLoader;

public class GamesGridAdapter extends BaseAdapter {

	private Context context;
	private ImageAsyncLoader loader;
	private List<GameBean> list;
	private AQuery aQuery;
	private int selcted = -1;

	public GamesGridAdapter(Context context, List<GameBean> gameBeanList
			) {
		this.context = context;
		setPeopleList(gameBeanList);
		loader = new ImageAsyncLoader();
		aQuery = new AQuery(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public GameBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridItemCache gridItemCache;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item_game, null);
			gridItemCache = new GridItemCache(convertView);
			convertView.setTag(gridItemCache);
		} else {
			gridItemCache = (GridItemCache) convertView.getTag();
		}
		GameBean gameBean = getItem(position);
		String imageURL = gameBean.getPic();
		TextView textView = gridItemCache.getTextView();
		textView.setText(gameBean.getName());
		ImageView imageView = gridItemCache.getImageView();
		convertView.findViewById(R.id.item_img).setBackgroundResource(
				R.drawable.grid_item_default);
		aQuery.id(convertView.findViewById(R.id.item_img)).image(imageURL);
		return convertView;
	}

	public void changData(ArrayList<GameBean> newList) {
		setPeopleList(newList);
		notifyDataSetChanged();
	}
	public void setPeopleList(List<GameBean> newList) {
		if (newList != null) {
			this.list = newList;
			return;
		}
		this.list = new ArrayList<GameBean>();
	}
	
	public void setSelctItem(int paramInt) {
		this.selcted = paramInt;
		 notifyDataSetChanged();
	}
}

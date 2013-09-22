package com.ccibs.ccvideoplayer.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccibs.ccvideoplayer.R;

public class ViewHolder {
	private ImageView imageView;
	private TextView textView;
	private View baseView;

	public ViewHolder(View baseView) {
		this.baseView = baseView;
	}

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.item_img);
		}
		return imageView;
	}

	public TextView getTextView() {
		if (textView == null) {
			textView = (TextView) baseView.findViewById(R.id.item_name);
		}
		return textView;
	}
}

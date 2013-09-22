package com.ccibs.ccvideoplayer.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccibs.ccvideoplayer.R;

public class GridItemCache {
	private View baseView;
	private ImageView imageView;
	private TextView textView;

	public GridItemCache(View baseView) {
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

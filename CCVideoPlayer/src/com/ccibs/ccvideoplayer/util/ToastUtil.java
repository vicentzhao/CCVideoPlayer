package com.ccibs.ccvideoplayer.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ccibs.ccvideoplayer.R;

public class ToastUtil {

	private static Toast toast;

	public static void showToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}


	public static void showToastbyId(Context context, int id) {
		if (toast == null) {
			toast = Toast.makeText(context, context.getResources()
					.getString(id), Toast.LENGTH_SHORT);
		} else {
			toast.setText(context.getResources().getString(id));
		}
		toast.show();
	}
}

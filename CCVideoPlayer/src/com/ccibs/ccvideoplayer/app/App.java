package com.ccibs.ccvideoplayer.app;
import java.util.HashMap;

import android.app.Application;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.ccibs.ccvideoplayer.R;

public class App extends Application {
	private static SoundPool soundPool;
	private static boolean loaded;
	private static HashMap<String, Integer> soundMap;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initSound();
	}
	private void initSound() {
		soundMap = new HashMap<String, Integer>();
		/**
		 * maxStream —�? 同时播放的流的最大数�? streamType —�? 流的类型，一般为STREAM_MUSIC
		 * srcQuality —�? 采样率转化质量，当前无效果，使用0作为默认�?
		 */
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundMap.put(getResources().getString(R.string.sound_movie_select),
				soundPool.load(App.this, R.raw.move_select, 1));
		soundMap.put(getResources().getString(R.string.sound_page_change),
				soundPool.load(App.this, R.raw.page_change, 1));
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				App.loaded = true;
			}
		});
	}
	public static void playSound(String paramString) {
		if (loaded) {
			// 其中leftVolume和rightVolume表示左右音量，priority表示优先�?loop表示循环次数,rate表示速率
			soundPool.play(((Integer) soundMap.get(paramString)).intValue(), 2,
					2, 0, 0, 1.0f);
		}
	}

}

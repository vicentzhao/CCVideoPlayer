/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ccibs.ccvideoplayer.play;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ccibs.ccvideoplayer.R;

public class MediaPlayerDemo_Video extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private Bundle extras;
	private static final String MEDIA = "media";
	private static final int LOCAL_AUDIO = 1;
	private static final int STREAM_AUDIO = 2;
	private static final int RESOURCES_AUDIO = 3;
	private static final int LOCAL_VIDEO = 4;
	private static final int STREAM_VIDEO = 5;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	
	private String path1="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030001070052412DC02C5300422C39EBED4FE4-2CCC-71BB-6AC3-59B4332828D7?K=7dcc204ff21af5da2828f274";
	private String path2="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030001070152412DC02C5300422C39EBED4FE4-2CCC-71BB-6AC3-59B4332828D7?K=60ab6764c3ea37402828f274";
	private String path3="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030001070252412DC02C5300422C39EBED4FE4-2CCC-71BB-6AC3-59B4332828D7?K=e9dcd0a096b6accf261d3f78";
	private String path4="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030001070352412DC02C5300422C39EBED4FE4-2CCC-71BB-6AC3-59B4332828D7?K=5a5af8b19ce6024624118c7b";
	private String path5="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030001070452412DC02C5300422C39EBED4FE4-2CCC-71BB-6AC3-59B4332828D7?K=eb9a585e30c6be092828f274";
	String myPath[] ={path1,path2,path3,path4};

	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.play_activity_layout);
		mPreview = (SurfaceView) findViewById(R.id.playSurfaceView);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBX_8888);
//		holder.setFormat(PixelFormat.TRANSLUCENT);
//		medi.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
		extras = getIntent().getExtras();

	}

	private void playVideo(Integer Media) {
		doCleanUp();
		try {

			String path ="http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/03000201005245A93B5F1F056AC6C7A02BA7AE-5EAA-845B-F512-9058BC0903D5?K=f422696d742f960c2411914b";
			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer(this);
			mMediaPlayer.setDataSource(path);
//			mMediaPlayer.setDataSegments(myPath,getCacheDir().toString());
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.getMetadata();
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		Log.d(TAG, "onBufferingUpdate percent:" + percent);

	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		playVideo(extras.getInt(MEDIA));

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
	}
}

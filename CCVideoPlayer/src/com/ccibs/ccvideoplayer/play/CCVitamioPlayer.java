package com.ccibs.ccvideoplayer.play;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ccibs.ccvideoplayer.R;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.pathparse.Letv;

public class CCVitamioPlayer extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

	
	/**
	 * 播放的状态
	 * ==============================================================
	 */
	private int mCurrentState = STATE_PLAYING;
	public static final int STATE_ERROR = -1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_PREPARING = 1;
	public static final int STATE_PREPARED = 2;
	public static final int STATE_PLAYING = 3;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_PLAYBACK_COMPLETED = 5;
	public static final int STATE_SUSPEND = 6;
	
	
	//==================================================================
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
	private AQuery aQuery;
	
	private ImageView iv_playcontrol_pause; //暂停按钮
	private RelativeLayout relative_playcotrol_playsetting;  //控制界面
	private RelativeLayout source_layout;  //资源头界面
	private RelativeLayout definition_layout;  //清晰度头界面
	private RelativeLayout episode_layout;  //选集头界面
	private RelativeLayout relEpisode;  //选集界面
	private RelativeLayout relativeLayout_playcontroll_voicebar;  //声音界面
	private RelativeLayout rl_playcontrol_mainseekbar;  //播放进度界面
	private RelativeLayout  re_player_loading;
	

	private ListView definitionList; // 清晰度列表
	private ListView sourceList; // 视频来源地址
	
	private TextView tvEpisode; //电视剧级数
	
	private String[] adapterDefinitionData;  //清晰度数据
	private String[] adaptersourceData;  //来源数据
	private TextView  tv_playcontrol_currenttimer;  //当前时间
	private TextView  tv_playcontrol_totaltime;  //总时间
	private ImageView playMaskImageView; //播放背景
	private TextView  tv_processbar ;  //缓冲度
	private Button  btn_tv_seekbartimers;     //seekbar上面的timers
	
	
	
	private int currentVocie ;
	
	private boolean isSeekTimersInit ;   //是否是seekbar上的time第一次加载
	
	private int currentProgress ;   // 当前进度
	
	int  seekProgress;
	
	private int count =0; // 当前级数
	
	
	private SeekBar  videoSeekBar;  //播放进度条
	private com.ccibs.ccvideoplayer.view.SeekBar  voiceBar;  //声音进度条
	
//	private MATDialog mDialog;  //  显示缓冲对话
	private int focusViewId;   //焦点所在的位置   seekbar
	private int mDuration = -1;// 当前播放位置
	private int _progress; 
	
	
	private boolean isSeekTO =true;  //是否跳转
	private boolean isSeekTOTEST =true;  //是否跳转测试
	private boolean isVocieBarVisable=false;  //判断voicebar是否显示
	public static final int MSG_WHAT_UPDATE_PROGRESS = 1;
	public static final int MSG_WHAT_SHOW_NETWORK_SPEED = 2;
	public static final int MSG_WHAT_SHOW_BUFFER_INFO = 3;
	public static final int MSG_WHAT_HIDE_CONTROLLER = 4;
	public static final int MSG_WHAT_UPDATE_CONTROLLER_INFO = 5;
	public static final int MSG_WHAT_AUTO_EXIT = 6;
	public static final int MSG_WHAT__UPDATEVOICEBAR = 7;

	/**
	 * 
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.play_activity_layout);
		aQuery =new AQuery(CCVitamioPlayer.this);
		mPreview = (SurfaceView) findViewById(R.id.playSurfaceView);
		initView();
		Intent i =getIntent();
		String episode=i.getStringExtra("Episode"); //当前级数
		String type =i.getStringExtra("type"); //频道类型
		String num = i.getStringExtra("num");//电视剧总级数
		String url =i.getStringExtra("url"); //当前播放视频的web地址
		System.out.println("传过来的url地址为"+url);
//		initVideoSeekBarTimers();
//		url="http://v.youku.com/v_show/id_XMjQ0ODk0NTAw.html";
//		url="http://www.letv.com/ptv/pplay/91112/1.html";
		url="http://v.youku.com/v_show/id_XMjQ0ODk0NTAw.html";
		String videoPath =i.getStringExtra("videopath"); //当前播放video 真实地址，如果有直接播放
		HttpRequest.getInstance().setVideoPath(videoPath);
		HttpRequest.getInstance().setUrl(url);
		HttpRequest.getInstance().setVideoPath(videoPath);
//		String path = HttpRequest.getInstance().getURL_TVEpisode_Play();
		adapterDefinitionData =new String[]{"高清","标清","流畅"};
		adaptersourceData =new String[]{"优酷","土豆","乐视"};
		definitionList.setAdapter(new ArrayAdapter<String>(aQuery.getContext(),  android.R.layout.simple_list_item_1, adapterDefinitionData));
		sourceList.setAdapter(new ArrayAdapter<String>(aQuery.getContext(), android.R.layout.simple_list_item_1, adaptersourceData));
		tvEpisode.setText(count+"");
		source_layout.requestFocus();
		source_layout.setFocusable(true);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		initDialog();
		holder.setFormat(PixelFormat.RGBX_8888);
//		holder.setFormat(PixelFormat.TRANSLUCENT);
//		medi.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
		extras = getIntent().getExtras();
		source_layout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					 relEpisode.setVisibility(View.GONE);
					 definitionList.setVisibility(View.GONE);
					 sourceList.setVisibility(View.VISIBLE); 
				}
			}
		});
		episode_layout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					relEpisode.setVisibility(View.VISIBLE);
					definitionList.setVisibility(View.GONE);
					sourceList.setVisibility(View.GONE); 
				}
			}
		});
		episode_layout.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==KeyEvent.ACTION_DOWN){
				if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
					count--;
					tvEpisode.setText(count+"");
				}
				if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
					count++;
					tvEpisode.setText(count+"");
				}
				}
				return false;
			}
		});
		definition_layout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					relEpisode.setVisibility(View.GONE);
					definitionList.setVisibility(View.VISIBLE);
					sourceList.setVisibility(View.GONE); 
				}
			}
		});
		videoSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.i(TAG, "controller.seekbar onStop");
				if(isInPlaybackState()){
					re_player_loading.setVisibility(View.VISIBLE);
					mMediaPlayer.pause();
					doSeek(currentProgress);
					
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.i(TAG, "controller.seekbar onStart");
			
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				if (fromUser&&isInPlaybackState()) {
					float y = seekBar.getY();
//					int xPos = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) /
//							seekBar.getMax()) + seekBar.getLeft()-33;
					int xPos =seekBar.getLeft()+
							((seekBar.getRight()-seekBar.getLeft())*progress/100);
					 float totaltimes  = mMediaPlayer.getDuration()*progress/100;
					 float f = totaltimes / 1000;
					    int i = (int)f / 3600;
					    int j = (int)f / 60 - i * 60;
					    int k = (int)f % 60;
					    if (i == 0){
					    	btn_tv_seekbartimers.setText(j + ":" + k);
					    }else{
					    	btn_tv_seekbartimers.setText(i + ":" + j + ":" + k);
					    }
					 if(progress<5){
							xPos= xPos - 40; 
					 }else if(progress<10){
						 xPos= xPos - 43;
					 }
					 else if(progress<15){
						 xPos= xPos - 46;
					 }
					 else if(progress<20){
						 xPos= xPos - 49;
					 }
					 else if(progress<25){
						 xPos= xPos - 52;
					 }
					 else if(progress<30){
						 xPos= xPos - 54;
					 }
					 else if(progress<35){
						 xPos= xPos -56;
					 }else if(progress<40){
						 xPos= xPos - 58;
					 }
					 else if(progress<45){
						 xPos= xPos - 60;
					 }
					 else if(progress<50){
						 xPos= xPos -62;
					 }
					 else if(progress<55){
						 xPos= xPos - 64;
					 }
					 else if(progress<60){
						 xPos= xPos - 66;
					 }
					 else if(progress<65){
						 xPos= xPos - 68;
					 }
					 else if(progress<70){
						 xPos= xPos - 70;
					 }
					 else if(progress<75){
						 xPos= xPos - 72;
					 }
					 else if(progress<80){
						 xPos= xPos - 74;
					 }
					 else if(progress<85){
						 xPos= xPos - 76;
					 }
					 else if(progress<90){
						 xPos= xPos - 78;
					 }else{
				    	xPos=xPos - 80;
				    }
					 System.out.println("x==="+xPos+"y======"+y);
					setCurrentTimeLayout(xPos,progress);
					focusViewId = R.id.controller_seekbar;
					Log.i(TAG, "controller.seekBar  onChanged-->"+ progress);
					System.out.println("fromProgress======"+progress);
					currentProgress = progress;
					isSeekTO =false;
					isSeekTOTEST=false;
					_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
//					syncTimeBar((mMediaPlayer.getDuration()* currentProgress) / 100, (mMediaPlayer.getDuration()));
				}
			}
		});
		
	
		
		videoSeekBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int i, KeyEvent keyevent) {
				if (!isInPlaybackState())
					return false;
				focusViewId = R.id.controller_seekbar;
				if (keyevent.getAction() == KeyEvent.ACTION_UP) {
					if (keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
							|| keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
						System.out.println("currentProgress====>>>>"+currentProgress);
						checkSeeking(currentProgress);
						
					}else if(keyevent.getKeyCode()==KeyEvent.KEYCODE_BACK){
//						hide();
					}
				}
				return false;
			}
		});
		initVolume();
		
	      voiceBar.setOnSeekBarChangeListener(new com.ccibs.ccvideoplayer.view.SeekBar.OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(
					com.ccibs.ccvideoplayer.view.SeekBar VerticalSeekBar) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void onStartTrackingTouch(
					com.ccibs.ccvideoplayer.view.SeekBar VerticalSeekBar) {
				// TODO Auto-generated method stub
				
			}
			
			
			public void onProgressChanged(
					com.ccibs.ccvideoplayer.view.SeekBar VerticalSeekBar, int progress,
					boolean fromUser) {
				
				 if(fromUser){
						changeVolume(progress);
				 }
				
			}
		});
	      
	      TimerTask tt = new TimerTask() {
				 @Override
				 public void run() {
				 Message ms = new Message();
				 ms.what =MSG_WHAT__UPDATEVOICEBAR;
				 _handler.sendMessage(ms);
				 }
				 };
				 Timer timer = new Timer();
				 timer.schedule(tt,2000,4000);
	
	}
	
	/**
	 *  设置seekbar
	 */
	
    private void initVideoSeekBarTimers(){
//    	LayoutParams layoutParams = videoSeekBar.getLayoutParams();
    	float x = videoSeekBar.getX();
    	float y = videoSeekBar.getY();
        final Button tv = new Button(aQuery.getContext());
        final LinearLayout.LayoutParams rpMini = new LinearLayout.LayoutParams(
                100, 50);
//                rpMini.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                tv.setLayoutParams(rpMini);
                tv.setPadding(0, 0, 0, 20);
                tv.setTextSize(24);
        tv.setText("1:23:04");
        tv.setBackgroundResource(R.drawable.player_dialog_timers);
        tv.setX(x);
        tv.setY(y-100);
        System.out.println("x==="+x+"y======"+y);
        tv.setLayoutParams(rpMini);
        rl_playcontrol_mainseekbar.addView(tv);
    	
    	
    }
	 
	 /**
	  * 设置seekbar上面的时间图标
	  * @param layoutParams
	  */
	
	private void setCurrentTimeLayout(float x ,float y) {
        btn_tv_seekbartimers.setTextSize(24);
//        btn_tv_seekbartimers.setText("1:23:04");
        btn_tv_seekbartimers.setBackgroundResource(R.drawable.player_dialog_timers);
        btn_tv_seekbartimers.setX(x);
//        btn_tv_seekbartimers.setY(y);   
        isSeekTimersInit = false;
	}
	
	public void syncTimeBar(long l, long m) {
		if(m==0)return;
		videoSeekBar.setProgress((100 * (int)l)/ (int)m);
//		time.setText(StringUtils.generateTime(mPosition) + "/" + StringUtils.generateTime(mDuration));
		//需要设定seekbar两边的时间
	}
	
	public void checkSeeking(int currentProgress) {
		// TODO Auto-generated method stub
		seekProgress = currentProgress;
		if (isSeeking) {
			_handler.removeCallbacks(seekRunnable);
			isSeeking = false;
		} else {
			_handler.postDelayed(seekRunnable, 2000);
		}
	}

	Handler _handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO:handleMsg
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_WHAT_UPDATE_PROGRESS:
//				mDialog.setMessage(getResources().getString(R.string.loading));
//				mDialog.setProgress(_progress);
				return;
			case MSG_WHAT_HIDE_CONTROLLER:
//				mMediaController.hide();
				break;
			case MSG_WHAT_SHOW_NETWORK_SPEED:
				
				return;
			case MSG_WHAT_SHOW_BUFFER_INFO:
				
				return;
			case MSG_WHAT_UPDATE_CONTROLLER_INFO:
//				updateControllerInfo();
//				mMediaController.updateInfo();
				break;
			case MSG_WHAT_AUTO_EXIT:
				
				doAutoExit(countdown--);
				break;
			case MSG_WHAT__UPDATEVOICEBAR:
				int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				if(streamVolume==currentVocie){
					relativeLayout_playcontroll_voicebar.setVisibility(View.GONE);
				}else{
					currentVocie=streamVolume;
				}
//				doAutoExit(s);
				break;
			default:
				break;
			}
		}
	};
//	private MATDialog initDialog() {
//		mDialog = new MATDialog(aQuery.getContext(),R.style.dialog);
//		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//		mDialog.showMessage("正在加载");
//		return mDialog;
//	}
	private void initDialog() {
		re_player_loading = (RelativeLayout) findViewById(R.id.re_player_loading);
		tv_processbar = (TextView) findViewById(R.id.tv_processbar);
		ImageView logo = (ImageView)findViewById(R.id.iv_loading);
		logo.setAnimation(AnimationUtils.loadAnimation(aQuery.getContext(), R.anim.rotate_dialog));
	}
	public boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}
	
	public void doSeek(int currentProgress) {
		// TODO Auto-generated method stub
		this.currentProgress = currentProgress;
		seekProgress=currentProgress;
		_handler.postDelayed(seekRunnable, 1000);
	}
	private boolean isSeeking = false;
	private Runnable seekRunnable = new Runnable() {

		@Override
		public void run() {
			if (isInPlaybackState()){
				isSeeking = true;
				int newposition = mDuration * seekProgress/100;
				seekTo(newposition);
			}
		}
	};
	
	public void seekTo(int pos) {
		if (isInPlaybackState()) {
//			if (!mDialog.isShowing()) {
//				mDialog.show();
//			}
			mMediaPlayer.seekTo(pos);
			isSeekTOTEST=true;
			isSeeking = false;
			startPlayProgressUpdater();
		}
	}
	 /**
	  * 	
					// Create a new media player and set the listeners
	  * @param paths
	  */
	private void initMediaPlayer(String[] paths){
		mMediaPlayer = new MediaPlayer(CCVitamioPlayer.this);
		mMediaPlayer.setDataSegments(paths, getCacheDir().toString());
//		mMediaPlayer.setDataSource(path);
//		mMediaPlayer.setDataSegments(myPath,getCacheDir().toString());
		mMediaPlayer.setDisplay(holder);
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.setOnBufferingUpdateListener(CCVitamioPlayer.this);
		mMediaPlayer.setOnCompletionListener(CCVitamioPlayer.this);
		mMediaPlayer.setOnPreparedListener(CCVitamioPlayer.this);
		mMediaPlayer.setOnVideoSizeChangedListener(CCVitamioPlayer.this);
		mMediaPlayer.setOnInfoListener(mInfoListener);
		mMediaPlayer.getMetadata();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	
	}
	private void playVideo() {
		doCleanUp();
		 if(""==HttpRequest.getInstance().getVideoPath()||null==HttpRequest.getInstance().getVideoPath()){
		   new  AsyncTask<Void, Void, String[]>(){
			   
			@Override
			protected void onPreExecute() {
				re_player_loading.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}
			@Override
			protected void onPostExecute(String[] result) {
//				mDialog.dismiss();
				
				if(null!=result&&!"".equals(result)&&result.length!=0){
					initMediaPlayer(result);
					}else{
						
						Uri uri = Uri.parse(HttpRequest.getInstance().getUrl());
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
						finish();
					}
				super.onPostExecute(result);
			}
			@Override
			protected String[] doInBackground(Void... params) {
				String url ="";
//				if("22".equals(HttpRequest.getInstance().getType())){
//				String path =HttpRequest.getInstance().getURL_TVEpisode_Play();
//				System.out.println("要解析的地址为===========》"+path);
//				String responseString = HttpUtil.getResponseString(path);
//				ArrayList<PlayPathBean> tvPlayPath = JSONUtil.getTvPlayPath(responseString);
//				 url= tvPlayPath.get(0).getHtmlPath();
//				 HttpRequest.getInstance().setUrl(url);
//				}else{
			        url =HttpRequest.getInstance().getUrl();
//				}
				System.out.println("要解析的地址为===========》"+url);
				String[] playPaths = getPlayPaths(url,"1");
				System.out.println("要解析的地址为===========》"+playPaths);
				return playPaths;
//				return new String[]{"http://220.196.51.20/31/37/" +
//						"53/letv-uts/4866649-AVC-254704-AAC-31586-1912000-71410804-b30f730e89055f3dc12ec26b64c29b0f-1371879685487.letv?crypt=90aa7f2e72&b=298&nlh=3072&nlt=5&bf=19&gn=738&p2p=1&video_type=flv&opck=1&check=0&tm=1383123600&key=0811524c2b20c24c628fdc0aa649e493&proxy=3703845661,2071812433&cips=112.65.228.90&geo=CN-9-123-2&lgn=letv&mmsid=2750" +
//						"163&platid=1&splatid=101&playid=0&tss=no&host=www_letv_com"};
			}
		   }.execute();
		 }else{
			 String[] myPaths= new String[]{HttpRequest.getInstance().getVideoPath()};
			 initMediaPlayer(myPaths);
		 }
//		String[] myPaths =new String[]{"http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070050DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=9ff3a5b4ffb9be3024119494",
//"http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070150DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=b8510b28a5a784662829055b",
// "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070250DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=d5c0dd902d8903e62829055b",
// "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070350DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=3875d9ade1228be92829055b",
// "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070450DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=829bb1124b457d6c261d4cf7",
// "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070550DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=f5705df0ce65691024119494",
// "http://f.youku.com/player/getFlvPath/sid/00_00/st/flv/fileid/030002070650DB98133F14041F3EEB184C5A4C-314A-B89C-6A1C-B2288D7226FB?K=3c7c81d05d089f172829055b"
//		};
//		initMediaPlayer(myPaths);
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//		Log.d(TAG, "" +
//				" percent:" + percent);
		playMaskImageView.setVisibility(View.VISIBLE);
		final int mCurrentBufferPercentage = percent;
		tv_processbar.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tv_processbar.setText(mCurrentBufferPercentage+"%");
			}
		});
//		if(percent==100||arg0.isPlaying()){
//			if(re_player_loading.getVisibility()==View.VISIBLE){
//				re_player_loading.setVisibility(View.GONE);
//			playMaskImageView.setVisibility(View.GONE);
//			if(isSeekTOTEST){
//			isSeekTO=true;
//			}
//			}
//		}
		

	}
	
	private OnInfoListener mInfoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_START");
				re_player_loading.setVisibility(View.VISIBLE);
				
			} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_END");
				re_player_loading.setVisibility(View.GONE);
				tv_processbar.setText(0+"%");
			}
			return true;
		}
	};

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
		finish();
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
		playVideo();

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
		startPlayProgressUpdater();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

//		if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
//			  if(!mDialog.isShowing()){
//			 if(iv_playcontrol_pause.getVisibility()==View.VISIBLE){
//				 iv_playcontrol_pause.setVisibility(View.GONE);
//				 relative_playcotrol_playsetting.setVisibility(View.GONE);
//				 mMediaPlayer.start();  
//			 }else{
//				 iv_playcontrol_pause.setVisibility(View.VISIBLE);
//				 relative_playcotrol_playsetting.setVisibility(View.VISIBLE);
//				 if(mMediaPlayer.isPlaying()){
//				 mMediaPlayer.pause();
//				 }
//				 
//			 }
//			  }
//		}
		if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN||keyCode==KeyEvent.KEYCODE_DPAD_UP){
			if(relative_playcotrol_playsetting.getVisibility()!=View.VISIBLE){
			if(relativeLayout_playcontroll_voicebar.getVisibility()!=View.VISIBLE){
				relativeLayout_playcontroll_voicebar.setVisibility(View.VISIBLE);
				rl_playcontrol_mainseekbar.setVisibility(View.GONE);
				voiceBar.requestFocus();
				voiceBar.setFocusable(true);
			}
			}
		}
		if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT||keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
			if(relative_playcotrol_playsetting.getVisibility()!=View.VISIBLE){
			if(rl_playcontrol_mainseekbar.getVisibility()!=View.VISIBLE){
				relativeLayout_playcontroll_voicebar.setVisibility(View.GONE);
				rl_playcontrol_mainseekbar.setVisibility(View.VISIBLE);
				videoSeekBar.requestFocus();
				videoSeekBar.setFocusable(true);
			}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
	   
        
	   public void startPlayProgressUpdater() {
		   if(null!=mMediaPlayer&&mMediaPlayer.isPlaying())
		   {
	    	float progress = (((float)mMediaPlayer.getCurrentPosition()/1000)/(mMediaPlayer.getDuration()/1000));
		   
	    	if(isSeekTO){
	    	videoSeekBar.setProgress((int)(progress*100));
	    	}
	    	mDuration=(int) mMediaPlayer.getDuration();
	    	int pos=(int) mMediaPlayer.getCurrentPosition();
	    	int alltime =(int) mMediaPlayer.getDuration();
	    	int allMin =(alltime/1000)/60;
	    	int allsec =(alltime/1000)%60;
	    	int min = (pos/1000)/60;
			int sec = (pos/1000)%60;
			tv_playcontrol_currenttimer.setText(min+":"+sec);
			tv_playcontrol_totaltime.setText(allMin+":"+allsec);
			if (mMediaPlayer.isPlaying()) {
				Runnable notification = new Runnable() {
			        public void run() {
			        	startPlayProgressUpdater();
					}
			    };
			    handler.postDelayed(notification,1000);
	    	}
	    }    
	   }
	   
	   
	   private AudioManager mAudioManager;
	    public void changeVolume(int voice) {
			// TODO Auto-generated method stub
			if (mAudioManager != null) {
				voiceBar.setProgress(voice);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice,0);
				
				
//				if (isMute) {
//					isMute = false;
//					sound.setBackgroundResource(R.drawable.player_sound_selector);
//					mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
//				}
			}
		}
		private void initVolume() {
			mAudioManager = (AudioManager) CCVitamioPlayer.this.getSystemService(Context.AUDIO_SERVICE);
			int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			currentVocie =mVolume;
			voiceBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			voiceBar.setProgress(mVolume);
//			if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
//				isMute = true;
//			} else {
//				isMute = false;
//			}
		}
		
		
		private String[] getPlayPaths(String url,String type){
			Letv letvParse=new Letv();
			String[] paths = null ;
			 ArrayList<String> videoUrls = letvParse.getVideoUrl(url, type);
			if(null!=videoUrls&&videoUrls.size()!=0){
				paths= new String[videoUrls.size()];
			  for (int i = 0; i < videoUrls.size(); i++) {
				  paths[i] =videoUrls.get(i);
			}
			}
			return paths;
		}
	  private void initView(){
			relative_playcotrol_playsetting=(RelativeLayout)findViewById(R.id.play_setting);
			source_layout=(RelativeLayout) findViewById(R.id.play_setting).findViewById(R.id.source_layout);
			episode_layout=(RelativeLayout) findViewById(R.id.play_setting).findViewById(R.id.episode_layout);
			relativeLayout_playcontroll_voicebar=(RelativeLayout)findViewById(R.id.RelativeLayout_playcontroll_voicebar);
			rl_playcontrol_mainseekbar=(RelativeLayout)findViewById(R.id.rl_playcontrol_mainseekbar);
			definition_layout=(RelativeLayout) findViewById(R.id.play_setting).findViewById(R.id.definition_layout);
			relEpisode=(RelativeLayout) findViewById(R.id.play_setting).findViewById(R.id.relEpisode);
			tvEpisode=(TextView) findViewById(R.id.play_setting).findViewById(R.id.tvEpisode);
			definitionList=(ListView) findViewById(R.id.play_setting).findViewById(R.id.definitionList);
			sourceList=(ListView) findViewById(R.id.play_setting).findViewById(R.id.sourceList);
			videoSeekBar =(SeekBar) findViewById(R.id.controller_seekbar);
			voiceBar=(com.ccibs.ccvideoplayer.view.SeekBar) findViewById(R.id.controller_voicebar);
			tv_playcontrol_totaltime=(TextView) findViewById(R.id.tv_playcontrol_totaltime);
			tv_playcontrol_currenttimer=(TextView) findViewById(R.id.tv_playcontrol_currenttimer);
			iv_playcontrol_pause=(ImageView) findViewById(R.id.iv_playcontrol_pause);
			playMaskImageView=(ImageView) findViewById(R.id.playMask).findViewById(R.id.playMaskImageView);
			btn_tv_seekbartimers = (Button) findViewById(R.id.btn_tv_seekbartimers);
		  
	  }
	  
		private Dialog errordialog;
		OnErrorListener mErrorListener = new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
				Log.d(TAG, "onError:" + framework_err + "," + impl_err);
				
//				if (mDialog != null)
//					mDialog.hide();
				if(re_player_loading.getVisibility()==View.VISIBLE)re_player_loading.setVisibility(View.GONE);
				mCurrentState = STATE_ERROR;
				if (mPreview.getWindowToken() != null) {
					int message = framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ? R.string.VideoView_error_text_invalid_progressive_playback
							: R.string.VideoView_error_text_unknown;

					errordialog = new AlertDialog.Builder(CCVitamioPlayer.this)
							.setTitle(R.string.VideoView_error_title)
							.setMessage(getResources().getString(message)
									  + getResources().getString(R.string.countdown))
							.setPositiveButton(R.string.VideoView_error_button,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int whichButton) {
											_handler.removeMessages(MSG_WHAT_AUTO_EXIT);
											finish();
											
										}
									}).setCancelable(false).create();
					errordialog.show();
					doAutoExit(5);
				}
				return true;
			}
		};
		int countdown =5;
		private void doAutoExit(int s) {
			System.out.println("s:"+s);
			if (s <= 1) {
				if (errordialog != null && errordialog.isShowing()
						&& errordialog.getWindow() != null) {
					errordialog.dismiss();
				}
				
				return;
			}
			_handler.sendMessageDelayed(_handler.obtainMessage(MSG_WHAT_AUTO_EXIT, --s), 1000);
		}
	  
}

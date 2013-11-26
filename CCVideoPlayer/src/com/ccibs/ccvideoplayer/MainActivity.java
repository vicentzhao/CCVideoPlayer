package com.ccibs.ccvideoplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.adapter.BookGridAdapter;
import com.ccibs.ccvideoplayer.adapter.EduGridAdapter;
import com.ccibs.ccvideoplayer.adapter.EpisodeGridAdapter;
import com.ccibs.ccvideoplayer.adapter.GamesGridAdapter;
import com.ccibs.ccvideoplayer.adapter.TvSourceGridAdapter;
import com.ccibs.ccvideoplayer.adapter.TypeDetailsSubMenuAdapter;
import com.ccibs.ccvideoplayer.app.App;
import com.ccibs.ccvideoplayer.bean.BookBean;
import com.ccibs.ccvideoplayer.bean.BookPageBean;
import com.ccibs.ccvideoplayer.bean.EduBean;
import com.ccibs.ccvideoplayer.bean.EduPlayBean;
import com.ccibs.ccvideoplayer.bean.GameBean;
import com.ccibs.ccvideoplayer.bean.MovieBean;
import com.ccibs.ccvideoplayer.bean.PlayPathBean;
import com.ccibs.ccvideoplayer.bean.TVBean;
import com.ccibs.ccvideoplayer.bean.VideoPlayInfo;
import com.ccibs.ccvideoplayer.constant.Constant;
import com.ccibs.ccvideoplayer.play.CCVitamioPlayer;
import com.ccibs.ccvideoplayer.util.AppUtil;
import com.ccibs.ccvideoplayer.util.CCdriveTextView;
import com.ccibs.ccvideoplayer.util.CryptUtil;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.HttpUtil;
import com.ccibs.ccvideoplayer.util.ImageAsyncLoader;
import com.ccibs.ccvideoplayer.util.ImageAsyncLoader.ImageCallback;
import com.ccibs.ccvideoplayer.util.ImageFileCache;
import com.ccibs.ccvideoplayer.util.JSONUtil;
import com.ccibs.ccvideoplayer.util.LoginUtil;
import com.ccibs.ccvideoplayer.util.SoapUtils;
import com.ccibs.ccvideoplayer.util.ToastUtil;
import com.ccibs.ccvideoplayer.util.UpdateApk;
import com.ccibs.ccvideoplayer.util.UpdateVersion;

/**
 * ���������棬���ݲ�ͬ�Ĳ������жϼ��ز�ͬ��
 * 
 * @author CCDrive.ZhaoYiqun
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * ========================================================================
	 * ==== movie ��һЩ����
	 */

	private int[] pic = { R.drawable.logo, R.drawable.logo, R.drawable.logo,
			R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo,
			R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo };
	private LinearLayout lay_list;
	private Button btn_leftarrow, btn_rightarrow;
	private HorizontalScrollView horizontalScrollView;
	private View oldView;
	private AQuery aQuery;
	private TextView btn_movie_info_area;
	private TextView btn_movie_info_type;
	private TextView btn_movie_info_playingtimer;
	private TextView btn_movie_info_director;
	private TextView btn_movie_info_actor;
	private TextView btn_movie_info_releasetime;
	private TextView btn_movie_info_title;
	private TextView btn_movie_info_introduction;
	private TextView btn_moive_lanselect;
	private TextView btn_movie_info_tv_format;
	private TextView btn_movie_info_tv_resolutionratio;
	private TextView btn_movie_info_tv_price_nomaruser;
	private TextView btn_movie_info_tv_price_vipuser;
	private TextView btn_movie_info_tv_price_monthuser;
	private Button view_movie_paly;
	private Button view_movie_collect;
	private ImageView btn_movie_info_logo;
	private View view_movie_payfor_item01;
	private View view_movie_payfor_item02;
	private View view_movie_payfor_item03;
	private View view_movie_payfor_item04;
	private View view_movie_payfor_item05;
	private View view_movie_payfor_item_main;

	private View textViewOldView = null;
	private View textViewforSelectOldView = null;
	private int[] view_movie_payforID = { R.id.view_movie_payfor_item01,
			R.id.view_movie_payfor_item02, R.id.view_movie_payfor_item03,
			R.id.view_movie_payfor_item04, R.id.view_movie_payfor_item05 };
	private int movieCount = 0; // �жϲ����Ĳ���ʽ
	private int typeCount = -1; // �жϲ����ĸ�ƬԴ
	private MovieBean movie;
	// private static int NETWORK_CONNECT_TIMEOUT = 500000;
	// private static int NETWORK_SO_TIMEOUT = 500000;
	private final static int beginConnectData = 500000;
	private int count = 1;
	// private String
	// testPath="http://192.168.1.3:8080/html/movie/137465088697000001.json";
	private String testPath = "http://192.168.1.3:2014/html/movie_137465088697000001.json";

	private RelativeLayout layoutview;
	private RelativeLayout re_movie_loading;
	private RelativeLayout dialog_test_re;
	private LinearLayout line_movie_detail;
	private long currentTimeMillis;
	private boolean isTiming = true;
	private View clickViewOldView;

	private Handler handler;

	private TextView re_movie_title;
	private ImageAsyncLoader loader;
	private boolean isFristClick = true;
	private boolean isDrawable = true;
	private boolean isVideoPlay = false; // �жϲ��ŵ�ַ�Ƿ����
	private ListView listView;
	private ArrayList<String> selectList;
	private String mac;
	/**
	 * ========================================================================
	 * ==================
	 */
	// edu��һЩȫ�ֱ���
	private TextView btn_edu_info_type;
	private TextView btn_edu_info_director;
	private TextView btn_edu_info_title;
	private TextView btn_edu_info_introduction;
	private GridView edu_grid_list;

	private ImageButton view_edu_collect;
	private String eduDetailPath = "http://apk.vocy.com/educationshop!information.action?token=myadmin&resultType=json&wid=137645341860600002";
	private String eduPlayList = "http://apk.vocy.com/educationshop!source.action?token=myadmin&resultType=json&wid=137645341860600002";
	
	private EduGridAdapter eduGridAdapter;
	private ArrayList<EduPlayBean> playPathList;
	private LinearLayout edu_lay_list;

	/**
	 * ========================================================================
	 * ================== book��һЩȫ�ֱ���
	 */
	// book��һЩȫ�ֱ���
	// private String eduDetailPath =
	// "http://apk.vocy.com/educationshop!information.action?token=myadmin&resultType=json&wid=137645341860600002";
	// private String eduPlayList =
	// "http://apk.vocy.com/educationshop!source.action?token=myadmin&resultType=json&wid=137645341860600002";
	private TextView btn_book_info_type;
	private TextView btn_book_info_director;
	private TextView btn_book_info_title;
	private TextView btn_book_info_introduction;
	private TextView btn_book_info_number;
	private TextView btn_book_info_status;
	private LinearLayout book_lay_list;
	private GridView book_grid_list;
	private ImageButton view_book_collect;

	private ArrayList<EduPlayBean> myBookNextList;
	private BookGridAdapter bookGridAdapter;
	private ArrayList<EduPlayBean> bookPlayPathList;

	/**
	 * ====================================================================
	 * ���Ӿ��һЩ����
	 */

	private RelativeLayout tv_layout_tvsource;
	private GridView tv_grid_source_list;
	private TextView btn_tv_info_title;
	private TextView btn_tv_info_introduction;
	private TextView btn_tv_info_area;
	private TextView btn_tv_info_type;
	private TextView btn_tv_info_playingtimer;
	private TextView btn_tv_info_director;
	private TextView btn_tv_info_actor;
	private ImageButton view_tv_collect;
	private ImageView btn_tv_info_logo;
	private GridView tv_grid_list;
	private LinearLayout tv_lay_list;
	int[] defaultImage = { R.drawable.leshilogo, R.drawable.qiyi,
			R.drawable.xunlei, R.drawable.tengxun, R.drawable.mdianyingwang,
			R.drawable.shiguangwang, R.drawable.xinlang, R.drawable.souhu,
			R.drawable.ppvt, R.drawable.youku, R.drawable.tudou };

	private int episodeGridCount = 1;
	private EpisodeGridAdapter episodeGridAdapter;
	private ArrayList<String> myArrayList;
	private BookBean bean;
	private View episodeCountOldView ;// ���ӵľ缯������ʾ��view
	/**
	 * ====================================================================
	 * ��Ϸ�Ĳ���
	 */
	private static final int MSG_LOAD_MOVIE = 1;
	private static final int MSG_LOAD_ERR = 4;
	/* ================================================ */
	private GridView game_gridview, edu_gridview;
	private Button btn_game_pageup;
	private Button btn_game_pagedown;
	private Button view_game_collect;

	private TextView tv_currentpage;
	private TextView tv_totalrows;
	// private RelativeLayout layoutview;
	// private LinearLayout line_movie_detail;
	// private RelativeLayout re_movie_loading;
	private GridView allgridview;
	/* ================================================ */
	private GamesGridAdapter gamesGridAdapter;
	private HttpRequest httpRequest;
	private GameBean gameBean;
	private Runnable changDatas = new Runnable() {
		@Override
		public void run() {
			MainActivity.this.handler.sendEmptyMessage(MSG_LOAD_MOVIE);
		}
	};
	/* ================================================ */
	private int currentPage = 1;
	private int totalPage;
	private int totalRows;
	private boolean pageFlag = true;
	private String content;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		aQuery = new AQuery(getApplicationContext());
		checkVersion();
		Intent i = getIntent();   
		String type = i.getStringExtra("type");
		System.out.println("�յ���typeΪ" + type);
		String id = i.getStringExtra("id");
		System.out.println("�յ���idΪ" + id);
//		url ="http://www.letv.com/ptv/pplay/48053.html";
		String toDayflag = i.getStringExtra("toDayflag");
		System.out.println("��������url��ַΪ"+toDayflag);
		String url =i.getStringExtra("url"); //��ǰ������Ƶ��web��ַ
		System.out.println("��������url��ַΪ"+url);
		HttpRequest.getInstance().setUrl(url);
		String mytoken = i.getStringExtra("mytoken");
		HttpRequest.getInstance().setType(type);
		String kind = getIntent().getStringExtra("kind");
		String web_root = getIntent().getStringExtra("webRoot");
		System.out.println("============="+web_root);
		if(null!=web_root){
		if(web_root.contains("192")){
			HttpRequest.getInstance().setSTATIC_WEB_ROOT("http://192.168.1.3:2014/");
		}else{
			HttpRequest.getInstance().setSTATIC_WEB_ROOT("http://html.pctoo.cn/");
		}
		}
		HttpRequest.getInstance().setWEB_ROOT(web_root);
		System.out.println("�յĵ�kind" + kind);
		HttpRequest.getInstance().setCommicKind(kind);
		HttpRequest.getInstance().setMytoken(mytoken);
		HttpRequest.getInstance().setId(id);
		httpRequest = HttpRequest.getInstance();
		if ("1".equals(type)) {
//			testPath = "http://html.vocy.com/html/moviesource/movie_" + id
//					+ ".html";
			testPath =HttpRequest.getInstance().getURL_TVEpisode_Play();
//			testPath ="http://html.vocy.com/html/workplay/workplay_1_137465088697000001_0.txt";
			HttpRequest.getInstance().setId("137465088697000001");
			setMovieDetail(mytoken, id);
			System.out.println("testPath�ĵ�ַΪ====="+testPath);
		}
		if ("26".equals(type) || "12".equals(type)||"20".equals(type) ||"25".equals(type) || "16".equals(type) || "19".equals(type)
				|| "4".equals(type) || "8".equals(type)|| "15".equals(type)|| "18".equals(type)) {
			setEduDetail(mytoken, id);
		}
		if ("22".equals(type)||"23".equals(type) || "7".equals(type)) {
			setTVDetail(mytoken, id);
		}
		if ("2".equals(type)) {
			setBookDetail(mytoken, id);
		}
		if ("6".equals(type) ||"9".equals(type) || "10".equals(type) || "24".equals(type)
				|| "5".equals(type) || "14".equals(type) || "13".equals(type)
				|| "21".equals(type) || "11".equals(type)|| "0".equals(type)||null!=toDayflag) {
			initGamesData(id,url);
		}
//		Intent intent = new Intent(MainActivity.this,CCVitamioPlayer.class);
//		startActivity(intent);
	}

	private void setTVDetail(String mytoken, String id) {
		System.out.println("mytoken===id===" + mytoken + "===" + id);
		setContentView(R.layout.tvdetails);
		tv_grid_list = (GridView) findViewById(R.id.tv_grid_list);
		tv_layout_tvsource = (RelativeLayout) findViewById(R.id.tv_layout_tvsource);
		btn_tv_info_introduction = (TextView) findViewById(R.id.btn_tv_info_introduction);
		aQuery = new AQuery(getApplicationContext());
		tv_lay_list = (LinearLayout) findViewById(R.id.tv_lay_list);
		btn_tv_info_title = (TextView) findViewById(R.id.btn_tv_info_title);
		btn_tv_info_area = (TextView) findViewById(R.id.btn_tv_info_area);
		btn_tv_info_type = (TextView) findViewById(R.id.btn_tv_info_type);
		btn_tv_info_director = (TextView) findViewById(R.id.btn_tv_info_director);
		btn_tv_info_playingtimer = (TextView) findViewById(R.id.btn_tv_info_playingtimer);
		btn_tv_info_actor = (TextView) findViewById(R.id.btn_tv_info_actor);
		btn_tv_info_logo = (ImageView) findViewById(R.id.btn_tv_info_logo);
		view_tv_collect = (ImageButton) findViewById(R.id.view_tv_collect);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		view_tv_collect.setVisibility(View.INVISIBLE);
		final String path = HttpRequest.getInstance().getURL_TVDetail();
		System.out.println("ͼƬ���ص���Ƶ=====����" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			private ArrayList<String> myNextList;

			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("���ص�����Ϊ" + object);
					TVBean tv = JSONUtil.getTV(object);
					btn_tv_info_title.setText(tv.getName());
					btn_tv_info_introduction.setText("��ӰƬ��顿�� "
							+ tv.getSynopsis());
					btn_tv_info_type.setText(tv.getType());
					btn_tv_info_area.setText(tv.getArea());
					btn_tv_info_playingtimer.setText(tv.getNum());
					btn_tv_info_director.setText(tv.getDirector());
					btn_tv_info_actor.setText(tv.getProtagonist());
					System.out.println("ͼƬ�ĵ�ַΪ" + tv.getPic());
					ImageAsyncLoader imageLoader = new ImageAsyncLoader();
					imageLoader.loadDrawable(tv.getPic(), new ImageCallback() {
						@Override
						public void imgeLoader(Bitmap draw, String imgeURL) {
							btn_tv_info_logo.setImageBitmap(draw);
						}
					});
					myArrayList = new ArrayList<String>();
					String num = tv.getNum();
					for (int i = 1; i <= Integer.parseInt(num); i++) {
						myArrayList.add(i + "");
					}
					if (myArrayList.size() > 30) {
						myNextList = new ArrayList<String>();
						for (int i = 0; i < 30; i++) {
							myNextList.add(myArrayList.get(i));
						}
						episodeGridAdapter = new EpisodeGridAdapter(
								MainActivity.this, myNextList);
					} else {
						myNextList = myArrayList;
						episodeGridAdapter = new EpisodeGridAdapter(
								MainActivity.this, myNextList);
					}
					int count = myArrayList.size() / 30;
					if (myArrayList.size() % 30 != 0 || count == 0) {
						count = count + 1;
					}
					final Button[] imageviewList = new Button[count];
					for (int i = 0; i < imageviewList.length; i++) {
						final int t = i;
						imageviewList[i] = new Button(MainActivity.this);
						imageviewList[i].setId(2000 * i);
						imageviewList[i].setText((30 * i + 1)
								+ "~"
								+ (30 * (i + 1) < (myArrayList.size()) ? 30 * (i + 1)
										: myArrayList.size()));
						imageviewList[i].setTextSize(20);
						imageviewList[i].setGravity(Gravity.CENTER);
						imageviewList[i]
								.setBackgroundResource(R.drawable.imageview_item_selector);
						if (i >= 0) {
							LinearLayout.LayoutParams layp = new LinearLayout.LayoutParams(
									120, 50);
							imageviewList[i].setLayoutParams(layp);
						}
						tv_lay_list.addView(imageviewList[i]);
						imageviewList[i]
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										
										if(episodeCountOldView!=null){
											episodeCountOldView.setSelected(false);
										}
										 v.setSelected(true);
										 episodeCountOldView = v;
										 
										myNextList = new ArrayList<String>();

										for (int j = (30 * t + 1); j <= (30 * (t + 1) > myArrayList
												.size() ? myArrayList.size()
												: 30 * (t + 1)); j++) {
											myNextList.add(j + "");
										}
										episodeGridAdapter
												.setArrayList(myNextList);
										episodeGridAdapter
												.notifyDataSetChanged();
									}
								});
					}
					tv_grid_list.setAdapter(episodeGridAdapter);
					tv_grid_list.setSelector(new ColorDrawable(0));
					// tv_grid_list.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
					tv_grid_list
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, final int position, long id) {
									System.out.println("���Ѿ���ִ���ˣ�лл==============��");
									String tag = myNextList.get(position);
									int tvcount = Integer.parseInt(tag);
									HttpRequest.getInstance().setCount(tvcount);
									System.out.println(" httpRequest.getCommicKind()"+ httpRequest.getCommicKind());
									System.out.println(" httpRequest.getType()"+ httpRequest.getType());
									
									if (null != httpRequest.getCommicKind()
											&& !"".equals(httpRequest
													.getCommicKind())) {
										if (("����").equals(HttpRequest
												.getInstance().getCommicKind())
												|| httpRequest.getCommicKind()
														.equals("��Ƶ")) {
											String path = HttpRequest
													.getInstance()
													.getURL_TVEpisode_Play();
											System.out.println("���ŵĵ�ַΪ" + path);
											setPlay(path);
										} else if (HttpRequest.getInstance()
												.getCommicKind().equals("����")
												|| httpRequest.getCommicKind()
														.equals("ͼƬ")) {
											String url_TVEpisode_Play = HttpRequest
													.getInstance()
													.getURL_TVEpisode_Play();
											System.out.println("�������صĵ�ַΪ"
													+ url_TVEpisode_Play);
											aQuery.ajax(url_TVEpisode_Play,
													String.class,
													new AjaxCallback<String>() {

														@Override
														public void callback(
																String url,
																String object,
																AjaxStatus status) {
															if (object != null) {
																ArrayList<PlayPathBean> tvPlayPathList = JSONUtil
																		.getTvPlayPath(object);
																System.out
																		.println("����������tv======��"
																				+ tvPlayPathList
																						.size());
																ArrayList<String> list = new ArrayList<String>();
																for (int i = 0; i < tvPlayPathList
																		.size(); i++) {
																	if (null != tvPlayPathList
																			.get(i)
																			.getVideoPath()
																			&& !"".equals(tvPlayPathList
																					.get(i)
																					.getVideoPath())) {
																		list.add(tvPlayPathList
																				.get(i)
																				.getVideoPath());
																	}
																}
																for (int j = 0; j < list
																		.size(); j++) {
																	System.out
																			.println("��ͼ�ĵ�ַΪ======="
																					+ j
																					+ "==="
																					+ list.get(j));
																}
																 if(Constant.ISSlientInstall){
																callCCBookPlayer(
																		position,
																		"com.ccdrive.photoviewer",
																		"http://sys.vocy.com/apk_file/137644812677720001.apk",
																		list,null);
																 }else{
																	 callCCBookPlayer(
																				position,
																				"com.ccdrive.photoviewer",
																				"http://api.vocy.com/apk_file/137607316956910001.apk",
																				list,null);
																 }
															}
														}
													});
										}
									} else if (httpRequest.getType().equals(
											"22")) {
										String path = HttpRequest.getInstance()
												.getURL_TVEpisode_Play();
										System.out.println("���Ӿ粥�ŵĵ�ַΪ" + path);
										setPlay(path);
									}
								}
							});
				} else {
					int code = status.getCode();
					System.out.println("����������״̬��" + code);
					Toast.makeText(MainActivity.this, "���ص�����Ϊnull����������", 1)
							.show();
				}

			}
		});

		view_tv_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String educollect = (String) view_tv_collect.getTag();
				if ("�ղ�".equals(educollect)) {
					// �ղ�
					view_tv_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_tv_collect.setTag("���ղ�");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVTV();
					System.out.println("Ҫ�����ϵĵ�ַΪ" + addFavMoviePaht);
					addMovieFav(addFavMoviePaht);
				}
			}
		});
		String collectpath = HttpRequest.getInstance().getURL_TV_ISCOLLECT();
		aQuery.ajax(collectpath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					view_tv_collect.setVisibility(View.VISIBLE);
					if (object.contains("false")) {
						view_tv_collect.setTag("�ղ�");
						view_tv_collect.setVisibility(View.VISIBLE);
						view_tv_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_tv_collect.setTag("���ղ�");
						view_tv_collect.setVisibility(View.VISIBLE);
						view_tv_collect
								.setBackgroundResource(R.drawable.edu_uncollect_selector);
					}
				}
				super.callback(url, object, status);
			}
		});
	}

	/**
	 * set Book detail conetent
	 * 
	 * @param mytoken
	 * @param id
	 */
	private void setBookDetail(String mytoken, String id) {
		setContentView(R.layout.bookdetails);
		// HttpRequest.getInstance().setId("137705335206980622");
		// HttpRequest.getInstance().setMytoken("EB75BE6F-C69D-31CF-8EC8-E8A5C691F0FE");
		HttpRequest.getInstance().setCount(1);
		HttpRequest.getInstance().setPageSize(10);
		btn_book_info_type = (TextView) findViewById(R.id.btn_book_info_type);
		btn_book_info_director = (TextView) findViewById(R.id.btn_book_info_director);
		btn_book_info_title = (TextView) findViewById(R.id.btn_book_info_title);
		btn_book_info_introduction = (TextView) findViewById(R.id.btn_book_info_introduction);
		btn_book_info_number = (TextView) findViewById(R.id.btn_book_info_number);
		btn_book_info_status = (TextView) findViewById(R.id.btn_book_info_status);
		book_lay_list = (LinearLayout) findViewById(R.id.book_lay_list);
		book_grid_list = (GridView) findViewById(R.id.book_grid_list);
		aQuery = new AQuery(MainActivity.this);
		final String path = HttpRequest.getInstance().getURL_BookDetail();
		System.out.println("ͼ�������ϸ���ص�ַ"+path);
//		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
//			@Override
//			public void callback(String url, String object, AjaxStatus status) {
//				if (object != null) {
//					System.out.println("�������ص�ͼ���json��ϢΪ"+object);
//					BookBean bookBean = JSONUtil.getBookBean(object);
//					btn_book_info_type.setText(bookBean.getType());
//					btn_book_info_director.setText(bookBean.getAuthor());
//					btn_book_info_introduction.setText(bookBean.getSynopsis());
//					btn_book_info_number.setText(bookBean.getNum());
//					btn_book_info_status.setText(bookBean.getState());
//					btn_book_info_title.setText(bookBean.getName());
//					aQuery.find(R.id.btn_book_info_logo).image(
//							bookBean.getPic());
//					setBookPage(bookBean);
//				}
//				super.callback(url, object, status);
//			}
//
//		});
		new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				HttpUtil.getResponseString(path);
				return HttpUtil.getResponseString(path);
			}

			@Override
			protected void onPostExecute(String result) {
				if(result!=null){
					System.out.println("�������ص�ͼ���json��ϢΪ"+result);
					BookBean bookBean = JSONUtil.getBookBean(result);
					btn_book_info_type.setText(bookBean.getType());
					btn_book_info_director.setText(bookBean.getAuthor());
					btn_book_info_introduction.setText(bookBean.getSynopsis());
					btn_book_info_number.setText(bookBean.getNum());
					btn_book_info_status.setText(bookBean.getState());
					btn_book_info_title.setText(bookBean.getName());
					aQuery.find(R.id.btn_book_info_logo).image(
							bookBean.getPic());
					setBookPage(bookBean);
//				}
				}
				super.onPostExecute(result);
			}
		}.execute();
		String collectPath = httpRequest.getURL_isCollect_FAVMOVIE();
		System.out.println("�ղ��ж�===========��" + collectPath);
		view_book_collect = (ImageButton) findViewById(R.id.view_book_collect);
		aQuery.ajax(collectPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_book_collect.setTag("�ղ�");
						view_book_collect.setVisibility(View.VISIBLE);
						view_book_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_book_collect.setTag("���ղ�");
						view_book_collect.setVisibility(View.VISIBLE);
						view_book_collect
								.setBackgroundResource(R.drawable.edu_uncollect_selector);
					}
				}
				super.callback(url, object, status);
			}
		});

	}

	/**
	 * ����ͼ���һЩ�½���Ϣ
	 * 
	 * @param bookBean
	 */
	private void setBookPage(final BookBean bookBean) {
		final ArrayList<BookPageBean> bookPageBeanList = bookBean
				.getBookPageBeanList();
		int number = Integer.parseInt(bookBean.getNum());
		bookGridAdapter = new BookGridAdapter(MainActivity.this,
				bookPageBeanList);
		count = number / 10;
		if (number % 10 != 0) {
			count = count + 1;
		}
		final Button[] imageviewList = new Button[count];
		for (int i = 0; i < imageviewList.length; i++) {
			final int t = i;
			imageviewList[i] = new Button(MainActivity.this);
			imageviewList[i].setId(2000 * i);
			imageviewList[i].setText((10 * i + 1) + "~"
					+ (10 * (i + 1) < number ? 10 * (i + 1) : number));
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
			book_lay_list.addView(imageviewList[i]);
			imageviewList[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HttpRequest.getInstance().setCount(t + 1);
					String path = HttpRequest.getInstance().getURL_BookDetail();
					System.out.println("���ص�ͼ��ĵ�ַΪ" + path);
					aQuery.ajax(path, String.class, new AjaxCallback<String>() {
						@Override
						public void callback(String url, String object,
								AjaxStatus status) {
							if (object != null) {
								BookBean myBookBean = JSONUtil
										.getBookBean(object);
								ArrayList<BookPageBean> mybookPageList = myBookBean
										.getBookPageBeanList();
								bookGridAdapter.setArrayList(mybookPageList);
								bookGridAdapter.notifyDataSetChanged();
							}
							super.callback(url, object, status);
						}
					});

				}
			});
		}
		book_grid_list.setAdapter(bookGridAdapter);
		book_grid_list.setSelector(new ColorDrawable(0));
		book_grid_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int myCount = (HttpRequest.getInstance().getCount() - 1) * 10
						+ position + 1;
				if(Constant.ISSlientInstall){
				callCCBookPlayer(position, "com.ccbookreading", "http://sys.vocy.com/apk_file/138355167490000001 .apk", null,null);
			}else{
				callCCBookPlayer(position, "com.ccbookreading", "http://api.vocy.com/apk_file/137987665514410001.apk", null,null);
			}
				}
		});

		view_book_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String educollect = (String) view_book_collect.getTag();
				if ("�ղ�".equals(educollect)) {
					// �ղ�
					view_book_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_book_collect.setTag("���ղ�");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVMOVIE();
					System.out.println("Ҫ�����ϵĵ�ַΪ" + addFavMoviePaht);
					addMovieFav(addFavMoviePaht);
				}
			}
		});
	}

	/**
	 * set edu detail content
	 * 
	 * @param mytoken
	 * @param id
	 */
	private void setEduDetail(String mytoken, String id) {
		setContentView(R.layout.edudetails);
		btn_edu_info_type = (TextView) findViewById(R.id.btn_edu_info_type);
		btn_edu_info_director = (TextView) findViewById(R.id.btn_edu_info_director);
		btn_edu_info_title = (TextView) findViewById(R.id.btn_edu_info_title);
		btn_edu_info_introduction = (TextView) findViewById(R.id.btn_edu_info_introduction);
		edu_lay_list = (LinearLayout) findViewById(R.id.edu_lay_list);
		edu_grid_list = (GridView) findViewById(R.id.edu_grid_list);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		aQuery = new AQuery(MainActivity.this);
		// eduDetailPath = HttpRequest.getInstance().getURL_eduDetailPath();
		httpRequest.setCount(1);
		httpRequest.setPageSize(10);
		eduDetailPath = HttpRequest.getInstance().getURL_BookDetail();
		System.out.println("���صĵ�ַΪ" + eduDetailPath);
		// ��ȡӰƬ����
		aQuery.ajax(eduDetailPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					EduBean eduBean = JSONUtil.getEduBean(object);
					btn_edu_info_director.setText(eduBean.getRose());
					btn_edu_info_type.setText(eduBean.getKind());
					btn_edu_info_title.setText(eduBean.getName());
					btn_edu_info_introduction.setText(("����顿��    ")
							+ eduBean.getNote());
					String num =eduBean.getNum();
					aQuery.find(R.id.btn_edu_info_logo).image(eduBean.getPic());
					setEduPlayInfo(object,num,eduBean);
				}
				super.callback(url, object, status);
			}
		});
		// eduPlayList = HttpRequest.getInstance().getURL_eduPlayList();
		view_edu_collect = (ImageButton) findViewById(R.id.view_edu_collect);
		view_edu_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String educollect = (String) view_edu_collect.getTag();
				if ("�ղ�".equals(educollect)) {
					// �ղ�
					view_edu_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_edu_collect.setTag("���ղ�");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVMOVIE();
					System.out.println("Ҫ�����ϵĵ�ַΪ" + addFavMoviePaht);
					addMovieFav(addFavMoviePaht);
				}
			}
		});
		String path = HttpRequest.getInstance().getURL_isCollect_FAVMOVIE();
		System.out.println("���Ե�idΪ000" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_edu_collect.setTag("�ղ�");
						view_edu_collect.setVisibility(View.VISIBLE);
						view_edu_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_edu_collect.setTag("���ղ�");
						view_edu_collect.setVisibility(View.VISIBLE);
						view_edu_collect
								.setBackgroundResource(R.drawable.edu_uncollect_selector);
					}
				}
				super.callback(url, object, status);
			}
		});

	}

	/**
	 * setEdu play info
	 * 
	 * @param mytoken
	 * @param id
	 */

	private void setEduPlayInfo(String info,String num,final EduBean eduBean) {
		if (info != null) {
			System.out.println("���ص�����Ϊ" + info);
			JSONObject jo;
			JSONArray ja = null;
			try {
				jo = new JSONObject(info);
				ja = jo.getJSONArray("data");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			playPathList = JSONUtil.getEduPlayBeanList(ja);
				
				eduGridAdapter = new EduGridAdapter(MainActivity.this,
						playPathList);
			int count =Integer.parseInt(num) / 10;
			if (Integer.parseInt(num) % 10 != 0) {
				count = count + 1;
			}
			System.out.println("count�Ĵ�СΪ" + count);
			final Button[] imageviewList = new Button[count];
			for (int i = 0; i < imageviewList.length; i++) {
				final int t = i;
				imageviewList[i] = new Button(MainActivity.this);
				imageviewList[i].setId(2000 * i);
				imageviewList[i].setText((10 * i + 1)
						+ "~"
						+ (10 * (i + 1) < (Integer.parseInt(num)) ? 10 * (i + 1)
								: Integer.parseInt(num)));
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
				edu_lay_list.addView(imageviewList[i]);
				imageviewList[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						myEduNextList = new ArrayList<EduPlayBean>();
//
//						for (int j = (10 * t + 1); j <= (10 * (t + 1) > playPathList
//								.size() ? playPathList.size() : 10 * (t + 1)); j++) {
//							myEduNextList.add(playPathList.get(j - 1));
//						}
//						eduGridAdapter.setArrayList(myEduNextList);
//						eduGridAdapter.notifyDataSetChanged();
						
						HttpRequest.getInstance().setCount(t+1);
						String nextPath=HttpRequest.getInstance().getURL_BookDetail();
						aQuery.ajax(nextPath, String.class, new AjaxCallback<String>(){
							@Override
							public void callback(String url, String object,
									AjaxStatus status) {
								JSONObject jo;
								JSONArray ja = null;
								try {
									jo = new JSONObject(object);
									ja= jo.getJSONArray("data");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								playPathList = JSONUtil.getEduPlayBeanList(ja);
								eduGridAdapter.setArrayList(playPathList);
								eduGridAdapter.notifyDataSetChanged();
								super.callback(url, object, status);
							}
						});

					}
				});
			}
			edu_grid_list.setAdapter(eduGridAdapter);
			edu_grid_list.setSelector(new ColorDrawable(0));
			edu_grid_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String playID = playPathList.get(position).getId();
					httpRequest.setSid(playID);
					// String playPaht = HttpRequest.getInstance()
					// .getURL_eduPlaySinglePath()
					// + playID;
					if(HttpRequest.getInstance().getType().equals("19")){
						String downloadpath;
						if(Constant.ISSlientInstall){
							downloadpath="http://sys.pctoo.cn/apk_file/137749629338470001.apk";// APK����·��
						}else{
							downloadpath = "http://api.pctoo.cn/apk_file/137987665514450001.apk";// APK����·��
						}
							callCCBookPlayer(position, "com.ccibs.musicplay", downloadpath, null,eduBean);
						return;
					}
					String playPaht = httpRequest.getURL_EDUEpisode_Play();
//					System.out.println("���ŵ���ϸ��ַΪ" + playPaht);
//					Intent i  = new Intent(MainActivity.this,CCVitamioPlayer.class);
//					startActivity(i);
					aQuery.ajax(playPaht, String.class,
							new AjaxCallback<String>() {
								@Override
								public void callback(String url, String object,
										AjaxStatus status) {
									if (object != null) {
										JSONArray jaArray;
										try {
											jaArray = new JSONArray(object);
											// for (int j = 0; j < jaArray
											// .length(); j++) {
											// EduPlayBean bean = new
											// EduPlayBean();
											// JSONObject playBeanOb = jaArray
											// .getJSONObject(j);
											// String path = playBeanOb
											// .getString("FILEPATH");
											// if (path.contains("http")) {
											// bean.setFilePath(path);
											// } else {
											// path = HttpRequest
											// .getInstance()
											// .getURL_QUERY_SINGLE_IMAGE()
											// + path;
											// bean.setFilePath(path);
											// }
											// eduPlayBeanList
											// .add(bean);
											//
											// }
											ArrayList<EduPlayBean> eduPlayBeanList = JSONUtil
													.getEduPlayBeanList(jaArray);
											String playURL = eduPlayBeanList
													.get(0).getUrl();
											System.out.println("���ŵ�url�ĵ�ַΪ========"+playURL);
											if (null != playURL
													&&!"".equals(playURL)) {
												getVideoPlayPath(playURL, "1");
											} else {
												String playSinglePath = eduPlayBeanList
														.get(0).getFilePath()
														.trim();
//												Intent i = new Intent(
//														Intent.ACTION_VIEW);
//												Uri uri = Uri
//														.parse(playSinglePath);
//												System.out.println("���ŵĵ�ַΪ"
//														+ playSinglePath);
//												i.setDataAndType(uri, "video/*");
												HttpRequest.getInstance().setVideoPath(playSinglePath);
												Intent i = new Intent(MainActivity.this,CCVitamioPlayer.class);
												i.putExtra("videopath", playSinglePath);
												startActivity(i);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										Toast.makeText(aQuery.getContext(),
												"û����Ӧ����Ƶ����ѡ��������Ƶ", 1).show();

									}

								}
							});
				}
			});

		}

	}
	private void setMovieDetail(String mytoken, String id) {
		System.out.println(mytoken + id);
		setContentView(R.layout.moviedetails);
		loader = new ImageAsyncLoader();
		aQuery = new AQuery(MainActivity.this);
		startBefor();
		init();
		currentTimeMillis = System.currentTimeMillis();
		setDataPage();
		
	}
	private void startBefor() {
		layoutview = (RelativeLayout) findViewById(R.id.line_movie_main);
		layoutview.setBackgroundResource(R.drawable.menu_bg);
		line_movie_detail = (LinearLayout) findViewById(R.id.line_movie_detail);
		line_movie_detail.setVisibility(View.INVISIBLE);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		re_movie_loading.setVisibility(View.VISIBLE);
		Timer timer = new Timer();
		LoginUtil lu = new LoginUtil(aQuery.getContext());
		mac = lu.getWifiMacAddress(aQuery.getContext()).toUpperCase();
		// TimerTask tt = new TimerTask() {
		// @Override
		// public void run() {
		// Message ms = new Message();
		// ms.what =beginConnectData;
		// handler.sendMessage(ms);
		// }
		// };
		// timer.schedule(tt,1000);
	}

	private void init() {
		String s1 = "����";
		String s2 = "����";
		String s3 = "����";
		selectList = new ArrayList<String>();
		selectList.add(s3);
		selectList.add(s2);
		selectList.add(s1);
		// checkVersion();
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		lay_list = (LinearLayout) findViewById(R.id.lay_list);
		btn_leftarrow = (Button) findViewById(R.id.btn_leftarrow);
		btn_rightarrow = (Button) findViewById(R.id.btn_rightarrow);
		btn_movie_info_area = (TextView) findViewById(R.id.btn_movie_info_area);
		btn_movie_info_type = (TextView) findViewById(R.id.btn_movie_info_type);
		btn_movie_info_playingtimer = (TextView) findViewById(R.id.btn_movie_info_playingtimer);
		btn_movie_info_director = (TextView) findViewById(R.id.btn_movie_info_director);
		btn_movie_info_actor = (TextView) findViewById(R.id.btn_movie_info_actor);
		btn_movie_info_releasetime = (TextView) findViewById(R.id.btn_movie_info_releasetime);
		btn_movie_info_title = (TextView) findViewById(R.id.btn_movie_info_title);
		re_movie_title = (TextView) findViewById(R.id.re_movie_title);
		btn_moive_lanselect = (TextView) findViewById(R.id.btn_moive_lanselect);
		btn_moive_lanselect.setOnClickListener(this);
		btn_movie_info_introduction = (TextView) findViewById(R.id.btn_movie_info_introduction);
		view_movie_paly = (Button) findViewById(R.id.view_movie_paly);
		view_movie_collect = (Button) findViewById(R.id.view_movie_collect);

		view_movie_collect.setVisibility(View.INVISIBLE);
		view_movie_collect.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.type_details_menu_alltype_lv);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				listView.setVisibility(View.GONE);
				btn_moive_lanselect.setSelected(false);
			}
		});
		// btn_moive_lanselect.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Dialog builder=new Dialog(MainActivity.this,R.style.dialog);
		// LayoutInflater inflater =LayoutInflater.from(MainActivity.this);
		// View view = inflater.inflate(R.layout.testdiag, null);
		// // dialog_test_re = (RelativeLayout)
		// view.findViewById(R.id.dialog_test_re);
		// // dialog_test_re.getBackground().setAlpha(200);
		// builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// builder.setContentView(view);
		// Window dialogWindow = builder.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// dialogWindow.setGravity(Gravity.CENTER);
		// lp.width = 130;
		// lp.height = 170;
		// lp.x=-190;
		// lp.y=145;
		// dialogWindow.setAttributes(lp);
		// builder.show();
		// }
		// });
		view_movie_paly.setOnClickListener(this);
		view_movie_payfor_item01 = findViewById(R.id.view_movie_payfor_item01);
		view_movie_payfor_item02 = findViewById(R.id.view_movie_payfor_item02);
		view_movie_payfor_item03 = findViewById(R.id.view_movie_payfor_item03);
		view_movie_payfor_item04 = findViewById(R.id.view_movie_payfor_item04);
		view_movie_payfor_item05 = findViewById(R.id.view_movie_payfor_item05);
		view_movie_payfor_item_main = findViewById(R.id.view_movie_payfor_item_main);
		view_movie_payfor_item01.setVisibility(View.GONE);
		view_movie_payfor_item02.setVisibility(View.GONE);
		view_movie_payfor_item03.setVisibility(View.GONE);
		view_movie_payfor_item04.setVisibility(View.GONE);
		btn_movie_info_logo = (ImageView) findViewById(R.id.btn_movie_info_logo);
		btn_rightarrow.setVisibility(View.INVISIBLE);
		btn_leftarrow.setVisibility(View.INVISIBLE);
		String path = HttpRequest.getInstance().getURL_isCollect_FAVMOVIE();
		System.out.println("���Ե�idΪ000" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_movie_collect.setTag("���ղ�");
						view_movie_collect.setVisibility(View.VISIBLE);
						view_movie_collect
								.setBackgroundResource(R.drawable.movie_collect_selector);
					} else {
						view_movie_collect.setTag("�ղ�");
						view_movie_collect.setVisibility(View.VISIBLE);
						view_movie_collect
								.setBackgroundResource(R.drawable.movie_uncollect_selector);
					}
				}
				super.callback(url, object, status);
			}
		});
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if(re_movie_loading.getVisibility()==View.VISIBLE&&line_movie_detail.getVisibility()==View.VISIBLE){
	// re_movie_loading.setVisibility(View.GONE);
	// }
	//
	// else if(re_movie_loading.getVisibility()==View.VISIBLE){
	// re_movie_loading.setVisibility(View.GONE);
	// }
	// else {
	// finish();
	// android.os.Process
	// .killProcess(android.os.Process
	// .myPid());
	// }
	//
	//
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	private String streamString(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in),
				16 * 1024);
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private void initPlayingSource(final MovieBean movie) {
		ArrayList<String> movieFromList = movie.getMovieFromList();
		if (movieFromList == null || movieFromList.size() == 0)
			return;
		final ImageView[] imageviewList = new ImageButton[movieFromList.size()];
		for (int j = 0; j < movieFromList.size(); j++) {
			imageviewList[j] = new ImageButton(MainActivity.this);
			imageviewList[j].setId(2000 * j);
			if ("����".equals(movieFromList.get(j))) {
				getWebLogo("����", 0, imageviewList[j]);
				imageviewList[j].setTag("����");
			}
			if ("������".equals(movieFromList.get(j))) {
				getWebLogo("������", 1, imageviewList[j]);
				imageviewList[j].setTag("������");
			}
			if ("Ѹ��".equals(movieFromList.get(j))) {
				getWebLogo("Ѹ��", 2, imageviewList[j]);
				imageviewList[j].setTag("Ѹ��");
			}
			if ("��Ѷ".equals(movieFromList.get(j))) {
				getWebLogo("��Ѷ", 3, imageviewList[j]);
				imageviewList[j].setTag("��Ѷ");
			}
			if ("M1905��Ӱ��".equals(movieFromList.get(j))) {
				getWebLogo("M1905��Ӱ��", 4, imageviewList[j]);
				imageviewList[j].setTag("M1905��Ӱ��");
			}
			if ("ʱ����".equals(movieFromList.get(j))) {
				getWebLogo("ʱ����", 5, imageviewList[j]);
				imageviewList[j].setTag("ʱ����");
			}
			if ("����".equals(movieFromList.get(j))) {
				getWebLogo("����", 6, imageviewList[j]);
				imageviewList[j].setTag("����");
			}
			if ("�Ѻ�".equals(movieFromList.get(j))) {
				getWebLogo("�Ѻ�", 7, imageviewList[j]);
				imageviewList[j].setTag("�Ѻ�");
			}
			if ("ppTV����".equals(movieFromList.get(j))) {
				getWebLogo("ppTV����", 8, imageviewList[j]);
				imageviewList[j].setTag("ppTV����");
			}
			if ("�ſ�".equals(movieFromList.get(j))) {
				getWebLogo("�ſ�", 9, imageviewList[j]);
				imageviewList[j].setTag("�ſ�");
			}
			if ("����".equals(movieFromList.get(j))) {
				getWebLogo("����", 10, imageviewList[j]);
				imageviewList[j].setTag("����");
			}
			imageviewList[j]
					.setBackgroundResource(R.drawable.imagebutton_selector);
			imageviewList[j].setScaleType(ImageView.ScaleType.FIT_XY);

			if (j >= 0) {
				// btn_leftarrow.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams layp = new LinearLayout.LayoutParams(
						100, 100);
				layp.setMargins(15, 10, 15, 10);
				imageviewList[j].setLayoutParams(layp);
				if (j == 0) {
					imageviewList[j].requestFocus();
					imageviewList[j].setFocusable(true);
					imageviewList[j].setSelected(true);
				}
			}
			imageviewList[j]
					.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							System.out.println("v�Ľ���Ϊ" + v.getId());
							if (hasFocus) {
								if (v.getId() == 0) {
									btn_leftarrow.setVisibility(View.INVISIBLE);
								} else if (v.getId() == 2000 * (pic.length - 2)) {
									btn_rightarrow
											.setVisibility(View.INVISIBLE);
								} else {
									btn_leftarrow.setVisibility(View.VISIBLE);
									btn_rightarrow.setVisibility(View.VISIBLE);
								}
							}
						}
					});
			imageviewList[j].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					movieCount = 0;
					if (oldView != null) {
						oldView.setSelected(false);
					} else {
						imageviewList[0].setSelected(false);
					}
					int position = v.getId() / 2000;
					isFristClick = true;
					isDrawable = true;
					initPlayState();
					v.setSelected(true);
					setPaySource(movie, position);
					oldView = v;
				}
			});
			lay_list.addView(imageviewList[j]);
		}

	}

	private void getWebLogo(String name, int defalutLogo, ImageView iv) {
		SharedPreferences sp = getSharedPreferences("image",
				Context.MODE_WORLD_READABLE);
		int[] defaultImage = { R.drawable.leshilogo, R.drawable.qiyi,
				R.drawable.xunlei, R.drawable.tengxun,
				R.drawable.mdianyingwang, R.drawable.shiguangwang,
				R.drawable.xinlang, R.drawable.souhu, R.drawable.ppvt,
				R.drawable.youku, R.drawable.tudou };
		String url = sp.getString(name, "");
		if (url == null || "".equals(url)) {
			// if(defalutLogo<defaultImage.length){
			iv.setImageResource(defaultImage[defalutLogo]);
			// }else{
			// iv.setImageResource(R.drawable.grid_item_default);
			// }
		} else {
			Bitmap image = ImageFileCache.getCashInstance().getImage(url);
			if (image != null) {
				iv.setImageBitmap(image);
			} else {
				ImageAsyncLoader imLoader = new ImageAsyncLoader();
				Bitmap bitmap = imLoader.getImage(url);
				ImageFileCache.getCashInstance().saveBmpToSd(bitmap, url);
				iv.setImageBitmap(image);
			}
		}

	}
	
	/**
	 * ֧����Ϣ
	 * @param movie
	 * @param position
	 */

	private void setPaySource(MovieBean movie, int position) {
		typeCount = position;
		System.out.println("����������position==========>"+position);
		
		ArrayList<VideoPlayInfo> VideoPlayInfoList = getVideoPlayInfoList(
				movie, position);
		if (null == VideoPlayInfoList || VideoPlayInfoList.size() == 0) {
			view_movie_payfor_item_main.setVisibility(View.GONE);
			view_movie_payfor_item01.setVisibility(View.GONE);
			view_movie_payfor_item02.setVisibility(View.GONE);
			view_movie_payfor_item03.setVisibility(View.GONE);
			view_movie_payfor_item04.setVisibility(View.GONE);
			view_movie_payfor_item05.setVisibility(View.GONE);
			return;
		}
		view_movie_payfor_item_main.setVisibility(View.VISIBLE);
		view_movie_payfor_item01.setVisibility(View.GONE);
		view_movie_payfor_item02.setVisibility(View.GONE);
		view_movie_payfor_item03.setVisibility(View.GONE);
		view_movie_payfor_item04.setVisibility(View.GONE);
		view_movie_payfor_item05.setVisibility(View.GONE);
		for (int i = 0; i < VideoPlayInfoList.size(); i++) {
			final int j = i;
			VideoPlayInfo videoPlayInfo = VideoPlayInfoList.get(i);
			findViewById(view_movie_payforID[i]).setVisibility(View.VISIBLE);
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_format)))
					.setText(videoPlayInfo.getQuality_cn());
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_resolutionratio)))
					.setText(videoPlayInfo.getResolution());
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_price_nomaruser)))
					.setText(videoPlayInfo.getNomaluser());
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
					.setText(videoPlayInfo.getVipuser());
			// ((TextView)(findViewById(view_movie_payforID[i]).findViewById(R.id.btn_movie_info_tv_price_vipuser))).setBackgroundResource(Color.parseColor("#003268"));
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_price_monthuser)))
					.setText(videoPlayInfo.getMonthuser());
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_ok_sele)))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							View oldClickView;
							if (isFristClick) {
								isFristClick = false;
								int id = ((TextView) (findViewById(view_movie_payforID[0])
										.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
										.getId();
								int id2 = v.getId();
								if (id == id2) {
									textViewOldView.setBackgroundColor(Color
											.parseColor("#003268"));
									movieCount = -1;
									return;
								}
							}
							if (v == clickViewOldView) {
								if (isDrawable) {
									textViewOldView.setBackgroundColor(Color
											.parseColor("#cc0000"));
									isDrawable = false;
									movieCount = j;
									return;
								} else {
									textViewOldView.setBackgroundColor(Color
											.parseColor("#003268"));
									isDrawable = true;
									movieCount = -1;
									return;
								}
							}
							((TextView) (findViewById(view_movie_payforID[j])
									.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
									.setBackgroundColor(Color
											.parseColor("#cc0000"));
							// ((TextView)(findViewById(view_movie_payforID[j]).findViewById(R.id.btn_movie_info_tv_ok_sele))).setBackgroundResource(R.drawable.movie_pay_selected);
							// ((TextView)(findViewById(view_movie_payforID[j]).findViewById(R.id.btn_movie_info_tv_ok_sele))).requestFocus();
							// ((TextView)(findViewById(view_movie_payforID[j]).findViewById(R.id.btn_movie_info_tv_ok_sele))).setFocusable(true);
							if (null != textViewOldView) {
								textViewOldView.setBackgroundColor(Color
										.parseColor("#003268"));
							}
							// if(null!=textViewforSelectOldView){
							// textViewforSelectOldView.setBackgroundColor(Color.parseColor("#003268"));
							// }
							textViewOldView = ((TextView) (findViewById(view_movie_payforID[j])
									.findViewById(R.id.btn_movie_info_tv_price_vipuser)));
							clickViewOldView = ((TextView) (findViewById(view_movie_payforID[j])
									.findViewById(R.id.btn_movie_info_tv_ok_sele)));
							// ((TextView)(findViewById(view_movie_payforID[j]).findViewById(R.id.btn_movie_info_tv_ok_sele))).setSelected(true);
							// textViewforSelectOldView=
							// ((TextView)(findViewById(view_movie_payforID[j]).findViewById(R.id.btn_movie_info_tv_ok_sele)));
							movieCount = j;
						}
					});
		}
		((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_ok_sele))).requestFocus();
		((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_ok_sele)))
				.setFocusable(true);
		((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_ok_sele)))
				.setSelected(true);
		// TextView
		// tv=((TextView)(findViewById(view_movie_payforID[0]).findViewById(R.id.btn_movie_info_tv_price_vipuser)));
		// tv.setBackgroundColor(Color.parseColor("#cc0000"));
		textViewOldView = ((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_price_vipuser)));
		clickViewOldView = ((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_ok_sele)));
		((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
				.setBackgroundColor(Color.parseColor("#cc0000"));
	}

	/**
	 * ��ȡ��Ƶ���ȣ����ж���Ƶ�Ƿ���Բ���
	 * 
	 * @param url
	 * @return
	 */
	// public long getLengthByURL(String url) {
	// HttpGet httpRequest = new HttpGet(url) ;
	// try {
	// HttpParams p = new BasicHttpParams();
	// HttpConnectionParams.setConnectionTimeout(p, NETWORK_CONNECT_TIMEOUT);
	// HttpConnectionParams.setSoTimeout(p, NETWORK_SO_TIMEOUT);
	// HttpClient httpClient = new DefaultHttpClient(p) ;
	//
	// HttpResponse httpResponse= httpClient.execute(httpRequest) ;
	// if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
	// HttpResponse mHttpResponse = httpResponse ;
	// HttpEntity entity = mHttpResponse.getEntity() ;
	// long movieLength = entity.getContentLength();
	// return movieLength ;
	// }
	// } catch (ClientProtocolException e) {
	// httpRequest.abort() ;
	// e.printStackTrace();
	// } catch (IOException e) {
	// httpRequest.abort() ;
	// e.printStackTrace();
	// }
	// return 0;
	// }


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ("24".equals(HttpRequest.getInstance().getType())) {

			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (game_gridview.getSelectedItemId() >= 4) {
					gamePageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (game_gridview.getSelectedItemId() <= 3) {
					gamePageUp();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (game_gridview.getSelectedItemId() >= 7) {
					gamePageDown();
				} else if (game_gridview.getSelectedItemId() == 3) {
					if (game_gridview.getSelectedItemId() > 3) {
						game_gridview.setSelection(4);
					} else {
						game_gridview.setSelection(3);
					}
				}

			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (game_gridview.getSelectedItemId() == 0) {
					gamePageUp();

				} else if (game_gridview.getSelectedItemId() == 4) {
					game_gridview.setSelection(3);
				}
			}

		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		/*	final Dialog dialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
			dialog.setContentView(R.layout.exitdialog);
			Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
			Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
			TextView tv = (TextView) dialog.findViewById(R.id.textView1);
			tv.setText(R.string.exit);
			
		btn_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
				android.os.Process
						.killProcess(android.os.Process
								.myPid());
			}
		});
		btn_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				exitdialog.setVisibility(View.GONE);
				dialog.dismiss();
				
			}
		});
			dialog.show();*/
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

	private ArrayList<VideoPlayInfo> getVideoPlayInfoList(MovieBean movie2,
			int position) {
		ArrayList<String> movieFromList = movie2.getMovieFromList();
		if (movieFromList == null || movieFromList.size() == 0) {
			return null;
		}
		String fromSource = movieFromList.get(position);
		HashMap<String, ArrayList<VideoPlayInfo>> moviePlayMap = movie2
				.getMoviePlayMap();
		if (null==moviePlayMap || moviePlayMap.size() == 0) {
			return null;
		}
		ArrayList<VideoPlayInfo> VideoPlayInfoList = moviePlayMap
				.get(fromSource);
		return VideoPlayInfoList;
	}
	/**
	 * check version
	 */
	private void checkVersion() {
		try {
			PackageManager packageManager = getPackageManager();
			// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
			PackageInfo packInfos = packageManager.getPackageInfo(
					getPackageName(), 0);
			final String version = packInfos.versionName;
			String packageName = packInfos.packageName;
			String apkPath = HttpRequest.getInstance().getURL_UPDATE_APK()
					+ packageName;
			aQuery.ajax(apkPath, String.class, new AjaxCallback<String>() {
				@Override
				public void callback(String url, String apkStr,
						AjaxStatus status) {
					System.out.println("���µĵ�ַΪ" + apkStr);
					if (apkStr != null && apkStr.length() != 0) {
						// TODO Auto-generated method stub
						super.callback(url, apkStr, status);
						// ����[TAB]com.ccdrive.comic[TAB]1.1.2.9[TAB]137048843775650001[CR]
						/**
						 * apkEntity[0]=���� apkEntity[1]=com.ccdrive.comic
						 * apkEntity[2]=1.1.2.9 apkEntity[3]=137048843775650001
						 */
						String[] apkEntity = apkStr.split("\\[TAB\\]|\\[CR\\]");
						System.out.println("version===="+version+"===apapkEntity"+apkEntity[2]);
						if (!version.equals(apkEntity[2])) {
							HttpRequest.getInstance().setApkuuid(apkEntity[3]);
							String path = HttpRequest.getInstance()
									.getURL_DOWN_UPDATE_APK();
//							setUpdateDiago(path, apkEntity[1]);
							UpdateApk.setInstall(MainActivity.this, apkEntity[1], path);
						}
					}
				}
			});
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * appearUpdateDialog
	 * 
	 * @param path
	 *            Update url path
	 * @param desc
	 *            Update content
	 */
	void setUpdateDiago(final String path, final String apkName) {
		final Handler hd = new Handler();
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(MainActivity.this,	R.style.Theme_Dialog);
		dialog.setContentView(R.layout.exitdialog);
		Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
		Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
		if(apkName.equals("com.ccbookreading")||apkName.equals("com.ccdrive.photoviewer")){
			TextView tv = (TextView) dialog.findViewById(R.id.textView1);
			tv.setText("��Ҫ������Ӧ�Ĳ�����!");
		}
		btn_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialogbar = new Dialog(MainActivity.this,
						R.style.Theme_Dialog);
				dialogbar.setContentView(R.layout.activity_progressbar);
				final ProgressBar pb_bar = (ProgressBar) dialogbar.findViewById(R.id.pb_bar);
				final TextView tv_progressbar = (TextView) dialogbar
						.findViewById(R.id.tv_progressbar);
				dialogbar.show();
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						UpdateVersion uv = UpdateVersion.instance(
								MainActivity.this, hd, true, false);
						uv.setUpdateUrl(path);
						uv.setProgressBar(pb_bar, dialogbar, tv_progressbar);
						uv.setLoadApkName(apkName);
						uv.run();
						return null;
					}
				}.execute();
				dialog.dismiss();
			}
			
		});
		btn_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

	private void setDataPage() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String content;
				int i = 0;
				while (true) {
					i++;
					content = HttpUtil.getResponseString(testPath);
					if (content != null) {
						return content;
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
							return null;
						}
					}
					if (i > 3) {
						finish();
					}
				}
			}

			@Override
			protected void onPreExecute() {

				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				if (null!=result &&!"".equals(result)&& result.length() != 0&&!result.contains("<html>")) {
					re_movie_loading.setVisibility(View.INVISIBLE);
					btn_movie_info_logo
							.setImageResource(R.drawable.grid_item_default);
					layoutview.setBackgroundResource(R.drawable.movie_main_bg);
					line_movie_detail.setVisibility(View.VISIBLE);
					movie = JSONUtil.getMoiveList(result);
					btn_movie_info_area.setText(movie.getArea());
					btn_movie_info_actor.setText(movie.getActor());
					btn_movie_info_type.setText(movie.getType());
					btn_movie_info_playingtimer.setText(movie.getPlayingTime());
					btn_movie_info_director.setText(movie.getAddtime());
					btn_movie_info_releasetime.setText(movie.getAddtime());
					btn_movie_info_title.setText(movie.getName());
					btn_movie_info_introduction.setText(movie.getOrotagonist());
					/**
					 *
					 */
					Bitmap drawable = loader.loadDrawable(movie.getPic(),
							new ImageCallback() {
								@Override
								public void imgeLoader(Bitmap draw,
										String imgeURL) {
									btn_movie_info_logo.setImageBitmap(draw);
								}
							});
					setPaySource(movie, 0);
					initPlayingSource(movie);
				} else {
					re_movie_loading.setVisibility(View.INVISIBLE);
					// finish();
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	private void addMovieFav(String path) {
		System.out.println("�ղصĵ�ַΪ" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object.contains("true")) {
					Toast.makeText(MainActivity.this, "�����ɹ�", 1);
				} else {
					Toast.makeText(MainActivity.this, "����ʧ��", 1);
				}
				super.callback(url, object, status);
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_moive_lanselect:
			if (listView.getVisibility() == View.GONE) {
				TypeDetailsSubMenuAdapter adapter = new TypeDetailsSubMenuAdapter(
						MainActivity.this, selectList);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
				btn_moive_lanselect.setSelected(true);
			} else {
				listView.setVisibility(View.GONE);
				btn_moive_lanselect.setSelected(false);
			}
			break;
		case R.id.view_movie_collect:
			String collect = (String) view_movie_collect.getTag();
			if ("���ղ�".equals(collect)) {
				// �ղ�
				view_movie_collect
						.setBackgroundResource(R.drawable.movie_uncollect_selector);
				view_movie_collect.setTag("�ղ�");
				String addFavMoviePaht = HttpRequest.getInstance()
						.getURL_LIST_ADDFAVMOVIE();
				addMovieFav(addFavMoviePaht);
			} else {
				// view_movie_collect.setBackgroundResource(R.drawable.movie_collect_selector);
				// view_movie_collect.setTag("���ղ�");
				// String
				// addFavMoviePaht=HttpRequest.getInstance().getURL_MOVIE_CANCLE_COLLECT();
				// addMovieFav(addFavMoviePaht);
			}
			break;
		case R.id.view_movie_paly:
			if (typeCount != -1) {
				ArrayList<VideoPlayInfo> videoPlayInfoList = getVideoPlayInfoList(
						movie, typeCount);
				if (null == videoPlayInfoList || videoPlayInfoList.size() == 0) {
					final String webUrl = movie.getDefaultPlayList().get(
							typeCount);
					System.out.println("��Ҫ����ȥ��urlΪ==========����"+webUrl);
//					Uri uri = Uri.parse(webUrl);
//					Intent it = new Intent(Intent.ACTION_VIEW, uri);
//					aQuery.getContext().startActivity(it);
////					getVideoPlayPath(webUrl, "1");
//					String addFavMoviePaht = HttpRequest.getInstance()
//							.getURL_LIST_ADDFAVMOVIE()
//							+ HttpRequest.getInstance().getId();
//					System.out.println("��ӵĵ�ַΪ" + addFavMoviePaht);
//					addMovieFav(addFavMoviePaht);
//					setVideoInfo("", webUrl);
					HttpRequest.getInstance().setUrl(webUrl);
					Intent  i = new Intent(MainActivity.this,CCVitamioPlayer.class);
					i.putExtra("url", webUrl);
					startActivity(i);
					return;
				} else {
					String type = movie.getMovieFromList().get(typeCount);
					final String playPath = movie.getMoviePlayMap().get(type)
							.get(movieCount).getPlayPath();
					final String webUrl = movie.getMoviePlayMap().get(type)
							.get(movieCount).getWebUrl();
					String quality = movie.getMoviePlayMap().get(type)
							.get(movieCount).getQuality();
//					getVideoPlayPath(webUrl, quality);
					HttpRequest.getInstance().setUrl(webUrl);
					HttpRequest.getInstance().setLetvQuity("1300");
					
					Intent  i = new Intent(MainActivity.this,CCVitamioPlayer.class);
					i.putExtra("url", webUrl);
					startActivity(i);
				}
			}
			break;
		case R.id.btn_game_pageup:
			gamePageUp();
			break;
		case R.id.btn_game_pagedown:
			gamePageDown();
			break;
		case R.id.view_game_collect:
			String gameCollect = (String) view_game_collect.getTag();
			if ("�� �ղ�".equals(gameCollect)) {
				// �ղ�
				view_game_collect
						.setBackgroundResource(R.drawable.movie_uncollect_selector);
				view_game_collect.setTag("�ղ�");
				String addFavMoviePaht = HttpRequest.getInstance()
						.getURL_LIST_ADDFAVTV();
				System.out.println("�����Ϸ�ղصĵ�ַΪ" + addFavMoviePaht);
				addMovieFav(addFavMoviePaht);
				System.out.println("�ղط���" + addFavMoviePaht);
			}
			break;
		case R.id.view_game_paly:
			String videopath = gameBean.getVideopath();
			String htmlPath = gameBean.getHtmlpath();
			if (!"".equals(htmlPath) && null != htmlPath) {
				getVideoPlayPath(htmlPath, "1");
			} else {
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse(gameBean.getVideopath());
				i.setDataAndType(uri, "video/*");
				startActivity(i);
			}
			// if("".equals(videopath)||null==videopath){
			// Uri uri = Uri.parse(gameBean.getHtmlpath());
			// Intent it = new Intent(Intent.ACTION_VIEW, uri);
			// aQuery.getContext().startActivity(it);
			// String addFavMoviePaht = HttpRequest.getInstance()
			// .getURL_LIST_ADDFAVMOVIE();
			// System.out.println("��ӵĵ�ַΪ" + addFavMoviePaht);
			// addMovieFav(addFavMoviePaht);
			// }else{
			// Intent i = new Intent(
			// Intent.ACTION_VIEW);
			// Uri uri = Uri.parse(gameBean.getVideopath());
			// i.setDataAndType(uri,
			// "video/*");
			// startActivity(i);
			// }

		}
	}

	private void setVideoInfo(String quality, String url) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("mac", mac);
		hashMap.put("id", movie.getId());
		if (!"".equals(quality)) {
			hashMap.put("quality", quality);
		}
		hashMap.put("url", url);
		try {
			SoapUtils.getInstance().log("movieplay", hashMap);
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "�ϴ�webservice����",
					Toast.LENGTH_SHORT);
		}

	}

	private void initPlayState() {
		for (int i = 0; i < view_movie_payforID.length; i++) {
			((TextView) (findViewById(view_movie_payforID[i])
					.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
					.setBackgroundColor(Color.parseColor("#003268"));
		}
		((TextView) (findViewById(view_movie_payforID[0])
				.findViewById(R.id.btn_movie_info_tv_price_vipuser)))
				.setBackgroundColor(Color.parseColor("#cc0000"));
	}

	private void setMovieSoruce() {
		String path = "http://apk.vocy.com/logo/logo.json";
		final ImageFileCache cache = ImageFileCache.getCashInstance();
		final ArrayList<String> emptyList = new ArrayList<String>();
		SharedPreferences preferences = getSharedPreferences("image",
				MODE_WORLD_READABLE);
		final SharedPreferences.Editor editor = preferences.edit();
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String Json, AjaxStatus status) {
				if (Json != null && "".equals(Json)) {

					try {
						JSONArray jsonArray = new JSONArray(Json);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String name = jsonObject.getString("source");
							String imagePath = jsonObject
									.getString("imagepath");
							editor.putString(name, imagePath);
							Bitmap image = cache.getImage(imagePath);
							if (image == null) {
								emptyList.add(imagePath);
							}
						}
						editor.commit();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void setPlay(String path) {
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println();
					ArrayList<PlayPathBean> PlayAllList = JSONUtil
							.getTvPlayPath(object);
					if (httpRequest.getType().equals("7")
							|| httpRequest.getType().equals("23")) {
//						if (null != PlayAllList.get(0).getVideoPath()
//								&& !PlayAllList.get(0).getVideoPath()
//										.equals("")) {
//							String path = PlayAllList.get(0).getVideoPath();
//							Intent i = new Intent(Intent.ACTION_VIEW);
//							Uri uri = Uri.parse(path);
//							System.out.println("���ŵĵ�ַΪ" + path);
//							i.setDataAndType(uri, "video/*");
//							startActivity(i);
//						} else {
//							String webUrl = PlayAllList.get(0).getHtmlPath();
//							Uri uri = Uri.parse(webUrl);
//							Intent it = new Intent(Intent.ACTION_VIEW, uri);
//							startActivity(it);
//						}
						 if(PlayAllList.get(0).getHtmlPath()!=null&&!"".equals(PlayAllList.get(0).getHtmlPath())){
							 String webUrl = PlayAllList.get(0).getHtmlPath();
							 getVideoPlayPath(webUrl, "1");
						 }
					} else {
						setDialog(PlayAllList);
					}
				}
				super.callback(url, object, status);
			}
		});
	}

	private void setDialog(final ArrayList<PlayPathBean> tvsourceList) {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View orderPageView = inflater.inflate(R.layout.tv_source_main, null);
		tv_grid_source_list = (GridView) orderPageView
				.findViewById(R.id.tv_grid_source_list);
		tv_grid_source_list.setSelector(new ColorDrawable(0));
		final Dialog builder = new Dialog(MainActivity.this);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		builder.setContentView(orderPageView);
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.dimAmount = 0.1f;
		lp.alpha = 0.8f;
		// dialogWindow.setGravity(Gravity.CENTER);
		tv_grid_source_list.setAdapter(new TvSourceGridAdapter(aQuery
				.getContext(), tvsourceList));
		int size = tvsourceList.size() / 5;
		if (size % 5 != 0 || size == 0) {
			size = size + 1;
		}
		lp.width = 885;
		// lp.height = 130*size;
		lp.height = 430;
		lp.x = 180;
		lp.y = 135;
		dialogWindow.setAttributes(lp);
		builder.show();
		tv_grid_source_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("���Ӳ��ŵ�λ��==" + position + "tv�Ĵ�СΪ"
						+ tvsourceList.size());
				PlayPathBean playPathBean = tvsourceList.get(position);
//				if (null != playPathBean.getVideoPath()
//						&& !playPathBean.getVideoPath().equals("")) {
//					String path = playPathBean.getVideoPath();
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					Uri uri = Uri.parse(path);
//					System.out.println("���ŵĵ�ַΪ" + path);
//					i.setDataAndType(uri, "video/*");
//					startActivity(i);
//				} else {
//					String webUrl = playPathBean.getHtmlPath();
//					Uri uri = Uri.parse(webUrl);
//					Intent it = new Intent(Intent.ACTION_VIEW, uri);
//					startActivity(it);
//				}
				 if(playPathBean.getHtmlPath()!=null&&!"".equals(playPathBean.getHtmlPath())){
					 String webUrl = playPathBean.getHtmlPath();
					 builder.dismiss();
//					 getVideoPlayPath(webUrl, "1");
					 Intent i = new Intent(MainActivity.this,CCVitamioPlayer.class);
					 i.putExtra("type", HttpRequest.getInstance().getType());
					 i.putExtra("url", webUrl);
					 String num = btn_tv_info_playingtimer.getText().toString();
					 i.putExtra("num", num);
					 startActivity(i);
				 }

			}
		});

	}

	/**
	 * ��������apk
	 */
	private void callCCBookPlayer(int count, final String packname,
			final String downloadpath, ArrayList<String> photoList,EduBean eduBean) {
		AppUtil au = new AppUtil(aQuery.getContext());
		if (!au.isInstall(packname)) {
			setUpdateDiago(downloadpath, packname);
		} else {
			/**
			 * Intent  musicIntent = new Intent();
							musicIntent.putExtra("albumid", HttpRequest.getInstance().getId());
							musicIntent.putExtra("flag", "0_2");
						musicIntent.putExtra("webRoot", HttpRequest.getInstance().getWEB_ROOT());
						musicIntent.putExtra("myToken", HttpRequest.getInstance().getMytoken());
						musicIntent.putExtra("webRootDetail", HttpRequest.getInstance().getSTATIC_WEB_ROOT());
						musicIntent.putExtra("type",HttpRequest.getInstance().getType());
						musicIntent.putExtra("pageSize", "10");
						musicIntent.putExtra("currpage", currentPage);
						callCCBookPlayer(count, packname, downloadpath, photoList)
						musicIntent.setClassName(packname, packnameMain);
			 */
			Intent intent = new Intent();
			//------------------------------------------------
			intent.putExtra("flag", "19_2");
			intent.putExtra("myToken", HttpRequest.getInstance().getMytoken());
			intent.putExtra("webRoot", HttpRequest.getInstance().getWEB_ROOT());
			intent.putExtra("currpage", currentPage);
			intent.putExtra("pageSize", "10");
			intent.putExtra("albumid", HttpRequest.getInstance().getId());
			if(null!=eduBean){
			intent.putExtra("imagePath", eduBean.getPic());
			intent.putExtra("rose", eduBean.getRose());
			}
			//-----------------------------------------------------
			intent.putExtra("id", HttpRequest.getInstance().getId());
			intent.putExtra("position", count);
			intent.putExtra("countPage", HttpRequest.getInstance().getCount());
			intent.putExtra("type", HttpRequest.getInstance().getType());
			intent.putExtra("mytoken", HttpRequest.getInstance().getMytoken());
			intent.putExtra("photopath", photoList);
			intent.putExtra("webroot", HttpRequest.getInstance().getWEB_ROOT());
			if(HttpRequest.getInstance().getWEB_ROOT().contains("192")){
				intent.putExtra("STATIC_WEB_ROOT", "http://192.168.1.3:2014/");
				intent.putExtra("webRootDetail","http://192.168.1.3:2014/");
			}else{
				intent.putExtra("webRootDetail", "http://html.vocy.com/");
				intent.putExtra("STATIC_WEB_ROOT", "http://html.vocy.com/");
			}
			if(HttpRequest.getInstance().getType().equals("19")){
			intent.setClassName(packname, packname + ".activity.MainActivity");
			}else{
				intent.setClassName(packname, packname + ".MainActivity");
			}
			startActivity(intent);
		}
	}

	/**
	 * ��ʼ����Ϸ��һЩ����
	 */
	private void initGamesData(String id,String webUrl) {
		Intent  i  =new Intent(MainActivity.this,CCVitamioPlayer.class);
		i.putExtra("url", webUrl);
		startActivity(i);
		finish();
/*		setContentView(R.layout.gamesdetail);
		HttpRequest.getInstance().setId(id);
		httpRequest.setCount(1);
		layoutview = (RelativeLayout) findViewById(R.id.line_movie_main);
		// layoutview.setBackgroundResource(R.drawable.menu_bg);
		Button view_game_paly = (Button) findViewById(R.id.view_game_paly);
		view_game_paly.setOnClickListener(this);
		line_movie_detail = (LinearLayout) findViewById(R.id.ll_line_movie_main);
		// line_movie_detail.setVisibility(View.INVISIBLE);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		re_movie_loading.setVisibility(View.VISIBLE);
		btn_game_pageup = (Button) findViewById(R.id.btn_game_pageup);
		btn_game_pagedown = (Button) findViewById(R.id.btn_game_pagedown);
		tv_totalrows = (TextView) findViewById(R.id.tv_totalrows); // ����
		tv_currentpage = (TextView) findViewById(R.id.tv_currentpage); // ��ǰҳ
  		game_gridview = (GridView) findViewById(R.id.game_gridview);
		btn_game_pagedown.setOnClickListener(this);
		btn_game_pageup.setOnClickListener(this);
		game_gridview.setVisibility(View.VISIBLE);
		game_gridview.requestFocus();
		game_gridview.setFocusable(true);
		game_gridview.setSelector(new ColorDrawable(     ));
		gamesGridAdapter = new GamesGridAdapter(this, null);
		game_gridview.setAdapter(gamesGridAdapter);
		httpRequest.setPageSize(8);
		btn_game_pageup.setOnClickListener(this);
		btn_game_pagedown.setOnClickListener(this);
		game_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				System.out.println("game_gridview===============>");
				if (null != gameBean) {
					String changeId = gameBean.getGameBeanList().get(position)
							.getId();
					if (!changeId.equals("")) {
						initGamesData(changeId);
					}
				}
			}
		});
		game_gridview.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View itemView,
					int arg2, long arg3) {
				
				 * if (itemView != null) {
				 * itemView.startAnimation(AnimationTools.getMaxAnimation(100));
				 * } if (preView != null) {
				 * preView.startAnimation(AnimationTools.getMiniAnimation(100));
				 * } preView = itemView;
				 
				App.playSound(getResources().getString(
						R.string.sound_movie_select));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		aQuery = new AQuery(MainActivity.this);
		httpRequest = HttpRequest.getInstance();
		String path = HttpRequest.getInstance().getURL_isCollect_FAVMOVIE();
		System.out.println("��Ϸ�ж��ղصĵ�ַ======" + path);
		System.out.println("���Ե��ղ�idΪ------>" + path);
		view_game_collect = (Button) findViewById(R.id.view_game_collect);
		view_game_collect.setOnClickListener(this);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_game_collect.setTag("���ղ�");
						view_game_collect.setVisibility(View.VISIBLE);
						view_game_collect
								.setBackgroundResource(R.drawable.movie_collect_selector);
					} else {
						view_game_collect.setTag("�ղ�");
						view_game_collect.setVisibility(View.VISIBLE);
						view_game_collect
								.setBackgroundResource(R.drawable.movie_uncollect_selector);
					}
				}
				super.callback(url, object, status);
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

				// case MSG_LOAD_MOVIEINFO:
				// getOpus();
				// break;
				case MSG_LOAD_MOVIE:
					// initData();
					getGameInfo();
					break;
				case MSG_LOAD_ERR:
					Toast.makeText(aQuery.getContext(), "���ش���", 0).show();
					break;
				default:
					break;
				}
			}
		};
		getGameInfo();*/
	}

	/**
	 * ��ȡ��Ϸ��һЩ�����Ϣ
	 */
	private void getGameInfo() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				int i = 0;
				while (true) {
					i++;
					content = HttpUtil.getResponseString(httpRequest
							.getURL_BookDetail());
					System.out.println("���ص���Ϸ�ĵ�ַΪ"
							+ httpRequest.getURL_BookDetail());
					if (content != null) {
						return content;
					} else {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
							return null;
						}
					}
					if (i > 10) {
						finish();
					}
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null && result.length() != 0) {
					System.out.println("��������������Ϊ" + result);
					gameBean = JSONUtil.getGameBeanInfo(result);
					re_movie_loading.setVisibility(View.INVISIBLE);
					layoutview.setBackgroundResource(R.drawable.people_main_bg);
					line_movie_detail.setVisibility(View.VISIBLE);
					// imageLoader = new ImageAsyncLoader(context);
					String url = gameBean.getPic();
					System.out.println("pic��ַ" + url);
					aQuery.find(R.id.btn_game_info_type).text(
							gameBean.getType());
					aQuery.find(R.id.btn_game_info_labl).text(
							gameBean.getLabel());
					aQuery.find(R.id.btn_game_info_logo).image(url);
					// aQuery.find(R.id.tv_game_intro).text(getResources().getString(R.string.movie_intro)+gameBean.getSynopsis());
					LinearLayout layout = (LinearLayout) findViewById(R.id.game_detail_line);
					setTextView(130, LayoutParams.FILL_PARENT,
							getResources().getString(R.string.movie_intro)
									+ gameBean.getSynopsis(), layout);
					setGameGridViewInfo(gameBean);

				} else {
					re_movie_loading.setVisibility(View.INVISIBLE);
					finish();
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	private void setTextView(int height, int width, String info,
			LinearLayout view) {
		CCdriveTextView tv2 = new CCdriveTextView(this);
		tv2.setLayoutParams(new LayoutParams(130, LayoutParams.FILL_PARENT));
		// tv2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		tv2.setEllipsis("...");// //
		// tv2.setEllipsisMore("...");
		tv2.setMaxLines(4);
		tv2.setTextSize(22);
		tv2.setText(info);// //
		tv2.setPadding(10, 10, 10, 10);
		tv2.setTextColor(MainActivity.this.getResources().getColor(
				R.color.white));
		view.addView(tv2);
		// tv2.setBackgroundColor(0xFFFCDFB2);
	}

	/**
	 * ������Ϸ�����Ƶ����Ϣ
	 * 
	 * @param gameBean
	 */
	private void setGameGridViewInfo(GameBean gameBean) {
		ArrayList<GameBean> gameBeanList = gameBean.getGameBeanList();
		if ((null != gameBeanList) && (null != gameBeanList)
				&& (!"null".equals(gameBeanList) && (gameBeanList.size() != 0))) {
			if (pageFlag) {
				// TODO
				currentPage = Integer.valueOf(gameBean.getPageBean()
						.getCurrentPage());
				totalPage = Integer.valueOf(gameBean.getPageBean()
						.getTotalPage());
				totalRows = Integer.valueOf(gameBean.getPageBean()
						.getTotalRows());
				if (totalPage > 1) {
					btn_game_pagedown.setVisibility(View.VISIBLE);
				}
				pageFlag = false;
			}
			if (currentPage == 1) {
				btn_game_pageup.setVisibility(View.INVISIBLE);
			} else if (currentPage == totalPage) {
				btn_game_pagedown.setVisibility(View.INVISIBLE);
			}
			tv_totalrows.setText("( ������� : �� " + totalRows + "�����Ʒ)");
			tv_currentpage.setText("�� " + totalPage + " ҳ   " + "��ǰ�� "
					+ currentPage + " ҳ");

			if (httpRequest.getType().equals("0")) {
				game_gridview.requestFocus();
				game_gridview.setFocusable(true);
				game_gridview.setSelection(0);
				gamesGridAdapter.changData(gameBeanList);

			} else {
				gamesGridAdapter.changData(gameBeanList);
				game_gridview.requestFocus();
				game_gridview.setSelection(0);
			}
		}
	}

	/**
	 * ������Ϸ��ҳ��Ϣ
	 */
	private void gamePageDown() {
		if (this.currentPage >= this.totalPage) {
			btn_game_pagedown.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(MainActivity.this, R.string.lastPage);
			if (view_game_collect.isFocusable()) {
				if (httpRequest.getType().equals("0")) {
					game_gridview.requestFocus();
					game_gridview.setFocusable(true);
					gamesGridAdapter.setSelctItem(-1);
				} else {
					game_gridview.requestFocus();
					game_gridview.setFocusable(true);
				}
			}
			return;
		}
		btn_game_pageup.setVisibility(View.VISIBLE);
		this.currentPage = (1 + this.currentPage);
		System.out.println("currentPage-------" + currentPage);
		httpRequest.setCount(currentPage);
		content = httpRequest.getURL_BookDetail();
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	/**
	 * ������Ϸ��ҳ��Ϣ
	 */
	private void gamePageUp() {
		if (this.currentPage <= 1) {
			btn_game_pageup.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(MainActivity.this, R.string.firstPage);
			view_game_collect.requestFocus();
			view_game_collect.setFocusable(true);
			return;
		}
		btn_game_pagedown.setVisibility(View.VISIBLE);
		this.currentPage = (-1 + this.currentPage);
		httpRequest.setCount(currentPage);
		content = httpRequest.getURL_BookDetail();
		this.handler.removeCallbacks(this.changDatas);
		this.handler.postDelayed(this.changDatas, 100L);
		App.playSound(getResources().getString(R.string.sound_page_change));
	}

	/**
	 * get videoplayPath by port
	 * 
	 * @param webUrl
	 * @param QUALITYID
	 */

	private void getVideoPlayPath(final String webUrl, String QUALITYID) {
		System.out.println("���Ӿ��ԭʼ��ҳ��ַ"+webUrl);
		String encryptURL = CryptUtil.getInstance().encryptURL(webUrl);
		
		String videoWebUrlRequest = "http://apk.pctoo.cn/html/request.jsp"
				+ "?url=" + encryptURL + "&type=" + QUALITYID;
//		re_movie_title.setText("���ڼ�����Ƶ�����Ժ�...");
		re_movie_loading.setVisibility(View.VISIBLE);
		String addFavMoviePaht = HttpRequest.getInstance()
				.getURL_LIST_ADDFAVMOVIE() + HttpRequest.getInstance().getId();
		addMovieFav(addFavMoviePaht);
		if("1".equals(httpRequest.getType())){
		setVideoInfo(QUALITYID, webUrl);
		}
		System.out.println("videoWebUrlRequest�����ص�ַΪ"+videoWebUrlRequest);
			aQuery.ajax(videoWebUrlRequest, String.class,
					new AjaxCallback<String>() {
						@Override
						public void callback(String url, String object,
								AjaxStatus status) {
							re_movie_loading.setVisibility(View.GONE);
							if (object != null) {
								try {
									JSONObject jo = new JSONObject(object);
									String result = jo.getString("SUCCESS");
									if ("true".equals(result)) {
										String path = jo.getString("URL");
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										Uri uri = Uri.parse(path);
										System.out.println("���ŵĵ�ַΪ" + path);
										i.setDataAndType(uri, "video/*");
										startActivity(i);
									} else {
										Uri uri = Uri.parse(webUrl);
										Intent it = new Intent(
												Intent.ACTION_VIEW, uri);
									startActivity(it);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch
									// block
									e.printStackTrace();
								}
							}
							super.callback(url, object, status);
						}
					});
	}

	private void isVideoPathPlay(final String vidoepath, final String weburl) {

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (isVideoPlay) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.parse(vidoepath);
					i.setDataAndType(uri, "video/*");
					startActivity(i);
				} else {
					if ("".equals(weburl) && null == weburl) {
						ToastUtil.showToast(aQuery.getContext(), "����ʧ�ܣ����Ժ�����");
						isVideoPlay = false;
					} else {
						Uri uri = Uri.parse(weburl);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						aQuery.getContext().startActivity(it);
						isVideoPlay = false;
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				long length = HttpUtil.getInstance().getInputStreamLength(
						vidoepath);
				if (length > 5000) {
					isVideoPlay = true;
				}
				super.onPostExecute(result);
			}

		}.execute();
	}
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		 if(null!=UpdateApk.dialog){
//		 if(UpdateApk.dialog.isShowing()){
//			 UpdateApk.dialog.dismiss();
//		 }
//		 }
		 android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
	
}

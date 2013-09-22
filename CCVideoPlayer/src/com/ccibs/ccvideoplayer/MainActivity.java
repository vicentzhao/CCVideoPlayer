package com.ccibs.ccvideoplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import org.json.JSONArray;
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
import com.ccibs.ccvideoplayer.util.UpdateVersion;

/**
 * 加载主界面，根据不同的参数来判断加载不同的
 * 
 * @author CCDrive.ZhaoYiqun
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * ========================================================================
	 * ==== movie 的一些变量
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
	private int movieCount = -1; // 判断播放哪部格式
	private int typeCount = -1; // 判断播放哪个片源
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
	private boolean isVideoPlay = false; // 判断播放地址是否可用
	private ListView listView;
	private ArrayList<String> selectList;
	private String mac;
	/**
	 * ========================================================================
	 * ==================
	 */
	// edu的一些全局变量
	private TextView btn_edu_info_type;
	private TextView btn_edu_info_director;
	private TextView btn_edu_info_title;
	private TextView btn_edu_info_introduction;
	private GridView edu_grid_list;

	private ImageButton view_edu_collect;
	private String eduDetailPath = "http://apk.vocy.com/educationshop!information.action?token=myadmin&resultType=json&wid=137645341860600002";
	private String eduPlayList = "http://apk.vocy.com/educationshop!source.action?token=myadmin&resultType=json&wid=137645341860600002";
	private ArrayList<EduPlayBean> myEduNextList;
	private EduGridAdapter eduGridAdapter;
	private ArrayList<EduPlayBean> playPathList;
	private LinearLayout edu_lay_list;

	/**
	 * ========================================================================
	 * ================== book的一些全局变量
	 */
	// book的一些全局变量
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
	 * 电视剧的一些参数
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
	BookBean bean;
	/**
	 * ====================================================================
	 * 游戏的参数
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
		System.out.println("收到的type为" + type);
		String id = i.getStringExtra("id");
		System.out.println("收到的id为" + id);
		String mytoken = i.getStringExtra("mytoken");
		HttpRequest.getInstance().setType(type);
		String kind = getIntent().getStringExtra("kind");
		String web_root = getIntent().getStringExtra("webRoot");
		HttpRequest.getInstance().setWEB_ROOT(web_root);
		System.out.println("收的的kind" + kind);
		HttpRequest.getInstance().setCommicKind(kind);
		HttpRequest.getInstance().setMytoken(mytoken);
		HttpRequest.getInstance().setId(id);
		httpRequest = HttpRequest.getInstance();
		if ("1".equals(type)) {
			testPath = "http://html.vocy.com/html/moviesource/movie_" + id
					+ ".html";
			setMovieDetail(mytoken, id);
		}
		if ("26".equals(type) || "12".equals(type)) {
			setEduDetail(mytoken, id);
		}
		if ("20".equals(type) ||"25".equals(type) || "16".equals(type) || "19".equals(type)
				|| "4".equals(type) || "8".equals(type)|| "15".equals(type)|| "18".equals(type)) {
			setEduDetail(mytoken, id);
		}

		if ("22".equals(type)) {
			setTVDetail(mytoken, id);
		}
		if ("2".equals(type)) {
			setBookDetail(mytoken, id);
		}
		if ("23".equals(type) || "7".equals(type)) {
			setTVDetail(mytoken, id);
		}
		if ("6".equals(type) ||"9".equals(type) || "10".equals(type) || "24".equals(type)
				|| "5".equals(type) || "14".equals(type) || "13".equals(type)
				|| "21".equals(type) || "9".equals(type) || "11".equals(type)|| "9".equals(type)) {
			initGamesData(id);
		}
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
		view_tv_collect.setVisibility(View.INVISIBLE);
		final String path = HttpRequest.getInstance().getURL_TVDetail();
		System.out.println("图片下载的视频=====》》" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			private ArrayList<String> myNextList;

			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("下载的数据为" + object);
					TVBean tv = JSONUtil.getTV(object);
					btn_tv_info_title.setText(tv.getName());
					btn_tv_info_introduction.setText("【影片简介】： "
							+ tv.getSynopsis());
					btn_tv_info_type.setText(tv.getType());
					btn_tv_info_area.setText(tv.getArea());
					btn_tv_info_playingtimer.setText(tv.getNum());
					btn_tv_info_director.setText(tv.getDirector());
					btn_tv_info_actor.setText(tv.getProtagonist());
					System.out.println("图片的地址为" + tv.getPic());
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
							// layp.setMargins(0, 10, 0, 10);
							imageviewList[i].setLayoutParams(layp);
						}
						tv_lay_list.addView(imageviewList[i]);
						imageviewList[i]
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
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
									String tag = myNextList.get(position);
									int tvcount = Integer.parseInt(tag);
									HttpRequest.getInstance().setCount(tvcount);
									if (null != httpRequest.getCommicKind()
											&& !"".equals(httpRequest
													.getCommicKind())) {
										if (("动画").equals(HttpRequest
												.getInstance().getCommicKind())
												|| httpRequest.getCommicKind()
														.equals("视频")) {
											String path = HttpRequest
													.getInstance()
													.getURL_TVEpisode_Play();
											System.out.println("播放的地址为" + path);
											setPlay(path);
										} else if (HttpRequest.getInstance()
												.getCommicKind().equals("漫画")
												|| httpRequest.getCommicKind()
														.equals("图片")) {
											String url_TVEpisode_Play = HttpRequest
													.getInstance()
													.getURL_TVEpisode_Play();
											System.out.println("漫画下载的地址为"
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
																		.println("下载下来的tv======》"
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
																			.println("组图的地址为======="
																					+ j
																					+ "==="
																					+ list.get(j));
																}
																callCCBookPlayer(
																		position,
																		"com.ccdrive.photoviewer",
																		"1",
																		list);
															}
														}
													});
										}
									} else if (httpRequest.getType().equals(
											"22")) {
										String path = HttpRequest.getInstance()
												.getURL_TVEpisode_Play();
										System.out.println("电视剧播放的地址为" + path);
										setPlay(path);
									}
								}
							});
				} else {
					int code = status.getCode();
					System.out.println("下载下来的状态码" + code);
					Toast.makeText(MainActivity.this, "下载的数据为null，请检查网络", 1)
							.show();
				}

			}
		});

		view_tv_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String educollect = (String) view_tv_collect.getTag();
				if ("收藏".equals(educollect)) {
					// 收藏
					view_tv_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_tv_collect.setTag("已收藏");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVTV();
					System.out.println("要更新上的地址为" + addFavMoviePaht);
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
						view_tv_collect.setTag("收藏");
						view_tv_collect.setVisibility(View.VISIBLE);
						view_tv_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_tv_collect.setTag("已收藏");
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
		String path = HttpRequest.getInstance().getURL_BookDetail();
		System.out.println("图书下载的地址为===" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					BookBean bookBean = JSONUtil.getBookBean(object);
					btn_book_info_type.setText(bookBean.getType());
					btn_book_info_director.setText(bookBean.getAuthor());
					btn_book_info_introduction.setText(bookBean.getSynopsis());
					btn_book_info_number.setText(bookBean.getNum());
					btn_book_info_status.setText(bookBean.getState());
					btn_book_info_title.setText(bookBean.getName());
					aQuery.find(R.id.btn_book_info_logo).image(
							bookBean.getPic());
					setBookPage(bookBean);
				}
				super.callback(url, object, status);
			}

		});
		String collectPath = httpRequest.getURL_isCollect_FAVMOVIE();
		System.out.println("收藏判断===========》" + collectPath);
		view_book_collect = (ImageButton) findViewById(R.id.view_book_collect);
		aQuery.ajax(collectPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_book_collect.setTag("收藏");
						view_book_collect.setVisibility(View.VISIBLE);
						view_book_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_book_collect.setTag("已收藏");
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
	 * 加载图书的一些章节信息
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
					System.out.println("下载的图书的地址为" + path);
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
				Toast.makeText(MainActivity.this, myCount + "", 1).show();
				callCCBookPlayer(position, "com.ccbookreading", "1", null);
			}
		});

		view_book_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String educollect = (String) view_book_collect.getTag();
				if ("收藏".equals(educollect)) {
					// 收藏
					view_book_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_book_collect.setTag("已收藏");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVMOVIE();
					System.out.println("要更新上的地址为" + addFavMoviePaht);
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
		httpRequest.setPageSize(1);
		eduDetailPath = HttpRequest.getInstance().getURL_BookDetail();
		System.out.println("下载的地址为" + eduDetailPath);
		// 获取影片详情
		aQuery.ajax(eduDetailPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					EduBean eduBean = JSONUtil.getEduBean(object);
					btn_edu_info_director.setText(eduBean.getRose());
					btn_edu_info_type.setText(eduBean.getKind());
					btn_edu_info_title.setText(eduBean.getName());
					btn_edu_info_introduction.setText(("【简介】：    ")
							+ eduBean.getNote());
					aQuery.find(R.id.btn_edu_info_logo).image(eduBean.getPic());
					setEduPlayInfo(object);
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
				if ("收藏".equals(educollect)) {
					// 收藏
					view_edu_collect
							.setBackgroundResource(R.drawable.edu_uncollect_selector);
					view_edu_collect.setTag("已收藏");
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVMOVIE();
					System.out.println("要更新上的地址为" + addFavMoviePaht);
					addMovieFav(addFavMoviePaht);
				}
			}
		});
		String path = HttpRequest.getInstance().getURL_isCollect_FAVMOVIE();
		System.out.println("测试的id为000" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_edu_collect.setTag("收藏");
						view_edu_collect.setVisibility(View.VISIBLE);
						view_edu_collect
								.setBackgroundResource(R.drawable.edu_collect_selector);
					} else {
						view_edu_collect.setTag("已收藏");
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

	private void setEduPlayInfo(String info) {

		if (info != null) {
			System.out.println("下载的数据为" + info);
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
			if (playPathList.size() > 10) {
				myEduNextList = new ArrayList<EduPlayBean>();
				for (int i = 0; i < 10; i++) {
					myEduNextList.add(playPathList.get(i));
				}
				eduGridAdapter = new EduGridAdapter(MainActivity.this,
						myEduNextList);
			} else {
				myEduNextList = playPathList;
				eduGridAdapter = new EduGridAdapter(MainActivity.this,
						myEduNextList);
			}
			int count = playPathList.size() / 10;
			if (playPathList.size() % 10 != 0) {
				count = count + 1;
			}
			System.out.println("count的大小为" + count);
			final Button[] imageviewList = new Button[count];
			for (int i = 0; i < imageviewList.length; i++) {
				final int t = i;
				imageviewList[i] = new Button(MainActivity.this);
				imageviewList[i].setId(2000 * i);
				imageviewList[i].setText((10 * i + 1)
						+ "~"
						+ (10 * (i + 1) < (playPathList.size()) ? 10 * (i + 1)
								: playPathList.size()));
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
						myEduNextList = new ArrayList<EduPlayBean>();

						for (int j = (10 * t + 1); j <= (10 * (t + 1) > playPathList
								.size() ? playPathList.size() : 10 * (t + 1)); j++) {
							myEduNextList.add(playPathList.get(j - 1));
						}
						eduGridAdapter.setArrayList(myEduNextList);
						eduGridAdapter.notifyDataSetChanged();

					}
				});
			}
			edu_grid_list.setAdapter(eduGridAdapter);
			edu_grid_list.setSelector(new ColorDrawable(0));
			edu_grid_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String playID = myEduNextList.get(position).getId();
					httpRequest.setSid(playID);
					// String playPaht = HttpRequest.getInstance()
					// .getURL_eduPlaySinglePath()
					// + playID;
					String playPaht = httpRequest.getURL_EDUEpisode_Play();
					System.out.println("播放的详细地址为" + playPaht);
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
											System.out.println("播放的url的地址为========"+playURL);
											if (null != playURL
													&&!"".equals(playURL)) {
												getVideoPlayPath(playURL, "1");
											} else {
												String playSinglePath = eduPlayBeanList
														.get(0).getFilePath()
														.trim();
												Intent i = new Intent(
														Intent.ACTION_VIEW);
												Uri uri = Uri
														.parse(playSinglePath);
												System.out.println("播放的地址为"
														+ playSinglePath);
												i.setDataAndType(uri, "video/*");
												startActivity(i);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										Toast.makeText(aQuery.getContext(),
												"没有相应的视频，请选择其他视频", 1).show();

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
		setMovieSoruce();
		init();
		currentTimeMillis = System.currentTimeMillis();
		setDataPage();
		// handler = new Handler(){
		// @Override
		// public void handleMessage(Message msg) {
		// switch (msg.what) {
		// case beginConnectData:
		// break;
		// default:
		// break;
		// }
		// super.handleMessage(msg);
		// }
		// };
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
		String s1 = "国语";
		String s2 = "粤语";
		String s3 = "美语";
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
		System.out.println("测试的id为000" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_movie_collect.setTag("已收藏");
						view_movie_collect.setVisibility(View.VISIBLE);
						view_movie_collect
								.setBackgroundResource(R.drawable.movie_collect_selector);
					} else {
						view_movie_collect.setTag("收藏");
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
			if ("乐视".equals(movieFromList.get(j))) {
				getWebLogo("乐视", 0, imageviewList[j]);
				imageviewList[j].setTag("乐视");
			}
			if ("爱奇艺".equals(movieFromList.get(j))) {
				getWebLogo("爱奇艺", 1, imageviewList[j]);
				imageviewList[j].setTag("爱奇艺");
			}
			if ("迅雷".equals(movieFromList.get(j))) {
				getWebLogo("迅雷", 2, imageviewList[j]);
				imageviewList[j].setTag("迅雷");
			}
			if ("腾讯".equals(movieFromList.get(j))) {
				getWebLogo("腾讯", 3, imageviewList[j]);
				imageviewList[j].setTag("腾讯");
			}
			if ("M1905电影网".equals(movieFromList.get(j))) {
				getWebLogo("M1905电影网", 4, imageviewList[j]);
				imageviewList[j].setTag("M1905电影网");
			}
			if ("时光网".equals(movieFromList.get(j))) {
				getWebLogo("时光网", 5, imageviewList[j]);
				imageviewList[j].setTag("时光网");
			}
			if ("新浪".equals(movieFromList.get(j))) {
				getWebLogo("新浪", 6, imageviewList[j]);
				imageviewList[j].setTag("新浪");
			}
			if ("搜狐".equals(movieFromList.get(j))) {
				getWebLogo("搜狐", 7, imageviewList[j]);
				imageviewList[j].setTag("搜狐");
			}
			if ("ppTV聚力".equals(movieFromList.get(j))) {
				getWebLogo("ppTV聚力", 8, imageviewList[j]);
				imageviewList[j].setTag("ppTV聚力");
			}
			if ("优酷".equals(movieFromList.get(j))) {
				getWebLogo("优酷", 9, imageviewList[j]);
				imageviewList[j].setTag("优酷");
			}
			if ("土豆".equals(movieFromList.get(j))) {
				getWebLogo("土豆", 10, imageviewList[j]);
				imageviewList[j].setTag("土豆");
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
							System.out.println("v的焦点为" + v.getId());
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

	private void setPaySource(MovieBean movie, int position) {
		typeCount = position;
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
	 * 获取视频长度，来判断视频是否可以播放
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
		return super.onKeyDown(keyCode, event);
	}

	private ArrayList<VideoPlayInfo> getVideoPlayInfoList(MovieBean movie2,
			int position) {
		ArrayList<String> movieFromList = movie.getMovieFromList();
		if (movieFromList == null || movieFromList.size() == 0) {
			return null;
		}
		String fromSource = movieFromList.get(position);
		HashMap<String, ArrayList<VideoPlayInfo>> moviePlayMap = movie
				.getMoviePlayMap();
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
			// getPackageName()是你当前类的包名，0代表是获取版本信息
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
					System.out.println("更新的地址为" + apkStr);
					if (apkStr != null && apkStr.length() != 0) {
						// TODO Auto-generated method stub
						super.callback(url, apkStr, status);
						// 动漫[TAB]com.ccdrive.comic[TAB]1.1.2.9[TAB]137048843775650001[CR]
						/**
						 * apkEntity[0]=动漫 apkEntity[1]=com.ccdrive.comic
						 * apkEntity[2]=1.1.2.9 apkEntity[3]=137048843775650001
						 */
						String[] apkEntity = apkStr.split("\\[TAB\\]|\\[CR\\]");
						if (!version.equals(apkEntity[2])) {
							HttpRequest.getInstance().setApkuuid(apkEntity[3]);
							String path = HttpRequest.getInstance()
									.getURL_DOWN_UPDATE_APK();
							setUpdateDiago(path, apkEntity[1]);
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
		Dialog dialog = new AlertDialog.Builder(aQuery.getContext())
				.setTitle("发现新版本")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								UpdateVersion uv = UpdateVersion.instance(
										aQuery.getContext(), hd, true, false,
										re_movie_loading);
								re_movie_title.post(new Runnable() {

									@Override
									public void run() {
										re_movie_title.setText("正在安装应用，请稍后...");
									}
								});
								uv.setUpdateUrl(path);
								uv.setLoadApkName(apkName);
								uv.run();
								return null;
							}
						}.execute();
					}
				})
				.setNegativeButton(getResources().getString(R.string.cancle),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
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
					btn_movie_info_director.setText(movie.getDirector());
					btn_movie_info_releasetime.setText(movie.getAddtime());
					btn_movie_info_title.setText(movie.getName());
					btn_movie_info_introduction.setText(movie.getOrotagonist());
					// aQuery.find(R.id.btn_movie_info_logo).image(R.drawable.menu_bg);
					// aQuery.find(R.id.btn_movie_info_logo).image(movie.getPic());
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
		System.out.println("收藏的地址为" + path);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object.contains("true")) {
					Toast.makeText(MainActivity.this, "操作成功", 1);
				} else {
					Toast.makeText(MainActivity.this, "操作失败", 1);
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
			if ("已收藏".equals(collect)) {
				// 收藏
				view_movie_collect
						.setBackgroundResource(R.drawable.movie_uncollect_selector);
				view_movie_collect.setTag("收藏");
				String addFavMoviePaht = HttpRequest.getInstance()
						.getURL_LIST_ADDFAVMOVIE();
				addMovieFav(addFavMoviePaht);
			} else {
				// view_movie_collect.setBackgroundResource(R.drawable.movie_collect_selector);
				// view_movie_collect.setTag("已收藏");
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
					Uri uri = Uri.parse(webUrl);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					aQuery.getContext().startActivity(it);
					String addFavMoviePaht = HttpRequest.getInstance()
							.getURL_LIST_ADDFAVMOVIE()
							+ HttpRequest.getInstance().getId();
					System.out.println("添加的地址为" + addFavMoviePaht);
					addMovieFav(addFavMoviePaht);
					setVideoInfo("", webUrl);
					return;
				} else {
					String type = movie.getMovieFromList().get(typeCount);
					final String playPath = movie.getMoviePlayMap().get(type)
							.get(movieCount).getPlayPath();
					final String webUrl = movie.getMoviePlayMap().get(type)
							.get(movieCount).getWebUrl();
					String quality = movie.getMoviePlayMap().get(type)
							.get(movieCount).getQuality();
					getVideoPlayPath(webUrl, quality);
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
			if ("已收藏".equals(gameCollect)) {
				// 收藏
				view_game_collect
						.setBackgroundResource(R.drawable.movie_uncollect_selector);
				view_game_collect.setTag("收藏");
				String addFavMoviePaht = HttpRequest.getInstance()
						.getURL_LIST_ADDFAVTV();
				System.out.println("添加游戏收藏的地址为" + addFavMoviePaht);
				addMovieFav(addFavMoviePaht);
				System.out.println("收藏返回" + addFavMoviePaht);
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
			// System.out.println("添加的地址为" + addFavMoviePaht);
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
			Toast.makeText(MainActivity.this, "上传webservice错误",
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
						if (null != PlayAllList.get(0).getVideoPath()
								&& !PlayAllList.get(0).getVideoPath()
										.equals("")) {
							String path = PlayAllList.get(0).getVideoPath();
							Intent i = new Intent(Intent.ACTION_VIEW);
							Uri uri = Uri.parse(path);
							System.out.println("播放的地址为" + path);
							i.setDataAndType(uri, "video/*");
							startActivity(i);
						} else {
							String webUrl = PlayAllList.get(0).getHtmlPath();
							Uri uri = Uri.parse(webUrl);
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(it);
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
		Dialog builder = new Dialog(MainActivity.this);
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
				System.out.println("电视播放的位置==" + position + "tv的大小为"
						+ tvsourceList.size());
				PlayPathBean playPathBean = tvsourceList.get(position);
				if (null != playPathBean.getVideoPath()
						&& !playPathBean.getVideoPath().equals("")) {
					String path = playPathBean.getVideoPath();
					Intent i = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.parse(path);
					System.out.println("播放的地址为" + path);
					i.setDataAndType(uri, "video/*");
					startActivity(i);
				} else {
					String webUrl = playPathBean.getHtmlPath();
					Uri uri = Uri.parse(webUrl);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}

			}
		});

	}

	/**
	 * 调用其他apk
	 */
	private void callCCBookPlayer(int count, final String packname,
			final String downloadpath, ArrayList<String> photoList) {
		AppUtil au = new AppUtil(aQuery.getContext());
		if (!au.isInstall(packname)) {
			Dialog dialog = new AlertDialog.Builder(aQuery.getContext())
					.setTitle(
							aQuery.getContext().getResources()
									.getString(R.string.pmt))
					.setMessage("需要下载PCTOO播放器,是否下载?")
					.setPositiveButton(
							aQuery.getContext().getResources()
									.getString(R.string.confirm),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final Handler handler = new Handler();
									new AsyncTask<Void, Void, Void>() {
										@Override
										protected Void doInBackground(
												Void... params) {
											UpdateVersion uv = UpdateVersion.instance(
													aQuery.getContext(),
													handler, true, false, null);
											uv.setLoadApkName(packname);
											String downPath = HttpRequest
													.getInstance()
													.getURL_QUERY_DOWNLOAD_URL()
													+ downloadpath;
											uv.setUpdateUrl(downPath);
											uv.run();
											return null;
										}
									}.execute();
								}
							})
					.setNegativeButton(
							aQuery.getContext().getResources()
									.getString(R.string.cancle),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							}).create();
			dialog.show();
		} else {
			Intent intent = new Intent();
			intent.putExtra("id", HttpRequest.getInstance().getId());
			intent.putExtra("position", count);
			intent.putExtra("countPage", HttpRequest.getInstance().getCount());
			intent.putExtra("type", HttpRequest.getInstance().getType());
			intent.putExtra("mytoken", HttpRequest.getInstance().getMytoken());
			intent.putExtra("photopath", photoList);
			intent.setClassName(packname, packname + ".MainActivity");
			startActivity(intent);
		}
	}

	/**
	 * 初始化游戏的一些数据
	 */
	private void initGamesData(String id) {
		setContentView(R.layout.gamesdetail);
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
		tv_totalrows = (TextView) findViewById(R.id.tv_totalrows); // 总数
		tv_currentpage = (TextView) findViewById(R.id.tv_currentpage); // 当前页
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
				System.out.println("gamebena是否为null"
						+ gameBean.getGameBeanList().get(position).getId());
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
				/*
				 * if (itemView != null) {
				 * itemView.startAnimation(AnimationTools.getMaxAnimation(100));
				 * } if (preView != null) {
				 * preView.startAnimation(AnimationTools.getMiniAnimation(100));
				 * } preView = itemView;
				 */
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
		System.out.println("游戏判断收藏的地址======" + path);
		System.out.println("测试的收藏id为------>" + path);
		view_game_collect = (Button) findViewById(R.id.view_game_collect);
		view_game_collect.setOnClickListener(this);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					if (object.contains("false")) {
						view_game_collect.setTag("已收藏");
						view_game_collect.setVisibility(View.VISIBLE);
						view_game_collect
								.setBackgroundResource(R.drawable.movie_collect_selector);
					} else {
						view_game_collect.setTag("收藏");
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
					Toast.makeText(aQuery.getContext(), "加载错误", 0).show();
					break;
				default:
					break;
				}
			}
		};
		getGameInfo();
	}

	/**
	 * 获取游戏的一些相关信息
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
					System.out.println("下载的游戏的地址为"
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
					System.out.println("下载下来的数据为" + result);
					gameBean = JSONUtil.getGameBeanInfo(result);
					re_movie_loading.setVisibility(View.INVISIBLE);
					layoutview.setBackgroundResource(R.drawable.people_main_bg);
					line_movie_detail.setVisibility(View.VISIBLE);
					// imageLoader = new ImageAsyncLoader(context);
					String url = gameBean.getPic();
					System.out.println("pic地址" + url);
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
	 * 设置游戏相关视频的信息
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
			tv_totalrows.setText("( 检索结果 : 共 " + totalRows + "相关作品)");
			tv_currentpage.setText("共 " + totalPage + " 页   " + "当前第 "
					+ currentPage + " 页");

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
	 * 设置游戏翻页信息
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
	 * 设置游戏翻页信息
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
		String encryptURL = CryptUtil.getInstance().encryptURL(webUrl);
		
		String videoWebUrlRequest = "http://apk.vocy.com/html/request.jsp"
				+ "?url=" + encryptURL + "&type=" + QUALITYID;
//		re_movie_title.setText("正在加载视频，请稍后...");
		re_movie_loading.setVisibility(View.VISIBLE);
		String addFavMoviePaht = HttpRequest.getInstance()
				.getURL_LIST_ADDFAVMOVIE() + HttpRequest.getInstance().getId();
		System.out.println("要获得的地址为======" + videoWebUrlRequest);
		addMovieFav(addFavMoviePaht);
		if("1".equals(httpRequest.getType())){
		setVideoInfo(QUALITYID, webUrl);
		}
			aQuery.ajax(videoWebUrlRequest, String.class,
					new AjaxCallback<String>() {
						@Override
						public void callback(String url, String object,
								AjaxStatus status) {
							re_movie_loading.setVisibility(View.GONE);
							if (url != null) {
								try {
									JSONObject jo = new JSONObject(object);
									String result = jo.getString("SUCCESS");
									if ("true".equals(result)) {
										String path = jo.getString("URL");
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										Uri uri = Uri.parse(path);
										System.out.println("播放的地址为" + path);
										i.setDataAndType(uri, "video/*");
										startActivity(i);
									} else {
										Uri uri = Uri.parse(webUrl);
										Intent it = new Intent(
												Intent.ACTION_VIEW, uri);
										aQuery.getContext().startActivity(it);
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
						ToastUtil.showToast(aQuery.getContext(), "播放失败，请稍后重试");
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
}

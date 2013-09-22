package com.ccibs.ccvideoplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.adapter.GamesGridAdapter;
import com.ccibs.ccvideoplayer.app.App;
import com.ccibs.ccvideoplayer.bean.GameBean;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.HttpUtil;
import com.ccibs.ccvideoplayer.util.JSONUtil;
import com.ccibs.ccvideoplayer.util.ToastUtil;

public class GameActivity extends Activity implements OnClickListener {
	
	
	private static final int MSG_LOAD_MOVIE = 1;
	private static final int MSG_LOAD_ERR = 4;
	/*================================================*/
	private GridView game_gridview, edu_gridview;
	private Button btn_game_pageup;
	private Button btn_game_pagedown;
	private Button view_game_collect;
	
	private TextView tv_currentpage;
	private TextView tv_totalrows;
	private RelativeLayout layoutview;
	private LinearLayout line_movie_detail;
	private RelativeLayout re_movie_loading;
	private GridView allgridview;
	/*================================================*/
	private AQuery aQuery;
	private GamesGridAdapter gamesGridAdapter;
	private HttpRequest httpRequest;
	private Handler handler;
	private Runnable changDatas = new Runnable() {
		@Override
		public void run() {
			GameActivity.this.handler.sendEmptyMessage(MSG_LOAD_MOVIE);
		}
	};
	/*================================================*/
	private int currentPage = 1;
	private int totalPage;
	private int totalRows;
	private boolean pageFlag = true;
	private String content;
	private String id;
	/**
	 * 加载游戏详细界面
	 * @author CCDrive.ZhaoYiqun
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gamesdetail);
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
	}

	private void initGamesData() {
		layoutview = (RelativeLayout) findViewById(R.id.line_movie_main);
		layoutview.setBackgroundResource(R.drawable.menu_bg);
		line_movie_detail = (LinearLayout) findViewById(R.id.ll_line_movie_main);
		line_movie_detail.setVisibility(View.INVISIBLE);
		re_movie_loading = (RelativeLayout) findViewById(R.id.re_movie_loading);
		ImageView frame_image05 = (ImageView) findViewById(R.id.frame_image05);
		frame_image05.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.logoanmi));
		re_movie_loading.setVisibility(View.VISIBLE);
		btn_game_pageup = (Button) findViewById(R.id.btn_game_pageup);
		btn_game_pagedown = (Button) findViewById(R.id.btn_game_pagedown);
		tv_totalrows = (TextView) findViewById(R.id.tv_totalrows);  //总数
		tv_currentpage = (TextView) findViewById(R.id.tv_currentpage);  //当前页
		game_gridview = (GridView) findViewById(R.id.game_gridview); 
		btn_game_pagedown.setOnClickListener(this);
		btn_game_pageup.setOnClickListener(this);
		game_gridview.setVisibility(View.VISIBLE);
		game_gridview.requestFocus();
		game_gridview.setFocusable(true);
		game_gridview.setSelector(new ColorDrawable(0));
		gamesGridAdapter = new GamesGridAdapter(this, null);
		game_gridview.setAdapter(gamesGridAdapter);
		httpRequest.setPageSize(8);

		btn_game_pageup.setOnClickListener(this);
		btn_game_pagedown.setOnClickListener(this);
		game_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				

			}
		});
		allgridview.setOnItemSelectedListener(new OnItemSelectedListener() {
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
		aQuery = new AQuery(GameActivity.this);
		httpRequest = HttpRequest.getInstance();
		String path = HttpRequest.getInstance().getURL_isCollect_FAVMOVIE();
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

//				case MSG_LOAD_MOVIEINFO:
//					getOpus();
//					break;
				case MSG_LOAD_MOVIE:
//					initData();
					getInfo();
					break;
				case MSG_LOAD_ERR:
					Toast.makeText(GameActivity.this, "加载错误", 0)
							.show();
					break;
				default:
					break;
				}
			}
		};
		getInfo();
	}
	private void setGridViewInfo(GameBean gameBean) {
		ArrayList<GameBean> gameBeanList = gameBean.getGameBeanList();
		if ((null != gameBeanList) && (null != gameBeanList)&& (!"null".equals(gameBeanList)&&(gameBeanList.size()!=0))
				) {
			if (pageFlag) {
				//TODO
				currentPage = Integer.valueOf(gameBean.getPageBean().getCurrentPage());
				totalPage = Integer.valueOf(gameBean.getPageBean().getTotalPage());
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

	private void getInfo() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				int i = 0;
				while (true) {
					i++;
					content = HttpUtil.getResponseString(httpRequest.getURL_BookDetail() );
					System.out.println("下载的游戏的地址为"+httpRequest.getURL_BookDetail());
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
					System.out.println("下载下来的数据为"+result);
					 GameBean gameBean = JSONUtil.getGameBeanInfo(result);
					re_movie_loading.setVisibility(View.INVISIBLE);
					layoutview.setBackgroundResource(R.drawable.people_main_bg);
					line_movie_detail.setVisibility(View.VISIBLE);
					// imageLoader = new ImageAsyncLoader(context);
					String url = gameBean.getPic();
					System.out.println("pic地址" + url);
					aQuery.find(R.id.btn_game_info_type).text(gameBean.getType());
				    aQuery.find(R.id.btn_game_info_labl).text(gameBean.getLabel());
					aQuery.find(R.id.btn_game_info_logo).image(url);
//					aQuery.find(R.id.tv_game_intro).text(getResources().getString(R.string.movie_intro)+gameBean.getSynopsis());
					setGridViewInfo(gameBean);

				} else {
					re_movie_loading.setVisibility(View.INVISIBLE);
					finish();
				}
				super.onPostExecute(result);
			}
		}.execute();

	}

	private void pageDown() {
		if (this.currentPage >= this.totalPage) {
			btn_game_pagedown.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(GameActivity.this, R.string.lastPage);
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
	private void pageUp() {
		if (this.currentPage <= 1) {
			btn_game_pageup.setVisibility(View.INVISIBLE);
			ToastUtil.showToastbyId(GameActivity.this, R.string.firstPage);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_game_pageup:
			pageUp();
			break;
		case R.id.btn_game_pagedown:
			pageDown();
			break;
		case R.id.view_game_collect:
			String collect = (String) view_game_collect.getTag();
			if ("已收藏".equals(collect)) {
				// 收藏
				view_game_collect.setBackgroundResource(R.drawable.movie_uncollect_selector);
				view_game_collect.setTag("收藏");
				String addFavMoviePaht=HttpRequest.getInstance().getURL_LIST_ADDFAVTV();
				addMovieFav(addFavMoviePaht);
				System.out.println("收藏返回" + addFavMoviePaht);
			}
			break;
		}
	}

	private void addMovieFav(String path) {
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("收藏返回的数据为" + object);
				}
				super.callback(url, object, status);
			}

		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (httpRequest.getType().equals("0")) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (edu_gridview.getSelectedItemId() >= 10) {
					pageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (edu_gridview.getSelectedItemId() <= 1) {
					pageUp();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (edu_gridview.getSelectedItemId() >= 11) {
					pageDown();
				}

			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (edu_gridview.getSelectedItemId() == 0) {
					pageUp();
				}
			}

		} else {

			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if (game_gridview.getSelectedItemId() >= 4) {
					pageDown();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				if (game_gridview.getSelectedItemId() <= 3) {
					pageUp();
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (game_gridview.getSelectedItemId() >= 7) {
					pageDown();
				} else if (game_gridview.getSelectedItemId() == 3) {
					if (game_gridview.getSelectedItemId() > 3) {
						game_gridview.setSelection(4);
					} else {
						game_gridview.setSelection(3);
					}
				}

			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (game_gridview.getSelectedItemId() == 0) {
					pageUp();

				} else if (game_gridview.getSelectedItemId() == 4) {
					game_gridview.setSelection(3);
				}
			}

		}
		return super.onKeyDown(keyCode, event);
	}


	public static void ProDiaglogDimiss(ProgressDialog dialog) {
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// Perform action on key press
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}

//	private void collectApp() {
//		LoginUtil loginUtil = new LoginUtil(GameActivity.this);
//		String mac = loginUtil.getWifiMacAddress(GameActivity.this)
//				.toUpperCase();
//		SoapUtils soapUtils = SoapUtils.getInstance();
//		HashMap<String, Object> appMap = new HashMap<String, Object>();
//		appMap.put("mac", mac);
//		appMap.put("orderId", id);
//		soapUtils.log("PeopleCollect", appMap);
//		long peopleid = Long.parseLong(id);
//		soapUtils.getPeopleClick(peopleid);
//	}


	public String toText(String text) {
		char[] c = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
}

package com.ccibs.ccvideoplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccibs.ccvideoplayer.adapter.EduGridAdapter;
import com.ccibs.ccvideoplayer.bean.EduBean;
import com.ccibs.ccvideoplayer.bean.EduPlayBean;
import com.ccibs.ccvideoplayer.util.HttpRequest;
import com.ccibs.ccvideoplayer.util.JSONUtil;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

public class EducationActivity extends Activity {

	private AQuery aQuery;
	private String eduDetailPath = "http://apk.vocy.com/educationshop!information.action?token=myadmin&resultType=json&wid=137645341860600002";
	private String eduPlayList = "http://apk.vocy.com/educationshop!source.action?token=myadmin&resultType=json&wid=137645341860600002";
	private TextView btn_edu_info_type;
	private TextView btn_edu_info_director;
	private TextView btn_edu_info_title;
	private TextView btn_edu_info_introduction;
	private ArrayList<EduPlayBean> myNextList;
	private EduGridAdapter eduGridAdapter;
	private ArrayList<EduPlayBean> playPathList;
	private LinearLayout edu_lay_list;
	private GridView edu_grid_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.edudetails);
		btn_edu_info_type = (TextView) findViewById(R.id.btn_edu_info_type);
		btn_edu_info_director = (TextView) findViewById(R.id.btn_edu_info_director);
		btn_edu_info_title = (TextView) findViewById(R.id.btn_edu_info_title);
		btn_edu_info_introduction = (TextView) findViewById(R.id.btn_edu_info_introduction);
		edu_lay_list = (LinearLayout) findViewById(R.id.edu_lay_list);
		edu_grid_list = (GridView) findViewById(R.id.edu_grid_list);
		aQuery = new AQuery(EducationActivity.this);
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
				}
				super.callback(url, object, status);

			}
		});

		// 获取影视列表
		aQuery.ajax(eduPlayList, String.class, new AjaxCallback<String>() {

			@Override
			public void callback(String url, String object, AjaxStatus status) {
				if (object != null) {
					System.out.println("下载的数据为" + object);
//					playPathList = JSONUtil.getEduPlayBeanList(object);
					if (playPathList.size() > 10) {
						myNextList = new ArrayList<EduPlayBean>();
						for (int i = 0; i < 10; i++) {
							myNextList.add(playPathList.get(i));
						}
						eduGridAdapter = new EduGridAdapter(
								EducationActivity.this, myNextList);
					} else {
						myNextList = playPathList;
						eduGridAdapter = new EduGridAdapter(
								EducationActivity.this, myNextList);
					}
					int count = playPathList.size() / 10;
					if (playPathList.size() % 10 != 0) {
						count = count + 1;
					}
					System.out.println("count的大小为" + count);
					final Button[] imageviewList = new Button[count];
					for (int i = 0; i < imageviewList.length; i++) {
						final int t = i;
						imageviewList[i] = new Button(EducationActivity.this);
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
						imageviewList[i]
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										myNextList = new ArrayList<EduPlayBean>();

										for (int j = (10 * t + 1); j <= (10 * (t + 1) > playPathList
												.size() ? playPathList.size()
												: 10 * (t + 1)); j++) {
											myNextList.add(playPathList
													.get(j - 1));
										}
										eduGridAdapter.setArrayList(myNextList);
										eduGridAdapter.notifyDataSetChanged();

									}
								});
					}
					edu_grid_list.setAdapter(eduGridAdapter);
					edu_grid_list.setSelector(new ColorDrawable(0));
					edu_grid_list
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									String path = myNextList.get(position)
											.getFilePath();
									Intent i = new Intent(Intent.ACTION_VIEW);
									Uri uri = Uri.parse(path);
									System.out.println("播放的地址为" + path);
									i.setDataAndType(uri, "video/*");
									startActivity(i);
								}
							});

				}
			}
		});
	}
	private void initView() {
		
 	}


}

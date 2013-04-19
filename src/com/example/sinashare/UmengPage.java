package com.example.sinashare;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adapter.UmengPagePagerAdapter;
import com.example.service.PagerCirculateService;
import com.umeng.analytics.MobclickAgent;

public class UmengPage extends Activity {
	private Button  backButton;
	private ViewPager pager;
	private View pagerItem;
	private PagerCirculateService server;
	int pic[] = {R.drawable.umeng_pager_pic_1,R.drawable.umeng_pager_pic_2,
			R.drawable.umeng_pager_pic_3,R.drawable.umeng_pager_pic_4,
			R.drawable.umeng_pager_pic_5,R.drawable.umeng_pager_pic_6};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_umeng_page);
		
		backButton = (Button)findViewById(R.id.back_button);
		pager = (ViewPager)findViewById(R.id.view_pager);
		server = new PagerCirculateService(pager, pic);
		final int point[] = {R.id.point0,R.id.point1,R.id.point2
				,R.id.point3,R.id.point4,R.id.point5};
		
		UmengPagePagerAdapter pagerAdapter = new UmengPagePagerAdapter(pic,this);
		pager.setAdapter(pagerAdapter);
		addButtonBackListenter();		
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				for(int i = 0; i < point.length;i++){
					if(arg0 == i){
						((ImageView)findViewById(point[i])).setImageResource(R.drawable.purple_point);
					}else{
						((ImageView)findViewById(point[i])).setImageResource(R.drawable.blue_point);
					}
				}				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {				
			}
		});
	}
	
	public void addButtonBackListenter(){
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	public void onResume() {
	    super.onResume();
	    server.startService();
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    server.stopService();
	    MobclickAgent.onPause(this);
	}
}

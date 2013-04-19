package com.example.sinashare;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class SinaHomePage extends Activity {
	private Button   backButton;
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_home_page);
		
		backButton = (Button)findViewById(R.id.back_button);
		web = (WebView)findViewById(R.id.web);
		
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl("http://open.weibo.com/");
		addButtonBackListenter();
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
	    MobclickAgent.onResume(this);
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}
}

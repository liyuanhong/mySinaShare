package com.example.sinashare;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private TextView button1;
	private TextView button2;
	private TextView button3;
	private TextView button4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();	
	}

	public void initView(){
		button1 = (TextView)findViewById(R.id.button1);
		button2 = (TextView)findViewById(R.id.button2);
		button3 = (TextView)findViewById(R.id.button3);
		button4 = (TextView)findViewById(R.id.button4);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);	
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == button1.getId()){
			Intent intent1 = new Intent();
			intent1.setClass(this, SinaSharePage.class);
			startActivity(intent1);
		}else if(v.getId() == button2.getId()){
			Intent intent2 = new Intent();
			intent2.setClass(this, SinaHomePage.class);
			startActivity(intent2);
		}else if(v.getId() == button3.getId()){
			Intent intent3 = new Intent();
			intent3.setClass(this, UmengPage.class);
			startActivity(intent3);
		}else if(v.getId() == button4.getId()){
			Intent intent4 = new Intent();
			intent4.setClass(this, UmengHomePage.class);
			startActivity(intent4);
		}		
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

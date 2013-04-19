package com.example.sinashare;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.util.SinaShare;
import com.umeng.analytics.MobclickAgent;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class AccontSeting extends Activity {
	private TextView button1;
	private Button   backButton;
	private SinaShare sinaShare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accont_seting);
		
		button1 = (TextView)findViewById(R.id.button1);
		backButton = (Button)findViewById(R.id.back_button);
		sinaShare = new SinaShare(this);
		
		addButtonListener1();
		addButtonBackListenter();
	}

	public void addButtonListener1(){
		button1.setText(sinaShare.isBundling() ? "解除绑定新浪微博" : "绑定新浪微博");
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sinaShare.isBundling()) {
					// 解除绑定
					sinaShare.unbundling();
					button1.setText("绑定新浪微博");
				} else {
					// 绑定
					sinaShare.bundling();
				}
			}
		});
		sinaShare.setUnBundlingListen(new SinaShare.BundlingListen() {

			@Override
			public void success() {
				new AlertDialog.Builder(AccontSeting.this)
						.setMessage("登陆成功！").setPositiveButton("确定", null)
						.show();

				button1.setText("解除绑定新浪微博");
			}

			@Override
			public void exction(WeiboException e) {
			}

			@Override
			public void error(WeiboDialogError e) {
			}

			@Override
			public void cancel() {
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
	
	
	public void addBindSinaListener() {
		
		
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

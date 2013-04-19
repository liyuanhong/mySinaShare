package com.example.util;

import com.weibo.sdk.android.Weibo;

public class ShareContent {
	private Weibo mWeibo;
	public static final String CONSUMER_KEY = "634196738";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://open.weibo.com/";

	public ShareContent() {
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
	}
}

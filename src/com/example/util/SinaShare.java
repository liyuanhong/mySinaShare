package com.example.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sinashare.R;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

@SuppressLint("HandlerLeak")
public class SinaShare {

	private Context mContext;

	public Oauth2AccessToken accessToken;
	private Weibo mWeibo;
	public static final String CONSUMER_KEY = "634196738";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://open.weibo.com/";
	private String title;
	private String content;
	public String imagePath;
	ProgressDialog dialog;
	AlertDialog builder;

	public SinaShare(Context mContext) {
		this.mContext = mContext;
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
	}

	public void shareText(final String title, String content,
			final String imagePath) {
		this.title = title;
		this.content = content;
		this.imagePath = imagePath;
		if (!AccessTokenKeeper.readAccessToken(mContext).isSessionValid()) {
			mWeibo.authorize(mContext, new AuthDialogListener() {
				@Override
				public void onComplete(Bundle values) {
					super.onComplete(values);
					shareText(title, "aaaa", imagePath);
				}
			});
		} else {
			WindowManager manage = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = manage.getDefaultDisplay();
			int screenHeight = display.getHeight();
			int screenWidth = display.getWidth();

			Log.d("-----", "=======================" + screenWidth);
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View view = inflater.inflate(R.layout.dialog_mdpi, null);
			ImageView imageTop = (ImageView) view.findViewById(R.id.image_view);
			EditText textTop = (EditText) view.findViewById(R.id.title_view);
			final EditText textCenter = (EditText) view
					.findViewById(R.id.center_view);
			Button buttonBottem = (Button) view
					.findViewById(R.id.botton_button);

			textTop.setText(title);
			textCenter.setText(content);

			Drawable drawable = null;
			drawable = new BitmapDrawable(imagePath);

			imageTop.setImageDrawable(drawable);

			builder = new AlertDialog.Builder(mContext).setView(view).show();
			buttonBottem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String content = textCenter.getEditableText().toString();
					if (content != null && content.length() > 0) {
						builder.dismiss();
						builder = null;
						shareContent(title + ":" + content);
					}
				}
			});
		}
	}

	public void shareContent(final String con) {
		dialog = ProgressDialog.show(mContext, "", "正在分享...", true);
		StatusesAPI api = new StatusesAPI(
				AccessTokenKeeper.readAccessToken(mContext));
		api.upload(con, imagePath, "90", "90", new RequestListener() {
			@Override
			public void onIOException(IOException arg0) {
			}

			@Override
			public void onError(WeiboException arg0) {
				dialog.dismiss();
				handler.sendMessage(handler.obtainMessage(5, arg0));
			}

			@Override
			public void onComplete(String arg0) {
				dialog.dismiss();
				handler.sendEmptyMessage(4);
			}
		});
	}

	class AuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (AccessTokenKeeper.readAccessToken(mContext).getToken() == "") {

			}
			AccessTokenKeeper.keepAccessToken(mContext, accessToken);
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(mContext, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(mContext, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 4:
				dialog.dismiss();
				new AlertDialog.Builder(mContext).setMessage("微博分享成功！")
						.setPositiveButton("确定", null).show();
				break;
			case 5:
				dialog.dismiss();
				new AlertDialog.Builder(mContext).setMessage("微博分享失败！")
						.setPositiveButton("确定", null).show();
				break;
			default:
				break;
			}

		}
	};

	// 绑定
	public void bundling() {

		mWeibo.authorize(mContext, new SinaAuthDialogListener());

	}

	// 解绑
	public void unbundling() {
		AccessTokenKeeper.clear(mContext);
		new AlertDialog.Builder(mContext).setMessage("登出成功！")
				.setPositiveButton("确定", null).show();
	}

	public boolean isBundling() {
		if ((AccessTokenKeeper.readAccessToken(mContext).getToken() == null || AccessTokenKeeper
				.readAccessToken(mContext).getToken().equals(""))) {
			return false;
		} else {
			return true;
		}
	}

	private class SinaAuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			AccessTokenKeeper.keepAccessToken(mContext, accessToken);

			if (bundlingListen != null) {
				bundlingListen.success();
			}
			StatusesAPI api = new StatusesAPI(accessToken);
			api.update("1234567890", "90", "90", new RequestListener() {

				@Override
				public void onIOException(IOException arg0) {
					// TODO Auto-generated method stub
					Log.e("tag", "onIOException -- " + arg0.getMessage(), arg0);
				}

				@Override
				public void onError(WeiboException arg0) {
					// TODO Auto-generated method stub
					Log.e("tag", "onError -- " + arg0.getMessage(), arg0);
				}

				@Override
				public void onComplete(String arg0) {
					// TODO Auto-generated method stub
					Log.d("tag", "发送成功" + arg0);
				}
			});

			if (accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(accessToken.getExpiresTime()));
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(mContext, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			if (bundlingListen != null) {
				bundlingListen.error(e);
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "Auth cancel", Toast.LENGTH_LONG).show();
			if (bundlingListen != null) {
				bundlingListen.cancel();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(mContext, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			if (bundlingListen != null) {
				bundlingListen.exction(e);
			}
		}
	}

	private BundlingListen bundlingListen;

	public void setUnBundlingListen(BundlingListen bundlingListen) {
		this.bundlingListen = bundlingListen;
	}

	public interface BundlingListen {
		public void success();

		public void error(WeiboDialogError e);

		public void cancel();

		public void exction(WeiboException e);
	}
}

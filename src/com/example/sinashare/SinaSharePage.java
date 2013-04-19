package com.example.sinashare;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.util.SinaShare;
import com.umeng.analytics.MobclickAgent;

public class SinaSharePage extends Activity implements OnClickListener{
	private TextView button1;
	private TextView button2;
	private TextView button3;
	private TextView shareTitle;
	private TextView shareContent;
	private ImageView sharePicture;
	private LinearLayout sharePictureLayout;
	private Button   backButton;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	private Intent getAlbum;
	private String path;
	private Bitmap bm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_share_page);
		
		button1 = (TextView)findViewById(R.id.button1);
		button2 = (TextView)findViewById(R.id.button2);
		button3 = (TextView)findViewById(R.id.button3);
		shareTitle = (TextView)findViewById(R.id.share_title);
		shareContent = (TextView)findViewById(R.id.share_content);
		backButton = (Button)findViewById(R.id.back_button);
		sharePicture = (ImageView)findViewById(R.id.share_picture);
		sharePictureLayout = (LinearLayout)findViewById(R.id.share_picture_layout);
		getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		
		initView();
	}
	
	public void initView(){
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		backButton.setOnClickListener(this);
		sharePictureLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == button1.getId()){
			SinaShare share = new SinaShare(this);
			share.shareText(shareTitle.getText().toString(),shareContent.getText().toString(),path);
		}else if(v.getId() == button2.getId()){
			Intent intent = new Intent();
			intent.setClass(SinaSharePage.this,AccontSeting.class);
			startActivity(intent);
		}else if(v.getId() == button3.getId()){
			Intent intent = new Intent();
			intent.setClass(SinaSharePage.this,SinaHomePage.class);
			startActivity(intent);
		}else if(v.getId() == sharePictureLayout.getId()){
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_CODE);
		}else if(v.getId() == backButton.getId()){
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量
	        Log.e("tag","ActivityResult resultCode error");
	        return;
	    }	 
	 
	    //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
	    ContentResolver resolver = getContentResolver();
	 
	    //此处的用于判断接收的Activity是不是你想要的那个
	    if (requestCode == IMAGE_CODE) {
	        try {
	            Uri originalUri = data.getData();        //获得图片的uri 
	 
	            bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片	  
	            sharePicture.setImageBitmap(bm);
	            String[] proj = {MediaStore.Images.Media.DATA};
	 
	            //好像是android多媒体数据库的封装接口，具体的看Android文档
	            Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
	            //按我个人理解 这个是获得用户选择的图片的索引值
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            //将光标移至开头 ，这个很重要，不小心很容易引起越界
	            cursor.moveToFirst();
	            //最后根据索引值获取图片路径
	            path = cursor.getString(column_index);
	            Log.d("----------", "------------------" + path);
	        }catch (IOException e) {
	            Log.e("tag",e.toString()); 
	        }
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

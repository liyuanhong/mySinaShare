package com.example.adapter;

import com.example.service.PagerCirculateService;
import com.example.sinashare.R;
import com.example.sinashare.UmengHomePage;
import com.example.sinashare.UmengPage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UmengPagePagerAdapter extends PagerAdapter {
	int[] pic;
	Context context;
	LayoutInflater inflate;
	
	public UmengPagePagerAdapter(int[] views,Context context) {
		this.pic= views;
		this.context = context;
		inflate = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return pic.length;

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1; // 这行代码很重要，它用于判断你当前要显示的页面
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflate.inflate(R.layout.umeng_pager_item, null);
		((ImageView)view.findViewById(R.id.view_item)).setImageResource(pic[position]);
		container.addView(view);
		view.setId(position);
				
		view.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, UmengHomePage.class);
				context.startActivity(intent);				
			}
		});
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}		
}

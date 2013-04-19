package com.example.service;

import android.os.Handler;
import android.support.v4.view.ViewPager;

public class PagerCirculateService {
	Handler hander = new Handler();
	int flag = 0;
	public static int i = 0;
	int pic[];
	ViewPager pager;	
	Runnable runnable;
	
	public PagerCirculateService(ViewPager pager,int pic[]) {
		this.pager = pager;
		this.pic = pic;
	}
	
	public void startService(){
		flag = 1;
		runnable = new Runnable() {		
			@Override
			public void run() {
				if(i <pic.length){
					pager.setCurrentItem(i);
					i++;
					if(flag == 1){
						hander.postDelayed(this, 5000);
					}
				}else{
					i =0;
					if(flag == 1){
						hander.postDelayed(this,1);
					}
				}	
			}
		};
		hander.postDelayed(runnable, 1);
	}
	
	public void stopService(){
		flag = 0;
		runnable = null;
	}
}

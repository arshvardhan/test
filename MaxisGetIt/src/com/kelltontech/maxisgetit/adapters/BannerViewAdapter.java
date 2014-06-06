package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelltontech.maxisgetit.ui.activities.ViewPagerFragment;


public class BannerViewAdapter extends FragmentPagerAdapter {

	private ArrayList<String> imgPathList;
//	private int bannerHeight;
	private String mFlowFrom;
	private	Context mcontext;

	
	public BannerViewAdapter(FragmentManager fm, ArrayList<String> imgPathList, Context context , String flowFrom) {
		super(fm);
		this.imgPathList = imgPathList;
		mcontext = context;
		mFlowFrom = flowFrom;
	}

	@Override
	public Fragment getItem(int position) {
		ViewPagerFragment pagerFragment = new ViewPagerFragment();
		pagerFragment.setImagePath(imgPathList.get(position).trim(),mFlowFrom ,(Activity) mcontext);
		return pagerFragment;
	}
	

	@Override
	public int getCount() {
		return imgPathList.size();
	}
	
}


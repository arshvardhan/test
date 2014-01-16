package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelltontech.maxisgetit.dao.IconUrl;
import com.kelltontech.maxisgetit.ui.activities.ViewPagerFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<IconUrl> imgPathList;
	private int bannerHeight;
	private String mFlowFrom;
	private	Context mcontext;

	
	public ViewPagerAdapter(FragmentManager fm, ArrayList<IconUrl> imgPathList, Context context) {
		super(fm);
		this.imgPathList = imgPathList;
		mcontext = context;
	}

	@Override
	public Fragment getItem(int position) {
		ViewPagerFragment pagerFragment = new ViewPagerFragment();
		pagerFragment.setImagePath(imgPathList.get(position).getDealIconUrl());
		return pagerFragment;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgPathList.size();
	}
	
}

package com.kelltontech.maxisgetit.adapters.matta;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelltontech.maxisgetit.ui.activities.matta.MattaViewPagerFragment;

public class MattaViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<String> imgPathList;
	private String mFlowFrom;
	private	Context mcontext;
	
	public MattaViewPagerAdapter(FragmentManager fm, ArrayList<String> imgPathList, Context context , String flowFrom) {
		super(fm);
		this.imgPathList = imgPathList;
		mcontext = context;
		mFlowFrom = flowFrom;
	}

	@Override
	public Fragment getItem(int position) {
		MattaViewPagerFragment pagerFragment = new MattaViewPagerFragment();
		pagerFragment.setImagePath(imgPathList.get(position),mFlowFrom ,(Activity) mcontext);
		return pagerFragment;
	}
	
	@Override
	public int getCount() {
		return imgPathList.size();
	}
	
}
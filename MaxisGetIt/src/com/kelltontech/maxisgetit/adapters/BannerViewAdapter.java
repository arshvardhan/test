package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelltontech.maxisgetit.dao.Banner;
import com.kelltontech.maxisgetit.ui.activities.ViewPagerFragment;


public class BannerViewAdapter extends FragmentPagerAdapter {

	private ArrayList<Banner> bannerList;
	private String mFlowFrom;
	private	Context mcontext;

	public BannerViewAdapter(FragmentManager fm, ArrayList<Banner> bannerList, Context context , String flowFrom) {
		super(fm);
		this.bannerList = bannerList;
		mcontext = context;
		mFlowFrom = flowFrom;
	}

	@Override
	public Fragment getItem(int position) {
		ViewPagerFragment pagerFragment = new ViewPagerFragment();
		pagerFragment.setImagePath(bannerList.get(position).getImage().trim(),mFlowFrom ,(Activity) mcontext, position, bannerList.get(position).getId().trim());
		return pagerFragment;
	}

	@Override
	public int getCount() {
		return bannerList.size();
	}

}


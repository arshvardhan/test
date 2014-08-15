package com.kelltontech.maxisgetit.adapters.matta;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kelltontech.maxisgetit.model.matta.booths.list.BoothListBanner;
import com.kelltontech.maxisgetit.ui.activities.matta.MattaViewPagerFragment;


public class BoothListBannerViewAdapter extends FragmentPagerAdapter {

	private ArrayList<BoothListBanner> bannerList;
	private String mFlowFrom;
	private	Context mcontext;

	public BoothListBannerViewAdapter(FragmentManager fm, ArrayList<BoothListBanner> bannerList, Context context , String flowFrom) {
		super(fm);
		this.bannerList = bannerList;
		mcontext = context;
		mFlowFrom = flowFrom;
	}

	@Override
	public Fragment getItem(int position) {
		MattaViewPagerFragment pagerFragment = new MattaViewPagerFragment();
		pagerFragment.setImagePath(bannerList.get(position).getImage().trim(),mFlowFrom ,(Activity) mcontext, position, bannerList.get(position).getId().trim());
		return pagerFragment;
	}

	@Override
	public int getCount() {
		return bannerList.size();
	}

}
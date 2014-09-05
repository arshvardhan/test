package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;


public class ViewPagerFragment extends Fragment {

	private String imgPath;
	private Activity mActivity;
	private String mFlowFrom;
	private int mPosition;
	private String mBannerId;
	private static Drawable dummyDrawable;
	private static Drawable errorDrawable;

	public ViewPagerFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Test", "hello");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageLoader.initialize(getActivity());
		if (AppConstants.FLOW_FROM_COMBIND_LIST.equals(mFlowFrom) 
				|| AppConstants.FLOW_FROM_SUB_CATEGORY_LIST.equals(mFlowFrom)) {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.banner_load);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.banner_cross);
		} else if (AppConstants.FLOW_FROM_COMPANY_DETAIL.equals(mFlowFrom) || AppConstants.FLOW_FROM_DEAL_DETAIL.equals(mFlowFrom)) {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.detail_loading);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.detail_cross);
		} else {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.group_load);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.group_cross);			
		}
		ImageView imageView = new ImageView(getActivity());
		if (AppConstants.FLOW_FROM_COMBIND_LIST.equals(mFlowFrom) 
				|| AppConstants.FLOW_FROM_SUB_CATEGORY_LIST.equals(mFlowFrom)) {
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		} else {
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}

		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		Log.i("Image path", "Imag path " + imgPath);
		//		imageView.setTag(imgPath);
		ImageLoader.start(imgPath, imageView, dummyDrawable, errorDrawable);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					if (AppConstants.FLOW_FROM_DEAL_DETAIL.equals(mFlowFrom)) {
						((DealDetailActivity) mActivity).viewFlipperTapped();
					} else if (AppConstants.FLOW_FROM_COMPANY_DETAIL.equals(mFlowFrom)) {
						((CompanyDetailActivity) mActivity).viewFlipperTapped();
					} else if (AppConstants.FLOW_FROM_COMBIND_LIST.equals(mFlowFrom)) {
						Log.i("Image Position", "Image Position : " + String.valueOf(mPosition)+ " : Image Path : " + imgPath + " : BannerID : " + mBannerId);
						((CombindListActivity) mActivity).bannerTapped(mPosition, mBannerId, (CombindListActivity) mActivity, Events.BANNER_NAVIGATION_EVENT);
					} else if (AppConstants.FLOW_FROM_SUB_CATEGORY_LIST.equals(mFlowFrom)) {
						Log.i("Image Position", "Image Position : " + String.valueOf(mPosition)+ " : Image Path : " + imgPath + " : BannerID : " + mBannerId);
						((SubCategoryListActivity) mActivity).bannerTapped(mPosition, mBannerId, (SubCategoryListActivity) mActivity, Events.BANNER_NAVIGATION_EVENT);
					}
				} catch (Exception ex){
					ex.printStackTrace();
				}

			}
		});

		return imageView;
	}

	public void setImagePath(String imgPath , String flowFrom , Activity activity) {
		this.imgPath = imgPath;
		this.mFlowFrom = flowFrom;
		this.mActivity = activity;
	}

	public void setImagePath(String imgPath , String flowFrom , Activity activity, int position, String bannerId) {
		this.imgPath = imgPath;
		this.mFlowFrom = flowFrom;
		this.mActivity = activity;
		this.mPosition = position;
		this.mBannerId = bannerId;
	}

}

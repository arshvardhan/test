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


public class ViewPagerFragment extends Fragment {

	private String imgPath;
	private Activity mActivity;
	//	private int bannerHeight;
	private String mFlowFrom;
	//	private ArrayList<SubCategory> categories;
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
		if ("Category List".equals(mFlowFrom)) {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.banner_load);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.banner_cross);
		} else if ("CompanyDetail".equals(mFlowFrom) || "DealDetail".equals(mFlowFrom)) {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.detail_loading);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.detail_cross);
		} else {
			dummyDrawable=getActivity().getResources().getDrawable(R.drawable.group_load);
			errorDrawable=getActivity().getResources().getDrawable(R.drawable.group_cross);			
		}
		ImageView imageView = new ImageView(getActivity());
		if ("Category List".equals(mFlowFrom)) {
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
					if (mFlowFrom.equals("DealDetail")) {
						((DealDetailActivity) mActivity).viewFlipperTapped();
					} else {
						((CompanyDetailActivity) mActivity).viewFlipperTapped();
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

}

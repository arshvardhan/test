package com.kelltontech.maxisgetit.adapters;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.dao.IconUrl;

public class ImagePinchZoomAdapter extends PagerAdapter {
	private Context mContext;
	private ArrayList<IconUrl> mPhotoList;
	private Drawable mCompLoading;
	private Drawable mCompError;
	private PhotoView photoView;
	


	public ImagePinchZoomAdapter(Context pContext,ArrayList<IconUrl> photoList){
		mContext=pContext;
		mPhotoList=photoList;
		mCompLoading = pContext.getResources().getDrawable(R.drawable.detail_loading);
		mCompError = pContext.getResources().getDrawable(R.drawable.detail_cross);
		
	}

	@Override
	public int getCount() {
		return mPhotoList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

			photoView=new PhotoView(mContext);
			
			ImageLoader.start(mPhotoList.get(position).getDealIconUrl(), photoView, mCompLoading, mCompError);
			((ViewPager) container).addView(photoView, ((ViewPager)container).getChildCount() > position ? position : ((ViewPager)container).getChildCount());
			return photoView;
		
	}
	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((PhotoView) object);
		super.destroyItem(container, position, object);
	}
	
}

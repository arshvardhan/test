package com.kelltontech.maxisgetit.ui.activities;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.maxisgetit.R;

public class CompanyDetailImageViewActivity extends Activity {

	private Drawable mCompLoading;
	private Drawable mCompError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail_image_view);
		mCompLoading = getResources().getDrawable(R.drawable.detail_loading);
		mCompError = getResources().getDrawable(R.drawable.detail_cross);

		String imgUrl = getIntent().getStringExtra("ImageURL");

		PhotoView imageView = (PhotoView) findViewById(R.id.imageview);
		ImageLoader.start(imgUrl, imageView, mCompLoading, mCompError);

		imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}

package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.ImagePinchZoomAdapter;
import com.kelltontech.maxisgetit.dao.IconUrl;



public class PhotoSlideActivity extends MaxisMainActivity{
	
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_pager);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setOnClickListener(this);

		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			ArrayList<IconUrl> listingData=getIntent().getExtras().getParcelableArrayList("list");
			if(listingData!=null && listingData.size()>0){
				int position =getIntent().getExtras().getInt("position");
				ImagePinchZoomAdapter adapter = new ImagePinchZoomAdapter(this,listingData);
				viewPager.setAdapter(adapter);
				viewPager.setCurrentItem(position);
			}
		}
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_pager:
			finish();
			break;
		}
	}

}

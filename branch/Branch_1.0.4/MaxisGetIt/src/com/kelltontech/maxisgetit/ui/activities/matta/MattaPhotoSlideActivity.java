package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.matta.MattaImagePinchZoomAdapter;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;

public class MattaPhotoSlideActivity extends MaxisMainActivity{
	
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_pager);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setOnClickListener(this);

		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			ArrayList<String> listingData=getIntent().getExtras().getStringArrayList("list");
			String mflowFrom =getIntent().getExtras().getString("flowFrom");
			if(listingData!=null && listingData.size()>0){
				int position =getIntent().getExtras().getInt("position");
				MattaImagePinchZoomAdapter adapter = new MattaImagePinchZoomAdapter(this,listingData, mflowFrom);
				viewPager.setAdapter(adapter);
				viewPager.setCurrentItem(position);
			}
		}
	}

	@Override
	public Activity getMyActivityReference() {
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
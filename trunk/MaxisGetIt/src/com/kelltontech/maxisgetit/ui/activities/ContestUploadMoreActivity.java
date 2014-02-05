package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

/**
 * This screen is shown after successful image upload.
 */
public class ContestUploadMoreActivity extends MaxisMainActivity {
/*	ImageView mLogo;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contest_upload_more_activity);
		
		((TextView)findViewById(R.id.header_title)).setText(getString(R.string.header_photo_contest));
		
		findViewById(R.id.header_btn_back).setOnClickListener(this);
		findViewById(R.id.goto_home_icon).setOnClickListener(this);
		findViewById(R.id.search_toggler).setVisibility(View.INVISIBLE);
		findViewById(R.id.show_profile_icon).setOnClickListener(this);
		
	/*	findViewById(R.id.footer_facebook_icon).setOnClickListener(this);
		findViewById(R.id.footer_twitterIcon).setOnClickListener(this);*/
		
		findViewById(R.id.upload_btn).setOnClickListener(this);
		/*mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(ContestUploadMoreActivity.this);*/
		
	}
	
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		findViewById(R.id.topLayout).setBackgroundDrawable(fetchImageFromGallery(intent));
//	}
//	
//	private Drawable fetchImageFromGallery(Intent intent)
//	{
//		File imgFile = new File(intent.getStringExtra("mImagePath"));
//		if(imgFile.exists())
//		{
//			Display display = getWindowManager().getDefaultDisplay(); 
//			int width = display.getWidth();  // deprecated
//			int height = display.getHeight();
//			Bitmap myBitmap=BitmapCalculation.decodeSampledBitmapFromPath(imgFile.getAbsolutePath(), width, height);
//			//Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//			return  new BitmapDrawable(getResources(),myBitmap); 
//		}
//		return null;
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		}
		case R.id.header_btn_back: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		}
		case R.id.show_profile_icon: {
			onProfileClick();
			break;
		}
		case R.id.upload_btn: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.CAPTURE_ANOTHER_IMAGE);
			Intent intent = new Intent(ContestUploadMoreActivity.this, ContestHomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		/*case R.id.footer_facebook_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL);
			break;
		}
		case R.id.footer_twitterIcon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL);
			break;
		}
		case R.id.logo:
			startActivity(new Intent(ContestUploadMoreActivity.this, GetItInfoActivity.class));
			break;*/
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return this;
	}
}

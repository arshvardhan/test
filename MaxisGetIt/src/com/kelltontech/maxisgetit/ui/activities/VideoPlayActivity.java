package com.kelltontech.maxisgetit.ui.activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class VideoPlayActivity extends MaxisMainActivity {

	private ImageView backbutton;
	private WebView videoView;
	private String videoUrl = "";
	private boolean isClicked = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);

		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_VIDEO_PLAY);

		startSppiner();
		videoUrl = getIntent().getStringExtra(AppConstants.VIDEO_URL);
		backbutton = (ImageView) findViewById(R.id.backbtn);
		videoView = (WebView) findViewById(R.id.videoView);
		backbutton.setVisibility(View.VISIBLE);

		backbutton.setOnClickListener(this);
		videoView.getSettings().setJavaScriptEnabled(true);
		videoView.getSettings().setDomStorageEnabled(true);
		final Activity activity = this;
		videoView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setProgress(progress * 100);
				if (progress == 100)
					stopSppiner();
			}
		});

		videoUrl = AppConstants.BASE_URL_VIDEOS + videoUrl;
		// mWebView.loadUrl("http://www.metacafe.com/embed/11333706/");
		// videoView.loadUrl("http://www.youtube.com/embed/vgm1u2gPxzw?rel=");
		videoView.loadUrl(videoUrl);


	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backbtn:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
			this.finish();
			break;

		default:
			break;
		}
	}

	/*
	 * @Override protected void onPause() { super.onPause();
	 * videoView.loadUrl("");
	 * 
	 * }
	 */

	/*
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); videoView.loadUrl(videoUrl); }
	 */
	@Override
	protected void onPause() {
		super.onPause();
		callHiddenWebViewMethod("onPause");
		videoView.pauseTimers();
		if (isFinishing()) {
			videoView.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		callHiddenWebViewMethod("onResume");
		videoView.resumeTimers();
	}

	private void callHiddenWebViewMethod(String name) {
		if (videoView != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(videoView);
			} catch (NoSuchMethodException e) {
				Log.d("No such method: " + name + e, "");
			} catch (IllegalAccessException e) {
				Log.d("Illegal Access: " + name + e, "");
			} catch (InvocationTargetException e) {
				Log.d("Invocation Target Exception: " + name + e, "");
			}
		}
	}

}

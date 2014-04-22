package com.kelltontech.maxisgetit.ui.activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.ui.widgets.VideoEnabledWebView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.VideoEnabledWebChromeClient;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class VideoPlayActivity extends MaxisMainActivity {

	private ImageView backbutton;
	private VideoEnabledWebView videoView;
	private String videoUrl = "";
	private boolean isClicked = true;
	private VideoEnabledWebChromeClient webChromeClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);

		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_VIDEO_PLAY);

		startSppiner();
		videoUrl = getIntent().getStringExtra(AppConstants.VIDEO_URL);
		backbutton = (ImageView) findViewById(R.id.backbtn);
		videoView = (VideoEnabledWebView) findViewById(R.id.videoView);
		backbutton.setVisibility(View.VISIBLE);

		backbutton.setOnClickListener(this);
		videoView.getSettings().setJavaScriptEnabled(true);
		videoView.getSettings().setDomStorageEnabled(true);
		// videoView.setLayerType(View.LAYER_TYPE_NONE, null);

		videoView.clearCache(true);

		final Activity activity = VideoPlayActivity.this;
//		videoView.setWebChromeClient(new WebChromeClient() {
//			public void onProgressChanged(WebView view, int progress) {
//				activity.setProgress(progress * 100);
//				if (progress == 100)
//					stopSppiner();
//			}
//		});
		// if (Build.VERSION.SDK_INT < 8) {
		// videoView.getSettings().setPluginsEnabled(true);
		// } else {
		// videoView.getSettings().setPluginState(PluginState.ON);
		// }
		videoView.getSettings().setLoadWithOverviewMode(true);
		videoView.getSettings().setUseWideViewPort(true);
		videoUrl = AppConstants.BASE_URL_VIDEOS + videoUrl;
		// mWebView.loadUrl("http://www.metacafe.com/embed/11333706/");
		// videoView.loadUrl("http://www.youtube.com/embed/vgm1u2gPxzw?rel=");
//		videoView.loadUrl(videoUrl);

		
		 View nonVideoLayout = findViewById(R.id.header_base_activity); // Your own view, read class comments
		    ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
//		    View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
		    webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, null, videoView) // See all available constructors...
		    {
		        // Subscribe to standard events, such as onProgressChanged()...
		        @Override
		        public void onProgressChanged(WebView view, int progress)
		        {
		        	activity.setProgress(progress * 100);
					if (progress == 100)
						stopSppiner();
		        }
		    };
		    webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
		    {
		        @Override
		        public void toggledFullscreen(boolean fullscreen)
		        {
		            // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
		            if (fullscreen)
		            {
		                WindowManager.LayoutParams attrs = getWindow().getAttributes();
		                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		                attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		                getWindow().setAttributes(attrs);
		                if (android.os.Build.VERSION.SDK_INT >= 14)
		                {
		                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		                }
		            }
		            else
		            {
		                WindowManager.LayoutParams attrs = getWindow().getAttributes();
		                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
		                attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		                getWindow().setAttributes(attrs);
		                if (android.os.Build.VERSION.SDK_INT >= 14)
		                {
		                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		                }
		            }

		        }
		    });
		    videoView.setWebChromeClient(webChromeClient);

		    // Navigate everywhere you want, this classes have only been tested on YouTube's mobile site
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

	@Override
	protected void onPause() {
		super.onPause();
		callHiddenWebViewMethod("onPause");
		videoView.pauseTimers();
		if (isFinishing()) {
			videoView.loadUrl("about:blank");
			setContentView(new FrameLayout(this));

		}

		((AudioManager) getSystemService(Context.AUDIO_SERVICE))
				.requestAudioFocus(new OnAudioFocusChangeListener() {
					@Override
					public void onAudioFocusChange(int focusChange) {
					}
				}, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (videoView != null) {
			videoView.clearCache(true);
		}
	}
	
	@Override
	public void onBackPressed()
	{
	    // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
	    if (!webChromeClient.onBackPressed())
	    {
	        if (videoView.canGoBack())
	        {
	            videoView.goBack();
	        }
	        else
	        {
	            // Close app (presumably)
	            super.onBackPressed();
	        }
	    }
	}
}

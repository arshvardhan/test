//package com.kelltontech.maxisgetit.ui.activities;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Message;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import com.kelltontech.framework.imageloader.ImageLoader;
//import com.kelltontech.framework.utils.UiUtils;
//import com.kelltontech.maxisgetit.R;
//import com.kelltontech.maxisgetit.constants.AppConstants;
//import com.kelltontech.maxisgetit.constants.Events;
//import com.kelltontech.maxisgetit.controllers.SendSmsController;
//import com.kelltontech.maxisgetit.dao.CompanyDetail;
//import com.kelltontech.maxisgetit.response.SendSmsResponse;
// 
//public class SendSmsActivity extends MaxisMainActivity {
//	private ImageView homeIconView, facebookIconView, twitterIconView, profileIconView;
//	private EditText searchEditText,mobileEditTxt;
//	private ImageView searchBtn;
//	private Drawable thumbLoading;
//	private Drawable thumbError;
//	private CompanyDetail compDetail;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_send_sms);
//		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.ass_root_layout), this);
//		homeIconView = (ImageView) findViewById(R.id.goto_home_icon);
//		homeIconView.setOnClickListener(this);
//		facebookIconView = (ImageView) findViewById(R.id.facebook_icon);
//		facebookIconView.setOnClickListener(this);
//		profileIconView = (ImageView) findViewById(R.id.show_profile_icon);
//		profileIconView.setOnClickListener(this);
//		twitterIconView = (ImageView) findViewById(R.id.twitterIcon);
//		twitterIconView.setOnClickListener(this);
//		searchBtn = (ImageView) findViewById(R.id.search_icon_button);
//		searchBtn.setOnClickListener(SendSmsActivity.this);
//		searchEditText = (EditText) findViewById(R.id.search_box);
//		thumbLoading = getResources().getDrawable(R.drawable.cat_thumb);
//		thumbError = getResources().getDrawable(R.drawable.cat_thumb_cross);
//		String categoryThumbUrl = getIntent().getStringExtra(AppConstants.THUMB_URL);
//		compDetail=getIntent().getParcelableExtra(AppConstants.COMP_DETAIL_DATA);
//		ImageView headerIconView = (ImageView) findViewById(R.id.ass_thumb_icon);
//		ImageLoader.start(categoryThumbUrl, headerIconView, thumbLoading, thumbError);
//		findViewById(R.id.ass_sendSmsBtn).setOnClickListener(this);
//		findViewById(R.id.ass_cancelSmsBtn).setOnClickListener(this);
//		mobileEditTxt=(EditText) findViewById(R.id.ass_mobileEdtTxt);
//		if(compDetail!=null && compDetail.getSmsNumber()!=null)
//			mobileEditTxt.setText(compDetail.getCallNumber());
//	}
//
//	@Override
//	public Activity getMyActivityReference() {
//		return null;
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.search_icon_button: 
//			searchEditText.setText(searchEditText.getText().toString().trim());
//			performSearch(searchEditText.getText().toString());
//			break;
//		case R.id.goto_home_icon:
//			Intent intentHome = new Intent(SendSmsActivity.this, HomeActivity.class);
//			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intentHome);
//			break;
//		case R.id.show_profile_icon:
//			onProfileClick();
//			break;
//		case R.id.facebook_icon:
//			displayUrlInBrowser(AppConstants.FB_PAGE_URL, SendSmsActivity.this);
//			break;
//		case R.id.twitterIcon:
//			displayUrlInBrowser(AppConstants.TWITTER_PAGE_URL, SendSmsActivity.this);
//			break;
//		case R.id.ass_sendSmsBtn:
//			EditText editText = (EditText) findViewById(R.id.ass_mobileEdtTxt);
//			String mobile = editText.getText().toString().trim();
//			if (mobile == null || mobile.length() == 0) {
//				showInfoDialog(getResources().getString(R.string.eneter_mobile_number));
//				return;
//			}
////			if(mobile.length()<10)
////			{
////				showToast(getResources().getString(R.string.invalid_mobile));
////				return;
////			}
//			startSppiner();
//			SendSmsController sendSmsController = new SendSmsController(this, Events.SEND_SMS);
//			sendSmsController.requestService(mobile);
//			break;
//		case R.id.ass_cancelSmsBtn:
//			finish();
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public void updateUI(Message msg) {
//		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
//			super.updateUI(msg);
//		} else if (msg.arg2 == Events.SEND_SMS) {
//			stopSppiner();
//			if (msg.arg1 == 0) {
//				SendSmsResponse sendSmsResponse = (SendSmsResponse) msg.obj;
//				if (sendSmsResponse.getStatusCode() == 1)
//					showFinalDialog("Sms sent successfully.");
//				else
//					showInfoDialog("Sms could not send.");
//			} else
//				showInfoDialog((String) msg.obj);
//		}
//	}
//
//	@Override
//	public void setScreenData(Object screenData, int event, long time) {
//		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
//			super.setScreenData(screenData, event, time);
//			return;
//		} else if (event == Events.SEND_SMS) {
//			Message message = (Message) screenData;
//			handler.sendMessage(message);
//		}
//	}
//}

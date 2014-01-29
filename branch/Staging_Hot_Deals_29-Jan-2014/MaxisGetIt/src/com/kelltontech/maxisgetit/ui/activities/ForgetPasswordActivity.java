package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.controllers.ForgetPasswordController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.utils.Utility;

public class ForgetPasswordActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private EditText mEdPhone;
	private TextView mHeaderTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(ForgetPasswordActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mEdPhone = (EditText) findViewById(R.id.fp_edt_mobile);
		
		mHeaderTitle=(TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(Html.fromHtml(getResources().getString(R.string.fp_title)));
		
		findViewById(R.id.fp_btn_reset_password).setOnClickListener(this);
		
		setData();
		
	}

	private void setData() {
		MaxisStore store = MaxisStore.getStore(ForgetPasswordActivity.this);
		if (store.isRegisteredUser()) {
			mEdPhone.setText(store.getUserMobileNumberToDispaly());
		}
		
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			Intent intentHome = new Intent(ForgetPasswordActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.search_toggler:
			if(mSearchContainer.getVisibility()==View.VISIBLE){
				mSearchContainer.setVisibility(View.GONE);
			}else{
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			this.finish();
			break;
		case R.id.fp_btn_reset_password:
			String mobile = mEdPhone.getText().toString();
			//if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 || (mobile.indexOf('+')!=-1 && mobile.length()<=10)) {
			if (mobile.length() < 7 || mobile.length()>12) {
				showAlertDialog(getString(R.string.invalid_mobile));
				return;
			}
			resetPassword(Utility.getMobileNoForWS(this, mobile));
			break;
		default:
			break;
		}

	}

	private void resetPassword(String mobile) {
		ForgetPasswordController fpController = new ForgetPasswordController(ForgetPasswordActivity.this, Events.FORGET_PASSWORD);
		startSppiner();
		fpController.requestService(mobile);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if(msg.arg2 == Events.FORGET_PASSWORD){
			stopSppiner();
			if(msg.arg1!=0){
				showInfoDialog((String) msg.obj);
			}else{
				showFinalDialog(getResources().getString(R.string.fp_success_msg));
			}
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if(event==Events.FORGET_PASSWORD){
			handler.sendMessage((Message) screenData);
		}
	}
}

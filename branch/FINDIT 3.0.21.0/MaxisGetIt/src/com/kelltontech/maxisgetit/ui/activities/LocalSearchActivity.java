package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CategoriesedLeadsController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.response.CategoriesedLeadsResponse;
import com.kelltontech.maxisgetit.response.LocalSearchResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class LocalSearchActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private LocalSearchResponse mLsResponse;
	private LinearLayout mCompaniesContainer;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mSearchToggler;
	private LinearLayout mSearchContainer;
	private ImageView mHeaderBackButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localsearch);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(LocalSearchActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		((TextView)findViewById(R.id.header_title)).setText(getResources().getString(R.string.acc_my_comp));
		
		
		Bundle bundle=getIntent().getExtras();
		mLsResponse=bundle.getParcelable(AppConstants.LOCAL_SEARCH_RESPONSE);
		mCompaniesContainer=(LinearLayout) findViewById(R.id.als_companies_container);
		if (mLsResponse != null) {
			for (int i = 0; i < mLsResponse.getCompaniesDetails().size(); i++) {
				inflateCompanyDetail(mLsResponse.getCompaniesDetails().get(i));
			}
		}
	}

	private void inflateCompanyDetail(CompanyDetail compDetail) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(R.layout.local_search_company_layout, null);
		TextView compTitle = (TextView) compContainer.findViewById(R.id.lscl_title);
		compTitle.setText(compDetail.getTitle());
		TextView editLink = (TextView) compContainer.findViewById(R.id.lscl_edit_link);
		editLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(LocalSearchActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		TextView viewLeads = (TextView) compContainer.findViewById(R.id.lscl_view_lead);
		viewLeads.setTag(compDetail);
		viewLeads.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CompanyDetail companyDetail=(CompanyDetail)v.getTag();
				CategoriesedLeadsController catLeadsController=new CategoriesedLeadsController(LocalSearchActivity.this, Events.COMPANY_LEADS);
				startSppiner();
				catLeadsController.requestService(companyDetail.getId());
//				showToast(LocalSearchActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		TextView removeLink = (TextView) compContainer.findViewById(R.id.lscl_remove_link);
		removeLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(LocalSearchActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		TextView listingType = (TextView) compContainer.findViewById(R.id.lscl_listing_type);
		TextView upgradeToPremium = (TextView) compContainer.findViewById(R.id.lscl_upgrade_btn);
		if (!compDetail.isPaid()) {
			listingType.setText(getResources().getString(R.string.free));
			upgradeToPremium.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showToast(LocalSearchActivity.this.getResources().getString(R.string.under_implement));
				}
			});
		} else {
			listingType.setText(getResources().getString(R.string.paid));
			upgradeToPremium.setVisibility(View.GONE);
		}
		if (!compDetail.isContactChannelExists()) {
			LinearLayout contactChannelContainer = (LinearLayout) compContainer.findViewById(R.id.lscl_contact_channel);
			contactChannelContainer.setVisibility(View.GONE);
		} else {
			TextView billingNumber = (TextView) compContainer.findViewById(R.id.lscl_bill_num);
			TextView FwdCommNumber = (TextView) compContainer.findViewById(R.id.lscl_fwd_comm_num);
			TextView emailId = (TextView) compContainer.findViewById(R.id.lscl_email);
			billingNumber.setText(compDetail.getBillingNumber());
			FwdCommNumber.setText(compDetail.getCallNumber());
			emailId.setText(compDetail.getMailId());
		}
		mCompaniesContainer.addView(compContainer);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(LocalSearchActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			//finish();
			onProfileClick();
			break;
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if(mSearchContainer.getVisibility()==View.VISIBLE){
				mSearchContainer.setVisibility(View.GONE);
			}else{
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if(msg.arg2==Events.COMPANY_LEADS){
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(LocalSearchActivity.this, CompanyLeadsActivity.class);
				intent.putExtra(AppConstants.MY_LEADS_RESPONSE, (CategoriesedLeadsResponse)msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if(event==Events.COMPANY_LEADS){
			handler.sendMessage((Message) screenData);
		}
	}
}

package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.CategoriesedLeads;
import com.kelltontech.maxisgetit.dao.LeadDao;
import com.kelltontech.maxisgetit.response.CategoriesedLeadsResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CompanyLeadsActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private CategoriesedLeadsResponse mMyDealsResponse;
	private LinearLayout mLeadsGroupContainer;
	private TextView mListTxtView;
	private TextView mCompanynameTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_leads);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mCompanynameTxt=(TextView) findViewById(R.id.acleads_comp_name);
		Bundle bundle = getIntent().getExtras();
		mMyDealsResponse = bundle.getParcelable(AppConstants.MY_LEADS_RESPONSE);
		mCompanynameTxt.setText(mMyDealsResponse.getCompanyName());
		mLeadsGroupContainer = (LinearLayout) findViewById(R.id.acleads_group_container);
		if (mMyDealsResponse != null) {
			if (mMyDealsResponse.getCatLeads() != null) {
				findViewById(R.id.acleads_lead_unavailable).setVisibility(View.GONE);
			}
			displayDealListing();
		}
	}

	private void displayDealListing() {
		mLeadsGroupContainer.removeAllViews();
		ArrayList<CategoriesedLeads> leadsGroupList = mMyDealsResponse.getCatLeads();
		for (CategoriesedLeads catLeads : leadsGroupList) {
			ArrayList<LeadDao> leadlist = catLeads.getLeadList();
			if (leadlist == null && leadlist.size() < 1)
				continue;
			View listRow = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.classified_list_row_base, null);
			mListTxtView = ((TextView) listRow.findViewById(R.id.clrb_title));
			mListTxtView.setText(catLeads.getCategoryName());
			mListTxtView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent();
					ArrayList<LeadDao> leadList = (ArrayList<LeadDao>) parent.getTag();
					LinearLayout innerlayout = (LinearLayout) parent.findViewById(R.id.clrb_inner_layout);
					if (innerlayout.getVisibility() == View.VISIBLE) {
						innerlayout.setVisibility(View.GONE);
						((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.down_arrow), null);
					} else {
						innerlayout.setVisibility(View.VISIBLE);
						((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.up_arrow), null);
						if (leadList != null && innerlayout.getChildCount() == 0) {
							for (int j = 0; j < leadList.size(); j++) {
								inflateLeadsDetail(leadList.get(j), innerlayout);
							}
						}
						View firstVu = innerlayout.getChildCount() > 0 ? innerlayout.getChildAt(0) : null;
						if (firstVu != null) {
							firstVu.setFocusable(true);
							firstVu.setFocusableInTouchMode(true);
							firstVu.requestFocus();
						}
					}
				}
			});
			listRow.setTag(leadlist);
			mLeadsGroupContainer.addView(listRow);
		}
	}

	private void inflateLeadsDetail(LeadDao lead, LinearLayout container) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(R.layout.my_lead_item, null);
		TextView counterTxt = (TextView) compContainer.findViewById(R.id.ml_contact);
		counterTxt.setText(lead.getCallerId());
		TextView compTitle = (TextView) compContainer.findViewById(R.id.ml_start_time);
		compTitle.setText(lead.getStartTime());
		container.addView(compContainer);
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
			Intent intentHome = new Intent(CompanyLeadsActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			finish();
			// onProfileClick();
			break;
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(AppConstants.MY_DEALS_RESPONSE, mMyDealsResponse);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}
	}
}

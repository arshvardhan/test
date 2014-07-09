/*package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.ExpandableListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.dao.Classified_Base;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;

public class ClassifiedListActivity extends MaxisMainActivity {
	private ImageView homeIconView, facebookIconView, twitterIconView, profileIconView;
	private ClassifiedListResponse clsResponse;
	private LinearLayout classifiedContainer;
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classified);
		homeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		homeIconView.setOnClickListener(this);
		facebookIconView = (ImageView) findViewById(R.id.facebook_icon);
		facebookIconView.setOnClickListener(this);
		profileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		profileIconView.setOnClickListener(this);
		twitterIconView = (ImageView) findViewById(R.id.twitterIcon);
		twitterIconView.setOnClickListener(this);
		Bundle bundle = getIntent().getExtras();
		clsResponse = bundle.getParcelable(AppConstants.CLASSIFIED_LIST_RESPONSE);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.acl_lvExp);
		// preparing list data
		prepareListData();
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);

		classifiedContainer = (LinearLayout) findViewById(R.id.acl_container);
		if (clsResponse != null) {
			for (int i = 0; i < clsResponse.getClassifiedList().size() || i < 1; i++) {
				inflateCompanyDetail(clsResponse.getClassifiedList().get(i), i + 1);
			}
		}
	}

	
	 * Preparing the list data
	 
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Top 250");
		listDataHeader.add("Now Showing");
		listDataHeader.add("Coming Soon..");

		// Adding child data
		List<String> top250 = new ArrayList<String>();
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");
		top250.add("The Godfather: Part II");
		top250.add("Pulp Fiction");
		top250.add("The Good, the Bad and the Ugly");
		top250.add("The Dark Knight");
		top250.add("12 Angry Men");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("2 Guns");
		comingSoon.add("The Smurfs 2");
		comingSoon.add("The Spectacular Now");
		comingSoon.add("The Canyons");
		comingSoon.add("Europa Report");

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);
	}

	private void inflateCompanyDetail(Classified_Base classified_Base, int countIndex) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(R.layout.classified_item_layout, null);
		TextView counterTxt = (TextView) compContainer.findViewById(R.id.class_counter);
		counterTxt.setText(countIndex + ".");
		TextView compTitle = (TextView) compContainer.findViewById(R.id.class_title);
		compTitle.setText(classified_Base.getTitle());
		TextView category = (TextView) compContainer.findViewById(R.id.class_category);
		category.setText(classified_Base.getCategory());
		TextView validity = (TextView) compContainer.findViewById(R.id.class_validity);
		validity.setText(classified_Base.getValidity());
		TextView listingType = (TextView) compContainer.findViewById(R.id.class_type);
		if (!classified_Base.isActive()) {
			listingType.setText(getResources().getString(R.string.inactive));
			listingType.setBackgroundColor(getResources().getColor(R.color.red));
		} else {
			listingType.setText(getResources().getString(R.string.active));
		}
		ImageView editLink = (ImageView) compContainer.findViewById(R.id.class_edit_link);
		editLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(ClassifiedListActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		ImageView stop = (ImageView) compContainer.findViewById(R.id.class_stop);
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(ClassifiedListActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		ImageView repost = (ImageView) compContainer.findViewById(R.id.class_repost);
		repost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(ClassifiedListActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		classifiedContainer.addView(compContainer);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			Intent intentHome = new Intent(ClassifiedListActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.facebook_icon:
			displayUrlInBrowser(AppConstants.FB_PAGE_URL, ClassifiedListActivity.this);
			break;
		case R.id.twitterIcon:
			displayUrlInBrowser(AppConstants.TWITTER_PAGE_URL, ClassifiedListActivity.this);
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
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}
	}
}
*/
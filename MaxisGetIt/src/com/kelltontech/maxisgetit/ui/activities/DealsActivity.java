package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.CompanyListAdapter;
import com.kelltontech.maxisgetit.adapters.DealsCategoryAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.HorizontalListView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class DealsActivity extends MaxisMainActivity {

	private HorizontalListView mCategoryListView;
	private SubCategoryResponse mSubcatResponse;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private DealsCategoryAdapter mCatAdapter;
	private SubCategory mSelectdCategory;
	private TextView mRecordsFoundView;

	private ListView mCompanyList;
	private CompanyListAdapter mCompListAdapter;
	private CompanyListResponse mClResponse;
	private CombinedListRequest mClRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydeals_activity1);
		Bundle bundle = getIntent().getExtras();
		mSubcatResponse = bundle
				.getParcelable(AppConstants.DATA_SUBCAT_RESPONSE);
		ImageLoader.initialize(DealsActivity.this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(DealsActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mRecordsFoundView = (TextView) findViewById(R.id.col_records_found);

		mCategoryListView = (HorizontalListView) findViewById(R.id.category_list);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText("Deals");

		// mHeaderTitle.setText(parCategory.getCategoryTitle()); TODO

		mCatAdapter = new DealsCategoryAdapter(DealsActivity.this);
		mCatAdapter.setData(mSubcatResponse.getCategories());
		mCategoryListView.setAdapter(mCatAdapter);

		mCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int position, long arg3) {

				mSelectdCategory = (SubCategory) adapter
						.getItemAtPosition(position);
				if (!StringUtil.isNullOrEmpty(mSelectdCategory
						.getCategoryTitle().trim())
						&& !StringUtil.isNullOrEmpty(mSelectdCategory
								.getCategoryId().trim())) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(FlurryEventsConstants.Sub_Category_Title,
							mSelectdCategory.getCategoryTitle().trim());
					map.put(FlurryEventsConstants.Sub_Category_Id,
							mSelectdCategory.getCategoryId().trim());
					AnalyticsHelper.logEvent(
							FlurryEventsConstants.SUB_CATEGORY, map);
				}
				if (mSelectdCategory.getmGroupActionType().trim()
						.equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_DEAL)) {
					showDealListing(mSelectdCategory);
				}
			}
		});

	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(DealsActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.USER_DETAIL) {
			handler.sendMessage((Message) screenData);
			return;
		}
		System.out.println(screenData);
		Response response = (Response) screenData;
		Message message = new Message();
		message.arg2 = event;
		message.arg1 = 1;
		if (response.isError()) {
			message.obj = response.getErrorText() + " "
					+ response.getErrorCode();
		} else {
			if (response.getPayload() instanceof CompanyListResponse) {
				mClResponse = (CompanyListResponse) response.getPayload();
				if (mClResponse.getErrorCode() != 0) {
					message.obj = getResources().getString(
							R.string.communication_failure);
					// clResponse.getServerMessage() + " " +
					// clResponse.getErrorCode();
				} else {
					if (mClResponse.getCompanyArrayList().size() < 1) {
						message.obj = new String("No Result Found");
					} else {
						message.arg1 = 0;
						message.obj = mClResponse;
					}
				}
			} else {
				message.obj = new String("Internal Error");
			}
		}
		handler.sendMessage(message);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mRecordsFoundView
						.setText(mClResponse.getTotalrecordFound()
								+ " "
								+ getResources().getString(
										R.string.record_found));
				// initNavigationButton(mClResponse.getPagesCount());
				ArrayList<CompanyDesc> compListData = mClResponse
						.getCompanyArrayList();

				mCompanyList = (ListView) findViewById(R.id.col_company_list);
				mCompListAdapter = new CompanyListAdapter(DealsActivity.this,
						false);
				mCompListAdapter.setData(compListData);
				mCompanyList.setAdapter(mCompListAdapter);
			}
			stopSppiner();

		}
	}

	@Override
	protected void onDestroy() {
		ImageLoader.clearCache();
		super.onDestroy();
	}

	private void showDealListing(SubCategory cat) {
		CombindListingController listingController = new CombindListingController(
				DealsActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		mClRequest = new CombinedListRequest(DealsActivity.this);
		mClRequest.setBySearch(false);
		mClRequest.setCompanyListing(false);
		// mClRequest.setKeywordOrCategoryId("-1");
		mClRequest.setKeywordOrCategoryId(cat.getCategoryId());
		mClRequest.setLatitude(GPS_Data.getLatitude());
		mClRequest.setLongitude(GPS_Data.getLongitude());
		mClRequest.setCategoryTitle(cat.getCategoryTitle());
		mClRequest.setParentThumbUrl(cat.getThumbUrl());
		mClRequest.setGroupActionType(cat.getmGroupActionType());
		mClRequest.setGroupType(cat.getMgroupType());
		setRequest(mClRequest);
		startSppiner();
		listingController.requestService(mClRequest);

	}
}

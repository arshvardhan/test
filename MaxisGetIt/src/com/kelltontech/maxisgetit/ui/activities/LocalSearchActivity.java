package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CategoriesedLeadsController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.CategoriesedLeadsResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
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

	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malaysia";
	private int city_id = -1;

	private ArrayList<String> selectedLocalityItems;
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;

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
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		((TextView) findViewById(R.id.header_title)).setText(getResources()
				.getString(R.string.acc_my_comp));

		Bundle bundle = getIntent().getExtras();
		mLsResponse = bundle.getParcelable(AppConstants.LOCAL_SEARCH_RESPONSE);
		mCompaniesContainer = (LinearLayout) findViewById(R.id.als_companies_container);
		if (mLsResponse != null) {
			for (int i = 0; i < mLsResponse.getCompaniesDetails().size(); i++) {
				inflateCompanyDetail(mLsResponse.getCompaniesDetails().get(i));
			}
		}

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html
				.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (LinearLayout) findViewById(R.id.whole_search_box_container);
		mSearchEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
	}

	private void inflateCompanyDetail(CompanyDetail compDetail) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(
				R.layout.local_search_company_layout, null);
		TextView compTitle = (TextView) compContainer
				.findViewById(R.id.lscl_title);
		compTitle.setText(compDetail.getTitle());
		TextView editLink = (TextView) compContainer
				.findViewById(R.id.lscl_edit_link);
		editLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(LocalSearchActivity.this.getResources().getString(
						R.string.under_implement));
			}
		});
		TextView viewLeads = (TextView) compContainer
				.findViewById(R.id.lscl_view_lead);
		viewLeads.setTag(compDetail);
		viewLeads.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CompanyDetail companyDetail = (CompanyDetail) v.getTag();
				CategoriesedLeadsController catLeadsController = new CategoriesedLeadsController(
						LocalSearchActivity.this, Events.COMPANY_LEADS);
				startSppiner();
				catLeadsController.requestService(companyDetail.getId());
				// showToast(LocalSearchActivity.this.getResources().getString(R.string.under_implement));
			}
		});
		TextView removeLink = (TextView) compContainer
				.findViewById(R.id.lscl_remove_link);
		removeLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showToast(LocalSearchActivity.this.getResources().getString(
						R.string.under_implement));
			}
		});
		TextView listingType = (TextView) compContainer
				.findViewById(R.id.lscl_listing_type);
		TextView upgradeToPremium = (TextView) compContainer
				.findViewById(R.id.lscl_upgrade_btn);
		if (!compDetail.isPaid()) {
			listingType.setText(getResources().getString(R.string.free));
			upgradeToPremium.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showToast(LocalSearchActivity.this.getResources()
							.getString(R.string.under_implement));
				}
			});
		} else {
			listingType.setText(getResources().getString(R.string.paid));
			upgradeToPremium.setVisibility(View.GONE);
		}
		if (!compDetail.isContactChannelExists()) {
			LinearLayout contactChannelContainer = (LinearLayout) compContainer
					.findViewById(R.id.lscl_contact_channel);
			contactChannelContainer.setVisibility(View.GONE);
		} else {
			TextView billingNumber = (TextView) compContainer
					.findViewById(R.id.lscl_bill_num);
			TextView FwdCommNumber = (TextView) compContainer
					.findViewById(R.id.lscl_fwd_comm_num);
			TextView emailId = (TextView) compContainer
					.findViewById(R.id.lscl_email);
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
			Intent intentHome = new Intent(LocalSearchActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			// finish();
			onProfileClick();
			break;
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) {
				wholeSearchBoxContainer.setVisibility(View.GONE);
			} else {
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			}
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
		case R.id.upArrow:
			if (isAdvanceSearchLayoutOpen) {
				isAdvanceSearchLayoutOpen = false;
				advanceSearchLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.currentCity:
			if (cityListString != null && cityListString.size() > 0) {
				localityItems = null;
				selectedLocalityindex = null;
				Intent cityIntent = new Intent(LocalSearchActivity.this,
						AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(LocalSearchActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(localityIntent,
						AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMPANY_LEADS) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(LocalSearchActivity.this,
						CompanyLeadsActivity.class);
				intent.putExtra(AppConstants.MY_LEADS_RESPONSE,
						(CategoriesedLeadsResponse) msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CityTable cityTable = new CityTable(
						(MyApplication) getApplication());
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				// cityTable.addCityList(glistRes.getCityOrLocalityList());
				cityList = glistRes.getCityOrLocalityList();
				// inflateCityList(cityList);
				Intent intent = new Intent(LocalSearchActivity.this,
						AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {

					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(intent, AppConstants.CITY_REQUEST);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				localityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(LocalSearchActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);

			}
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.COMPANY_LEADS) {
			handler.sendMessage((Message) screenData);
		} else {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK && reqCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity
					.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity
					+ "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if(index==-1)
			{
				city_id =-1;
			}else
			{
			city_id = cityList.get(index).getId();
			}

		} else if (resultCode == RESULT_OK
				&& reqCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";

			selectedLocalityItems = data
					.getStringArrayListExtra("SELECTED_LOCALITIES");

			selectedLocalityindex = data
					.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null
					&& selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {

					if (i == selectedLocalityItems.size() - 1) {
						locality += selectedLocalityItems.get(i);
					} else {
						locality += selectedLocalityItems.get(i) + ",";
					}
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area "
						+ "<b>" + locality + "</b>"));
			} else {
				currentLocality.setText("Choose your Area");
			}

			ids = new ArrayList<String>();

			if (selectedLocalityindex != null
					&& selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {

					ids.add(String.valueOf(localityList.get(
							Integer.parseInt(selectedLocalityindex.get(i)))
							.getId()));
				}
			}

		}

	}

	public String jsonForSearch() {

		// {"city":{"city_id":5,"city_name":"adyui"},"locality":[{"locality_id":5,"locality_name":"adyui"},{"locality_id":5,"locality_name":"adyui"}]}
		JSONObject jArray = new JSONObject();
		try {

			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCity);

				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItems.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",
								selectedLocalityItems.get(i));
						jsonArray.put(localityArray);

					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();
			}
			else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

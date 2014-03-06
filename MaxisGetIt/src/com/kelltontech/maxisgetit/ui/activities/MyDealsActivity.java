package com.kelltontech.maxisgetit.ui.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
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
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.MyDealsController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.MyDeal;
import com.kelltontech.maxisgetit.dao.MyDealsList;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.DealsListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MyDealsActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private DealsListResponse mMyDealsResponse;
	private LinearLayout mLatestDealContainer;
	private LinearLayout mDealsGroupContainer;
	private LinearLayout mLeadsGroupContainer;
	private ImageView mDealPostBtn;
	private String mUserId;
	private LinearLayout mLeadsOuterContainer;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
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
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_MY_DEALS);
		setContentView(R.layout.activity_mydeals);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(MyDealsActivity.this);
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
				.getString(R.string.acc_my_deals));

		mDealPostBtn = (ImageView) findViewById(R.id.amd_post_deal_btn);
		mDealPostBtn.setOnClickListener(this);
		Bundle bundle = getIntent().getExtras();
		mMyDealsResponse = bundle.getParcelable(AppConstants.MY_DEALS_RESPONSE);
		mUserId = bundle.getString(AppConstants.USER_ID);
		mLeadsOuterContainer = (LinearLayout) findViewById(R.id.amd_leads_outer_container);
		mDealsGroupContainer = (LinearLayout) findViewById(R.id.amd_deals_group_container);
		mLeadsGroupContainer = (LinearLayout) findViewById(R.id.amd_leads_group_container);
		mLatestDealContainer = (LinearLayout) findViewById(R.id.amd_latest_deal_container);
		if (mMyDealsResponse != null) {
			if (mMyDealsResponse.getLatestDeal() != null) {
				inflateCompanyDetail(mMyDealsResponse.getLatestDeal(), 1,
						mLatestDealContainer, true);
				findViewById(R.id.amd_deal_unavailable)
						.setVisibility(View.GONE);
			}
			displayDealListing();

			// // ############# temp code for leads display ###############
			// leadsOuterContainer.setVisibility(View.GONE);
			// for (MyDealsList dealGroup : dealGroupList) {
			// ArrayList<MyDeal> dealList = dealGroup.getDealList();
			// if (dealList == null && dealList.size() < 1)
			// continue;
			// View listRow = ((LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.classified_list_row_base,
			// null);
			// MyDeal temp = dealList.get(0);
			// listTxtView = ((TextView) listRow.findViewById(R.id.clrb_title));
			// listTxtView.setText(temp.getCategory());
			// listTxtView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// View parent = (View) v.getParent();
			// ArrayList<MyDeal> catList = (ArrayList<MyDeal>) parent.getTag();
			// LinearLayout innerlayout = (LinearLayout)
			// parent.findViewById(R.id.clrb_inner_layout);
			// if (innerlayout.getVisibility() == View.VISIBLE) {
			// innerlayout.setVisibility(View.GONE);
			// ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null,
			// null, getResources().getDrawable(R.drawable.down_arrow), null);
			// } else {
			// innerlayout.setVisibility(View.VISIBLE);
			// ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null,
			// null, getResources().getDrawable(R.drawable.up_arrow), null);
			// if (catList != null && innerlayout.getChildCount() == 0) {
			// for (int j = 0; j < catList.size(); j++) {
			// inflateCompanyDetail(catList.get(j), j + 1, innerlayout);
			// }
			// }
			// View firstVu = innerlayout.getChildCount() > 0 ?
			// innerlayout.getChildAt(0) : null;
			// if (firstVu != null) {
			// firstVu.setFocusable(true);
			// firstVu.setFocusableInTouchMode(true);
			// firstVu.requestFocus();
			// }
			// }
			// }
			// });
			// listRow.setTag(dealList);
			// leadsGroupContainer.addView(listRow);
			// }
			// // #############-----------------------------###############
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

	private void displayDealListing() {
		mDealsGroupContainer.removeAllViews();
		ArrayList<MyDealsList> dealGroupList = mMyDealsResponse
				.getDealGroupList();
		for (MyDealsList dealGroup : dealGroupList) {
			ArrayList<MyDeal> dealList = dealGroup.getDealList();
			if (dealList == null && dealList.size() < 1)
				continue;
			View listRow = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.classified_list_row_base, null);
			MyDeal temp = dealList.get(0);
			TextView mListTxtView = ((TextView) listRow
					.findViewById(R.id.clrb_title));
			mListTxtView.setText(temp.getCategory());
			mListTxtView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					View parent = (View) v.getParent();
					ArrayList<MyDeal> catList = (ArrayList<MyDeal>) parent
							.getTag();
					LinearLayout innerlayout = (LinearLayout) parent
							.findViewById(R.id.clrb_inner_layout);
					if (innerlayout.getVisibility() == View.VISIBLE) {
						innerlayout.setVisibility(View.GONE);
						((TextView) v).setCompoundDrawablesWithIntrinsicBounds(
								null,
								null,
								getResources().getDrawable(
										R.drawable.down_arrow), null);
					} else {
						// ********************************
						for (int i = 0; i < mDealsGroupContainer
								.getChildCount(); i++) {
							View outerRowLayour = mDealsGroupContainer
									.getChildAt(i);
							View innerContainer = outerRowLayour
									.findViewById(R.id.clrb_inner_layout);
							if (innerContainer != null) {
								innerContainer.setVisibility(View.GONE);
								((TextView) outerRowLayour
										.findViewById(R.id.clrb_title))
										.setCompoundDrawablesWithIntrinsicBounds(
												null,
												null,
												getResources().getDrawable(
														R.drawable.down_arrow),
												null);
							}
						}
						// ********************************
						innerlayout.setVisibility(View.VISIBLE);
						((TextView) v)
								.setCompoundDrawablesWithIntrinsicBounds(
										null,
										null,
										getResources().getDrawable(
												R.drawable.up_arrow), null);
						if (catList != null && innerlayout.getChildCount() == 0) {
							for (int j = 0; j < catList.size(); j++) {
								inflateCompanyDetail(catList.get(j), j + 1,
										innerlayout, false);
							}
						}
						View firstVu = innerlayout.getChildCount() > 0 ? innerlayout
								.getChildAt(0) : null;
						if (firstVu != null) {
							firstVu.setFocusable(true);
							firstVu.setFocusableInTouchMode(true);
							firstVu.requestFocus();
						}
					}
				}
			});
			listRow.setTag(dealList);
			mDealsGroupContainer.addView(listRow);
		}
	}

	private void inflateCompanyDetail(MyDeal deal, int countIndex,
			LinearLayout container, boolean isLatest) {
		if (isLatest)
			container.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(
				R.layout.mydeal_item_layout, null);
		TextView counterTxt = (TextView) compContainer
				.findViewById(R.id.mydeal_id);
		counterTxt.setText(deal.getId());
		TextView compTitle = (TextView) compContainer
				.findViewById(R.id.mydeal_title);
		compTitle.setText(deal.getTitle());
		TextView validity = (TextView) compContainer
				.findViewById(R.id.mydeal_validity);
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			formatter.setTimeZone(TimeZone.getDefault());
			Date date = new Date(Long.parseLong(deal.getValidity()) * 1000);
			validity.setText(formatter.format(date));
		} catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.DATE_ERR,
					"MyDealsActivity" + AppConstants.DATE_ERROR_MSG, e);
		}
		TextView listingType = (TextView) compContainer
				.findViewById(R.id.mydeal_type);
		listingType.setText(deal.getDealType() ? MyDeal.DEAL_TYPE_VOUCHERED
				: MyDeal.DEAL_TYPE_NON_VOUCHERED);
		// %%%%%%%
		String adStatus = deal.getDealStatus();
		if (adStatus != null) {

			if (adStatus.equalsIgnoreCase("Active")
					|| adStatus.equalsIgnoreCase("Incomplete")
					|| adStatus.equalsIgnoreCase("Reject")) {
				// edit
				ImageView editLink = (ImageView) compContainer
						.findViewById(R.id.mydeal_edit_link);
				editLink.setVisibility(View.GONE);
				editLink.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showToast(MyDealsActivity.this.getResources()
								.getString(R.string.under_implement));
					}
				});
				if (adStatus.equalsIgnoreCase("Active")) {
					// stop
					ImageView stop = (ImageView) compContainer
							.findViewById(R.id.mydeal_stop);
					stop.setVisibility(View.GONE);
					stop.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showToast(MyDealsActivity.this.getResources()
									.getString(R.string.under_implement));
						}
					});
				}
			} else if (adStatus.equalsIgnoreCase("Stop")
					|| adStatus.equalsIgnoreCase("Expire")) {
				// repost
				ImageView repost = (ImageView) compContainer
						.findViewById(R.id.mydeal_repost);
				repost.setVisibility(View.GONE);
				repost.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showToast(MyDealsActivity.this.getResources()
								.getString(R.string.under_implement));
					}
				});
			}
		}
		// %%%%%%%%%%%
		container.addView(compContainer);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 0) {
			MyDealsController mdController = new MyDealsController(
					MyDealsActivity.this, Events.MY_DEALS_LIST);
			startSppiner();
			mdController.requestService(mUserId);
		}
		if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
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
			if (index == -1) {
				city_id = -1;
			} else {
				city_id = cityList.get(index).getId();
			}

		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.LOCALITY_REQUEST) {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(MyDealsActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			// finish();
			onProfileClick();
			break;
		case R.id.amd_post_deal_btn:
			// CityTable cityTable=new CityTable((MyApplication)
			// MyDealsActivity.this.getApplication());
			// ArrayList<CityOrLocality> cityList=cityTable.getAllCitiesList();
			// if(cityList==null || cityList.size()<1){
			// CityAreaListController clController=new
			// CityAreaListController(MyDealsActivity.this,
			// Events.CITY_LISTING);
			// startSppiner();
			// clController.requestService(null);
			// }else{
			if (mMyDealsResponse.getCompCategoryList() != null
					&& mMyDealsResponse.getCompCategoryList().size() > 1
					&& mMyDealsResponse.getCompCategoryList().get(1)
							.getCategoryList() != null
					&& mMyDealsResponse.getCompCategoryList().get(1)
							.getCompanyId() != null
					&& mMyDealsResponse.getCompCategoryList().get(0)
							.getCategoryList().size() >= 1) {
				Intent dealPostIntent = new Intent(MyDealsActivity.this,
						DealPostActivity.class);
				dealPostIntent.putExtra(AppConstants.MY_DEALS_RESPONSE,
						mMyDealsResponse);
				startActivityForResult(dealPostIntent, 0);

			} else {
				showInfoDialog(getResources().getString(
						R.string.category_list_not_found));
			}
			// }
			// MyDealsController mdController = new
			// MyDealsController(MyDealsActivity.this,
			// Events.DEAL_CATEGORIES_LIST);
			// startSppiner();
			// mdController.requestService(userId);
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
				Intent cityIntent = new Intent(MyDealsActivity.this,
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
				Intent localityIntent = new Intent(MyDealsActivity.this,
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
		} else if (msg.arg2 == Events.MY_DEALS_LIST) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mMyDealsResponse = (DealsListResponse) msg.obj;
				inflateCompanyDetail(mMyDealsResponse.getLatestDeal(), 1,
						mLatestDealContainer, true);
				displayDealListing();
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
				Intent intent = new Intent(MyDealsActivity.this,
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
				Intent intent = new Intent(MyDealsActivity.this,
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
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(AppConstants.MY_DEALS_RESPONSE, mMyDealsResponse);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.MY_DEALS_LIST) {
			handler.sendMessage((Message) screenData);
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

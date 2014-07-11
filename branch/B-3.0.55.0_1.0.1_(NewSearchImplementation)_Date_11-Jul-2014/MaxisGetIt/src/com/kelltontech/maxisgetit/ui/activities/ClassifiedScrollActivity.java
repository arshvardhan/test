package com.kelltontech.maxisgetit.ui.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.Classified_Base;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ClassifiedScrollActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private ClassifiedListResponse mClsResponse;
	private LinearLayout mClassifiedContainer;
	private LinearLayout mClassifiedGroupContainer;
	private LinearLayout mLeadsGroupContainer;
	private TextView mListTxtView;
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
		setContentView(R.layout.activity_classified_scroll);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(ClassifiedScrollActivity.this);
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
				.getString(R.string.acc_my_classified));

		Bundle bundle = getIntent().getExtras();
		mClsResponse = bundle
				.getParcelable(AppConstants.CLASSIFIED_LIST_RESPONSE);
		mClassifiedGroupContainer = (LinearLayout) findViewById(R.id.acl_classified_group_container);
		mLeadsGroupContainer = (LinearLayout) findViewById(R.id.acl_leads_group_container);
		mClassifiedContainer = (LinearLayout) findViewById(R.id.acl_container);
		if (mClsResponse != null) {
			if (mClsResponse.getLatestAd() != null)
				inflateCompanyDetail(mClsResponse.getLatestAd(), 1,
						mClassifiedContainer);
			ArrayList<Classified_Base> classifiedList = sortListAndStoreLatest();
			ArrayList<List> groupList = new ArrayList<List>();
			String categoryName = null;
			ArrayList<Classified_Base> catBasedList = null;
			for (int i = 0; i < classifiedList.size(); i++) {
				if (!classifiedList.get(i).getCategory().equals(categoryName)) {
					categoryName = classifiedList.get(i).getCategory();
					if (catBasedList != null)
						groupList.add(catBasedList);
					catBasedList = new ArrayList<Classified_Base>();
					catBasedList.add(classifiedList.get(i));
				} else {
					catBasedList.add(classifiedList.get(i));
				}
			}
			if (catBasedList != null && catBasedList.size() > 0) {
				groupList.add(catBasedList);
			}
			for (int i = 0; i < groupList.size(); i++) {
				View listRow = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.classified_list_row_base, null);
				if (groupList.get(i) == null || groupList.get(i).size() < 1)
					continue;
				Classified_Base temp = (Classified_Base) groupList.get(i)
						.get(0);
				mListTxtView = ((TextView) listRow
						.findViewById(R.id.clrb_title));
				mListTxtView.setText(temp.getCategory());
				mListTxtView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						View parent = (View) v.getParent();
						ArrayList<Classified_Base> catList = (ArrayList<Classified_Base>) parent
								.getTag();
						LinearLayout innerlayout = (LinearLayout) parent
								.findViewById(R.id.clrb_inner_layout);
						if (innerlayout.getVisibility() == View.VISIBLE) {
							innerlayout.setVisibility(View.GONE);
							((TextView) v)
									.setCompoundDrawablesWithIntrinsicBounds(
											null,
											null,
											getResources().getDrawable(
													R.drawable.down_arrow),
											null);
						} else {
							// ********************************
							// code for switching off all open expandables
							for (int i = 0; i < mClassifiedGroupContainer
									.getChildCount(); i++) {
								View outerRowLayour = mClassifiedGroupContainer
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
													getResources()
															.getDrawable(
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
							if (catList != null
									&& innerlayout.getChildCount() == 0) {
								for (int j = 0; j < catList.size(); j++) {
									inflateCompanyDetail(catList.get(j), j + 1,
											innerlayout);
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
				listRow.setTag(groupList.get(i));
				mClassifiedGroupContainer.addView(listRow);
			}
			// ############# temp code for deals display ###############
			// for (int i = 0; i < groupList.size(); i++) {
			// View listRow = ((LayoutInflater)
			// getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.classified_list_row_base,
			// null);
			// if (groupList.get(i) == null || groupList.get(i).size() < 1)
			// continue;
			// Classified_Base temp = (Classified_Base) groupList.get(i).get(0);
			// listTxtView = ((TextView) listRow.findViewById(R.id.clrb_title));
			// listTxtView.setText(temp.getCategory());
			// listTxtView.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// View parent = (View) v.getParent();
			// ArrayList<Classified_Base> catList = (ArrayList<Classified_Base>)
			// parent.getTag();
			// LinearLayout innerlayout = (LinearLayout)
			// parent.findViewById(R.id.clrb_inner_layout);
			// if (innerlayout.getVisibility() == View.VISIBLE) {
			// innerlayout.setVisibility(View.GONE);
			// ((TextView)v).setCompoundDrawablesWithIntrinsicBounds(null, null,
			// getResources().getDrawable(R.drawable.down_arrow), null);
			// } else {
			// innerlayout.setVisibility(View.VISIBLE);
			// ((TextView)v).setCompoundDrawablesWithIntrinsicBounds(null, null,
			// getResources().getDrawable(R.drawable.up_arrow), null);
			// if (catList != null && innerlayout.getChildCount() == 0) {
			// for (int j = 0; j < catList.size(); j++) {
			// inflateCompanyDetail(catList.get(j), j + 1, innerlayout);
			// }
			// }
			// View
			// firstVu=innerlayout.getChildCount()>0?innerlayout.getChildAt(0):null;
			// if(firstVu!=null){
			// firstVu.setFocusable(true);
			// firstVu.setFocusableInTouchMode(true);
			// firstVu.requestFocus();
			// }
			// }
			// }
			// });
			// listRow.setTag(groupList.get(i));
			// leadsGroupContainer.addView(listRow);
			// }
			// #############-----------------------------###############
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

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(ClassifiedScrollActivity.this, AppConstants.MyClassifieds);
	}
	
	private ArrayList<Classified_Base> sortListAndStoreLatest() {
		ArrayList<Classified_Base> list = mClsResponse.getClassifiedList();
		Collections.sort(list, new Comparator<Classified_Base>() {
			@Override
			public int compare(Classified_Base lhs, Classified_Base rhs) {
				return lhs.getCategory().compareTo(rhs.getCategory());
			}
		});
		return list;
	}

	private void inflateCompanyDetail(Classified_Base classified_Base,
			int countIndex, LinearLayout container) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout compContainer = (LinearLayout) inflater.inflate(
				R.layout.classified_item_layout, null);
		TextView counterTxt = (TextView) compContainer
				.findViewById(R.id.class_id);
		counterTxt.setText(classified_Base.getId());
		TextView compTitle = (TextView) compContainer
				.findViewById(R.id.class_title);
		compTitle.setText(classified_Base.getTitle());
		TextView category = (TextView) compContainer
				.findViewById(R.id.class_category);
		category.setText(classified_Base.getCategory());
		TextView validity = (TextView) compContainer
				.findViewById(R.id.class_validity);
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			formatter.setTimeZone(TimeZone.getDefault());
			Date date = new Date(
					Long.parseLong(classified_Base.getValidity()) * 1000);
			validity.setText(formatter.format(date));
		} catch (Exception e) {
			AnalyticsHelper
					.onError(FlurryEventsConstants.DATE_ERR,
							"ClassifiedScrollActivity"
									+ AppConstants.DATE_ERROR_MSG, e);
		}
		// validity.setText(classified_Base.getValidity());
		TextView listingType = (TextView) compContainer
				.findViewById(R.id.class_type);
		TextView upgradeBtn = (TextView) compContainer
				.findViewById(R.id.class_upgrade_btn);
		if (!classified_Base.isPaid()) {
			listingType.setText(getResources().getString(R.string.free));
			upgradeBtn.setOnClickListener(this);
		} else {
			listingType.setText(getResources().getString(R.string.paid));
			upgradeBtn.setVisibility(View.GONE);
		}
		// Actions that can be performed based on the status :
		// 1. Pending , moderation --- no action can be taken.
		// 2. Active --- stop / edit .
		// 3. Incomplete , Reject -- Edit
		// 4. Stop , Expire --- Repost
		String adStatus = classified_Base.getAdStatus();
		if (adStatus != null) {

			if (adStatus.equalsIgnoreCase("Active")
					|| adStatus.equalsIgnoreCase("Incomplete")
					|| adStatus.equalsIgnoreCase("Reject")) {
				// edit
				ImageView editLink = (ImageView) compContainer
						.findViewById(R.id.class_edit_link);
				editLink.setVisibility(View.VISIBLE);
				editLink.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showToast(ClassifiedScrollActivity.this.getResources()
								.getString(R.string.under_implement));
					}
				});
				if (adStatus.equalsIgnoreCase("Active")) {
					// stop
					ImageView stop = (ImageView) compContainer
							.findViewById(R.id.class_stop);
					stop.setVisibility(View.VISIBLE);
					stop.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showToast(ClassifiedScrollActivity.this
									.getResources().getString(
											R.string.under_implement));
						}
					});
				}
			} else if (adStatus.equalsIgnoreCase("Stop")
					|| adStatus.equalsIgnoreCase("Expire")) {
				// repost
				ImageView repost = (ImageView) compContainer
						.findViewById(R.id.class_repost);
				repost.setVisibility(View.VISIBLE);
				repost.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showToast(ClassifiedScrollActivity.this.getResources()
								.getString(R.string.under_implement));
					}
				});
			}
		}
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
			showHomeScreen();
			break;
		case R.id.show_profile_icon:
			// finish();
			onProfileClick();
			break;
		case R.id.class_upgrade_btn:
			showInfoDialog(getResources().getString(R.string.under_implement));
			break;
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
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
				Intent cityIntent = new Intent(ClassifiedScrollActivity.this,
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
				Intent localityIntent = new Intent(
						ClassifiedScrollActivity.this,
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
				Intent intent = new Intent(ClassifiedScrollActivity.this,
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
				Intent intent = new Intent(ClassifiedScrollActivity.this,
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

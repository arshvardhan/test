package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.DealOutletsAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.OutLetDetailtController;
import com.kelltontech.maxisgetit.controllers.OutletRefineController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.OutLet;
import com.kelltontech.maxisgetit.dao.OutLetDetails;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.model.CommonResponse;
import com.kelltontech.maxisgetit.requests.OutLetDetailRequest;
import com.kelltontech.maxisgetit.requests.OutletRefineRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ViewAllOutletsActivity extends MaxisMainActivity {

	private SubCategoryResponse mSubcatResponse;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	// private DealsCategoryAdapter mCatAdapter;
	private SubCategory mSelectdCategory;
	private TextView mRecordsFoundView;

	private TextView mViewAllOnMap;
	private TextView mRefineSearchView;
	private TextView mRefineSearchView1;
	private boolean loadingNextPageData;
	private boolean isModifySearchDialogOpen;
	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malaysia";
	private int city_id = -1;
	private boolean mScrollUp;
	private boolean mIsFreshSearch = true;
	private ListView mOutletList;

	private ArrayList<String> selectedLocalityItems;
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;

	private OutLetDetails mOutletResponse;
	private ArrayList<OutLet> outLets;
	private DealOutletsAdapter mOutletListAdapter;
	private int mapIndex;
	private int totalOutlets;
	private OutLetDetailRequest detailRequest;
	private String title;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				mOutletResponse = bundle
						.getParcelable(AppConstants.OUTLET_DETAIL_DATA);
				totalOutlets = bundle.getInt("totalCount");
				mRecordsFoundView.setText(totalOutlets + " "
						+ getResources().getString(R.string.outlet_found));
				outLets = mOutletResponse.getOutlet();
				detailRequest = bundle.getParcelable("OutletRequest");
				mOutletListAdapter.setData(outLets);
				mOutletList.setAdapter(mOutletListAdapter);
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CITY_REQUEST) {
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_outlets);

		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mRecordsFoundView = (TextView) findViewById(R.id.col_records_found);

		mOutletList = (ListView) findViewById(R.id.col_company_list);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);

		mViewAllOnMap = (TextView) findViewById(R.id.col_view_on_map);
		mViewAllOnMap.setOnClickListener(this);

		findViewById(R.id.col_view_on_map1).setOnClickListener(this);

		mRefineSearchView = (TextView) findViewById(R.id.col_refine_search);
		mRefineSearchView.setOnClickListener(this);

		mRefineSearchView1 = (TextView) findViewById(R.id.col_refine_search1);
		mRefineSearchView1.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();

		mOutletResponse = bundle.getParcelable(AppConstants.OUTLET_DETAIL_DATA);
		outLets = mOutletResponse.getOutlet();
		totalOutlets = bundle.getInt("totalCount");
		detailRequest = bundle.getParcelable("OutletRequest");
		title = bundle.getString("deal_title");
		if (!StringUtil.isNullOrEmpty(title)) {
			mHeaderTitle.setText(title);
		}

		mRecordsFoundView.setText(totalOutlets + " "
				+ getResources().getString(R.string.outlet_found));
		mOutletListAdapter = new DealOutletsAdapter(this, false, totalOutlets);
		mOutletListAdapter.setData(outLets);
		mOutletList.setAdapter(mOutletListAdapter);

		mOutletList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView,
					int scrollState) {
			}

			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.w("", "firstVisibleItem " + firstVisibleItem
						+ " visibleItemCount " + visibleItemCount
						+ " totalItemCount " + totalItemCount);
				int number = firstVisibleItem + visibleItemCount;
				if (mOutletResponse != null) {
					if ((number) % mOutletResponse.getRecords_per_page() == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < Integer
									.parseInt(mOutletResponse
											.getTotal_records()))// mNewsList.size()>totalItemCount
					{
						Log.d("maxis", "list detail before next page"
								+ mOutletResponse.getPage_number() + "  "
								+ totalItemCount + " "
								+ mOutletResponse.getOutlet().size());
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (mOutletResponse.getPage_number() < AppConstants.MAX_RECORD_COUNT / 10) {
							loadPageData(mOutletResponse.getPage_number() + 1);
						}
					} else if (number == AppConstants.MAX_RECORD_COUNT + 1
							&& !isModifySearchDialogOpen
							&& mScrollUp
							&& Integer.parseInt(mOutletResponse
									.getTotal_records()) > 100) {
						showConfirmationDialog(
								CustomDialog.CONFIRMATION_DIALOG,
								getResources().getString(
										R.string.modify_to_filter));
						isModifySearchDialogOpen = true;
						AnalyticsHelper
								.logEvent(FlurryEventsConstants.COMBINED_LIST_VISITED_ITEMS_EXCEEDED_70);
					}
				}
			}
		});

		mOutletList.setOnTouchListener(new OnTouchListener() {
			private float mInitialX;
			private float mInitialY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mInitialX = event.getX();
					mInitialY = event.getY();
					mScrollUp = false;
					// mScrollDown = true;
					return true;
				case MotionEvent.ACTION_MOVE:
					final float x = event.getX();
					final float y = event.getY();
					final float yDiff = y - mInitialY;
					if (yDiff > 0.0) {
						Log.d("maxis", "SCROLL DOWN");
						mScrollUp = false;
						break;

					} else if (yDiff < 0.0) {
						Log.d("maxis", "SCROLL up");
						mScrollUp = true;
						break;
					}
					break;
				}
				return false;
			}

		});

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
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;

		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		case R.id.col_view_on_map:
		case R.id.col_view_on_map1:
			AnalyticsHelper.logEvent(FlurryEventsConstants.VIEW_ON_MAP_CLICK);
			int index = -1;
			showMap(index);
			break;

		case R.id.col_refine_search:
		case R.id.col_refine_search1:

			// TODO
			refineOutlets(detailRequest.getDeal_id());

			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
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
				Intent cityIntent = new Intent(ViewAllOutletsActivity.this,
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
				Intent localityIntent = new Intent(ViewAllOutletsActivity.this,
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
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.DOWNLOAD_DEAL) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.CITY_LISTING_OUTLETS) {
			Log.i("manish", "inside setscreendata");
			CommonResponse cResponse = (CommonResponse) screenData;
			Message message = new Message();
			message.arg2 = event;

			if (cResponse.getResults().getError_Code().equalsIgnoreCase("1")) {
				message.arg1 = 1;
				message.obj = getResources().getString(
						R.string.communication_failure);
			} else {
				message.arg1 = 0;
				message.obj = cResponse;
			}
			handler.sendMessage(message);

		} else {
			System.out.println(screenData);
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;

			if (response.isError()) {
				message.obj = response.getErrorText();
			} else if (event == Events.OUTLET_DETAIL_PAGINATION) {

				try {
					if (response.getPayload() instanceof OutLetDetails) {
						OutLetDetails outLetDetails = (OutLetDetails) response
								.getPayload();
						if (outLetDetails.getErrorCode() != 0) {
							message.obj = getResources().getString(
									R.string.communication_failure);
						} else {
							if (outLetDetails.getOutlet().size() < 1) {
								message.obj = new String(getResources()
										.getString(R.string.no_result_found));
							} else {
								message.arg1 = 0;
								message.obj = outLetDetails;
							}
						}
					} else {
						message.obj = new String(getResources().getString(
								R.string.communication_failure));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (event == Events.DEAL_DETAIL) {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail compDetail = (CompanyDetail) response
							.getPayload();
					if (compDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = compDetail;
					}
				} else {
					message.obj = new String(getResources().getString(
							R.string.communication_failure));
				}
			} else {
				if (response.getPayload() instanceof CompanyListResponse) {
					mOutletResponse = (OutLetDetails) response.getPayload();
					if (mOutletResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
						// clResponse.getServerMessage() + " " +
						// clResponse.getErrorCode();
					} else {
						if (mOutletResponse.getOutlet().size() < 1) {
							message.obj = new String("No Result Found");
						} else {
							message.arg1 = 0;
							message.obj = mOutletResponse;
						}
					}
				} else {
					message.obj = new String("Internal Error");
				}
			}

			handler.sendMessage(message);
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
				Intent intent = new Intent(ViewAllOutletsActivity.this,
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
				Intent intent = new Intent(ViewAllOutletsActivity.this,
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
		} else if (msg.arg2 == Events.OUTLET_DETAIL_PAGINATION) {

			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				OutLetDetails oldResponse = mOutletResponse;
				mOutletResponse = (OutLetDetails) msg.obj;
				oldResponse.appendOutletListAtEnd(mOutletResponse.getOutlet());
				mOutletResponse.setOutletList(oldResponse.getOutlet());
				// TODO append result set in existing
				// changeNavigationButtonState(mClResponse.getPagesCount(),
				// mClResponse.getPageNumber());

				mOutletListAdapter.setData(mOutletResponse.getOutlet());
				mOutletListAdapter.notifyDataSetChanged();
			}
			stopSppiner();

		} else if (msg.arg2 == Events.CITY_LISTING_OUTLETS) {
			// TODO
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {

				CommonResponse response = (CommonResponse) msg.obj;
				ArrayList<String> cities = response.getResults().getCities()
						.getCity();

				Intent refineOutletIntent = new Intent(
						ViewAllOutletsActivity.this, RefineOutletActivity.class);
				refineOutletIntent.putStringArrayListExtra("CITIES_ARRAYLIST",
						cities);
				refineOutletIntent.putExtra("OutletRequest", detailRequest);
				refineOutletIntent.putExtra("deal_title", title);
				startActivityForResult(refineOutletIntent, 1);
			}
			stopSppiner();
		}

	}

	private void loadPageData(int pageNumber) {
		try {

			OutLetDetailtController detailtController = new OutLetDetailtController(
					ViewAllOutletsActivity.this,
					Events.OUTLET_DETAIL_PAGINATION);
			detailRequest.setPage_number(pageNumber);
			startSppiner();
			detailtController.requestService(detailRequest);
		} catch (Exception e) {
			// TODO: handle exception
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

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void showMap(int index) {

		mapIndex = index;
		if (isDialogToBeShown()) {
			showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG,
					getResources().getString(R.string.cd_msg_data_usage));
		} else {
			if (isLocationAvailable()) {
				redirectToMap();
			}
		}
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.DATA_USAGE_DIALOG) {

			if (isLocationAvailable()) {
				redirectToMap();
			}
		}
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		if (id == CustomDialog.LOGIN_CONFIRMATION_DIALOG) {
			stopSppiner();
		} else if (id == CustomDialog.DELETE_CONFIRMATION_DIALOG) {
			stopSppiner();
		} else {
			super.onNegativeDialogbutton(id);
		}
	}

	public void redirectToMap() {
		Intent intent = new Intent(ViewAllOutletsActivity.this,
				ViewDealMapActivity.class);

		intent.putParcelableArrayListExtra(AppConstants.OUTLET_DATA, outLets);
		intent.putExtra("index", mapIndex);
		if (mapIndex != -1) {
			if (!StringUtil.isNullOrEmpty(outLets.get(mapIndex).getTitle()))
				intent.putExtra("DEAL_TITLE", outLets.get(mapIndex).getTitle());
		} else {
			intent.putExtra("DEAL_TITLE", title);
		}

		startActivity(intent);
	}

	public void refineOutlets(String deal_id) {
		OutletRefineRequest request = new OutletRefineRequest();
		request.setDeal_id(deal_id);

		OutletRefineController outletRefineController = new OutletRefineController(
				ViewAllOutletsActivity.this, Events.CITY_LISTING_OUTLETS);
		startSppiner();
		outletRefineController.requestService(request);

	}
}

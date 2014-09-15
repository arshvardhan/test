package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.PaidCompanyListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.PaidCompanyListController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.model.paidcompany.PaidCompany;
import com.kelltontech.maxisgetit.model.paidcompany.PaidCompanyResponse;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.PaidCompanyListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.ui.widgets.AnimatedLinearLayout;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class PaidCompanyListActivity extends MaxisMainActivity implements OnClickListener {

	private LinearLayout 					advanceSearchLayout;
	private LinearLayout 					mSearchContainer;
	private AnimatedLinearLayout 			wholeSearchBoxContainer;
	private TextView 						mRecordsFoundView;
	private TextView 						currentCity, currentLocality;
	private TextView 						mainSearchButton;
	private TextView 						mHeaderTitle;

	private ImageView 						mSearchToggler;
	private ImageView 						mSearchBtn;
	private ImageView 						upArrow;
	private ImageView 						mHomeIconView;
	private ImageView 						mProfileIconView;
	private ImageView 						mHeaderBackButton;

	private PaidCompanyListRequest 			mPaidCompanyListRequest;
	private PaidCompanyResponse 			mPaidCompanyResponse;
	private PaidCompanyListAdapter 			mPaidCompListAdapter;

	private ListView 						mCompanyList;
	private EditText 						mSearchEditText;
	ArrayList<CityOrLocality> 				localityList;
	ArrayList<String> 						ids = new ArrayList<String>();
	ArrayList<String> 						selectedLocalityindex;
	ArrayList<CityOrLocality> 				cityList;
	private ArrayList<String> 				selectedLocalityItemsforHeader = new ArrayList<String>();
	private ArrayList<String> 				cityListString	= new ArrayList<String>();
	private ArrayList<String> 				localityItems;
	private String 							selectedCityforHeader = "Entire Malaysia";
	private boolean 						loadingNextPageData;
	private boolean 						isModifySearchDialogOpen;
	private boolean 						mScrollUp;
	private boolean 						isAdvanceSearchLayoutOpen = false;
	boolean 								isFirstTime = false;
	boolean 								isSuccessfull = false;
	boolean 								isFromSearch = false;
	private int 							city_id = -1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle 				=	data.getExtras();
				mPaidCompanyResponse 		=	(PaidCompanyResponse) bundle.getParcelable(AppConstants.PAID_COMPANY_LIST_DATA);
				mPaidCompanyListRequest 	=	(PaidCompanyListRequest) bundle.getSerializable(AppConstants.PAID_COMPANY_LIST_REQUEST);
				mRecordsFoundView.setText(mPaidCompanyResponse.getResults().getTotalRecordsFound() + " " + getResources().getString(R.string.matta_record_found));
				if (StringUtil.isNullOrEmpty(mPaidCompanyResponse.getResults().getTotalRecordsFound()) || mPaidCompanyResponse.getResults().getTotalRecordsFound().equals("0") )
					showInfoDialog(getResources().getString(R.string.no_result_found));
				ArrayList<PaidCompany> paidCompanyListData 	= mPaidCompanyResponse.getResults().getCompany();
				mPaidCompListAdapter.setData(paidCompanyListData);
				mPaidCompListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mPaidCompListAdapter);
			}
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCityforHeader.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems 						= 		null;
				ids 								= 		null;
				selectedLocalityindex 				= 		null;
				currentLocality.setText("Choose your Area");
			}
			selectedCityforHeader 					= 		data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));
			int index 								= 		data.getIntExtra("CITY_INDEX", 0);
			if (index == -1) 
				city_id = -1;
			else 
				city_id = cityList.get(index).getId();
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";
			selectedLocalityItemsforHeader = data.getStringArrayListExtra("SELECTED_LOCALITIES");
			selectedLocalityindex = data.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItemsforHeader != null && selectedLocalityItemsforHeader.size() > 0) {
				for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
					if (i == selectedLocalityItemsforHeader.size() - 1)
						locality += selectedLocalityItemsforHeader.get(i);
					else
						locality += selectedLocalityItemsforHeader.get(i) + ",";
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area " + "<b>" + locality + "</b>"));
			} else
				currentLocality.setText("Choose your Area");
			ids = new ArrayList<String>();
			if (selectedLocalityindex != null && selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {
					ids.add(String.valueOf(localityList.get(Integer.parseInt(selectedLocalityindex.get(i))).getId()));
				}
			}
		} 
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paid_company_list);
		AnalyticsHelper.logEvent(FlurryEventsConstants.MATTA_PACKAGE_LISTING, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),this);
		ImageLoader.initialize(PaidCompanyListActivity.this);

		advanceSearchLayout 	= (LinearLayout) 	findViewById(R.id.advanceSearch);		
		wholeSearchBoxContainer = (AnimatedLinearLayout) 	findViewById(R.id.whole_search_box_container);
		mSearchContainer 		= (LinearLayout) 	findViewById(R.id.search_box_container);
		mProfileIconView 		= (ImageView) 		findViewById(R.id.show_profile_icon);
		mHeaderBackButton 		= (ImageView) 		findViewById(R.id.header_btn_back);
		mHomeIconView 			= (ImageView) 		findViewById(R.id.goto_home_icon);
		mSearchBtn 				= (ImageView) 		findViewById(R.id.search_icon_button);
		upArrow 				= (ImageView) 		findViewById(R.id.upArrow);
		mSearchEditText 		= (EditText) 		findViewById(R.id.search_box);
		//		mRefineSearchView 		= (TextView) 		findViewById(R.id.col_refine_search);
		currentCity 			= (TextView) 		findViewById(R.id.currentCity);
		currentLocality 		= (TextView) 		findViewById(R.id.currentLocality);
		mainSearchButton 		= (TextView) 		findViewById(R.id.mainSearchButton);
		mHeaderTitle 			= (TextView) 		findViewById(R.id.header_title);
		mRecordsFoundView 		= (TextView) 		findViewById(R.id.col_records_found);
		mCompanyList 			= (ListView) 		findViewById(R.id.col_company_list);
		mSearchToggler 			= (ImageView) 		findViewById(R.id.search_toggler);
		findViewById(R.id.play_pasue_icon).setOnClickListener(this);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView.setOnClickListener(this);
		mSearchBtn.setOnClickListener(PaidCompanyListActivity.this);
		upArrow.setOnClickListener(this);
		//		mRefineSearchView.setOnClickListener(this);
		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);
		mainSearchButton.setOnClickListener(this);
		mSearchToggler.setOnClickListener(this);
		advanceSearchLayout.setVisibility(View.GONE);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));

		if(getIntent().getExtras() != null) {
			Bundle bundle 	= getIntent().getExtras();
			mPaidCompanyListRequest = (PaidCompanyListRequest) bundle.getSerializable(AppConstants.PAID_COMPANY_LIST_REQUEST);
			mPaidCompanyResponse 	= (PaidCompanyResponse) bundle.getParcelable(AppConstants.PAID_COMPANY_LIST_DATA);
		}

		mHeaderTitle.setText(Html.fromHtml(getResources().getString(R.string.cd_you_may_also_browse)));

		//		if (mMattaPackageListRequest != null) {
		//			mMattaThumbUrl = mMattaPackageListRequest.getmMattaThumbUrl();
		//		}

		mRecordsFoundView.setText(mPaidCompanyResponse.getResults().getTotalRecordsFound() + " " + getResources().getString(R.string.matta_record_found));

		ArrayList<PaidCompany> paidCompanyListData 	= mPaidCompanyResponse.getResults().getCompany();
		mPaidCompListAdapter 						= new PaidCompanyListAdapter(PaidCompanyListActivity.this);
		mPaidCompListAdapter.setData(paidCompanyListData);
		mPaidCompListAdapter.notifyDataSetChanged();
		mCompanyList.setAdapter(mPaidCompListAdapter);

		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CompanyDetailController companyDetailController = new CompanyDetailController(PaidCompanyListActivity.this, Events.PAID_COMPANY_DETAIL_EVENT);
				String id = ((PaidCompany)mPaidCompListAdapter.getItem(arg2)).getId() ;
				DetailRequest detailRequest = new DetailRequest(PaidCompanyListActivity.this, id, false,((PaidCompany) mPaidCompListAdapter.getItem(arg2)).getL3Catid());
				startSppiner();
				companyDetailController.requestService(detailRequest);
			}
		});

		mCompanyList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) { }
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.w("", "firstVisibleItem " + firstVisibleItem + " visibleItemCount " + visibleItemCount + " totalItemCount " + totalItemCount);
				int number = firstVisibleItem + visibleItemCount;
				if (mPaidCompanyResponse != null) {
					if ((number) % mPaidCompanyListRequest.getPerPageRecord() == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < Integer.parseInt(mPaidCompanyResponse.getResults().getTotalRecordsFound())) {
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (Integer.parseInt(mPaidCompanyResponse.getResults().getPageNumber()) < MattaConstants.MAX_RECORD_COUNT / 10)
							loadPageData(Integer.parseInt(mPaidCompanyResponse.getResults().getPageNumber()) + 1);
					} else if (number >= MattaConstants.MAX_RECORD_COUNT 
							&& !isModifySearchDialogOpen 
							&& mScrollUp 
							&& Integer.parseInt(mPaidCompanyResponse.getResults().getTotalRecordsFound()) > 100) {
						showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG, getResources().getString(R.string.modify_to_filter));
						isModifySearchDialogOpen = true;
						AnalyticsHelper.logEvent(FlurryEventsConstants.COMBINED_LIST_VISITED_ITEMS_EXCEEDED_70);
					}
				}
			}
		});

		mCompanyList.setOnTouchListener(new OnTouchListener() {
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

		mSearchEditText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
		AnalyticsHelper.trackSession(PaidCompanyListActivity.this, MattaConstants.Matta_Package_Listing);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.USER_DETAIL || (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch)) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
			return;
		} else if (event == Events.PAID_COMPANY_VIEW_MORE_EVENT) {
			PaidCompanyResponse paidCompanyResponse = (PaidCompanyResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((paidCompanyResponse.getResults() != null) 
					&& (!StringUtil.isNullOrEmpty(paidCompanyResponse.getResults().getErrorCode())) 
					&& (paidCompanyResponse.getResults().getErrorCode().equals("0"))) {
				if (paidCompanyResponse.getResults().getCompany() != null 
						&& paidCompanyResponse.getResults().getCompany().size() > 0) {
					message.arg1 = 0;
					message.obj = paidCompanyResponse;
				} else {
					message.arg1 = 1;
					message.obj = getResources().getString(R.string.communication_failure);
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;	
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (response.getPayload() instanceof CompanyDetail) {
					CompanyDetail compDetail = (CompanyDetail) response.getPayload();
					if (compDetail.getErrorCode() != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = compDetail;
					}
				} else {
					message.obj = getResources().getString(R.string.communication_failure);
				}
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(PaidCompanyListActivity.this, AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {
					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCityforHeader);
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
				Intent intent = new Intent(PaidCompanyListActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				intent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch) {
			isFromSearch = false;
			super.updateUI(msg);
		} else if (msg.arg2 == Events.PAID_COMPANY_VIEW_MORE_EVENT) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				PaidCompanyResponse oldResponse = mPaidCompanyResponse;
				mPaidCompanyResponse = (PaidCompanyResponse) msg.obj;
				oldResponse.getResults().appendCompanyListAtEnd(mPaidCompanyResponse.getResults().getCompany());
				mPaidCompanyResponse.getResults().setCompany(oldResponse.getResults().getCompany());
				mPaidCompListAdapter.setData(mPaidCompanyResponse.getResults().getCompany());
				mPaidCompListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.PAID_COMPANY_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(PaidCompanyListActivity.this,CompanyDetailActivity.class);
					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void loadPageData(int pageNumber) {
		PaidCompanyListController controller = new PaidCompanyListController(PaidCompanyListActivity.this, Events.PAID_COMPANY_VIEW_MORE_EVENT);
		mPaidCompanyListRequest.setPageNumber(pageNumber);
		controller.requestService(mPaidCompanyListRequest);
		startSppiner();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) 
				wholeSearchBoxContainer.setVisibility(View.GONE);
			else
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			if (mSearchContainer.getVisibility() == View.VISIBLE)
				mSearchContainer.setVisibility(View.GONE);
			else
				mSearchContainer.setVisibility(View.VISIBLE);
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
			MaxisMainActivity.isCitySelected = false;
			this.finish();
			break;
			/*		case R.id.col_refine_search:
			if (StringUtil.isNullOrEmpty(mMattaPackageListResponse.getResults().getTotal_Records_Found()) 
					|| mMattaPackageListResponse.getResults().getTotal_Records_Found().equals("0") ) {
				showInfoDialog(getResources().getString(R.string.no_result_found));
			} else {
				refineSearch();
			}
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;*/
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			mPaidCompanyListRequest.setKeyword(mSearchEditText.getText().toString());
			isFromSearch = true;
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			MaxisMainActivity.isCitySelected = false;
			break;
		case R.id.show_profile_icon:
			onProfileClick();
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
				Intent cityIntent = new Intent(PaidCompanyListActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(PaidCompanyListActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else 
				setSearchLocality(city_id);
			break;
		}
	}

	public String jsonForSearch() {
		JSONObject jArray = new JSONObject();
		try {
			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCityforHeader);
				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",selectedLocalityItemsforHeader.get(i));
						jsonArray.put(localityArray);
					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		MaxisMainActivity.isCitySelected = false;
		super.onBackPressed();
	}
}